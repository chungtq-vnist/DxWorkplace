package com.example.test.dxworkspace.presentation.ui.home.workplace.create_task

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.text.style.ImageSpan
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.MultiAutoCompleteTextView
import androidx.core.os.bundleOf
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProvider
import com.example.test.dxworkspace.R
import com.example.test.dxworkspace.core.extensions.observe
import com.example.test.dxworkspace.core.extensions.viewModel
import com.example.test.dxworkspace.data.entity.manufacturing_work.OrganizationUnit
import com.example.test.dxworkspace.data.entity.manufacturing_work.UserRoleInOrganizationUnit
import com.example.test.dxworkspace.data.entity.project.ProjectResponse
import com.example.test.dxworkspace.data.entity.task.CollaboratedWithOrganizationalUnitRequest
import com.example.test.dxworkspace.data.entity.task.RequestCreateTask
import com.example.test.dxworkspace.data.entity.task.TaskTemplateResponse
import com.example.test.dxworkspace.data.entity.user.UserProfileResponse
import com.example.test.dxworkspace.databinding.FragmentCreateTaskBinding
import com.example.test.dxworkspace.presentation.ui.BaseFragment
import com.example.test.dxworkspace.presentation.ui.home.workplace.ChooseUserFragment
import com.example.test.dxworkspace.presentation.utils.common.clearText
import com.example.test.dxworkspace.presentation.utils.common.getTextz
import com.example.test.dxworkspace.presentation.utils.common.postNormal
import com.example.test.dxworkspace.presentation.utils.convertToUTCTime
import com.example.test.dxworkspace.presentation.utils.event.EventBus
import com.example.test.dxworkspace.presentation.utils.event.EventNextHome
import com.example.test.dxworkspace.presentation.utils.event.EventToast
import com.example.test.dxworkspace.presentation.utils.event.EventUpdate
import com.example.test.dxworkspace.presentation.utils.getDateYYYYMMDDHHMMSS
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipDrawable
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

class CreateTaskFragment : BaseFragment<FragmentCreateTaskBinding>() {
    override fun getLayoutId(): Int {
        return R.layout.fragment_create_task
    }

    @Inject
    lateinit var viewModel: CreateTaskViewModel

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    val calendar = Calendar.getInstance()

    var lOrganization = listOf<OrganizationUnit>()
    var lTemplates = listOf<TaskTemplateResponse>()
    var lProjects = listOf<ProjectResponse>()
    var task = RequestCreateTask()
    var listUserInAlUnit = mutableListOf<UserProfileResponse>()

    override fun onStart() {
        super.onStart()
        EventBus.getDefault().register(this)
    }

    override fun onStop() {
        super.onStop()
        EventBus.getDefault().unregister(this)
    }

    fun onBusEvent(event: EventUpdate) {
        when (event.type) {
            EventUpdate.UPDATE_LIST_USER -> {
                val t = event.value as Pair<String, MutableList<String>>
                when (t.first) {
                    "RESPONSIBLE" -> {
                        task.responsibleEmployees = t.second
                        binding?.edtUserResponsible?.setText(
                            getString(
                                R.string.number_user,
                                t.second.size.toString()
                            )
                        )
                    }
                    "ACCOUNTABLE" -> {
                        task.accountableEmployees = t.second
                        binding?.edtUserApproval?.setText(
                            getString(
                                R.string.number_user,
                                t.second.size.toString()
                            )
                        )
                    }
                    "CONSULT" -> {
                        task.consultedEmployees = t.second
                        binding?.edtUserConsult?.setText(
                            getString(
                                R.string.number_user,
                                t.second.size.toString()
                            )
                        )
                    }
                    "INFORMER" -> {
                        task.informedEmployees = t.second
                        binding?.edtUserInformer?.setText(
                            getString(
                                R.string.number_user,
                                t.second.size.toString()
                            )
                        )
                    }

                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = viewModel(viewModelFactory) {
            observe(listOrganization) {
                lOrganization = it ?: lOrganization
                setupUnit()
                setupListPeople()
            }
            observe(listProjects) {
                lProjects = it ?: lProjects
            }
            observe(listTemplates) {
                lTemplates = it ?: lTemplates
            }
            observe(createStatus){
                if(it == false) showToast(EventToast(0,true,"Tạo task thất bại!"))
                else {
                    showToast(EventToast(0,false,"Tạo task thành công" ))
                    onBackPress()
                }
            }
        }
        observeLoading(viewModel)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding?.apply {
            ivBack.setOnClickListener { onBackPress() }
            val adapter1 = ArrayAdapter(
                requireContext(),
                android.R.layout.simple_dropdown_item_1line,
                listOf("Khẩn cấp", "Cao", "Tiêu chuẩn", "Trung Bình", "Thấp")
            )
            edtPriority.setAdapter(adapter1)
            edtPriority.onItemClickListener =
                AdapterView.OnItemClickListener { p0, p1, p2, p3 ->
                    task.priority = 5 - p2
                }
            edtStartTime.setOnClickListener {
                showDateTimePickerDialog(true)
            }
            edtEndTime.setOnClickListener {
                showDateTimePickerDialog(false)
            }
            edtUserResponsible.setOnClickListener {
//                val bundle = Bundle()
//                bundle.put
                postNormal(
                    EventNextHome(
                        ChooseUserFragment::class.java,
                        bundleOf(
                            Pair("LIST_USER", task.responsibleEmployees ?: listOf()),
                            Pair("LIST_DATA", listUserInAlUnit),
                            Pair("USING_FOR", "RESPONSIBLE")
                        )
                    )
                )
            }
            edtUserApproval.setOnClickListener {
//                val bundle = Bundle()
//                bundle.put
                postNormal(
                    EventNextHome(
                        ChooseUserFragment::class.java,
                        bundleOf(
                            Pair("LIST_USER", task.accountableEmployees ?: listOf()),
                            Pair("LIST_DATA", listUserInAlUnit),
                            Pair("USING_FOR", "ACCOUNTABLE")
                        )
                    )
                )
            }
            edtUserConsult.setOnClickListener {
//                val bundle = Bundle()
//                bundle.put
                postNormal(
                    EventNextHome(
                        ChooseUserFragment::class.java,
                        bundleOf(
                            Pair("LIST_USER", task.consultedEmployees ?: listOf()),
                            Pair("LIST_DATA", listUserInAlUnit),
                            Pair("USING_FOR", "CONSULT")
                        )
                    )
                )
            }
            edtUserInformer.setOnClickListener {
//                val bundle = Bundle()
//                bundle.put
                postNormal(
                    EventNextHome(
                        ChooseUserFragment::class.java,
                        bundleOf(
                            Pair("LIST_USER", task.informedEmployees ?: listOf()),
                            Pair("LIST_DATA", listUserInAlUnit),
                            Pair("USING_FOR", "INFORMER")
                        )
                    )
                )
            }
            btnSave.setOnClickListener {
                if(task.organizationalUnit.isEmpty() || task.responsibleEmployees.isNullOrEmpty()
                    || task.accountableEmployees.isNullOrEmpty() || task.startDate.isNullOrEmpty()
                    || task.endDate.isEmpty()) showToast(EventToast(0,true,"Vui lòng nhập đủ thông tin "))
                task.name = edtName.getTextz()
                task.description = edtDes.getTextz()
                viewModel.createTask(task)
            }

        }
        viewModel.getAllOrganizationUnit()
        viewModel.getAllTemplates()
        viewModel.getAllProjects()
    }

    fun setupListPeople() {
        lOrganization.forEach { k ->
            val t = k.employees?.forEach { q ->
                addPeople(q.users)
            }
        }
    }

    fun addPeople(listUser: List<UserRoleInOrganizationUnit>?) {
        listUser?.forEach { t ->
            if (t.userId != null && listUserInAlUnit.find { it.id == t.userId?.id } == null)
                listUserInAlUnit.add(t.userId!!)
        }
    }

    fun setupUnit() {
        val adapter = ArrayAdapter<OrganizationUnit>(
            requireContext(),
            android.R.layout.simple_dropdown_item_1line,
            lOrganization
        )
        binding?.edtOrganization?.apply {
            setAdapter(adapter)
            onItemClickListener =
                AdapterView.OnItemClickListener { p0, p1, p2, p3 ->
                    task.organizationalUnit = (p0.getItemAtPosition(p2) as OrganizationUnit)._id
                    hideKeyboard()
                }
        }
        val adapter2 = ArrayAdapter<OrganizationUnit>(
            requireContext(),
            android.R.layout.simple_dropdown_item_1line,
            lOrganization
        )
        binding?.edtCollabUnit?.apply {
            setAdapter(adapter2)
            onItemClickListener =
                AdapterView.OnItemClickListener { p0, p1, p2, p3 ->
                    val chip = Chip(requireContext())
                    chip.text = (p0.getItemAtPosition(p2) as OrganizationUnit).name
//                    chip.setBackgroundColor(R.color.cardview_shadow_end_color)
                    chip.isCloseIconVisible = true
                    chip.closeIcon = resources.getDrawable(R.drawable.ic_close_chip)
                    chip.setOnCloseIconClickListener { v ->
                        binding?.listChipCollab?.removeView(v)
                        val del = lOrganization.firstOrNull { it.name == (v as Chip).text }?._id
                        if (del != null) {
                            task.collaboratedWithOrganizationalUnits?.removeIf { it.organizationalUnit == del }
                        }

                    }
                    chip.setEnsureMinTouchTargetSize(false)


                    val select = p0.getItemAtPosition(p2) as OrganizationUnit
                    val t = task.collaboratedWithOrganizationalUnits ?: mutableListOf()
                    if (t.firstOrNull { it.organizationalUnit == select._id } == null) {
                        t.add(CollaboratedWithOrganizationalUnitRequest(select._id, false))
                        task.collaboratedWithOrganizationalUnits = t
                        binding?.listChipCollab?.addView(chip)
                    }

                    binding?.edtCollabUnit?.clearText()
                }
        }


        // setup chip , maybe use later
//        binding?.edtUserResponsible?.apply {
//            setAdapter(adapter)
//            onItemClickListener =
//                AdapterView.OnItemClickListener { p0, p1, p2, p3 ->
//                    val chip = Chip(requireContext())
//                    chip.text = (p0.getItemAtPosition(p2) as OrganizationUnit).name
////                    chip.setBackgroundColor(R.color.cardview_shadow_end_color)
//                    chip.isCloseIconVisible = true
//                    chip.closeIcon = resources.getDrawable(R.drawable.ic_close_chip)
//                    chip.setOnCloseIconClickListener {
//                        binding?.listResponsible?.removeView(it)
//                    }
//                    chip.setEnsureMinTouchTargetSize(false)
////                    val t = binding?.edtUserResponsible?.text
////                    val chipDrawable = ChipDrawable.createFromResource(requireContext(),R.layout)
////                    chip.setBounds(0, 0, chip.intrinsicWidth, chip.intrinsicHeight)
////                    t?.setSpan(ImageSpan(chip))
//
//                    binding?.listResponsible?.addView(chip)
////                    binding?.listResponsible?.chipSpacingVertical = 3
//                    binding?.edtUserResponsible?.clearText()
//                }
//        }
    }


    private fun showDateTimePickerDialog(isStart: Boolean) {
        val datePickerDialog = DatePickerDialog(
            requireContext(),
            { _, year, month, dayOfMonth ->
                calendar.set(year, month, dayOfMonth)
                if (isStart) {
                    binding?.edtStartTime?.setText(
                        SimpleDateFormat(
                            "HH:mm dd-MM-yyyy",
                            Locale.getDefault()
                        ).format(calendar.time)
                    )
                    task.startDate = convertToUTCTime(getDateYYYYMMDDHHMMSS(calendar.time))
                } else {
                    binding?.edtEndTime?.setText(
                        SimpleDateFormat(
                            "HH:mm dd-MM-yyyy",
                            Locale.getDefault()
                        ).format(calendar.time)
                    )
                    task.endDate = convertToUTCTime(getDateYYYYMMDDHHMMSS(calendar.time))

                }
                showTimePickerDialog(isStart) // sua lai materialtimepicker
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        )
        datePickerDialog.show()
    }

    private fun showTimePickerDialog(isStart: Boolean) {
        val timePickerDialog = TimePickerDialog(
            requireContext(),
            { _, hourOfDay, minute ->
                calendar.set(Calendar.HOUR_OF_DAY, hourOfDay)
                calendar.set(Calendar.MINUTE, minute)
                if (isStart) {
                    binding?.edtStartTime?.setText(
                        SimpleDateFormat(
                            "HH:mm dd-MM-yyyy",
                            Locale.getDefault()
                        ).format(calendar.time)
                    )
                    task.startDate = convertToUTCTime(getDateYYYYMMDDHHMMSS(calendar.time))
                } else {
                    binding?.edtEndTime?.setText(
                        SimpleDateFormat(
                            "HH:mm dd-MM-yyyy",
                            Locale.getDefault()
                        ).format(calendar.time)
                    )
                    task.endDate = convertToUTCTime(getDateYYYYMMDDHHMMSS(calendar.time))
                }
            },
            calendar.get(Calendar.HOUR_OF_DAY),
            calendar.get(Calendar.MINUTE),
            true
        )
        timePickerDialog.show()
    }
}