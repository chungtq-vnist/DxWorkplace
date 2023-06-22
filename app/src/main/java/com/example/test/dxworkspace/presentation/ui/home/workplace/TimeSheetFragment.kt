package com.example.test.dxworkspace.presentation.ui.home.workplace

import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProvider
import com.example.test.dxworkspace.R
import com.example.test.dxworkspace.core.extensions.observe
import com.example.test.dxworkspace.core.extensions.viewModel
import com.example.test.dxworkspace.data.entity.timesheet.StopTimeModel
import com.example.test.dxworkspace.databinding.FragmentTimeSheetBinding
import com.example.test.dxworkspace.domain.repository.ConfigRepository
import com.example.test.dxworkspace.presentation.ui.BaseFragment
import com.example.test.dxworkspace.presentation.ui.home.workplace.adapter.ActionTimeSheet
import com.example.test.dxworkspace.presentation.ui.home.workplace.adapter.TaskActionTimeSheetAdapter
import com.example.test.dxworkspace.presentation.utils.common.Constants
import javax.inject.Inject
import com.example.test.dxworkspace.data.local.preferences.AppPreferences.get
import com.example.test.dxworkspace.presentation.utils.event.EventBus
import com.example.test.dxworkspace.presentation.utils.event.EventUpdate

class TimeSheetFragment : BaseFragment<FragmentTimeSheetBinding>() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    @Inject
    lateinit var viewModel: WorkplaceViewModel

    @Inject
    lateinit var configRepository: ConfigRepository

    var action : ActionTimeSheet? = null

    override fun getLayoutId(): Int {
        return R.layout.fragment_time_sheet
    }

    val adapter by lazy { TaskActionTimeSheetAdapter() }
    var list = mutableListOf<ActionTimeSheet>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel =viewModel(viewModelFactory){
            observe(taskSelected){
                val t = it!!
                binding?.apply {
                    tvName.text = t.name
                }
                list = t.taskActions.map { ActionTimeSheet(it._id,false,it.description.toString()) }.toMutableList()
                adapter.items = list
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.getTaskById(sharedPreferences[Constants.TASK_ID_COUNTING,""] ?: "")
        binding?.apply {
            adapter.onClick = {
                action = it
            }
            rcvAction.adapter = adapter
            tvCancel.setOnClickListener {
//                viewModel.stopTimer(sharedPreferences[Constants.TASK_ID_COUNTING]!! ,
//                    StopTimeModel(autoStopped = null,employee = configRepository.getUser().id, timesheetLog = sharedPreferences[Constants.TIMERID_COUNTING] , type = "cancel")
//                )
                EventBus.getDefault().post(EventUpdate(EventUpdate.UPDATE_TIMER,false))
                onBackPress()
            }
            tvApply.setOnClickListener {
                if(action != null){

                }
            }
        }
    }
}