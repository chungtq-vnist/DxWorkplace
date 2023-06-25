package com.example.test.dxworkspace.presentation.ui.home.workplace.detail_task

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import com.example.test.dxworkspace.R
import com.example.test.dxworkspace.core.extensions.observe
import com.example.test.dxworkspace.core.extensions.viewModel
import com.example.test.dxworkspace.data.entity.task.TaskModel
import com.example.test.dxworkspace.data.entity.task.TaskModelDetail
import com.example.test.dxworkspace.databinding.FragmentDetailTaskBinding
import com.example.test.dxworkspace.presentation.ui.BaseFragment
import com.example.test.dxworkspace.presentation.ui.home.workplace.WorkplaceViewModel
import com.example.test.dxworkspace.presentation.ui.home.workplace.adapter.TaskActionAdapter
import com.example.test.dxworkspace.presentation.ui.home.workplace.adapter.TaskResponsibleEmployeeAdapter
import com.example.test.dxworkspace.presentation.utils.common.Constants
import com.example.test.dxworkspace.presentation.utils.common.setHtml
import com.example.test.dxworkspace.presentation.utils.event.EventToast
import com.example.test.dxworkspace.data.local.preferences.AppPreferences.get
import com.example.test.dxworkspace.data.local.preferences.AppPreferences.set
import com.example.test.dxworkspace.domain.repository.ConfigRepository
import com.example.test.dxworkspace.presentation.ui.home.workplace.adapter.TaskTimeSheetLogAdapter
import com.example.test.dxworkspace.presentation.utils.common.clearText
import com.example.test.dxworkspace.presentation.utils.common.getTextz
import com.example.test.dxworkspace.presentation.utils.event.EventBus
import com.example.test.dxworkspace.presentation.utils.event.EventUpdate
import com.example.test.dxworkspace.presentation.utils.getTimeDDMMYYYYHHMMFromString
import com.example.test.dxworkspace.presentation.utils.getTimeDDMMYYYYHHMMFromStringOneLine
import javax.inject.Inject

class DetailTaskFragment : BaseFragment<FragmentDetailTaskBinding>() {
    override fun getLayoutId(): Int = R.layout.fragment_detail_task

    @Inject
    lateinit var viewModel: WorkplaceViewModel

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    @Inject
    lateinit var configRepository: ConfigRepository

    val taskActionAdapter by lazy { TaskActionAdapter() }
    val taskResponsibleEmpl by lazy { TaskResponsibleEmployeeAdapter() }
    val taskAccountableEmpl by lazy { TaskResponsibleEmployeeAdapter() }
    val taskConsultedEmpl by lazy { TaskResponsibleEmployeeAdapter() }
    val taskTimeSheetAdapter by lazy { TaskTimeSheetLogAdapter() }
    var taskId = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        taskId = arguments?.getString("TASK_ID") ?: ""
        viewModel = viewModel(viewModelFactory) {
            observe(taskSelected) {
                if (it?._id.isNullOrEmpty()) {
                    showToast(EventToast(0, true, "Get thông tin task thất bại"))
                } else handleTaskSuccess(it!!)
            }
        }
        observeLoading(viewModel)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (taskId.isNotEmpty()) viewModel.getTaskById(taskId)
        binding?.apply {
            ivBack.setOnClickListener { onBackPress() }
            btnTimeSheet.isSelected = sharedPreferences[Constants.IS_COUNTING,false] == false
            btnTimeSheet.isEnabled = sharedPreferences[Constants.IS_COUNTING,false] == false
            btnTimeSheet.setOnClickListener {
                viewModel.taskIdTimer = viewModel.taskSelected?.value?._id ?: ""
                sharedPreferences[Constants.IS_COUNTING] = true
                sharedPreferences[Constants.TASK_ID_COUNTING] = viewModel.taskSelected?.value?._id ?: ""
                viewModel.startTimer(viewModel.taskIdTimer , configRepository.getUser().id)
                EventBus.getDefault().post(EventUpdate(EventUpdate.UPDATE_TIMER,true))
                onBackPress()
            }
        }
    }

    fun handleTaskSuccess(task: TaskModelDetail) {
        binding?.apply {
            tvTaskName.text = task.name
            tvUnitManage.text = task.organizationalUnit?.name ?: "Không có đơn vị quản lý"
            when (task.priority) {
                1 -> {
                    tvLevel.text = "Thấp"
                    tvLevel.setTextColor(resources.getColor(R.color.clr_green))
                }
                2 -> {
                    tvLevel.text = "Trung bình"
                    tvLevel.setTextColor(resources.getColor(R.color.rajah))
                }
                3 -> {
                    tvLevel.text = "Tiêu chuẩn"
                    tvLevel.setTextColor(resources.getColor(R.color.azure_radiance))
                }
                4 -> {
                    tvLevel.text = "Cao"
                    tvLevel.setTextColor(resources.getColor(R.color.clr_cancel_kitchen))
                }
                5 -> {
                    tvLevel.text = "Khẩn cấp"
                    tvLevel.setTextColor(resources.getColor(R.color.color_item_delivery))
                }
            }
            tvTimeStart.text = getTimeDDMMYYYYHHMMFromStringOneLine(task.startDate)
            tvTimeEnd.text = getTimeDDMMYYYYHHMMFromStringOneLine(task.endDate)
            tvStatus.text = when (task.status) {
                "inprocess" -> "Đang thực hiện"
                "wait_for_approval" -> "Chờ phê duyệt"
                "finished" -> "Đã hoàn thành"
                "delayed" -> "Tạm hoãn"
                "canceled" -> "Bị hủy"
                else -> "Đã hoàn thành"
            }
            tvProgress.text = "${task.progress}%"
            tvProgress.setTextColor(
                if (task.progress >= 80) resources.getColor(R.color.clr_green)
                else resources.getColor(R.color.clr_cancel_kitchen)
            )
            tvDescription.setHtml(task.description)
            rcvAction.adapter = taskActionAdapter
            taskActionAdapter.items = task.taskActions.toMutableList()
            constraintAction.setOnClickListener {
                rcvAction.isVisible = !rcvAction.isVisible
                lnAddAction.isVisible = !lnAddAction.isVisible
                ivExpandAction.setImageResource(if(rcvAction.isVisible) R.drawable.ic_expand_up else R.drawable.ic_expand_down)

            }
            rcvResponsibleEmp.adapter = taskResponsibleEmpl
            taskResponsibleEmpl.items = task.responsibleEmployees?.toMutableList() ?: mutableListOf()
            constraintResponsibleEmp.setOnClickListener {
                rcvResponsibleEmp.isVisible = !rcvResponsibleEmp.isVisible
                ivExpandResponsible.setImageResource(if(rcvResponsibleEmp.isVisible) R.drawable.ic_expand_up else R.drawable.ic_expand_down)

            }
            rcvAccountableEmp.adapter = taskAccountableEmpl
            taskAccountableEmpl.items = task.accountableEmployees?.toMutableList() ?: mutableListOf()
            constraintAccountableEmp.setOnClickListener {
                rcvAccountableEmp.isVisible = !rcvAccountableEmp.isVisible
                ivExpandAccountable.setImageResource(if(rcvAccountableEmp.isVisible) R.drawable.ic_expand_up else R.drawable.ic_expand_down)

            }
            rcvConsultedEmp.adapter = taskConsultedEmpl
            taskConsultedEmpl.items = task.consultedEmployees?.toMutableList() ?: mutableListOf()
            constraintConsultedEmp.setOnClickListener {
                rcvConsultedEmp.isVisible = !rcvConsultedEmp.isVisible
                ivExpandConsulted.setImageResource(if(rcvConsultedEmp.isVisible) R.drawable.ic_expand_up else R.drawable.ic_expand_down)
            }

            rcvTimeLog.adapter = taskTimeSheetAdapter
            taskTimeSheetAdapter.items = task.timesheetLogs.toMutableList()
            constraintTimeLog.setOnClickListener {
                rcvTimeLog.isVisible = !rcvTimeLog.isVisible
                ivExpandTimeLog.setImageResource(if(rcvTimeLog.isVisible) R.drawable.ic_expand_up else R.drawable.ic_expand_down)
            }
            ivSendAction.setOnClickListener {
                if(edtAddAction.getTextz().isNotEmpty()) viewModel.postAction(viewModel.taskSelected.value?._id ?: "" , configRepository.getUser().id,edtAddAction.getTextz(),viewModel.taskSelected.value?.timesheetLogs?.size ?: 0)
                edtAddAction.clearText()
                hideKeyboard()

            }
            rlTimeSheet.isVisible = task.status != "finished" && task.status != "canceled"
        }
    }
}