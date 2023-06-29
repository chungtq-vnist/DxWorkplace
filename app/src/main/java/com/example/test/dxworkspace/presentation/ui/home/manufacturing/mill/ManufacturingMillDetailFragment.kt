package com.example.test.dxworkspace.presentation.ui.home.manufacturing.mill

import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.lifecycle.ViewModelProvider
import com.example.test.dxworkspace.R
import com.example.test.dxworkspace.core.extensions.observe
import com.example.test.dxworkspace.core.extensions.viewModel
import com.example.test.dxworkspace.data.entity.manufacturing_mill.ManufacturingMillModel
import com.example.test.dxworkspace.data.entity.manufacturing_work.ManufacturingWorkModel
import com.example.test.dxworkspace.data.entity.manufacturing_work.OrganizationUnit
import com.example.test.dxworkspace.data.entity.user.UserProfileResponse
import com.example.test.dxworkspace.databinding.FragmentManufacturingMillDetailBinding
import com.example.test.dxworkspace.presentation.ui.BaseFragment
import com.example.test.dxworkspace.presentation.ui.home.HomeViewModel
import com.example.test.dxworkspace.presentation.utils.common.clearText
import com.example.test.dxworkspace.presentation.utils.event.EventBus
import javax.inject.Inject

class ManufacturingMillDetailFragment : BaseFragment<FragmentManufacturingMillDetailBinding>() {
    override fun getLayoutId(): Int = R.layout.fragment_manufacturing_mill_detail

    override fun onStart() {
        super.onStart()
        EventBus.getDefault().register(this)
    }

    override fun onStop() {
        super.onStop()
        EventBus.getDefault().unregister(this)
    }

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    @Inject
    lateinit var viewModel: ManufacturingMillViewModel

    @Inject
    lateinit var homeViewModel: HomeViewModel

    var isEditMode = false
    var millId = ""
    var mill: ManufacturingMillModel = ManufacturingMillModel()
    var listOrganizationUnit = listOf<OrganizationUnit>()
    var listWork = listOf<ManufacturingWorkModel>()

    var leader: UserProfileResponse? = null
    var work: ManufacturingWorkModel? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        isEditMode = arguments?.getBoolean("EDIT_MODE") ?: false
        millId = arguments?.getString("MILL_ID") ?: ""
        viewModel = viewModel(viewModelFactory) {
            observe(millDetail) {
                mill = it ?: ManufacturingMillModel()
                setData()
                work = listWork.firstOrNull { it._id == mill.manufacturingWorks?._id }
                setupLeader()
            }
            observe(listWorks) {
                homeViewModel.listManufacturingWork.value = it
                listWork = it ?: listWork
                work = listWork.firstOrNull { it._id == mill.manufacturingWorks?._id }
                setupWork()
                setupLeader()
            }
        }
        observeLoading(viewModel)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        listOrganizationUnit = homeViewModel.listOrganization.value ?: listOf()
        binding?.apply {
            ivBack.setOnClickListener { onBackPress() }
            tvHeaderToolbar.text = if (isEditMode) "Chi tiết nhà xưởng" else "Tạo nhà xưởng mới"
            val statusAdapter = ArrayAdapter(
                requireContext(),
                android.R.layout.simple_spinner_dropdown_item,
                listOf("Hoạt động", "Tạm dừng")
            )
            tvStatus.adapter = statusAdapter
            tvStatus.setSelection(0)
        }
        if (millId.isNotEmpty()) viewModel.getMillById(millId)
        if (homeViewModel.listManufacturingWork.value.isNullOrEmpty()) viewModel.getManufacturingWorks()
        else viewModel.listWorks.value = homeViewModel.listManufacturingWork.value
    }

    fun setData() {
        binding?.apply {
            edtCode.setText(mill.code)
            edtName.setText(mill.name)
            edtManufacturingWork.setText(mill.manufacturingWorks?.name)
            edtLeader.setText(mill.teamLeader?.name)
            tvStatus.setSelection(if (mill.status == 0) 1 else 0)
            edtDes.setText(mill.description)
        }
    }

    fun setupWork() {
        binding?.apply {
            val adapter = ArrayAdapter<ManufacturingWorkModel>(
                requireContext(),
                android.R.layout.simple_spinner_dropdown_item,
                listWork
            )
            edtManufacturingWork.setAdapter(adapter)
            edtManufacturingWork.setOnFocusChangeListener { view, b ->
                edtManufacturingWork.showDropDown()
            }
            edtManufacturingWork.threshold = 1
            edtManufacturingWork.onItemClickListener =
                AdapterView.OnItemClickListener { p0, p1, p2, p3 ->
                    work = p0.getItemAtPosition(p2) as ManufacturingWorkModel
                    hideKeyboard()
                    edtLeader.clearText()
                    setupLeader()
                }
        }
    }

    fun setupLeader() {
        binding?.apply {
            val adapter = ArrayAdapter<UserProfileResponse>(
                requireContext(),
                android.R.layout.simple_spinner_dropdown_item,
                listOrganizationUnit.firstOrNull { work?.organizationalUnit?._id == it._id }?.employees?.first()?.users?.map { it.userId }
                    ?: listOf()
            )
            edtLeader.setAdapter(adapter)
            edtLeader.setOnFocusChangeListener { view, b ->
                edtLeader.showDropDown()
            }
            edtLeader.threshold = 1
            edtLeader.onItemClickListener =
                AdapterView.OnItemClickListener { p0, p1, p2, p3 ->
                    leader = p0.getItemAtPosition(p2) as UserProfileResponse
                    hideKeyboard()
                }
        }
    }
}