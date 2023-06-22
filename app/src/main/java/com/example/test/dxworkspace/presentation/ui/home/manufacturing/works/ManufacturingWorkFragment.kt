package com.example.test.dxworkspace.presentation.ui.home.manufacturing.works

import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.test.dxworkspace.R
import com.example.test.dxworkspace.core.extensions.observe
import com.example.test.dxworkspace.core.extensions.viewModel
import com.example.test.dxworkspace.databinding.FragmentManufacturingWorksBinding
import com.example.test.dxworkspace.presentation.ui.BaseFragment
import com.example.test.dxworkspace.presentation.ui.home.manufacturing.works.adapter.ManufacturingWorkAdapter
import com.example.test.dxworkspace.presentation.ui.home.workplace.WorkplaceViewModel
import javax.inject.Inject

class ManufacturingWorkFragment : BaseFragment<FragmentManufacturingWorksBinding>() {


    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    @Inject
    lateinit var viewModel: ManufacturingViewModel

    val adapter by lazy { ManufacturingWorkAdapter() }

    override fun getLayoutId(): Int {
        return R.layout.fragment_manufacturing_works

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = viewModel(viewModelFactory){
            observe(listWorks){
                adapter.items = listWorks.value?.toMutableList() ?: mutableListOf()
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding?.apply {
            rcvWork.adapter = adapter
            rcvWork.addItemDecoration(
                DividerItemDecoration(requireContext(),
                    LinearLayoutManager.VERTICAL)
            )
        }
        viewModel.getManufacturingWorks()
    }
}