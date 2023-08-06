package com.example.test.dxworkspace.presentation.ui.home.workplace

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.View
import androidx.core.os.bundleOf
import androidx.core.view.GravityCompat
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.test.dxworkspace.BuildConfig
import com.example.test.dxworkspace.R
import com.example.test.dxworkspace.core.extensions.observe
import com.example.test.dxworkspace.core.extensions.viewModel
import com.example.test.dxworkspace.data.di.NetworkModule
import com.example.test.dxworkspace.data.entity.login.CompanyModel
import com.example.test.dxworkspace.data.local.database.RealmManager
import com.example.test.dxworkspace.data.local.preferences.AppPreferences.set
import com.example.test.dxworkspace.data.local.preferences.AppPreferences.get
import com.example.test.dxworkspace.databinding.FragmentWorkplaceBinding
import com.example.test.dxworkspace.domain.model.task.RequestTaskModel
import com.example.test.dxworkspace.domain.repository.ConfigRepository
import com.example.test.dxworkspace.presentation.model.menu.MenuModel
import com.example.test.dxworkspace.presentation.ui.BaseFragment
import com.example.test.dxworkspace.presentation.ui.home.manufacturing.works.ManufacturingWorkFragment
import com.example.test.dxworkspace.presentation.ui.home.workplace.adapter.LeftMenuAdapter
import com.example.test.dxworkspace.presentation.ui.home.workplace.adapter.TaskAdapter
import com.example.test.dxworkspace.presentation.ui.home.workplace.detail_task.DetailTaskFragment
import com.example.test.dxworkspace.presentation.ui.home.workplace.time_picker.RangeMonthFragment
import com.example.test.dxworkspace.presentation.ui.splash.SplashActivity
import com.example.test.dxworkspace.presentation.utils.common.*
import com.example.test.dxworkspace.presentation.utils.event.*
import com.example.test.dxworkspace.presentation.utils.getDateTimer
import com.example.test.dxworkspace.presentation.utils.getTimeInMillisFromString
import org.greenrobot.eventbus.Subscribe
import java.util.*
import javax.inject.Inject
import android.os.Looper
import com.example.test.dxworkspace.core.extensions.GetInfoDevice
import com.example.test.dxworkspace.core.extensions.justTry
import com.example.test.dxworkspace.data.entity.good.InventoryGoodWrap
import com.example.test.dxworkspace.data.entity.notify.ParamGetPageNotify
import com.example.test.dxworkspace.data.entity.timesheet.TimeSheetLogResponse
import com.example.test.dxworkspace.presentation.model.menu.MenuSample
import com.example.test.dxworkspace.presentation.model.menu.TaskType
import com.example.test.dxworkspace.presentation.ui.dialog.DialogNotification
import com.example.test.dxworkspace.presentation.ui.home.HomeViewModel
import com.example.test.dxworkspace.presentation.ui.home.manufacturing.command.ManufacturingCommandFragment
import com.example.test.dxworkspace.presentation.ui.home.manufacturing.dashboard.control.DashboardControlManufacturingFragment
import com.example.test.dxworkspace.presentation.ui.home.manufacturing.dashboard.quality.DashboardQualityManufacturingFragment
import com.example.test.dxworkspace.presentation.ui.home.manufacturing.lot.ManufacturingLotFragment
import com.example.test.dxworkspace.presentation.ui.home.manufacturing.mill.ManufacturingMillFragment
import com.example.test.dxworkspace.presentation.ui.home.manufacturing.plan.ManufacturingPlanFragment
import com.example.test.dxworkspace.presentation.ui.home.manufacturing.request.ManufacturingRequestFragment
import com.example.test.dxworkspace.presentation.ui.home.manufacturing.work_schedule.WorkScheduleFragment
import com.example.test.dxworkspace.presentation.ui.home.report.financial.ReportFinancialFragment
import com.example.test.dxworkspace.presentation.ui.home.report.manufacturing.ReportManufacturingFragment
import com.example.test.dxworkspace.presentation.ui.home.report.sale.ReportSaleFragment
import com.example.test.dxworkspace.presentation.ui.home.report.warehouse.ReportWarehouseFragment
import com.example.test.dxworkspace.presentation.ui.home.workplace.adapter.TaskTypeAdapter
import com.example.test.dxworkspace.presentation.ui.home.workplace.create_task.CreateTaskFragment
import com.example.test.dxworkspace.presentation.ui.home.workplace.notify.NotifyFragment
import com.example.test.dxworkspace.presentation.utils.convertFromUTCtoLocal
import com.google.android.material.tabs.TabLayout
import com.google.gson.reflect.TypeToken


class WorkplaceFragment : BaseFragment<FragmentWorkplaceBinding>() {


    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    @Inject
    lateinit var viewModel: WorkplaceViewModel

    @Inject
    lateinit var configRepository: ConfigRepository

    @Inject
    lateinit var homeViewModel : HomeViewModel

    val taskTypeAdapter by lazy { TaskTypeAdapter() }
    var taskTypeFilter = 0

    var timer = Timer()

    var fromMonth = "6-2023"
    var toMonth = "6-2023"

    val menuAdapter by lazy { LeftMenuAdapter() }
    val taskAdapter by lazy { TaskAdapter() }
    var listMenu = mutableListOf<MenuModel>()

    override fun getLayoutId(): Int = R.layout.fragment_workplace

    override fun onStart() {
        super.onStart()
        EventBus.getDefault().register(this)
        registerGreen()
    }

    override fun onStop() {
        super.onStop()
        EventBus.getDefault().unregister(this)
        unregisterGreen()
    }

    @Subscribe
    fun onBusEventGreen(event: EventSyncMessage) {

    }

    fun onBusEvent(event: EventSyncMessage) {
        when (event.type) {
            EventSyncMessage.SYNC_FIRST -> {
                viewModel.getLinksCanAccess()
            }
//            EventSyncMessage.SYNC_PRIVILEGE -> {
//                viewModel.getLinksCanAccess()
//            }
            EventSyncMessage.SYNC_USER -> {
                setupMenuHeader()
                viewModel.getLinksCanAccess()
            }

            else -> {}
        }
    }

    fun onBusEvent(event: EventUpdate) {
        when (event.type) {
            EventUpdate.UPDATE_TIME_FILTER_HOME -> {
                val p = event.value as Pair<*, *>
                fromMonth = p.first.toString()
                toMonth = p.second.toString()
                binding?.tvRangeTime?.text = fromMonth + " - " + toMonth
                getAllTask()
            }
            EventUpdate.UPDATE_TIMER -> {
                if (event.value as Boolean == true) {
//                    sharedPreferences[Constants.START_TIME_COUNT] = getDateTimer()
                    startTimer()
                    showToast(EventToast(isFail = false, text = "Bắt đầu bấm giờ"))
                } else {
                    showToast(EventToast(isFail = false, text = "Đã dừng bấm giờ"))
                    stopTimer()
                }
            }
            EventUpdate.UPDATE_TIMESHEET -> {
                println("UPDATE_TIMESHEETTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTT")
                justTry {
                    val t = event.value as TimeSheetLogResponse
                    if(t._id.isEmpty()) {
                        if(sharedPreferences[Constants.IS_COUNTING,false] == true) {
                            stopTimer()
                            sharedPreferences[Constants.START_TIME_COUNT] = ""
                            sharedPreferences[Constants.TIMERID_COUNTING] = ""
                        }
                    }
                    else {
                        if(sharedPreferences[Constants.IS_COUNTING,false] == true && sharedPreferences[Constants.TIMERID_COUNTING,""] == t.timesheetLogs?.first()?._id) {

                        } else {
                            sharedPreferences[Constants.IS_COUNTING] = true
                            sharedPreferences[Constants.TASK_ID_COUNTING] = t._id
                            sharedPreferences[Constants.TIMERID_COUNTING] = t.timesheetLogs?.first()?._id
                            sharedPreferences[Constants.START_TIME_COUNT] = convertFromUTCtoLocal(t.timesheetLogs?.first()?.startedAt ?: "")
                            startTimer()
                        }
                    }
                }
            }
            EventUpdate.UPDATE_COUNT_NOTIFY -> {
                val total = event.value as Int
                binding?.apply {
                    val count = total
                    frameTotalCart.isVisible = count > 0
                    binding?.tvCartCount?.text = if (count < 100) count.toString() else "${count}+"
                }
            }

        }
    }

    fun onBusEvent(event : EventGoToNotification){
        when(event.type){
            EventGoToNotification.DIALOG_DETAIL -> {
                val noti = event.message ?: listOf()
                showDialogNotification(noti.getOrNull(0) ?: "" , noti.getOrNull(1) ?: "")
            }
            EventGoToNotification.DETAIL_TASK -> {
                postNormal(EventNextHome(WorkplaceFragment::class.java))
            }
            EventGoToNotification.DETAIL_REQUEST -> {
                postNormal(EventNextHome(ManufacturingRequestFragment::class.java))
            }
            else -> {}
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = viewModel(viewModelFactory) {
            observe(linksCanAccess) {
                setUpMenu()
            }
            observe(logoutSuccess) {
                if (it == true) {
                    sharedPreferences[Constants.APLogin.IS_LOGGED] = false
                    RealmManager.closeDb()
                    sharedPreferences[Constants.APLogin.TOKEN] = ""
                    sharedPreferences[Constants.APLogin.IS_LOGGED] = false
                    sharedPreferences[Constants.APLogin.CURRENT_USER] = ""
                    sharedPreferences[Constants.APLogin.CURRENT_ROLE] = ""
                    sharedPreferences[Constants.APLogin.CURRENT_ROLE_ID] = ""
                    sharedPreferences[Constants.APLogin.CURRENT_PAGE] = ""
                    sharedPreferences[Constants.APLogin.LOGIN_RESPONSE_INFO] = ""
                    val intent = Intent(activity, SplashActivity::class.java)
                    startActivity(intent)
                    activity?.finishAffinity()
                } else {
                    postNormal(EventToast(isFail = true, textId = R.string.error_notification))
                }
            }
            observe(isLoadTaskSuccess) {
                binding?.pullToRefresh?.isRefreshing = false
                setupRcvTask()
            }
            observe(notifyResponse) {
                binding?.apply {
                    val count = it?.docs?.filter { !it.readed }?.size ?: 0
                    frameTotalCart.isVisible = count > 0
                    binding?.tvCartCount?.text = if (count < 100) count.toString() else "${count}+"
                }
            }

        }
        observeLoading(viewModel)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        homeViewModel.getTaskTimeSheetLog()
        if (sharedPreferences[Constants.IS_COUNTING, false] == true) startTimer()
        // init time filter task
        val calendar = Calendar.getInstance()
        val month = calendar.get(Calendar.MONTH) + 1
        val year = calendar.get(Calendar.YEAR)
        fromMonth = "$month-$year"
        toMonth = fromMonth
        setupRcvTaskType()
        binding?.apply {
            tvRangeTime.text = fromMonth + " - " + toMonth
            toolbar.setNavigationOnClickListener {
                layoutDrawer.openDrawer(GravityCompat.START)
            }
            tabTaskType.addTab(tabTaskType.newTab().setText("Cần làm"),0)
            tabTaskType.addTab(tabTaskType.newTab().setText("Phê duyệt"),1)
            tabTaskType.addTab(tabTaskType.newTab().setText("Tư vấn"),2)
            tabTaskType.addTab(tabTaskType.newTab().setText("Giám sát"),3)
            tabTaskType.addTab(tabTaskType.newTab().setText("Đã tạo"),4)
            tabTaskType.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
                override fun onTabSelected(tab: TabLayout.Tab?) {
                    onChangeFilter(tab?.position ?: 0)
                }

                override fun onTabUnselected(tab: TabLayout.Tab?) {
//                    TODO("Not yet implemented")
                }

                override fun onTabReselected(tab: TabLayout.Tab?) {
//                    TODO("Not yet implemented")
                }

            })
            layoutLeftMenu.layoutLogout.setOnClickListener {
                viewModel.logout()
            }
            tvRangeTime.setOnClickListener {
                val bundle = Bundle()
                bundle.putString("FROM_MONTH", fromMonth)
                bundle.putString("TO_MONTH", toMonth)
                postNormal(EventNextHome(RangeMonthFragment::class.java, bundle))
            }
            pullToRefresh.setOnRefreshListener {
                getAllTask()
            }
            layoutLeftMenu.rcvMenu.adapter = menuAdapter
            rcvTask.adapter = taskAdapter
            rcvTask.addItemDecoration(
                DividerItemDecoration(
                    requireContext(),
                    LinearLayoutManager.VERTICAL
                )
            )
            tvTimer.setOnClickListener {
                postNormal(EventNextHome(TimeSheetFragment::class.java))
            }
            btnCreateNewTask.setOnClickListener {
                postNormal(EventNextHome(CreateTaskFragment::class.java))
            }
            ivNotify.setOnClickListener {
                postNormal(EventNextHome(NotifyFragment::class.java))
            }
//            layoutLeftMenu.rcvMenu.layoutManager = LinearLayoutManager(requireContext(),LinearLayoutManager.VERTICAL,false)
        }
        viewModel.getLinksCanAccess()
        setupMenuHeader()
        menuAdapter.onItemClick = { menu ->
            if (menu.level == 2) {
                if (menu.iconEnd != 0) {
                    menu.isExpand = !menu.isExpand
                    menuAdapter.onExpandMenu(menu)
                } else {
                    when (menu.url) {
                        "/change-role" -> {
                            postNormal(EventNextHome(SelectRoleFragment::class.java))
                        }
                    }
                    handlerPostDelay({
                        binding!!.layoutDrawer.closeDrawer(GravityCompat.START)
                    }, 100)
                }
            } else if (menu.level == 3) {
                when (menu.url) {
                    "/manage-manufacturing-works" -> {
                        postNormal(EventNextHome(ManufacturingWorkFragment::class.java))
                    }
                    "/manage-manufacturing-mill" -> {
                        postNormal(EventNextHome(ManufacturingMillFragment::class.java))
                    }
                    "/manage-manufacturing-lot" -> {
                        postNormal(EventNextHome(ManufacturingLotFragment::class.java))
                    }
                    "/manufacturing-dashboard" -> postNormal(
                        EventNextHome(
                            DashboardControlManufacturingFragment::class.java
                        )
                    )
                    "/report-financial" -> postNormal(
                        EventNextHome(
                            ReportFinancialFragment::class.java
                        )
                    )
                    "/report-sale" -> postNormal(
                        EventNextHome(
                            ReportSaleFragment::class.java
                        )
                    )
                    "/report-manufacturing" -> postNormal(
                        EventNextHome(
                            ReportManufacturingFragment::class.java
                        )
                    )
                    "/manufacturing-dashboard-quality" -> postNormal(
                        EventNextHome(
                            DashboardQualityManufacturingFragment::class.java
                        )
                    )
                    "/report-warehouse" -> postNormal(
                        EventNextHome(
                            ReportWarehouseFragment::class.java
                        )
                    )
                    "/manage-manufacturing-command" -> postNormal(
                        EventNextHome(
                            ManufacturingCommandFragment::class.java
                        )
                    )
                    "/manage-manufacturing-plan" -> postNormal(
                        EventNextHome(
                            ManufacturingPlanFragment::class.java
                        )
                    )
                    "/product-request-management/manufacturing" -> postNormal(
                        EventNextHome(ManufacturingRequestFragment::class.java)
                    )
                    "/manage-work-schedule" -> postNormal(
                        EventNextHome(WorkScheduleFragment::class.java)
                    )
                }
                handlerPostDelay({
                    binding!!.layoutDrawer.closeDrawer(GravityCompat.START)
                }, 100)
            }

        }
        taskAdapter.onClick = {
            postNormal(
                EventNextHome(
                    DetailTaskFragment::class.java,
                    bundleOf(Pair("TASK_ID", it._id),Pair("TASK_TYPE",taskTypeFilter))
                )
            )
        }
        getAllTask()
        viewModel.getNotifications(ParamGetPageNotify(1,50))
    }

    fun setupRcvTaskType() {
        val listType = listOf<TaskType>(
            TaskType(R.drawable.ic_task_todo, "Cần làm ", true),
            TaskType(R.drawable.ic_task_approve, "Phê duyệt"),
            TaskType(R.drawable.ic_task_consult, "Tư vấn"),
            TaskType(R.drawable.ic_task_view, "Giám sát"),
            TaskType(R.drawable.ic_task_created, "Đã tạo"),
        )
        binding?.apply {
            rcvTaskType.adapter = taskTypeAdapter
        }
        taskTypeAdapter.items = listType
        taskTypeAdapter.onItemClick = object : OnItemClick {
            override fun onItemClickListener(view: View, position: Int) {
                onChangeFilter(position)
            }

        }
    }

    fun onChangeFilter(pos: Int) {
        if (pos == taskTypeFilter) return
        else {
            taskTypeFilter = pos
            setupRcvTask()
        }
    }

    fun setupRcvTask() {
        when (taskTypeFilter) {
            0 -> {
                taskAdapter.items = viewModel.listAllTask0.value ?: mutableListOf()
            }
            1 -> {
                taskAdapter.items = viewModel.listAllTask1.value ?: mutableListOf()

            }
            2 -> {
                taskAdapter.items = viewModel.listAllTask2.value ?: mutableListOf()

            }
            3 -> {
                taskAdapter.items = viewModel.listAllTask3.value ?: mutableListOf()

            }
            4 -> {
                taskAdapter.items = viewModel.listAllTask4.value ?: mutableListOf()

            }

        }
    }


    fun startTimer() {
            binding?.tvTimer?.isVisible = true
        println("timer is start ")
            timer.cancel()
            timer = Timer()
            timer.scheduleAtFixedRate(
                object : TimerTask() {
                    override fun run() {
                        Handler(Looper.getMainLooper()).post(Runnable {
                            println("timer is runningggggggggggggggggggggg")
                            binding?.tvTimer?.text = timeStringFromLong(
                                getTimeInMillisFromString(getDateTimer())
                                        - getTimeInMillisFromString(sharedPreferences[Constants.START_TIME_COUNT]!!)
                            )
                        })

                    }
                }, 0, 500

            )

    }

    fun stopTimer() {
        justTry {
            binding?.tvTimer?.isVisible = false
            sharedPreferences[Constants.IS_COUNTING] = false
            sharedPreferences[Constants.TASK_ID_COUNTING] = ""
            timer.cancel()
        }
    }


    private fun timeStringFromLong(ms: Long): String {
        if(ms < 0L) return "00:00:00"
        val seconds = (ms / 1000) % 60
        val minutes = (ms / (1000 * 60) % 60)
        val hours = (ms / (1000 * 60 * 60) % 24)
        return String.format("%02d:%02d:%02d", hours, minutes, seconds)
    }

    fun getAllTask() {
        viewModel.getAllTask(
            RequestTaskModel(
                "", configRepository.getUser().id, 1000,
                fromMonth.convertMonthYear(), toMonth.convertMonthYear(), true
            )
        )

    }


    fun setUpMenu() {
        var links = viewModel.linksCanAccess.value ?: emptyList()
        listMenu.clear()
        if (links.isNotEmpty()) {
            links =
                links.filter { listOf("common", "manufacturing-management").contains(it.category) }
            val category = links.groupBy { it.category }
            category.forEach { c ->
                val first = c.value.first()
                listMenu.add(
                    MenuModel(
                        first._id,
                        first.category,
                        2,
                        "",
                        "",
                    )
                ) // viet func get ra desc va icon cho tung link tuong ung
                listMenu.addAll(c.value.map {
                    MenuModel(
                        it._id,
                        it.category,
                        3,
                        it.description,
                        it.url
                    )
                })
            }
            genMenuModel()
            Log.d("KGM", "list menu  : ${listMenu.toString()}")
            addTempMenu()
            sortMenu()
//            menuAdapter.items = listMenu
        }
    }

    fun setupMenuHeader() {
        val company = configRepository.getUser().company

        binding?.apply {
            layoutLeftMenu.tvCompanyName.text = company.name
            layoutLeftMenu.tvUserName.text = configRepository.getUser().name
            layoutLeftMenu.tvRoleName.text = configRepository.getCurrentRole().name
            layoutLeftMenu.imgAvatarUser
            layoutLeftMenu.imgAvatarUser.apply {
                Glide.with(this)
                    .load(
                        (if (BuildConfig.DEBUG) NetworkModule.BASE_URL_DEV else NetworkModule.BASE_URL)
                            .plus(configRepository.getUser().avatar)
                    )
                    .placeholder(resources.getDrawable(R.drawable.ic_user))
                    .error(resources.getDrawable(R.drawable.ic_user))
                    .into(this)
            }
        }
    }

    fun genMenuModel() {
        for (i in listMenu.indices.reversed()) {
            val it = listMenu[i]
            when (it.category) {
                "common" -> {
                    when (it.url) {
                        "/home" -> {
                            it.iconStart = R.drawable.ic_home
                            it.level = 2
                        }
                        else -> {
//                            it.isVisible = false
                            listMenu.removeAt(i)
                        }
                    }

                }
                "manufacturing-management" -> {
                    when (it.url) {
                        "/manage-manufacturing-command" -> {
                            it.iconStart = R.drawable.ic_manufacturing_command
                            it.level = 3
                            it.desc = "Lệnh sản xuất"
                        }
                        "/manage-manufacturing-plan" -> {
                            it.iconStart = R.drawable.ic_manufacturing_plan
                            it.level = 3
                            it.desc ="Kế hoạch sản xuất"
                        }
                        "/manage-work-schedule" -> {
                            it.iconStart = R.drawable.ic_manufacturing_workschedule
                            it.level = 3
                            it.desc ="Lịch sản xuất"
                        }
//                        "/manage-purchasing-request" -> {
//                            it.iconStart = R.drawable.ic_purchasing_request
//                            it.level = 3
//                            // menu nay da duco thay the
//                        }
                        "/manufacturing-dashboard" -> {
                            it.iconStart = R.drawable.ic_home
                            it.level = 3
                            it.desc ="Điều khiển sản xuất"
                        }
//                        "/manage-manufacturing-works" -> {
//                            it.iconStart = R.drawable.ic_manufacturing_work
//                            it.level = 3
//                            it.desc ="Nhà máy sản xuất"
//                        }
//                        "/manage-manufacturing-mill" -> {
//                            it.iconStart = R.drawable.ic_manufacturing_mill
//                            it.level = 3
//                            it.desc ="Xưởng sản xuất"
//                        }
                        "/manage-manufacturing-lot" -> {
                            it.iconStart = R.drawable.ic_manufacturing_lot
                            it.level = 3
                            it.desc ="Lô sản xuất"
                        }
                        "/product-request-management/manufacturing" -> {
                            it.iconStart = R.drawable.ic_purchasing_request
                            it.level = 3
                            it.desc ="Đề nghị"
                        }
                        "" -> {
                            it.iconStart = R.drawable.ic_manufacturing
                            it.level = 2
                            it.desc = "Quản lý sản xuất"
                            it.iconEnd = R.drawable.ic_down_menu
                        }
                        else -> {
                            listMenu.removeAt(i)
                        }
                    }
                }
            }
        }
    }

    fun addTempMenu() {
//        listMenu.add(
//            MenuModel(
//                id = "asjkld",
//                category = "manufacturing-management",
//                level = 3,
//                iconStart = R.drawable.ic_home,
//                desc = "Chất lượng sản xuất",
//                url = "/manufacturing-dashboard-quality"
//            )
//        )
        listMenu.add(
            MenuModel(
                id = "change_role",
                category = "setting_app",
                level = 2,
                iconStart = R.drawable.ic_change_role,
                desc = "Chuyển vai trò",
                url = "/change-role"
            )
        )
        listMenu.add(
            1,
            MenuModel(
                id = "report_overview",
                category = "report_overview",
                level = 2,
                iconStart = R.drawable.ic_home,
                desc = "Báo cáo tổng quan",
                url = "",
                iconEnd = R.drawable.ic_down_menu
            )
        )
        listMenu.add(
            2,
            MenuModel(
                id = "report_financial",
                category = "report_overview",
                level = 3,
                iconStart = R.drawable.ic_business_report,
                desc = "Báo cáo tài chính",
                url = "/report-financial"
            )
        )
        listMenu.add(
            2,
            MenuModel(
                id = "report_sale",
                category = "report_overview",
                level = 3,
                iconStart = R.drawable.ic_sales_report,
                desc = "Báo cáo kinh doanh",
                url = "/report-sale"
            )
        )
        listMenu.add(
            2,
            MenuModel(
                id = "report_manufacturing",
                category = "report_overview",
                level = 3,
                iconStart = R.drawable.ic_report_manufacturing,
                desc = "Báo cáo sản xuất",
                url = "/report-manufacturing"
            )
        )
        listMenu.add(
            2,
            MenuModel(
                id = "report_warehouse",
                category = "report_overview",
                level = 3,
                iconStart = R.drawable.ic_report_warehouse,
                desc = "Báo cáo kho hàng",
                url = "/report-warehouse"
            )
        )

    }

    fun sortMenu() {
        val stringMenu = GetInfoDevice.loadJSONFromAsset(requireContext(), "menus.json")
        var list = listOf<MenuSample>()
        list = gson.fromJson(stringMenu, object : TypeToken<List<MenuSample>>() {}.type)
        var listMenuNew = mutableListOf<MenuModel>()
        list.forEach { t ->
            val s = listMenu.find { it.category == t.category && it.url == t.url }
            if (s != null) listMenuNew.add(s)
        }
        menuAdapter.items = listMenuNew
    }

    private fun showDialogNotification(header:String,content:String){
        val dialogConfirm = DialogNotification(requireContext(),header,content)
        dialogConfirm.show()
    }

}