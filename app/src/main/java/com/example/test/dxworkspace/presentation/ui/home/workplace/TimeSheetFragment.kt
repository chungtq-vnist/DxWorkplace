package com.example.test.dxworkspace.presentation.ui.home.workplace

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
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
import com.example.test.dxworkspace.data.local.preferences.AppPreferences.set
import com.example.test.dxworkspace.presentation.utils.common.getTextz
import com.example.test.dxworkspace.presentation.utils.event.EventBus
import com.example.test.dxworkspace.presentation.utils.event.EventToast
import com.example.test.dxworkspace.presentation.utils.event.EventUpdate
import com.example.test.dxworkspace.presentation.utils.getDateYYYYMMDDHHMMSS
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.min

class TimeSheetFragment : BaseFragment<FragmentTimeSheetBinding>() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    @Inject
    lateinit var viewModel: WorkplaceViewModel

    @Inject
    lateinit var configRepository: ConfigRepository

    var action : ActionTimeSheet? = null


    var endTimeSchedule = ""
    override fun getLayoutId(): Int {
        return R.layout.fragment_time_sheet
    }

    val calendar = Calendar.getInstance()
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
            observe(failure){
                showToast(EventToast(R.string.error_notification))
            }
            observe(stopSuccess){
                if(it == true){
                    sharedPreferences[Constants.TASK_ID_COUNTING] = ""
                    sharedPreferences[Constants.START_TIME_COUNT] = ""
                    sharedPreferences[Constants.TIMERID_COUNTING] = ""
                    sharedPreferences[Constants.IS_COUNTING] = false
                    onBackPress()
                    EventBus.getDefault().post(EventUpdate(EventUpdate.UPDATE_TIMER,false))

                }
            }
        }
        observeLoading(viewModel)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        endTimeSchedule = getDateYYYYMMDDHHMMSS(Date())
        viewModel.getTaskById(sharedPreferences[Constants.TASK_ID_COUNTING,""] ?: "")
        binding?.apply {
            adapter.onClick = {
                action = it
            }
            ivBack.setOnClickListener { onBackPress() }
            cbSelected.setOnCheckedChangeListener { compoundButton, b ->
                rlSelectTime.isVisible = b
            }
            rcvAction.adapter = adapter
            tvCancel.setOnClickListener {
//                viewModel.stopTimer(sharedPreferences[Constants.TASK_ID_COUNTING]!! ,
//                    StopTimeModel(autoStopped = null,employee = configRepository.getUser().id, timesheetLog = sharedPreferences[Constants.TIMERID_COUNTING] , type = "cancel")
//                )
                val model = StopTimeModel(
                    null,
                    null,
                    configRepository.getUser().id,
                    null,
                    null,
                    sharedPreferences[Constants.TIMERID_COUNTING],
                    null,
                    "cancel"
                )
                viewModel.stopTimer(sharedPreferences[Constants.TASK_ID_COUNTING] ?: "" , model)

            }
            tvApply.setOnClickListener {
                if(action != null){
                    val model = StopTimeModel(
                        if(cbSelected.isChecked) 2 else 1,
                        edtDesc.getTextz(),
                        configRepository.getUser().id,
                        sharedPreferences[Constants.START_TIME_COUNT],
                        if(cbSelected.isChecked) endTimeSchedule else getDateYYYYMMDDHHMMSS(Date()),
                        sharedPreferences[Constants.TIMERID_COUNTING],
                        action!!.id,
                        null
                    )
                    viewModel.stopTimer(sharedPreferences[Constants.TASK_ID_COUNTING] ?: "" , model)
                }
            }
            llDayStart.setOnClickListener {
                showDateTimePickerDialog()
            }
            llHourStart.setOnClickListener {
                showTimePickerDialog()
            }
            tvDayStart.text = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(calendar.time)
            tvHourStart.text = SimpleDateFormat("HH:mm", Locale.getDefault()).format(calendar.time)

        }
    }

    private fun showDateTimePickerDialog() {
        val datePickerDialog = DatePickerDialog(
            requireContext(),
            { _, year, month, dayOfMonth ->
                calendar.set(year, month, dayOfMonth)
                binding?.tvDayStart?.text = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(calendar.time)
                showTimePickerDialog()
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        )
        datePickerDialog.show()
    }

    private fun showTimePickerDialog() {
        val timePickerDialog = TimePickerDialog(
            requireContext(),
            { _, hourOfDay, minute ->
                calendar.set(Calendar.HOUR_OF_DAY, hourOfDay)
                calendar.set(Calendar.MINUTE, minute)
                endTimeSchedule = getDateYYYYMMDDHHMMSS(calendar.time)
                // Hiển thị thời gian đã chọn
                binding?.tvHourStart?.text = "$hourOfDay:${minute}"
            },
            calendar.get(Calendar.HOUR_OF_DAY),
            calendar.get(Calendar.MINUTE),
            true
        )
        timePickerDialog.show()
    }

}