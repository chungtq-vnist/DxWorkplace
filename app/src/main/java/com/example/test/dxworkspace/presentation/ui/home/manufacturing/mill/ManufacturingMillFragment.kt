package com.example.test.dxworkspace.presentation.ui.home.manufacturing.mill

import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.lifecycle.ViewModelProvider
import com.example.test.dxworkspace.R
import com.example.test.dxworkspace.core.extensions.observe
import com.example.test.dxworkspace.core.extensions.viewModel
import com.example.test.dxworkspace.data.entity.manufacturing_mill.ManufacturingMillModel
import com.example.test.dxworkspace.databinding.FragmentManufacturingMillDetailBinding
import com.example.test.dxworkspace.databinding.FragmentManufacturingMillsBinding
import com.example.test.dxworkspace.presentation.ui.BaseFragment
import com.example.test.dxworkspace.presentation.ui.home.HomeViewModel
import com.example.test.dxworkspace.presentation.ui.home.manufacturing.mill.adapter.ManufacturingMillAdapter
import com.example.test.dxworkspace.presentation.ui.home.manufacturing.works.ManufacturingWorkDetailFragment
import com.example.test.dxworkspace.presentation.ui.home.manufacturing.works.ManufacturingWorkViewModel
import com.example.test.dxworkspace.presentation.utils.common.postNormal
import com.example.test.dxworkspace.presentation.utils.event.EventBus
import com.example.test.dxworkspace.presentation.utils.event.EventNextHome
import javax.inject.Inject

class ManufacturingMillFragment : BaseFragment<FragmentManufacturingMillsBinding>() {
    override fun getLayoutId(): Int = R.layout.fragment_manufacturing_mills

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    @Inject
    lateinit var viewModel: ManufacturingMillViewModel

    @Inject
    lateinit var homeViewModel : HomeViewModel

    val adapter by lazy { ManufacturingMillAdapter() }

    override fun onStart() {
        super.onStart()
        EventBus.getDefault().register(this)
    }

    override fun onStop() {
        super.onStop()
        EventBus.getDefault().unregister(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = viewModel(viewModelFactory){
            observe(listMills){
                adapter.items = it ?: listOf()
            }
        }
        observeLoading(viewModel)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding?.apply {
            tvHeaderToolbar.text = "Quản lý xưởng"
            ivBack.setOnClickListener { onBackPress() }
            rcvMill.adapter = adapter
            btnSave.setOnClickListener {
                postNormal(
                    EventNextHome(
                        ManufacturingMillDetailFragment::class.java, bundleOf(
                            Pair("EDIT_MODE", false),
                        )
                    )
                )
            }
        }
        adapter.onClick = {
            postNormal(
                EventNextHome(
                    ManufacturingMillDetailFragment::class.java, bundleOf(
                        Pair("EDIT_MODE", true),
                        Pair("MILL_ID", it)
                    )
                )
            )
        }
        viewModel.getManufacturingMills()
    }

}