package com.example.test.dxworkspace.presentation.ui.home.manufacturing.command

import android.app.Dialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.core.os.bundleOf
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import com.example.test.dxworkspace.R
import com.example.test.dxworkspace.core.di.viewmodel.ViewModelFactory
import com.example.test.dxworkspace.core.extensions.observe
import com.example.test.dxworkspace.core.extensions.viewModel
import com.example.test.dxworkspace.data.entity.manufacturing_command.ManufacturingCommandModel
import com.example.test.dxworkspace.data.entity.manufacturing_command.StockModel
import com.example.test.dxworkspace.data.entity.manufacturing_command.SubMaterialModel
import com.example.test.dxworkspace.data.entity.user.UserProfileResponse
import com.example.test.dxworkspace.databinding.DialogExportMaterialBinding
import com.example.test.dxworkspace.domain.repository.ConfigRepository
import com.example.test.dxworkspace.domain.usecase.manufacturing_manage.GetAllUsersUseCase
import com.example.test.dxworkspace.presentation.model.menu.Approver
import com.example.test.dxworkspace.presentation.model.menu.BillExportMaterialRequest
import com.example.test.dxworkspace.presentation.model.menu.Good
import com.example.test.dxworkspace.presentation.model.menu.QualityControlStaff
import com.example.test.dxworkspace.presentation.ui.BaseFragment
import com.example.test.dxworkspace.presentation.ui.home.manufacturing.command.adapter.InfoBillExportAdapter
import com.example.test.dxworkspace.presentation.ui.home.manufacturing.command.adapter.InfoMaterialAdapter
import com.example.test.dxworkspace.presentation.ui.home.manufacturing.command.adapter.InfoMaterialExportAdapter
import com.example.test.dxworkspace.presentation.ui.home.workplace.ChooseUserFragment
import com.example.test.dxworkspace.presentation.utils.common.generateCode
import com.example.test.dxworkspace.presentation.utils.common.getTextz
import com.example.test.dxworkspace.presentation.utils.common.postNormal
import com.example.test.dxworkspace.presentation.utils.event.EventBus
import com.example.test.dxworkspace.presentation.utils.event.EventNextHome
import com.example.test.dxworkspace.presentation.utils.event.EventUpdate
import javax.inject.Inject

class DialogExportMaterialFragment(
    val infoAdapter: InfoMaterialAdapter,
    val quantity: Int,
    val command: ManufacturingCommandModel ,
) : BaseFragment<DialogExportMaterialBinding>() {
    override fun getLayoutId(): Int {
        return R.layout.dialog_export_material
    }

//    @Inject
//    lateinit var viewModel: ManufacturingCommandViewModel
//
//    @Inject
//    lateinit var viewModelFactory: ViewModelProvider.Factory
//
//
//
//    var infoMateritalAdapter = InfoMaterialExportAdapter()
//    var infoBillAdapter = InfoBillExportAdapter()
//
//    var request = mutableListOf<BillExportMaterialRequest>()
//    var billNow = BillExportMaterialRequest()
//    var materialNow = SubMaterialModel()
//
//    var lUser = listOf<UserProfileResponse>()
//    var lStock = listOf<StockModel>()
//
//
//    override fun onStart() {
//        super.onStart()
//        EventBus.getDefault().register(this)
//        val dialog = dialog
//        if (dialog != null) {
//            val width = ViewGroup.LayoutParams.MATCH_PARENT
//            val height = ViewGroup.LayoutParams.MATCH_PARENT
//            dialog.window!!.setLayout(width, height)
//        }
//    }
//    override fun onStop() {
//        super.onStop()
//        EventBus.getDefault().unregister(this)
//    }
//
//    fun onBusEvent(event: EventUpdate) {
//        when (event.type) {
//            EventUpdate.UPDATE_LIST_USER -> {
//                val t = event.value as Pair<String, MutableList<String>>
//                when (t.first) {
//                    "APPROVE" -> {
//                        billNow.approvers = t.second.map { Approver(it) }.toMutableList()
//                        binding?.edtApprove?.setText(
//                            getString(
//                                com.example.test.dxworkspace.R.string.number_user,
//                                t.second.size.toString()
//                            )
//                        )
//                    }
//                    "TESTER" -> {
//                        billNow.qualityControlStaffs = t.second.map { QualityControlStaff(it,1) }.toMutableList()
//                        binding?.edtTester?.setText(
//                            getString(
//                                com.example.test.dxworkspace.R.string.number_user,
//                                t.second.size.toString()
//                            )
//                        )
//                    }
//                    "ACCOUNTABLE" -> {
//                        billNow.accountables = t.second
//                        binding?.edtObserver?.setText(
//                            getString(
//                                com.example.test.dxworkspace.R.string.number_user,
//                                t.second.size.toString()
//                            )
//                        )
//                    }
//                    "RESPONSIBLE" -> {
//                        billNow.responsibles = t.second
//                        binding?.edtResponsible?.setText(
//                            getString(
//                                com.example.test.dxworkspace.R.string.number_user,
//                                t.second.size.toString()
//                            )
//                        )
//                    }
//
//                }
//            }
//        }
//    }
//
//    override fun onCreateView(
//        inflater: LayoutInflater,
//        container: ViewGroup?,
//        savedInstanceState: Bundle?
//    ): View? {
//        binding = DialogExportMaterialBinding.inflate(inflater, container, false)
//        return binding.root
//    }
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setStyle(
//            STYLE_NORMAL,
//            R.style.Theme_Black_NoTitleBar_Fullscreen
//        )
////        viewModel = ViewModelProvider(this).get(ManufacturingCommandViewModel::class.java)
//        viewModel.listUser.observe(this,{
//            lUser = it!!
//        })
//////            observe(listUser) {
//////                lUser = it!!
//////            }
////        viewModel.listStock.observe(this,{
////            lStock = it!!
////            binding?.apply {
////                edtStock.setAdapter(
////                    ArrayAdapter(
////                        requireContext(),
////                        android.R.layout.simple_dropdown_item_1line,
////                        lStock
////                    )
////                )
////            }
////        })
////            observe(listStock) {
////                lStock = it!!
////                binding?.apply {
////                    edtStock.setAdapter(
////                        ArrayAdapter(
////                            requireContext(),
////                            android.R.layout.simple_dropdown_item_1line,
////                            lStock
////                        )
////                    )
////                }
////            }
//
////        }
//    }
//
//    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//        super.onViewCreated(view, savedInstanceState)
//        binding.rcvInfoMaterial.adapter = infoAdapter
//        infoAdapter.notifyDataSetChanged()
////        Log.v("TAG",viewModel)
//        viewModel.getAllUser()
//        viewModel.getStock()
//        billNow = BillExportMaterialRequest(code = generateCode("BILL"))
//        binding.apply {
//            edtVariantName.setAdapter(ArrayAdapter(requireContext(),android.R.layout.simple_dropdown_item_1line,command.good.materials!!))
//            edtVariantName.setOnItemClickListener { adapterView, view, i, l ->
//                val m = (adapterView.getItemAtPosition(i) as SubMaterialModel)
//                materialNow = m
//                edtNeedToExport.setText(quantity*(m.quantity!!))
//            }
//            rcvMaterialExport.adapter = infoMateritalAdapter
//            rcvBill.adapter = infoBillAdapter
//            edtApprove.setOnClickListener {
//                postNormal(
//                    EventNextHome(
//                        ChooseUserFragment::class.java,
//                        bundleOf(
//                            Pair("LIST_USER", billNow.approvers.map{it.approver}),
//                            Pair("LIST_DATA", lUser),
//                            Pair("USING_FOR", "APPROVE")
//                        )
//                    )
//                )
//            }
//            edtObserver.setOnClickListener {
//                postNormal(
//                    EventNextHome(
//                        ChooseUserFragment::class.java,
//                        bundleOf(
//                            Pair("LIST_USER", billNow.accountables),
//                            Pair("LIST_DATA", lUser),
//                            Pair("USING_FOR", "ACCOUNTABLE")
//                        )
//                    )
//                )
//            }
//            edtTester.setOnClickListener {
//                postNormal(
//                    EventNextHome(
//                        ChooseUserFragment::class.java,
//                        bundleOf(
//                            Pair("LIST_USER", billNow.qualityControlStaffs.map { it.staff }),
//                            Pair("LIST_DATA", lUser),
//                            Pair("USING_FOR", "TESTER")
//                        )
//                    )
//                )
//            }
//            edtResponsible.setOnClickListener {
//                postNormal(
//                    EventNextHome(
//                        ChooseUserFragment::class.java,
//                        bundleOf(
//                            Pair("LIST_USER", billNow.responsibles),
//                            Pair("LIST_DATA", lUser),
//                            Pair("USING_FOR", "RESPONSIBLE")
//                        )
//                    )
//                )
//            }
//
//
//
//            btnAddProduct.setOnClickListener {
//                billNow.goods.add(Good(materialNow.good!!._id,edtExportQuantity.getTextz().toInt()))
//                materialNow = SubMaterialModel()
//                infoMateritalAdapter.items = billNow.goods
//            }
//            btnAddBill.setOnClickListener {
//                // check thong tin truoc
//                billNow.group = "2"
//                billNow.status = "1"
//                billNow.type = "3"
//                billNow.manufacturingCommand = command._id
//                billNow.manufacturingMill = command.manufacturingMill!!._id
//                billNow.creator = ""
//                Log.v("TAG",billNow.toString())
//                request.add(billNow)
//                billNow = BillExportMaterialRequest(code = generateCode("BILL"))
//                reset()
//
//            }
//
//        }
//    }
//
//    // goi khi luu va khi chinh sua bill
//    fun reset() {
//        binding.apply {
//            edtCode.setText(billNow.code)
//            edtStock.setText(lStock.find{it._id == billNow.fromStock}?.name , false)
//            edtObserver.setText(
//                getString(
//                    com.example.test.dxworkspace.R.string.number_user,
//                    billNow.accountables.size.toString()
//                )
//            )
//            edtApprove.setText(
//                getString(
//                    com.example.test.dxworkspace.R.string.number_user,
//                    billNow.approvers.size.toString()
//                )
//            )
//            edtResponsible.setText(
//                getString(
//                    com.example.test.dxworkspace.R.string.number_user,
//                    billNow.responsibles.size.toString()
//                )
//            )
//            edtTester.setText(
//                getString(
//                    com.example.test.dxworkspace.R.string.number_user,
//                    billNow.qualityControlStaffs.size.toString()
//                )
//            )
//            edtNameShipper.setText(billNow.receiver.name)
//            edtPhoneShipper.setText(billNow.receiver.phone)
//            edtAddressShipper.setText(billNow.receiver.address)
//            infoMateritalAdapter.items = billNow.goods
//        }
//    }
}