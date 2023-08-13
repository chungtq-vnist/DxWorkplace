package com.example.test.dxworkspace.presentation.ui.home.workplace

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import com.example.test.dxworkspace.R
import com.example.test.dxworkspace.data.entity.user.UserProfileResponse
import com.example.test.dxworkspace.databinding.FragmentChooseListUserBinding
import com.example.test.dxworkspace.presentation.ui.BaseFragment
import com.example.test.dxworkspace.presentation.ui.home.workplace.adapter.SelectUsersAdapter
import com.example.test.dxworkspace.presentation.utils.common.clearText
import com.example.test.dxworkspace.presentation.utils.event.EventBus
import com.example.test.dxworkspace.presentation.utils.event.EventToast
import com.example.test.dxworkspace.presentation.utils.event.EventUpdate

class ChooseUserFragment : BaseFragment<FragmentChooseListUserBinding>() {

    val adapterView by lazy { SelectUsersAdapter() }
    override fun getLayoutId(): Int {
        return R.layout.fragment_choose_list_user
    }


    var listDataChoose = mutableListOf<String>()
    var listDataSource = mutableListOf<UserProfileResponse>()
    var usingfor = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        listDataChoose = arguments?.getStringArrayList("LIST_USER") ?: mutableListOf<String>()
        listDataSource = arguments?.getParcelableArrayList("LIST_DATA") ?: mutableListOf()
        listDataSource = listDataSource.distinctBy { it.id }.toMutableList()
        usingfor = arguments?.getString("USING_FOR") ?:"RESPONSIBLE"
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        adapterView.listChoose = listDataChoose
        binding?.apply {
            rcvRole.adapter = adapterView
            tvHeaderToolbar.text = "Chọn nhân viên"
            ivBack.setOnClickListener { onBackPress() }
            btnMenuMore.setOnClickListener {
                listDataChoose = adapterView.listChoose
                if (listDataChoose.isNotEmpty()) {
                    EventBus.getDefault()
                        .post(EventUpdate(EventUpdate.UPDATE_LIST_USER,Pair(usingfor,listDataChoose)))
                    onBackPress()
                } else showToast(EventToast(0, true, "Không đuọc để trống"))
            }
            search(layoutSearch.itemSearch){
                val x = search(it)
                adapterView.items = x
            }
            layoutSearch.itemSearch.ivClearSearch.setOnClickListener {
                layoutSearch.itemSearch.edtSearch.clearText()
                layoutSearch.itemSearch.ivClearSearch.isVisible = false
                adapterView.items = listDataSource
            }

        }
        adapterView.items = listDataSource

    }

    fun search(query : String) : MutableList<UserProfileResponse>{
        val t = listDataSource.filter { it.name.contains(query,true) }
        return t.toMutableList()
    }
}

