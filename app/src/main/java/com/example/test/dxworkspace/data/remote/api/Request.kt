package com.example.test.dxworkspace.data.remote.api

//import io.sentry.Sentry
//import io.sentry.event.BreadcrumbBuilder
import android.util.Log
import com.example.test.dxworkspace.core.exception.Failure
import com.example.test.dxworkspace.core.extensions.Either
import retrofit2.Call
import retrofit2.Response


fun <T, R> requestApi(call: Call<T>, transform: (T) -> R, default: R, sendLog: Boolean = true): Either<Failure, R> {
    return try {
        val response = call.execute()

        when (response.isSuccessful) {
            true -> {
                response.body()?.let {
                    Either.Right(transform(it))
                } ?: Either.Right(default)
            }
            else -> {
                if (response.code() == 401) {
//                    EventBus.getDefault().post(SPEventSyncMessage(SPEventSyncMessage.SYNC_UNAUTHORIZED))
                }
                Either.Left(Failure.ServerError(response.code(), logSentry(call, response, sendLog)))
            }
        }
    } catch (exception: Throwable) {
        logError(exception)
    }
}

fun <T, R, I> requestApiLoadMore(list: List<I>, genCall: (param: List<I>) -> Call<T>, transform: (T) -> List<R>, default: List<R>): Either<Failure, List<R>> {
    return try {
        var index = 0
        val result = mutableListOf<R>()
        while (index < list.size) {
            val subList = if (index + 50 > list.size) {
                list.subList(index, list.size)
            } else {
                list.subList(index, index + 50)
            }
            index += 50

            val call = genCall(subList)
            val response = call.execute()

            when (response.isSuccessful) {
                true -> {
                    response.body()?.let {
                        result.addAll(transform(response.body()!!))
                    }
                }
                else -> {
                    if (response.code() == 401) {
//                        EventBus.getDefault().post(SPEventSyncMessage(SPEventSyncMessage.SYNC_UNAUTHORIZED))
                    }

                    return Either.Left(Failure.ServerError(response.code(), logSentry(call, response)))
                }
            }
        }
        return Either.Right(result)
    } catch (exception: Throwable) {
        logError(exception)
    }
}

suspend fun <T, R, I> requestApiLoadMoreAsync(list: List<I>, genCall: (param: List<I>) -> Call<T>, transform: suspend (T) -> List<R>): Either<Failure, List<R>> {
    return try {
        var index = 0
        val result = mutableListOf<R>()
        while (index < list.size) {
            val subList = if (index + 50 > list.size) {
                list.subList(index, list.size)
            } else {
                list.subList(index, index + 50)
            }
            index += 50

            val call = genCall(subList)
            val response = call.execute()

            when (response.isSuccessful) {
                true -> {
                    response.body()?.let {
                        result.addAll(transform(response.body()!!))
                    }
                }
                else -> {
                    if (response.code() == 401) {
//                        EventBus.getDefault().post(SPEventSyncMessage(SPEventSyncMessage.SYNC_UNAUTHORIZED))
                    }

                    return Either.Left(Failure.ServerError(response.code(), logSentry(call, response)))
                }
            }
        }
        return Either.Right(result)
    } catch (exception: Throwable) {
        logError(exception)
    }
}

suspend fun <T, R> requestApiAsyn(call: Call<T>, transform: suspend (T) -> R, default: R): Either<Failure, R> {
    return try {
        val response = call.execute()
        when (response.isSuccessful) {
            true -> {
                response.body()?.let {
                    Either.Right(transform(it))
                } ?: Either.Right(default)
            }
            else -> {
                if (response.code() == 401) {
//                    EventBus.getDefault().post(SPEventSyncMessage(SPEventSyncMessage.SYNC_UNAUTHORIZED))
                }

                Either.Left(Failure.ServerError(response.code(), logSentry(call, response)))
            }
        }
    } catch (exception: Throwable) {
        logError(exception)
    }
}

fun <T, R> requestApiWithTime(call: Call<T>, transform: (T, time: Long) -> R, default: R): Either<Failure, R> {
    return try {
        val response = call.execute()
        when (response.isSuccessful) {
            true -> {
                response.body()?.let {
                    val currentTime = response.headers()["x-fnb-now"]?.toLong()
                    Either.Right(transform(it, (
                            currentTime ?: System.currentTimeMillis())))
                } ?: Either.Right(default)
            }
            else -> {
                if (response.code() == 401) {
//                    EventBus.getDefault().post(SPEventSyncMessage(SPEventSyncMessage.SYNC_UNAUTHORIZED))
                }

                Either.Left(Failure.ServerError(response.code(), logSentry(call, response)))
            }
        }
    } catch (exception: Throwable) {
        logError(exception)
    }
}


fun <T> requestApi(call: Call<T>, default: T): Either<Failure, T> {
    return try {
        val response = call.execute()
        when (response.isSuccessful) {
            true -> {
                response.body()?.let {
                    Either.Right(it)
                } ?: Either.Right(default)
            }
            else -> {
                if (response.code() == 401) {
//                    EventBus.getDefault().post(SPEventSyncMessage(SPEventSyncMessage.SYNC_UNAUTHORIZED))
                }

                Either.Left(Failure.ServerError(response.code(), logSentry(call, response)))
            }
        }
    } catch (exception: Throwable) {
        logError(exception)
    }
}

suspend fun <T, R> requestApiWithoutFailure(call: Call<T>, transform: suspend (T) -> R, default: R): R {
    return try {
        val response = call.execute()
        when (response.isSuccessful) {
            true -> {
                response.body()?.let {
                    transform(it)
                } ?: default
            }
            else -> {
                if (response.code() == 401) {
//                    EventBus.getDefault().post(SPEventSyncMessage(SPEventSyncMessage.SYNC_UNAUTHORIZED))
                }

                default
            }
        }
    } catch (exception: Throwable) {
        exception.printStackTrace()
        default
    }
}

suspend fun <T, R> requestApiWithVersion(call: Call<T>, transform: suspend (T, Int) -> R, default: R): Either<Failure, R> {
    return try {
        var response = call.execute()

        when (response.isSuccessful) {
            true -> {
                response.body()?.let {
                    val version = response.headers()["x-version"]?.toInt() ?: 0
                    Either.Right(transform(it, version))
                } ?: Either.Right(default)
            }
            else -> {
                if (response.code() == 401) {
//                    EventBus.getDefault().post(SPEventSyncMessage(SPEventSyncMessage.SYNC_UNAUTHORIZED))
                }

                val errorResponse = if (response.errorBody() != null) {
                    String(response.errorBody()!!.bytes())
                } else {
                    ""
                }

                Either.Left(Failure.ServerError(response.code(), errorResponse))
            }
        }
    } catch (exception: Throwable) {
        logError(exception)
    }
}

fun <T, R> requestApiWithoutFailureNonAsyn(call: Call<T>, transform: (T, time: Long) -> R, default: R): R {
    return try {
        val response = call.execute()
        when (response.isSuccessful) {
            true -> {
                response.body()?.let {
                    val currentTime = response.headers()["x-fnb-now"]?.toLong()
                    transform(it, (currentTime ?: System.currentTimeMillis()))
                } ?: default
            }
            else -> {
                if (response.code() == 401) {
//                    EventBus.getDefault().post(SPEventSyncMessage(SPEventSyncMessage.SYNC_UNAUTHORIZED))
                }

                default
            }
        }
    } catch (exception: Throwable) {
        logError(exception)
        default
    }
}


fun <T> requestApiNotResponseWithoutFailure(call: Call<T>): Boolean {
    return try {
        val response = call.execute().code()
        when (response == 200) {
            true -> true
            else -> false
        }
    } catch (exception: Throwable) {
        logError(exception)
        false
    }
}

fun <T> requestApiWithFailure(call: Call<T>, sendLog: Boolean = true): Failure? {
    return try {
        val response = call.execute()
        return when (response.code() == 200 || response.code() == 201) {
            true -> null
            else -> {
                Failure.ServerError(response.code(), logSentry(call, response, sendLog))
            }
        }
    } catch (exception: Throwable) {
        logError(exception).getFailure()
    }
}

fun <T, R> requestDb(call: T, transform: (T) -> R, default: R): Either<Failure, R> {
    return try {
        return call?.let {
            Either.Right(transform(call))
        } ?: Either.Right(default)
    } catch (exception: Throwable) {
        logError(exception)
    }
}

fun <T, R> requestDBWithoutFailure(call: T, transform: (T) -> R, default: R): R {
    return try {
        return call?.let {
            transform(it)
        } ?: default
    } catch (exception: Throwable) {
        exception.printStackTrace()
        logError(exception)
        default
    }
}

suspend fun <T, R, I> requestDBLoadMoreWithoutFailure(list: List<I>, query: suspend (param: List<I>) -> T, transform: suspend (T) -> List<R>, default: List<R>): List<R> {
    try {
        var index = 0
        val result = mutableListOf<R>()
        while (index < list.size) {
            val subList = if (index + 100 > list.size) {
                list.subList(index, list.size)
            } else {
                list.subList(index, index + 100)
            }
            index += 100

            val subResult = query(subList)

            subResult?.let {
                result.addAll(transform(subResult))
            }
        }
        return result
    } catch (exception: Throwable) {
        logError(exception)
        return default
    }
}

suspend fun <I> updateDBLoadMore(list: List<I>, query: suspend (param: List<I>) -> Unit) {
    try {
        var index = 0
        while (index < list.size) {
            val subList = if (index + 100 > list.size) {
                list.subList(index, list.size)
            } else {
                list.subList(index, index + 100)
            }
            index += 100

            query(subList)
        }
    } catch (exception: Throwable) {
        logError(exception)
    }
}


fun logError(exception: Throwable): Either.Left<Failure> {
    Log.d("KGM", exception.printStackTrace().toString())
    return Either.Left(Failure.ServerError(404, "[Error Server!]|==>${exception.message}"))
}

fun <T> logSentry(call: Call<T>, response: Response<T>, sendLog: Boolean = true): String {
    return "error"
}