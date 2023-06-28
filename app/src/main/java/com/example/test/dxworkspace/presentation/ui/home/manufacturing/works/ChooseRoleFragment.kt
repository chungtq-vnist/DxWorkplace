package com.example.test.dxworkspace.presentation.ui.home.manufacturing.works

import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProvider
import com.example.test.dxworkspace.R
import com.example.test.dxworkspace.core.extensions.viewModel
import com.example.test.dxworkspace.databinding.FragmentChooseManyRolesBinding
import com.example.test.dxworkspace.presentation.ui.BaseFragment
import com.example.test.dxworkspace.presentation.ui.home.HomeViewModel
import com.example.test.dxworkspace.presentation.ui.home.manufacturing.works.adapter.RolesAdapter
import com.example.test.dxworkspace.presentation.utils.event.EventBus
import com.example.test.dxworkspace.presentation.utils.event.EventToast
import com.example.test.dxworkspace.presentation.utils.event.EventUpdate
import javax.inject.Inject

class ChooseRoleFragment : BaseFragment<FragmentChooseManyRolesBinding>() {

    val adapterView by lazy { RolesAdapter() }
    override fun getLayoutId(): Int {
        return R.layout.fragment_choose_many_roles
    }
    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    @Inject
    lateinit var viewModel: ManufacturingWorkViewModel

    @Inject
    lateinit var homeViewModel : HomeViewModel

    var listDataChoose = mutableListOf<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        listDataChoose = arguments?.getStringArrayList("LIST_ROLE") ?: mutableListOf<String>()
        viewModel = viewModel(viewModelFactory) {

        }
        adapterView.listChoose = listDataChoose
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding?.apply {
            rcvRole.adapter = adapterView
            tvHeaderToolbar.text = "Chọn role quản lý"
            ivBack.setOnClickListener { onBackPress() }
            btnMenuMore.setOnClickListener {
                listDataChoose = adapterView.listChoose
                if(listDataChoose.isNotEmpty()) {
                    EventBus.getDefault().post(EventUpdate(EventUpdate.UPDATE_ROLE,listDataChoose))
                    onBackPress()
                } else showToast(EventToast(0,true,"Không đuọc để trống"))
            }
        }
        val roles = homeViewModel.listRole.value ?: listOf()
        adapterView.items = roles.toMutableList()

    }
}

