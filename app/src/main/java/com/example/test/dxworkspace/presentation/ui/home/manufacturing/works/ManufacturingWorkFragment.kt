package com.example.test.dxworkspace.presentation.ui.home.manufacturing.works

import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.test.dxworkspace.R
import com.example.test.dxworkspace.core.extensions.observe
import com.example.test.dxworkspace.core.extensions.viewModel
import com.example.test.dxworkspace.databinding.FragmentManufacturingWorksBinding
import com.example.test.dxworkspace.presentation.ui.BaseFragment
import com.example.test.dxworkspace.presentation.ui.home.HomeViewModel
import com.example.test.dxworkspace.presentation.ui.home.manufacturing.works.adapter.ManufacturingWorkAdapter
import com.example.test.dxworkspace.presentation.utils.common.postNormal
import com.example.test.dxworkspace.presentation.utils.event.EventNextHome
import javax.inject.Inject

class ManufacturingWorkFragment : BaseFragment<FragmentManufacturingWorksBinding>() {


    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    @Inject
    lateinit var viewModel: ManufacturingWorkViewModel

    @Inject
    lateinit var homeViewModel : HomeViewModel

    val adapter by lazy { ManufacturingWorkAdapter() }

    override fun getLayoutId(): Int {
        return R.layout.fragment_manufacturing_works

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = viewModel(viewModelFactory) {
            observe(listWorks) {
                adapter.items = listWorks.value?.toMutableList() ?: mutableListOf()
                homeViewModel.listManufacturingWork.value = it
            }
        }
        observeLoading(homeViewModel)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding?.apply {
            ivBack.setOnClickListener { onBackPress() }
            rcvWork.adapter = adapter
            rcvWork.addItemDecoration(
                DividerItemDecoration(
                    requireContext(),
                    LinearLayoutManager.VERTICAL
                )
            )
            adapter.onClick = {
                val t= it
                postNormal(
                    EventNextHome(
                        ManufacturingWorkDetailFragment::class.java, bundleOf(
                            Pair("EDIT_MODE", true),
                            Pair("WORK_ENTITY", t)
                        )
                    )
                )
            }
            btnSave.setOnClickListener {
                postNormal(
                    EventNextHome(
                        ManufacturingWorkDetailFragment::class.java, bundleOf(
                            Pair("EDIT_MODE", false),
                        )
                    )
                )
            }
        }
        homeViewModel.getAllOrganizationUnit()
        homeViewModel.getAllRoles()
        if(homeViewModel.listManufacturingWork.value.isNullOrEmpty()) viewModel.getManufacturingWorks() else {
            viewModel.listWorks.value = homeViewModel.listManufacturingWork.value
        }
    }
}