package com.example.test.dxworkspace.presentation.ui.home.manufacturing.lot

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ArrayAdapter
import androidx.core.os.bundleOf
import androidx.lifecycle.ViewModelProvider
import com.example.test.dxworkspace.R
import com.example.test.dxworkspace.core.extensions.observe
import com.example.test.dxworkspace.core.extensions.viewModel
import com.example.test.dxworkspace.data.entity.manufacturing_command.StockModel
import com.example.test.dxworkspace.data.entity.manufacturing_lot.ManufacturingLotDetailModel
import com.example.test.dxworkspace.data.entity.user.UserProfileResponse
import com.example.test.dxworkspace.databinding.FragmentImportProductBinding
import com.example.test.dxworkspace.domain.repository.ConfigRepository
import com.example.test.dxworkspace.presentation.model.menu.*
import com.example.test.dxworkspace.presentation.ui.BaseFragment
import com.example.test.dxworkspace.presentation.ui.home.HomeViewModel
import com.example.test.dxworkspace.presentation.ui.home.manufacturing.command.ManufacturingCommandViewModel
import com.example.test.dxworkspace.presentation.ui.home.manufacturing.command.adapter.InfoBillExportAdapter
import com.example.test.dxworkspace.presentation.ui.home.manufacturing.lot.adapter.InfoBillImportAdapter
import com.example.test.dxworkspace.presentation.ui.home.workplace.ChooseUserFragment
import com.example.test.dxworkspace.presentation.utils.common.generateCode
import com.example.test.dxworkspace.presentation.utils.common.getTextz
import com.example.test.dxworkspace.presentation.utils.common.postNormal
import com.example.test.dxworkspace.presentation.utils.event.EventBus
import com.example.test.dxworkspace.presentation.utils.event.EventNextHome
import com.example.test.dxworkspace.presentation.utils.event.EventToast
import com.example.test.dxworkspace.presentation.utils.event.EventUpdate
import javax.inject.Inject

class ImportProductFragment : BaseFragment<FragmentImportProductBinding>() {
    override fun getLayoutId(): Int {
        return R.layout.fragment_import_product
    }

    @Inject
    lateinit var viewModel: ManufacturingCommandViewModel

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    @Inject
    lateinit var lotViewModel : ManufacturingLotViewModel

    @Inject
    lateinit var homeViewModel : HomeViewModel

    @Inject
    lateinit var configRepository: ConfigRepository

    var lotId =""
    var lot = ManufacturingLotDetailModel()
    var request = mutableListOf<BillExportMaterialRequest>()
    var billNow = BillExportMaterialRequest()
    var infoBillAdapter = InfoBillImportAdapter()

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
                                R.string.number_user,
                                t.second.size.toString()
                            )
                        )
                    }
                    "ACCOUNTABLE" -> {
                        billNow.accountables = t.second
                        binding?.edtObserver?.setText(
                            getString(
                                R.string.number_user,
                                t.second.size.toString()
                            )
                        )
                    }
                    "RESPONSIBLE" -> {
                        billNow.responsibles = t.second
                        binding?.edtResponsible?.setText(
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
        lotId = arguments?.getString("LOT_ID") ?: ""
        viewModel = viewModel(viewModelFactory){
        }
        lotViewModel = viewModel(viewModelFactory){
            observe(lotDetail){
                lot = it ?:lot
                binding?.apply {
                    edtProductCode.setText(lot.good?.code)
                    edtProductName.setText(lot.good?.name)
                    edtUnit.setText(lot.good?.baseUnit)
                    edtQuantity.setText(lot.originalQuantity.toString())
                }
            }
            observe(updateStatus){
                if(it == false) showToast(EventToast(text ="Tạo phiếu nhập kho thất bại"))
                else {
                    showToast(EventToast(isFail = false , text ="Tạo phiếu nhập thành công"))
                    EventBus.getDefault().post(EventUpdate(EventUpdate.UPDATE_LOT))
                    onBackPress()
                }
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        lotViewModel.getLotById(lotId)
        binding?.apply {
            billNow = BillExportMaterialRequest(code = generateCode("BILL"))
            ivBack.setOnClickListener { onBackPress() }
            rcvBill.adapter = infoBillAdapter
            lStock = homeViewModel.listStock.value ?: listOf()
            lUser = homeViewModel.listUser.value ?: listOf()
            edtCode.setText(billNow.code)
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
            infoBillAdapter.listStock = lStock
            infoBillAdapter.lot = lot
            btnAddBill.setOnClickListener {
                if(edtNameShipper.getTextz().isEmpty() ||edtPhoneShipper.getTextz().isEmpty() ||billNow.fromStock.isEmpty()
                    || billNow.accountables.isEmpty() || billNow.approvers.isEmpty() || billNow.responsibles.isEmpty() || edtImportQuantity.getTextz().isEmpty()){
                    showToast(EventToast(text = "Vui lòng nhập đủ thông tin"))
                    return@setOnClickListener
                }
                // check thong tin truoc
                billNow.group = "1"
                billNow.status = "1"
                billNow.type = "2"
                billNow.receiver = Receiver(edtNameShipper.getTextz(),edtPhoneShipper.getTextz(),"",edtAddressShipper.getTextz())
                billNow.manufacturingCommand = lot.manufacturingCommand?._id ?: ""
                billNow.manufacturingMill = lot.manufacturingCommand?.manufacturingMill?._id ?: ""
                billNow.goods = mutableListOf(Good(good = lot.good?._id ?:"", quantity = edtImportQuantity.getTextz().toInt(), lots = listOf(
                    SubLot(lot._id,edtImportQuantity.getTextz().toInt())
                )))
                billNow.creator = configRepository.getUser().id
                Log.v("TAG",billNow.toString())
                request.add(billNow)
                infoBillAdapter.items = request
                billNow = BillExportMaterialRequest(code = generateCode("BILL"))
                reset()

            }

        }
    }

    fun reset(){
        binding?.apply {
            edtCode.setText(billNow.code)
            edtStock.setText(lStock.find{it._id == billNow.fromStock}?.name , false)
            edtObserver.setText(
                getString(
                    R.string.number_user,
                    billNow.accountables.size.toString()
                )
            )
            edtApprove.setText(
                getString(
                    R.string.number_user,
                    billNow.approvers.size.toString()
                )
            )
            edtResponsible.setText(
                getString(
                    R.string.number_user,
                    billNow.responsibles.size.toString()
                )
            )
            edtNameShipper.setText(billNow.receiver.name)
            edtPhoneShipper.setText(billNow.receiver.phone)
            edtAddressShipper.setText(billNow.receiver.address)
            edtDes.setText(billNow.description)
            btnSave.setOnClickListener {
                if(request.isEmpty()) showToast(EventToast(text ="Vui lòng chọn đủ thông tin!"))
                else{
                    lotViewModel.updateLot(lotId,2)
                    viewModel.createExportBills(request)
                }
            }
        }
    }

}