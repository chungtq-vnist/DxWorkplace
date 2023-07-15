package com.example.test.dxworkspace.presentation.ui.home.manufacturing.work_schedule

import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.test.dxworkspace.R
import com.example.test.dxworkspace.core.extensions.observe
import com.example.test.dxworkspace.core.extensions.viewModel
import com.example.test.dxworkspace.data.entity.manufacturing_mill.ManufacturingMillModel
import com.example.test.dxworkspace.data.entity.manufacturing_mill.SubUserBasicModel
import com.example.test.dxworkspace.data.entity.work_schedule.ParamCreateWorkSchedule
import com.example.test.dxworkspace.data.entity.work_schedule.ParamWorkSchedule
import com.example.test.dxworkspace.data.entity.work_schedule.WorkScheduleDetailModel
import com.example.test.dxworkspace.databinding.FragmentMillScheduleBinding
import com.example.test.dxworkspace.databinding.FragmentUserScheduleBinding
import com.example.test.dxworkspace.domain.repository.ConfigRepository
import com.example.test.dxworkspace.presentation.ui.BaseFragment
import com.example.test.dxworkspace.presentation.ui.home.HomeViewModel
import com.example.test.dxworkspace.presentation.ui.home.manufacturing.command.CreateLotFragment
import com.example.test.dxworkspace.presentation.ui.home.manufacturing.command.ManufacturingCommandDetailFragment
import com.example.test.dxworkspace.presentation.ui.home.manufacturing.plan.adapter.ShiftInMonthAdapter
import com.example.test.dxworkspace.presentation.ui.home.manufacturing.request.ManufacturingRequestViewModel
import com.example.test.dxworkspace.presentation.ui.home.workplace.time_picker.MonthYearPickerDialog
import com.example.test.dxworkspace.presentation.utils.common.postNormal
import com.example.test.dxworkspace.presentation.utils.common.setTextColorz
import com.example.test.dxworkspace.presentation.utils.event.EventBus
import com.example.test.dxworkspace.presentation.utils.event.EventNextHome
import com.example.test.dxworkspace.presentation.utils.event.EventToast
import com.example.test.dxworkspace.presentation.utils.event.EventUpdate
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

class UserWorkScheduleFragment : BaseFragment<FragmentUserScheduleBinding>() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    @Inject
    lateinit var viewModel: WorkScheduleViewModel

    @Inject
    lateinit var homeViewModel: HomeViewModel

    @Inject
    lateinit var configRepository: ConfigRepository


    var mills = listOf<ManufacturingMillModel>()
    var work = WorkScheduleDetailModel()
    var user = listOf<SubUserBasicModel>()
    var month = ""
    var codeMill = ""
    var userNow = SubUserBasicModel()
    val calendar = Calendar.getInstance()
    val shiftAdapter = ShiftInMonthAdapter()
    var dialog : DialogCreateUserSchedule? = null

    override fun getLayoutId(): Int {
        return R.layout.fragment_user_schedule
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel= viewModel(viewModelFactory){
            observe(listUser){
                user = it ?: listOf()
                binding?.apply {
                    edtManufacturingMill.setAdapter(
                        ArrayAdapter(requireContext(),android.R.layout.simple_dropdown_item_1line,
                            user)
                    )
                    edtManufacturingMill.setOnItemClickListener { adapterView, view, i, l ->
                        val m = (adapterView.getItemAtPosition(i) as SubUserBasicModel)
                        if(userNow._id != m._id){
                            userNow = m
                            getWorkSchedule()
                        }
                        hideKeyboard()
                    }
                }
            }
            observe(statusCreated){
                if(it == false ) showToast(EventToast(text ="Tạo lịch sản xuất thất bại"))
                else {
                    showToast(EventToast(isFail = false, text ="Tạo lịch sản xuất thành công"))
                }
            }
            observe(workSchedule){
                work = it ?: WorkScheduleDetailModel()
                if(!work._id.isNullOrEmpty() && work.turns.isNotEmpty()) {
                    shiftAdapter.data = work.turns
                    binding?.apply {
                        rcvSchedule.isVisible = true
                        tvNodata.isVisible = false
                    }
                } else {
                    binding?.apply {
                        rcvSchedule.isVisible = false
                        tvNodata.isVisible = true
                    }
                }
            }
        }
        observeLoading(viewModel)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.getUserInWork(configRepository.getCurrentRole().id)
        month =  SimpleDateFormat("yyyy-MM", Locale.getDefault()).format(calendar.time)
        binding?.apply {
            edtMonth.setText(SimpleDateFormat("MM/yyyy", Locale.getDefault()).format(calendar.time))
            rcvSchedule.layoutManager= LinearLayoutManager(requireContext(),
                LinearLayoutManager.HORIZONTAL,false)
            rcvSchedule.adapter = shiftAdapter
            shiftAdapter.onViewDetailShift = {s,d ->
                val command = work.turns.get(s).get(d-1)
                val status =  when(command?.status){
                    1 -> {
                       "Chờ phê duyệt"
                    }
                    2 -> {
                         "Đã phê duyệt"
                    }
                    3 -> {
                        "Đang thực hiện"
                    }
                    4 -> {
                        "Đã hoàn thành"
                    }
                    5 -> {
                        "Đã hủy"
                    }
                    6 -> {
                        "Mới tạo"
                    }
                    else -> {
                       "Error"
                    }
                }
                MaterialAlertDialogBuilder(requireContext())
                    .setTitle("Lệnh sản xuất")
                    .setMessage("Mã lệnh : ${command?.code} \nTrạng thái : ${status}")
                    .setPositiveButton("Xem chi tiết") { dialog, which ->
                        postNormal(EventNextHome(ManufacturingCommandDetailFragment::class.java, bundleOf(Pair("COMMAND_ID",command?._id))))
                        dialog.dismiss()
                    }
                    .show()
            }
            edtMonth.setOnClickListener {
                showMonthYearTimePickerDialog()
            }
            btnSave.setOnClickListener { showDialog() }
        }
    }
    fun getWorkSchedule(){
        viewModel.getWorkSchedule(
            ParamWorkSchedule(10,1,"user",month,configRepository.getCurrentRole().id,"",userNow._id
            )
        )
    }
    private fun showMonthYearTimePickerDialog() {
        val time = month
        val dialog = MonthYearPickerDialog(
            requireContext(),
            time.substringAfter("-").toInt(),time.substringBefore("-").toInt()
        )
        dialog.onClick = { a, b ->
            calendar.set(Calendar.YEAR,b)
            calendar.set(Calendar.MONTH,a-1)
            month =  SimpleDateFormat("yyyy-MM", Locale.getDefault()).format(calendar.time)
            if(userNow._id.isNotEmpty()) getWorkSchedule()
            println("month : $month")
            binding?.edtMonth?.setText(SimpleDateFormat("MM/yyyy", Locale.getDefault()).format(calendar.time))
        }
        dialog.show()
    }
    fun showDialog(){
        if(dialog != null || dialog?.showsDialog == true){
            dialog?.dismiss()
            dialog= null
        }
        dialog = DialogCreateUserSchedule(user.toMutableList(),month)
        dialog?.onAccept = { user,month ->
            val param = ParamCreateWorkSchedule(
                null,
                if(user._id.isEmpty()) true else null ,
                month,
                3,
                configRepository.getCurrentRole().id,
                null,
                if(user._id.isEmpty()) null else user._id
            )
            viewModel.createSchedule(data = param)


        }
        dialog?.show(childFragmentManager,"dialogCheckQuality")
    }
}