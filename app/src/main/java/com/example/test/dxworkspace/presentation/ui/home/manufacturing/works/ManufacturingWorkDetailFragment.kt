package com.example.test.dxworkspace.presentation.ui.home.manufacturing.works

import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.core.os.bundleOf
import androidx.lifecycle.ViewModelProvider
import com.example.test.dxworkspace.R
import com.example.test.dxworkspace.core.extensions.observe
import com.example.test.dxworkspace.core.extensions.viewModel
import com.example.test.dxworkspace.data.entity.manufacturing_work.ManufacturingWorkDetailModel
import com.example.test.dxworkspace.data.entity.manufacturing_work.ManufacturingWorkEntity
import com.example.test.dxworkspace.data.entity.manufacturing_work.OrganizationUnit
import com.example.test.dxworkspace.data.entity.role.RoleModel
import com.example.test.dxworkspace.databinding.FragmentManufacturingWorkDetailBinding
import com.example.test.dxworkspace.presentation.ui.BaseFragment
import com.example.test.dxworkspace.presentation.ui.home.HomeViewModel
import com.example.test.dxworkspace.presentation.utils.common.postNormal
import com.example.test.dxworkspace.presentation.utils.event.EventBus
import com.example.test.dxworkspace.presentation.utils.event.EventNextHome
import com.example.test.dxworkspace.presentation.utils.event.EventUpdate
import javax.inject.Inject

class ManufacturingWorkDetailFragment : BaseFragment<FragmentManufacturingWorkDetailBinding>() {
    override fun getLayoutId(): Int {
        return R.layout.fragment_manufacturing_work_detail
    }

    var isEditMode = false
    var work: ManufacturingWorkDetailModel = ManufacturingWorkDetailModel()
    var workId = ""
    var listRole = listOf<RoleModel>()
    var listOrganizationUnit = listOf<OrganizationUnit>()
    var listRoleManager = listOf<String>()

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    @Inject
    lateinit var viewModel: ManufacturingWorkViewModel

    @Inject
    lateinit var homeViewModel: HomeViewModel

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
            EventUpdate.UPDATE_ROLE -> {
                listRoleManager = event.value as MutableList<String>
                updateManagerRoles(event.value as MutableList<String>)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        isEditMode = arguments?.getBoolean("EDIT_MODE") ?: false
        workId = arguments?.getString("WORK_ENTITY") ?: ""
        viewModel = viewModel(viewModelFactory) {
            observe(workDetail) {
                work = it ?: ManufacturingWorkDetailModel()
                setData()
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        listRole = homeViewModel.listRole.value ?: listOf()
        listOrganizationUnit = homeViewModel.listOrganization.value ?: listOf()
        binding?.apply {
            tvHeaderToolbar.text = if (isEditMode) "Chi tiết nhà máy" else "Tạo nhà máy mới"
            edtManagerOther.setOnClickListener {
                postNormal(
                    EventNextHome(
                        ChooseRoleFragment::class.java,
                        bundleOf(Pair("LIST_ROLE", listRoleManager))
                    )
                )
            }
            ivBack.setOnClickListener { onBackPress() }
            val statusAdapter = ArrayAdapter(
                requireContext(),
                android.R.layout.simple_spinner_dropdown_item,
                listOf("Hoạt động", "Tạm dừng")
            )
            tvStatus.adapter = statusAdapter
            tvStatus.setSelection(0)
            var arrayAdapter = ArrayAdapter(
                requireContext(), android.R.layout.simple_list_item_1,
                work.organizationalUnit?.managers?.map { it.name + " : " + it.users?.first()?.userId?.name }
                    ?.toList() ?: listOf()
            )
            rcvCaptain.adapter = arrayAdapter
            val listData = listOrganizationUnit.map { it.name }
            val adapter =
                ArrayAdapter<String>(
                    requireContext(),
                    android.R.layout.simple_list_item_1,
                    listData
                )
            edtOrganization.setAdapter(adapter)
            edtOrganization.setOnFocusChangeListener { view, b ->
                edtOrganization.showDropDown()
            }
            edtOrganization.threshold = 1
            edtOrganization.onItemClickListener =
                AdapterView.OnItemClickListener { p0, p1, p2, p3 -> hideKeyboard() }
        }

        if (workId.isNotEmpty()) viewModel.getWorkById(workId)

    }

    fun setData() {

        binding?.apply {
            edtCode.setText(work.code)
            edtName.setText(work.name)
            edtAddress.setText(work.address)
            edtDes.setText(work.description)
            edtPhone.setText(work.phoneNumber)
            edtShift.setText((work.turn ?: 0).toString())
            edtOrganization.setText(work.organizationalUnit?.name)
            listRoleManager = work.manageRoles?.map { it._id } ?: listOf()
            val l = work.manageRoles?.map { it.name } ?: listOf()
            edtManagerOther.setText(l.joinToString())
            tvStatus.setSelection(if (work.status == 0) 1 else 0)
        }
    }

    fun updateManagerRoles(roleIds: MutableList<String>) {
        val t = (listRole.filter { roleIds.contains(it._id) }).map { it.name }.toList()
        binding?.edtManagerOther?.setText(t.joinToString())
    }


}