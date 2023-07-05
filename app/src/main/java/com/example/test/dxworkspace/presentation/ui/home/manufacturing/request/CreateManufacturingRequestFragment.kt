package com.example.test.dxworkspace.presentation.ui.home.manufacturing.request

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import com.example.test.dxworkspace.R
import com.example.test.dxworkspace.core.extensions.observe
import com.example.test.dxworkspace.core.extensions.viewModel
import com.example.test.dxworkspace.data.entity.good.GoodDetailModel
import com.example.test.dxworkspace.data.entity.good.InventoryGoodWrap
import com.example.test.dxworkspace.data.entity.manufacturing_command.ManufacturingCommandModel
import com.example.test.dxworkspace.data.entity.manufacturing_command.StockModel
import com.example.test.dxworkspace.data.entity.manufacturing_work.ManufacturingWorkModel
import com.example.test.dxworkspace.data.entity.manufacturing_work.OrganizationUnit
import com.example.test.dxworkspace.data.entity.product_request.*
import com.example.test.dxworkspace.data.entity.user.UserProfileResponse
import com.example.test.dxworkspace.databinding.FragmentCreateRequestBinding
import com.example.test.dxworkspace.presentation.ui.BaseFragment
import com.example.test.dxworkspace.presentation.ui.home.HomeViewModel
import com.example.test.dxworkspace.presentation.ui.home.manufacturing.command.adapter.InfoMaterialAdapter
import com.example.test.dxworkspace.presentation.ui.home.manufacturing.request.adapter.ItemRequestAdapter
import com.example.test.dxworkspace.presentation.utils.common.generateCode
import com.example.test.dxworkspace.presentation.utils.common.getTextz
import com.example.test.dxworkspace.presentation.utils.convertToDate
import com.example.test.dxworkspace.presentation.utils.event.EventBus
import com.example.test.dxworkspace.presentation.utils.event.EventToast
import com.example.test.dxworkspace.presentation.utils.event.EventUpdate
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

class CreateManufacturingRequestFragment : BaseFragment<FragmentCreateRequestBinding>() {
    override fun getLayoutId(): Int {
        return R.layout.fragment_create_request
    }

    var from: String = ""
    var command = ManufacturingCommandModel()
    var listInventory = listOf<InventoryGoodWrap>()
    val infoAdapter = InfoMaterialAdapter()


    val calendar = Calendar.getInstance()
    var request = ParamCreateProductRequest()
    var adapter = ItemRequestAdapter()
    var allUser = listOf<UserProfileResponse>()
    var allUnit = listOf<OrganizationUnit>()
    var allStock = listOf<StockModel>()
    var allWork = listOf<ManufacturingWorkModel>()
    var goodNow = GoodDetailModel()
    var listGood = mutableListOf<GoodDetailModel>()
    var type = 0

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    @Inject
    lateinit var viewModel: ManufacturingRequestViewModel

    @Inject
    lateinit var homeViewModel: HomeViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        type = arguments?.getInt("TYPE") ?: 1
        from = arguments?.getString("FROM") ?: ""
        command = arguments?.getParcelable("COMMAND") ?: command
        listInventory = arguments?.getParcelableArrayList<InventoryGoodWrap>("INVENTORY") ?: listOf()
        viewModel = viewModel(viewModelFactory) {
            observe(listGoods) {
                binding?.apply {
                    edtProduct.setAdapter(
                        ArrayAdapter(
                            requireContext(),
                            android.R.layout.simple_dropdown_item_1line,
                            it!!
                        )
                    )
                    edtProduct.setOnItemClickListener { adapterView, view, i, l ->
                        val t = (adapterView.getItemAtPosition(i) as GoodDetailModel)
                        goodNow = t
                        edtBaseUnit.setText(t.baseUnit)
                    }
                }
            }
            observe(statusUpdate) {
                if (it == false) showToast(EventToast(text = "Update thất bại"))
                else {
                    showToast(EventToast(isFail = false, text = "Update thành công"))
                    EventBus.getDefault().post(EventUpdate(EventUpdate.UPDATE_REQUEST))
                    onBackPress()
                }
            }
        }
        observe(homeViewModel.listUser) {
            allUser = it ?: allUser
        }
        observe(homeViewModel.listOrganization) {
            allUnit = it ?: allUnit
            binding?.apply {
                edtUnit.setAdapter(
                    ArrayAdapter(
                        requireContext(),
                        android.R.layout.simple_dropdown_item_1line,
                        allUnit
                    )
                )
                edtUnit.setOnItemClickListener { adapterView, view, i, l ->
                    val t = (adapterView.getItemAtPosition(i) as OrganizationUnit)
                    request.orderUnit = t._id
                    edtBuyApprover.setAdapter(
                        ArrayAdapter(
                            requireContext(), android.R.layout.simple_dropdown_item_1line,
                            (t.managers?.first()?.users?.map { it.userId })?.toMutableList()
                                ?: mutableListOf()
                        )
                    )
                }
            }
        }
        observe(homeViewModel.listStock) {
            allStock = it ?: allStock
            binding?.apply {
                edtStock.setAdapter(
                    ArrayAdapter(
                        requireContext(),
                        android.R.layout.simple_dropdown_item_1line,
                        allStock
                    )
                )
                edtStock.setOnItemClickListener { adapterView, view, i, l ->
                    val t = (adapterView.getItemAtPosition(i) as StockModel)
                    request.stock = t._id
                }
            }
        }
        observe(homeViewModel.listManufacturingWork) {
            allWork = it ?: allWork
            if (command._id.isNotEmpty()) allWork = allWork.filter {
                it.manufacturingMills?.map { it._id }
                    ?.contains(command.manufacturingMill?._id) == true
            }
            binding?.apply {
                edtWork.setAdapter(
                    ArrayAdapter(
                        requireContext(),
                        android.R.layout.simple_dropdown_item_1line,
                        allWork
                    )
                )
                edtWork.setOnItemClickListener { adapterView, view, i, l ->
                    val t = (adapterView.getItemAtPosition(i) as ManufacturingWorkModel)
                    request.manufacturingWork = t._id
                    edtWorkApprover.setAdapter(
                        ArrayAdapter(
                            requireContext(), android.R.layout.simple_dropdown_item_1line,
                            (t.organizationalUnit?.managers?.first()?.users?.map { it.userId })?.toMutableList()
                                ?: mutableListOf()
                        )
                    )
                }
            }

        }
        observeLoading(homeViewModel)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        allUser = homeViewModel.listUser.value ?: listOf()
        allStock = homeViewModel.listStock.value ?: listOf()
        allUnit = homeViewModel.listOrganization.value ?: listOf()
        allWork = homeViewModel.listManufacturingWork.value ?: listOf()
        binding?.apply {
            constraintInfoMaterial.isVisible = command._id.isNotEmpty()
            rcvMaterial.adapter = infoAdapter
            infoAdapter.quantity = command.quantity
            infoAdapter.listInventory = listInventory
            infoAdapter.items = command.good.materials ?: listOf()
        }
        if (type == 1) {
            request.code = generateCode("PCR")
            request.status = 1
            request.requestType = 1
            request.type = type
            binding?.tilUnit?.isVisible = true
            binding?.tilBuyApprover?.isVisible = true
        } else if (type == 2) {
            request.code = generateCode("GRR")
            request.status = 1
            request.requestType = 1
            request.type = type
            binding?.tilUnit?.isVisible = false
            binding?.tilBuyApprover?.isVisible = false
        } else {
            request.code = generateCode("GIR")
            request.status = 1
            request.requestType = 1
            request.type = type
            binding?.tilUnit?.isVisible = false
            binding?.tilBuyApprover?.isVisible = false
        }
        binding?.apply {
            edtCode.setText(request.code)
            rcvCreate.adapter = adapter
            edtTime.setOnClickListener {
                showDateTimePickerDialog()
            }
            edtStock.setAdapter(
                ArrayAdapter(
                    requireContext(),
                    android.R.layout.simple_dropdown_item_1line,
                    allStock
                )
            )
            edtStock.setOnItemClickListener { adapterView, view, i, l ->
                val t = (adapterView.getItemAtPosition(i) as StockModel)
                request.stock = t._id
            }
            edtWork.setAdapter(
                ArrayAdapter(requireContext(), android.R.layout.simple_dropdown_item_1line, allWork)
            )
            edtWork.setOnItemClickListener { adapterView, view, i, l ->
                val t = (adapterView.getItemAtPosition(i) as ManufacturingWorkModel)
                request.manufacturingWork = t._id
                edtWorkApprover.setAdapter(
                    ArrayAdapter(
                        requireContext(), android.R.layout.simple_dropdown_item_1line,
                        (t.organizationalUnit?.managers?.first()?.users?.map { it.userId })?.toMutableList()
                            ?: mutableListOf()
                    )
                )
            }
            edtWorkApprover.setOnItemClickListener { adapterView, view, i, l ->
                val t = (adapterView.getItemAtPosition(i) as UserProfileResponse)
                request.approvers?.removeIf { it.approveType == 1 }
                request.approvers?.add(
                    ParamInformation(
                        mutableListOf(ParamApprover(null, t.id)),
                        1
                    )
                )
            }
            edtUnit.setAdapter(
                ArrayAdapter(
                    requireContext(),
                    android.R.layout.simple_dropdown_item_1line,
                    allUnit
                )
            )
            edtUnit.setOnItemClickListener { adapterView, view, i, l ->
                val t = (adapterView.getItemAtPosition(i) as OrganizationUnit)
                request.orderUnit = t._id
                edtBuyApprover.setAdapter(
                    ArrayAdapter(
                        requireContext(), android.R.layout.simple_dropdown_item_1line,
                        (t.managers?.first()?.users?.map { it.userId })?.toMutableList()
                            ?: mutableListOf()
                    )
                )
            }
            edtBuyApprover.setOnItemClickListener { adapterView, view, i, l ->
                val t = (adapterView.getItemAtPosition(i) as UserProfileResponse)
                request.approvers?.removeIf { it.approveType == 2 }
                request.approvers?.add(
                    ParamInformation(
                        mutableListOf(ParamApprover(null, t.id)),
                        2
                    )
                )
            }
            edtProductType.setAdapter(
                ArrayAdapter(
                    requireContext(), android.R.layout.simple_dropdown_item_1line,
                    listOf("Nguyên vật liệu", "Dụng cụ", "Thành phẩm", "Phế phẩm")
                )
            )
            edtProductType.setOnItemClickListener { adapterView, view, i, l ->
                val t = (adapterView.getItemAtPosition(i) as String)
                when (t) {
                    "Nguyên vật liệu" -> {
                        viewModel.getGoodByType("material")
                    }
                    "Dụng cụ" -> {
                        viewModel.getGoodByType("equipment")
                    }
                    "Thành phẩm" -> {
                        viewModel.getGoodByType("product")
                    }
                    "Phế phẩm" -> {
                        viewModel.getGoodByType("waste")
                    }
                }
            }
            btnAdd.setOnClickListener {
                if (goodNow._id.isNotEmpty() && edtQuantity.getTextz().isNotEmpty()) {
                    goodNow.quantity = edtQuantity.getTextz().toLong()
                    listGood.add(goodNow)
                    goodNow = GoodDetailModel()
                    adapter.items = listGood
                }
            }
            btnSave.setOnClickListener {
                request.goods =
                    listGood.map { ParamGood(it._id, it.quantity.toString()) }.toMutableList()
                println(request.toString())
                if(from == "BUY_IN_COMMAND")  request.commandId = command._id
                viewModel.createProductRequest(request)
            }

        }
        if(from.isNotEmpty()){
            homeViewModel.getAllUser()
            homeViewModel.getAllOrganizationUnit()
            homeViewModel.getStock()
            homeViewModel.getManufacturingWorksWithoutRole()
        }

    }


    private fun showDateTimePickerDialog() {
        val datePickerDialog = DatePickerDialog(
            requireContext(),
            { _, year, month, dayOfMonth ->
                calendar.set(year, month, dayOfMonth)
                binding?.edtTime?.setText(
                    SimpleDateFormat(
                        "dd-MM-yyyy",
                        Locale.getDefault()
                    ).format(calendar.time)
                )
                request.desiredTime =
                    SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault()).format(
                        calendar.time
                    )

            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        )
        datePickerDialog.show()
    }
}