package com.example.test.dxworkspace.presentation.utils.event;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.util.MutableBoolean;

import java.lang.ref.WeakReference;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.ConcurrentModificationException;
import java.util.HashMap;
import java.util.List;

/**
 * Represents a subscriber, which implements various event bus handler methods.
 */
class Subscriber {

    private WeakReference<Object> mSubscriber;

    Subscriber(Object subscriber) {
        mSubscriber = new WeakReference<>(subscriber);
    }

    public String toString(int priority) {
        Object sub = mSubscriber.get();
        String id = Integer.toHexString(System.identityHashCode(sub));
        return sub.getClass().getSimpleName() + " [0x" + id + ", P" + priority + "]";
    }

    public Object getReference() {
        return mSubscriber.get();
    }
}

/**
 * Represents an event handler with a priority.
 */
class EventHandler {

    int priority;
    Subscriber subscriber;
    EventHandlerMethod method;

    EventHandler(Subscriber subscriber, EventHandlerMethod method, int priority) {
        this.subscriber = subscriber;
        this.method = method;
        this.priority = priority;
    }

    @Override
    public String toString() {
        return subscriber.toString(priority) + " " + method.toString();
    }
}

/**
 * Represents the low level method handling a particular event.
 */
class EventHandlerMethod {

    Class<? extends EventBus.Event> eventType;
    private Method mMethod;

    EventHandlerMethod(Method method, Class<? extends EventBus.Event> eventType) {
        mMethod = method;
        mMethod.setAccessible(true);
        this.eventType = eventType;
    }

    public void invoke(Object target, EventBus.Event event)
            throws InvocationTargetException, IllegalAccessException {
        mMethod.invoke(target, event);
    }

    @Override
    public String toString() {
        return mMethod.getName() + "(" + eventType.getSimpleName() + ")";
    }
}

/**
 * A simple in-process event bus.  It is simple because we can make assumptions about the state of
 * SystemUI and Recent's lifecycle.
 *
 * <p>
 * Currently, there is a single EventBus that handles {@link Event}s for each subscriber on
 * the main application thread.  Publishers can send() events to synchronously call subscribers of
 * that event, or post() events to be processed in the next run of the {@link Looper}.  In addition,
 * the EventBus supports sending and handling {@link InterprocessEvent}s (within the same
 * package) implemented using standard {@link BroadcastReceiver} mechanism. Interprocess events must
 * be posted using postInterprocess() to ensure that it is dispatched correctly across processes.
 *
 * <p>
 * Subscribers must be registered with a particular EventBus before they will receive events, and
 * handler methods must match a specific signature.
 *
 * <p>
 * Event method signature:<ul>
 * <li>Methods must be public final
 * <li>Methods must return void
 * <li>Methods must be called "onBusEvent"
 * <li>Methods must take one parameter, of class type deriving from {@link Event}
 * </ul>
 *
 * <p>
 * Interprocess-Event method signature:<ul>
 * <li>Methods must be public final
 * <li>Methods must return void
 * <li>Methods must be called "onInterprocessBusEvent"
 * <li>Methods must take one parameter, of class type deriving from {@link
 * InterprocessEvent}
 * </ul>
 * </p>
 *
 * </p>
 * Each subscriber can be registered with a given priority (default 1), and events will be dispatch
 * in decreasing cart of priority.  For subscribers with the same priority, events will be
 * dispatched by latest registration time to earliest.
 *
 * <p>
 * Interprocess events must extend {@link InterprocessEvent}, have a constructor which
 * takes a {@link Bundle} and implement toBundle().  This allows us to serialize events to be sent
 * across processes.
 *
 * <p>
 * Caveats:<ul>
 * <li>The EventBus keeps a {@link WeakReference} to the publisher to prevent memory leaks, so
 * there must be another strong prefReference to the publisher for it to not get garbage-collected and
 * continue receiving events.
 * <li>Because the event handlers are called back using reflection, the EventBus is not intended
 * for use in tight, performance criticial loops.  For most user input/system callback events, this
 * is generally of low enough frequency to use the EventBus.
 * <li>Because the event handlers are called back using reflection, there will often be no
 * references to them from actual code.  The proguard configuration will be need to be updated to
 * keep these extra methods:
 * <p>
 * -keepclassmembers class ** { public void onBusEvent(**); public void onInterprocessBusEvent(**);
 * } -keepclassmembers class ** extends **.EventBus$InterprocessEvent { public
 * <init>(android.os.Bundle); }
 *
 * <li>Subscriber registration can be expensive depending on the subscriber's {@link Class}.  This
 * is only done once per class type, but if possible, it is best to pre-register an instance of that
 * class beforehand or when idle.
 * <li>Each event should be sent once.  Events may hold internal information about the current
 * dispatch, or may be queued to be dispatched on another thread (if posted from a non-main thread),
 * so it may be unsafe to edit, change, or re-send the event again.
 * <li>Events should follow a pattern of public-final POD (plain old rawData) objects, where they are
 * initialized by the constructor and read by each subscriber of that event.  Subscribers should
 * never alter events as they are processed, and this enforces that pattern.
 * </ul>
 *
 * <p>
 * Future optimizations:
 * <li>throw exception/log when a subscriber loses the prefReference
 * <li>trace cost per registration & invocation
 * <li>trace cross-process invocation
 * <li>register(subscriber, Class&lt;?&gt;...) -- pass in exact class types you want registered
 * <li>setSubscriberEventHandlerPriority(subscriber, Class<Event>, priority)
 * <li>allow subscribers to implement interface, ie. EventBus.Subscriber, which lets then test a
 * message before invocation (ie. check if task id == this task id)
 * <li>add postOnce() which automatically debounces
 * <li>add postDelayed() which delays / postDelayedOnce() which delays and bounces
 * <li>consolidate register() and registerInterprocess()
 * <li>sendForResult&lt;ReturnType&gt;(Event) to send and get a result, but who will send the
 * result?
 * </p>
 */
public class EventBus extends BroadcastReceiver {

    public static final String TAG = "EventBus";
    public static final int SPECIAL_SUBSCRIBER_PRIORITY = 0;
    /**
     * Proguard must also know, and keep, all methods matching this signature.
     * <p>
     * -keepclassmembers class ** { public void onBusEvent(**); public void
     * onInterprocessBusEvent(**); }
     */
    private static final String METHOD_PREFIX = "onBusEvent";
    private static final String INTERPROCESS_METHOD_PREFIX = "onInterprocessBusEvent";
    // Ensures that interprocess events can only be sent from a process holding this permission. */
    private static final String PERMISSION_SELF = "com.android.systemui.permission.SELF";
    // Used for passing event rawData across process boundaries
    private static final String EXTRA_INTERPROCESS_EVENT_BUNDLE = "interprocess_event_bundle";
    // The default priority of all subscribers
    private static final int DEFAULT_SUBSCRIBER_PRIORITY = 1;
    // Orders the handlers by priority and registration time
    private static final Comparator<EventHandler> EVENT_HANDLER_COMPARATOR = new Comparator<EventHandler>() {
        @Override
        public int compare(EventHandler h1, EventHandler h2) {
            // Rank the handlers by priority descending, followed by registration time descending.
            // aka. the later registered
            return h2.priority - h1.priority;
        }
    };
    // Used for initializing the default bus
    private static final Object sLock = new Object();
    private static EventBus sDefaultBus;
    // The handler to post all events
    private Handler mHandler;
    // Keep track of whether we have registered a broadcast receiver already, so that we can
    // unregister ourselves before re-registering again with a new IntentFilter.
    private boolean mHasRegisteredReceiver;
    /**
     * Map from event class -> event handler list.  Keeps track of the actual mapping from event to
     * subscriber method.
     */
    private HashMap<Class<? extends Event>, ArrayList<EventHandler>> mEventTypeMap = new HashMap<>();
    /**
     * Map from subscriber class -> event handler method lists.  Used to determine upon registration
     * of a new subscriber whether we need to read all the subscriber's methods again using reflection
     * or whether we can just add the subscriber to the event type map.
     */
    private HashMap<Class<? extends Object>, ArrayList<EventHandlerMethod>> mSubscriberTypeMap = new HashMap<>();
    /**
     * Map from interprocess event name -> interprocess event class.  Used for mapping the event name
     * after receiving the broadcast, to the event type.  After which a new instance is created and
     * posted in the local process.
     */
    private HashMap<String, Class<? extends InterprocessEvent>> mInterprocessEventNameMap = new HashMap<>();
    /**
     * Set of all currently registered subscribers
     */
    private ArrayList<Subscriber> mSubscribers = new ArrayList<>();

    public EventBus() {
        mHandler = new Handler(Looper.getMainLooper());
    }

    /**
     * Private constructor to create an event bus for a given looper.
     */
    private EventBus(Looper looper) {
        mHandler = new Handler(looper);
    }

    /**
     * @return the default event bus for the application's main thread.
     */
    public static EventBus getDefault() {
        if (sDefaultBus == null) {
            synchronized (sLock) {
                if (sDefaultBus == null) {
                    sDefaultBus = new EventBus(Looper.getMainLooper());
                }
            }
        }
        return sDefaultBus;
    }

    /**
     * Registers a subscriber to receive events with the default priority.
     *
     * @param subscriber the subscriber to handle events.  If this is the first instance of the
     *                   subscriber's class type that has been registered, the class's methods will be scanned for
     *                   appropriate event handler methods.
     */
    public void register(Object subscriber) {
        registerSubscriber(subscriber, DEFAULT_SUBSCRIBER_PRIORITY, null);
    }

    /**
     * Registers a subscriber to receive events with the given priority.
     *
     * @param subscriber the subscriber to handle events.  If this is the first instance of the
     *                   subscriber's class type that has been registered, the class's methods will be scanned for
     *                   appropriate event handler methods.
     * @param priority   the priority that this subscriber will receive events relative to other
     *                   subscribers
     */
    public void register(Object subscriber, int priority) {
        registerSubscriber(subscriber, priority, null);
    }

    /**
     * Explicitly registers a subscriber to receive interprocess events with the default priority.
     *
     * @param subscriber the subscriber to handle events.  If this is the first instance of the
     *                   subscriber's class type that has been registered, the class's methods will be scanned for
     *                   appropriate event handler methods.
     */
    public void registerInterprocessAsCurrentUser(Context context, Object subscriber) {
        registerInterprocessAsCurrentUser(context, subscriber, DEFAULT_SUBSCRIBER_PRIORITY);
    }

    /**
     * Registers a subscriber to receive interprocess events with the given priority.
     *
     * @param subscriber the subscriber to handle events.  If this is the first instance of the
     *                   subscriber's class type that has been registered, the class's methods will be scanned for
     *                   appropriate event handler methods.
     * @param priority   the priority that this subscriber will receive events relative to other
     *                   subscribers
     */
    public void registerInterprocessAsCurrentUser(Context context, Object subscriber,
                                                  int priority) {
        // Register the subscriber normally, and update the broadcast receiver filterUpdate if this is
        // a new subscriber type with interprocess events
        MutableBoolean hasInterprocessEventsChanged = new MutableBoolean(false);
        registerSubscriber(subscriber, priority, hasInterprocessEventsChanged);
        if (hasInterprocessEventsChanged.value) {
            registerReceiverForInterprocessEvents(context);
        }
    }

    /**
     * Remove all EventHandlers pointing to the specified subscriber.  This does not remove the
     * mapping of subscriber type to event handler method, in case new instances of this subscriber
     * are registered.
     */
    public void unregister(Object subscriber) {
        // Fail immediately if we are being called from the non-main thread
        long callingThreadId = Thread.currentThread().getId();
        if (callingThreadId != mHandler.getLooper().getThread().getId()) {
            throw new RuntimeException("Can not unregister() a subscriber from a non-main thread.");
        }
        // Return early if this is not a registered subscriber
        if (!findRegisteredSubscriber(subscriber, true /* removeFoundSubscriber */)) {
            return;
        }
        Class<?> subscriberType = subscriber.getClass();
        ArrayList<EventHandlerMethod> subscriberMethods = mSubscriberTypeMap.get(subscriberType);
        if (subscriberMethods != null) {
            // For each of the event handlers the subscriber handles, remove all references of that
            // handler
            for (EventHandlerMethod method : subscriberMethods) {
                ArrayList<EventHandler> eventHandlers = mEventTypeMap.get(method.eventType);
                for (int i = eventHandlers.size() - 1; i >= 0; i--) {
                    if (eventHandlers.get(i).subscriber.getReference() == subscriber) {
                        eventHandlers.remove(i);
                    }
                }
            }
        }
    }

    /**
     * Sends an event to the subscribers of the given event type immediately.  This can only be called
     * from the same thread as the EventBus's looper thread (for the default EventBus, this is the
     * main application thread).
     */
    public void send(Event event) {
        // Fail immediately if we are being called from the non-main thread
        long callingThreadId = Thread.currentThread().getId();
        if (callingThreadId != mHandler.getLooper().getThread().getId()) {
            throw new RuntimeException("Can not send() a message from a non-main thread.");
        }
        // Reset the event's cancelled state
        event.requiresPost = false;
        event.cancelled = false;
        queueEvent(event);
    }

    public void post(Event event) {
        // Reset the event's cancelled state
        event.requiresPost = true;
        event.cancelled = false;
        queueEvent(event);
    }

    /**
     * Receiver for interprocess events.
     */
    @Override
    public void onReceive(Context context, Intent intent) {
        Bundle eventBundle = intent.getBundleExtra(EXTRA_INTERPROCESS_EVENT_BUNDLE);
        Class<? extends InterprocessEvent> eventType = mInterprocessEventNameMap
                .get(intent.getAction());
        try {
            Constructor<? extends InterprocessEvent> ctor = eventType.getConstructor(Bundle.class);
            send((Event) ctor.newInstance(eventBundle));
        } catch (NoSuchMethodException |
                InvocationTargetException |
                InstantiationException |
                IllegalAccessException e) {
            Log.e(TAG, "Failed to create InterprocessEvent", e);
        }
    }

    /**
     * Registers a new subscriber.
     *
     * @return return whether or not this
     */
    private void registerSubscriber(Object subscriber, int priority,
                                    MutableBoolean hasInterprocessEventsChangedOut) {
        // Fail immediately if we are being called from the non-main thread
        long callingThreadId = Thread.currentThread().getId();
        if (callingThreadId != mHandler.getLooper().getThread().getId()) {
            throw new RuntimeException("Can not register() a subscriber from a non-main thread.");
        }
        // Return immediately if this exact subscriber is already registered
        if (findRegisteredSubscriber(subscriber, false /* removeFoundSubscriber */)) {
            return;
        }
        Subscriber sub = new Subscriber(subscriber);
        Class<?> subscriberType = subscriber.getClass();
        ArrayList<EventHandlerMethod> subscriberMethods = mSubscriberTypeMap.get(subscriberType);
        if (subscriberMethods != null) {
            // If we've parsed this subscriber type before, just add to the set for all the known
            // events
            for (EventHandlerMethod method : subscriberMethods) {
                ArrayList<EventHandler> eventTypeHandlers = mEventTypeMap.get(method.eventType);
                eventTypeHandlers.add(new EventHandler(sub, method, priority));
                sortEventHandlersByPriority(eventTypeHandlers);
            }
            mSubscribers.add(sub);
            return;
        } else {
            // If we are parsing this type from scratch, ensure we add it to the subscriber type
            // map, and pull out he handler methods below
            subscriberMethods = new ArrayList<>();
            mSubscriberTypeMap.put(subscriberType, subscriberMethods);
            mSubscribers.add(sub);
        }
        // Find all the valid event bus handler methods of the subscriber
        MutableBoolean isInterprocessEvent = new MutableBoolean(false);
        Method[] methods = subscriberType.getDeclaredMethods();
        for (Method m : methods) {
            Class<?>[] parameterTypes = m.getParameterTypes();
            isInterprocessEvent.value = false;
            if (isValidEventBusHandlerMethod(m, parameterTypes, isInterprocessEvent)) {
                Class<? extends Event> eventType = (Class<? extends Event>) parameterTypes[0];
                ArrayList<EventHandler> eventTypeHandlers = mEventTypeMap.get(eventType);
                if (eventTypeHandlers == null) {
                    eventTypeHandlers = new ArrayList<>();
                    mEventTypeMap.put(eventType, eventTypeHandlers);
                }
                if (isInterprocessEvent.value) {
                    try {
                        // Enforce that the event must have a Bundle constructor
                        eventType.getConstructor(Bundle.class);
                        mInterprocessEventNameMap.put(eventType.getName(),
                                (Class<? extends InterprocessEvent>) eventType);
                        if (hasInterprocessEventsChangedOut != null) {
                            hasInterprocessEventsChangedOut.value = true;
                        }
                    } catch (NoSuchMethodException e) {
                        throw new RuntimeException(
                                "Expected InterprocessEvent to have a Bundle constructor");
                    }
                }
                EventHandlerMethod method = new EventHandlerMethod(m, eventType);
                EventHandler handler = new EventHandler(sub, method, priority);
                eventTypeHandlers.add(handler);
                subscriberMethods.add(method);
                sortEventHandlersByPriority(eventTypeHandlers);
            }
        }
    }

    /**
     * Adds a new message.
     */
    private void queueEvent(final Event event) {
        try {
            ArrayList<EventHandler> eventHandlers = mEventTypeMap.get(event.getClass());
            if (eventHandlers == null) {
                return;
            }
            // We need to clone the list in case a subscriber unregisters itself during traversal
            eventHandlers = (ArrayList<EventHandler>) eventHandlers.clone();
            for (final EventHandler eventHandler : eventHandlers) {
                if (eventHandler.subscriber != null && eventHandler.subscriber.getReference() != null) {
                    if (event.requiresPost) {
                        mHandler.post(() -> processEvent(eventHandler, event));
                    } else {
                        processEvent(eventHandler, event);
                    }
                }
            }
        } catch (ConcurrentModificationException e) {
            e.printStackTrace();
        }
    }

    /**
     * Processes and dispatches the given event to the given event handler, on the thread of whoever
     * calls this method.
     */
    private void processEvent(final EventHandler eventHandler, final Event event) {
        // Skip if the event was already cancelled
        if (event.cancelled) {
            return;
        }
        try {
            Object sub = eventHandler.subscriber.getReference();
            if (sub != null) {
                eventHandler.method.invoke(sub, event);
            } else {
                Log.e(TAG, "Failed to deliver event to null subscriber");
            }
        } catch (IllegalAccessException | NullPointerException e) {
            Log.e(TAG, "Failed to invoke method", e);
        } catch (InvocationTargetException e) {
            throw new RuntimeException(e.getCause());
        }
    }

    /**
     * Re-registers the broadcast receiver for any new messages that we want to listen for.
     */
    private void registerReceiverForInterprocessEvents(Context context) {
        // Rebuild the receiver filterUpdate with the new interprocess events
        IntentFilter filter = new IntentFilter();
        for (String eventName : mInterprocessEventNameMap.keySet()) {
            filter.addAction(eventName);
        }
        // Re-register the receiver with the new filterUpdate
        if (mHasRegisteredReceiver) {
            context.unregisterReceiver(this);
        }
        mHasRegisteredReceiver = true;
    }

    /**
     * Returns whether this subscriber is currently registered.  If {@param removeFoundSubscriber} is
     * true, then remove the subscriber before returning.
     */
    private boolean findRegisteredSubscriber(Object subscriber, boolean removeFoundSubscriber) {
        for (int i = mSubscribers.size() - 1; i >= 0; i--) {
            Subscriber sub = mSubscribers.get(i);
            if (sub.getReference() == subscriber) {
                if (removeFoundSubscriber) {
                    mSubscribers.remove(i);
                }
                return true;
            }
        }
        return false;
    }

    /**
     * @return whether {@param method} is a valid (normal or interprocess) event bus handler method
     */
    private boolean isValidEventBusHandlerMethod(Method method, Class<?>[] parameterTypes,
                                                 MutableBoolean isInterprocessEventOut) {
        int modifiers = method.getModifiers();
        if (Modifier.isPublic(modifiers) &&
                Modifier.isFinal(modifiers) &&
                method.getReturnType().equals(Void.TYPE) &&
                parameterTypes.length == 1) {
            if (InterprocessEvent.class.isAssignableFrom(parameterTypes[0]) &&
                    method.getName().startsWith(INTERPROCESS_METHOD_PREFIX)) {
                isInterprocessEventOut.value = true;
                return true;
            } else if (Event.class.isAssignableFrom(parameterTypes[0]) &&
                    method.getName().startsWith(METHOD_PREFIX)) {
                isInterprocessEventOut.value = false;
                return true;
            }
        }
        return false;
    }

    /**
     * Sorts the event handlers by priority and registration time.
     */
    private void sortEventHandlersByPriority(List<EventHandler> eventHandlers) {
        Collections.sort(eventHandlers, EVENT_HANDLER_COMPARATOR);
    }

    /**
     * An event super class that allows us to track internal event state across subscriber
     * invocations.
     * <p>
     * Events should not be edited by subscribers.
     */
    public static class Event implements Cloneable {

        // Indicates that this event's dispatch should be traced and logged to logcat
        boolean trace;
        // Indicates that this event must be posted on the EventBus's looper thread before invocation
        boolean requiresPost;
        // Not currently exposed, allows a subscriber to cancel further dispatch of this event
        boolean cancelled;

        // Only accessible from derived events
        protected Event() {
        }

        @Override
        protected Object clone() throws CloneNotSupportedException {
            Event evt = (Event) super.clone();
            // When cloning an event, reset the cancelled-dispatch state
            evt.cancelled = false;
            return evt;
        }
    }

    /**
     * An inter-process event super class that allows us to track user state across subscriber
     * invocations.
     */
    public static class InterprocessEvent extends Event {

        private static final String EXTRA_USER = "_user";
        // The user which this event originated from
        public final int user;

        // Only accessible from derived events
        protected InterprocessEvent(int user) {
            this.user = user;
        }

        /**
         * Called from the event bus
         */
        protected InterprocessEvent(Bundle b) {
            user = b.getInt(EXTRA_USER);
        }

        protected Bundle toBundle() {
            Bundle b = new Bundle();
            b.putInt(EXTRA_USER, user);
            return b;
        }
    }
}