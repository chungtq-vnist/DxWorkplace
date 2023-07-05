package com.example.test.dxworkspace.presentation.ui.home.manufacturing.command

import android.app.Dialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import com.example.test.dxworkspace.R
import com.example.test.dxworkspace.core.di.viewmodel.ViewModelFactory
import com.example.test.dxworkspace.core.extensions.observe
import com.example.test.dxworkspace.core.extensions.viewModel
import com.example.test.dxworkspace.data.entity.good.InventoryGoodWrap
import com.example.test.dxworkspace.data.entity.manufacturing_command.ManufacturingCommandModel
import com.example.test.dxworkspace.data.entity.manufacturing_command.StockModel
import com.example.test.dxworkspace.data.entity.manufacturing_command.SubMaterialModel
import com.example.test.dxworkspace.data.entity.user.UserProfileResponse
import com.example.test.dxworkspace.databinding.DialogExportMaterialBinding
import com.example.test.dxworkspace.domain.repository.ConfigRepository
import com.example.test.dxworkspace.domain.usecase.manufacturing_manage.GetAllUsersUseCase
import com.example.test.dxworkspace.presentation.model.menu.*
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
import com.example.test.dxworkspace.presentation.utils.event.EventToast
import com.example.test.dxworkspace.presentation.utils.event.EventUpdate
import javax.inject.Inject
import com.google.gson.reflect.TypeToken
import kotlinx.android.synthetic.main.custom_toast.*


class ExportMaterialFragment(

) : BaseFragment<DialogExportMaterialBinding>() {
    override fun getLayoutId(): Int {
        return R.layout.dialog_export_material
    }

    @Inject
    lateinit var viewModel: ManufacturingCommandViewModel

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    @Inject
    lateinit var configRepository: ConfigRepository

    var quantity: Int =1
    var command: ManufacturingCommandModel = ManufacturingCommandModel()
    var listInventory = listOf<InventoryGoodWrap>()

    var infoMateritalAdapter = InfoMaterialExportAdapter()
    var infoBillAdapter = InfoBillExportAdapter()
    val infoAdapter = InfoMaterialAdapter()

    var request = mutableListOf<BillExportMaterialRequest>()
    var billNow = BillExportMaterialRequest()
    var materialNow = SubMaterialModel()

    var lUser = listOf<UserProfileResponse>()
    var lStock = listOf<StockModel>()


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
                    "APPROVE" -> {
                        billNow.approvers = t.second.map { Approver(it) }.toMutableList()
                        binding?.edtApprove?.setText(
                            getString(
                                com.example.test.dxworkspace.R.string.number_user,
                                t.second.size.toString()
                            )
                        )
                    }
                    "TESTER" -> {
                        billNow.qualityControlStaffs = t.second.map { QualityControlStaff(it,1) }.toMutableList()
                        binding?.edtTester?.setText(
                            getString(
                                com.example.test.dxworkspace.R.string.number_user,
                                t.second.size.toString()
                            )
                        )
                    }
                    "ACCOUNTABLE" -> {
                        billNow.accountables = t.second
                        binding?.edtObserver?.setText(
                            getString(
                                com.example.test.dxworkspace.R.string.number_user,
                                t.second.size.toString()
                            )
                        )
                    }
                    "RESPONSIBLE" -> {
                        billNow.responsibles = t.second
                        binding?.edtResponsible?.setText(
                            getString(
                                com.example.test.dxworkspace.R.string.number_user,
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
        quantity = arguments?.getInt("QUANTITY") ?: 1
        command = arguments?.getParcelable<ManufacturingCommandModel>("COMMAND") ?: command
        val t = arguments?.getString("INVENTORY") ?: ""
        listInventory = gson.fromJson(t, object : TypeToken<List<InventoryGoodWrap?>?>() {}.type)
        viewModel = viewModel(viewModelFactory){
            observe(listUser) {
                lUser = it!!
            }
            observe(listStock) {
                lStock = it!!
                binding?.apply {
                    edtStock.setAdapter(
                        ArrayAdapter(
                            requireContext(),
                            android.R.layout.simple_dropdown_item_1line,
                            lStock
                        )
                    )
                    edtStock.setOnItemClickListener { adapterView, view, i, l ->
                        val t = (adapterView.getItemAtPosition(i) as StockModel)
                        billNow.fromStock = t._id
                    }
                }
            }
            observe(statusExportBill){
                if(it == false) showToast(EventToast(text ="Tạo phiếu xuất kho thất bại"))
                else {
                    showToast(EventToast(isFail = false , text ="Tạo phiếu xuất thành công"))
                    EventBus.getDefault().post(EventUpdate(EventUpdate.UPDATE_COMMAND))
                    onBackPress()
                }
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
//        Log.v("TAG",viewModel)
        viewModel.getAllUser()
        viewModel.getStock()
        billNow = BillExportMaterialRequest(code = generateCode("BILL"))
        binding?.apply {
            edtCode.setText(billNow.code)
            rcvInfoMaterial.adapter = infoAdapter
            infoAdapter.listInventory = listInventory
            infoAdapter.quantity = quantity
            infoAdapter.items = command.good.materials ?: listOf()
            btnAddBill.visibility = View.INVISIBLE
            edtVariantName.setAdapter(ArrayAdapter(requireContext(),android.R.layout.simple_dropdown_item_1line,command.good.materials!!))
            edtVariantName.setOnItemClickListener { adapterView, view, i, l ->
                val m = (adapterView.getItemAtPosition(i) as SubMaterialModel)
                materialNow = m
                edtNeedToExport.setText((quantity*(m.quantity!!)).toString())
            }
            rcvMaterialExport.adapter = infoMateritalAdapter
            rcvBill.adapter = infoBillAdapter
            infoBillAdapter.command = command
            edtApprove.setOnClickListener {
                postNormal(
                    EventNextHome(
                        ChooseUserFragment::class.java,
                        bundleOf(
                            Pair("LIST_USER", billNow.approvers.map{it.approver}),
                            Pair("LIST_DATA", lUser),
                            Pair("USING_FOR", "APPROVE")
                        )
                    )
                )
            }
            edtObserver.setOnClickListener {
                postNormal(
                    EventNextHome(
                        ChooseUserFragment::class.java,
                        bundleOf(
                            Pair("LIST_USER", billNow.accountables),
                            Pair("LIST_DATA", lUser),
                            Pair("USING_FOR", "ACCOUNTABLE")
                        )
                    )
                )
            }
            edtTester.setOnClickListener {
                postNormal(
                    EventNextHome(
                        ChooseUserFragment::class.java,
                        bundleOf(
                            Pair("LIST_USER", billNow.qualityControlStaffs.map { it.staff }),
                            Pair("LIST_DATA", lUser),
                            Pair("USING_FOR", "TESTER")
                        )
                    )
                )
            }
            edtResponsible.setOnClickListener {
                postNormal(
                    EventNextHome(
                        ChooseUserFragment::class.java,
                        bundleOf(
                            Pair("LIST_USER", billNow.responsibles),
                            Pair("LIST_DATA", lUser),
                            Pair("USING_FOR", "RESPONSIBLE")
                        )
                    )
                )
            }



            btnAddProduct.setOnClickListener {
                if(materialNow.quantity ?: 0 > 0 && materialNow.good?._id?.isNotEmpty() == true) {
                    billNow.goods.add(
                        Good(
                            materialNow.good!!._id,
                            edtExportQuantity.getTextz().toInt(),
                            materialNow.good!!.code
                        )
                    )
                    materialNow = SubMaterialModel()
                    infoMateritalAdapter.items = billNow.goods
                    btnAddBill.visibility = View.VISIBLE
                } else showToast(EventToast(text = "Vui lòng chọn đủ thông tin!"))
            }
            btnAddBill.setOnClickListener {
                // check thong tin truoc
                billNow.group = "2"
                billNow.status = "1"
                billNow.type = "3"
                billNow.receiver = Receiver(edtNameShipper.getTextz(),edtPhoneShipper.getTextz(),"",edtAddressShipper.getTextz())
                billNow.manufacturingCommand = command._id
                billNow.manufacturingMill = command.manufacturingMill!!._id
                billNow.creator = configRepository.getUser().id
                Log.v("TAG",billNow.toString())
                request.add(billNow)
                infoBillAdapter.items = request
                billNow = BillExportMaterialRequest(code = generateCode("BILL"))
                reset()

            }

        }
    }

    // goi khi luu va khi chinh sua bill
    fun reset() {
        binding?.apply {
            edtCode.setText(billNow.code)
            edtStock.setText(lStock.find{it._id == billNow.fromStock}?.name , false)
            edtObserver.setText(
                getString(
                    com.example.test.dxworkspace.R.string.number_user,
                    billNow.accountables.size.toString()
                )
            )
            edtApprove.setText(
                getString(
                    com.example.test.dxworkspace.R.string.number_user,
                    billNow.approvers.size.toString()
                )
            )
            edtResponsible.setText(
                getString(
                    com.example.test.dxworkspace.R.string.number_user,
                    billNow.responsibles.size.toString()
                )
            )
            edtTester.setText(
                getString(
                    com.example.test.dxworkspace.R.string.number_user,
                    billNow.qualityControlStaffs.size.toString()
                )
            )
            edtNameShipper.setText(billNow.receiver.name)
            edtPhoneShipper.setText(billNow.receiver.phone)
            edtAddressShipper.setText(billNow.receiver.address)
            infoMateritalAdapter.items = billNow.goods

            btnSave.setOnClickListener {
                if(request.isEmpty()) showToast(EventToast(text ="Vui lòng chọn đủ thông tin!"))
                else{
                    viewModel.updateCommand(RequestApproveCommand(configRepository.getUser().id ,2 ),command._id)
                    viewModel.createExportBills(request)
                }
            }
        }
    }
}