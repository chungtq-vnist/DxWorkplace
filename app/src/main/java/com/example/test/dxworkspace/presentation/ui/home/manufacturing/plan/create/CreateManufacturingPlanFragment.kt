package com.example.test.dxworkspace.presentation.ui.home.manufacturing.plan.create

import android.app.DatePickerDialog
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ArrayAdapter
import android.widget.LinearLayout
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.core.widget.doAfterTextChanged
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.test.dxworkspace.R
import com.example.test.dxworkspace.core.extensions.justTry
import com.example.test.dxworkspace.core.extensions.observe
import com.example.test.dxworkspace.core.extensions.viewModel
import com.example.test.dxworkspace.data.entity.good.GoodDetailModel
import com.example.test.dxworkspace.data.entity.good.InventoryGoodWrap
import com.example.test.dxworkspace.data.entity.manufacturing_mill.ManufacturingMillModel
import com.example.test.dxworkspace.data.entity.manufacturing_mill.SubUserBasicModel
import com.example.test.dxworkspace.data.entity.manufacturing_plan.SubGoodPlan
import com.example.test.dxworkspace.data.entity.user.UserProfileResponse
import com.example.test.dxworkspace.data.entity.work_schedule.SubCommandInWorkSchedule
import com.example.test.dxworkspace.data.entity.work_schedule.WorkScheduleModel
import com.example.test.dxworkspace.databinding.FragmentCreateManufacturingPlanBinding
import com.example.test.dxworkspace.domain.model.manufacturing_plan.ArrayWorkerSchedule
import com.example.test.dxworkspace.domain.model.manufacturing_plan.RequestManufacturingPlan
import com.example.test.dxworkspace.domain.model.manufacturing_plan.SubRequestCommand
import com.example.test.dxworkspace.domain.model.manufacturing_plan.SubRequestGood
import com.example.test.dxworkspace.domain.repository.ConfigRepository
import com.example.test.dxworkspace.presentation.model.menu.WorkerScheduleRequest
import com.example.test.dxworkspace.presentation.ui.BaseFragment
import com.example.test.dxworkspace.presentation.ui.home.manufacturing.command.ManufacturingCommandDetailFragment
import com.example.test.dxworkspace.presentation.ui.home.manufacturing.command.ManufacturingCommandViewModel
import com.example.test.dxworkspace.presentation.ui.home.manufacturing.plan.ManufacturingPlanViewModel
import com.example.test.dxworkspace.presentation.ui.home.manufacturing.plan.adapter.*
import com.example.test.dxworkspace.presentation.ui.home.workplace.ChooseUserFragment
import com.example.test.dxworkspace.presentation.utils.common.generateCode
import com.example.test.dxworkspace.presentation.utils.common.getTextz
import com.example.test.dxworkspace.presentation.utils.common.postNormal
import com.example.test.dxworkspace.presentation.utils.convertToUTCTime
import com.example.test.dxworkspace.presentation.utils.event.EventBus
import com.example.test.dxworkspace.presentation.utils.event.EventNextHome
import com.example.test.dxworkspace.presentation.utils.event.EventToast
import com.example.test.dxworkspace.presentation.utils.event.EventUpdate
import com.example.test.dxworkspace.presentation.utils.getDateYYYYMMDDHHMMSS
import com.example.test.dxworkspace.presentation.utils.isDateInMonth
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

class CreateManufacturingPlanFragment : BaseFragment<FragmentCreateManufacturingPlanBinding>() {
    override fun getLayoutId(): Int {
        return R.layout.fragment_create_manufacturing_plan
    }

    val calendar = Calendar.getInstance()
    var page = 1
    var listGoodForCommand = listOf<SubRequestGood>()
    val request = RequestManufacturingPlan()
    var listApproveUsers = listOf<UserProfileResponse>()
    var listGoods = listOf<GoodDetailModel>()
    var listInventorys = listOf<InventoryGoodWrap>()
    var listMill = listOf<ManufacturingMillModel>()
    var listUsers = listOf<UserProfileResponse>()
    var listCommandTemp = mutableListOf<SubRequestCommand>()
    var listSchedules = listOf<WorkScheduleModel?>()
    var listUsersFree = listOf<UserProfileResponse>()

    var commandNow = SubRequestCommand(generateCode("LSX"))
    var commandNowSchedule = SubRequestCommand(generateCode("LSX"))
    var listTempUsesResponsible = listOf<String>()

    val adapterProduct by lazy{ ChooseProductAdapter() }

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    @Inject
    lateinit var viewModel: ManufacturingPlanViewModel

    @Inject
    lateinit var configRepository: ConfigRepository

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
                    "APPROVE_PLAN" -> {
                        request.approvers = t.second
                        binding?.edtUserApproval?.setText(
                            getString(
                                R.string.number_user,
                                t.second.size.toString()
                            )
                        )
                    }
                    "TESTER_COMMAND" -> {
                        commandNow.qualityControlStaffs = t.second
                        binding?.edtTester?.setText(
                            getString(
                                R.string.number_user,
                                t.second.size.toString()
                            )
                        )
                    }
                    "ACCOUNTABLE_COMMAND" -> {
                        commandNow.accountables = t.second
                        binding?.edtObserver?.setText(
                            getString(
                                R.string.number_user,
                                t.second.size.toString()
                            )
                        )
                    }
                    "APPROVE_COMMAND" -> {
                        commandNow.approvers = t.second
                        binding?.edtLineCost?.setText(
                            getString(
                                R.string.number_user,
                                t.second.size.toString()
                            )
                        )
                    }
                    "RESPONSIBLE_COMMAND" -> {
//                        commandNowSchedule.responsibles = t.second
                        listTempUsesResponsible = t.second
                        binding?.edtUserResponsible?.setText(
                            getString(
                                R.string.number_user,
                                t.second.size.toString()
                            )
                        )
                    }
//                    "ACCOUNTABLE" -> {
//                        task.accountableEmployees = t.second
//                        binding?.edtUserApproval?.setText(
//                            getString(
//                                R.string.number_user,
//                                t.second.size.toString()
//                            )
//                        )
//                    }
//                    "CONSULT" -> {
//                        task.consultedEmployees = t.second
//                        binding?.edtUserConsult?.setText(
//                            getString(
//                                R.string.number_user,
//                                t.second.size.toString()
//                            )
//                        )
//                    }
//                    "INFORMER" -> {
//                        task.informedEmployees = t.second
//                        binding?.edtUserInformer?.setText(
//                            getString(
//                                R.string.number_user,
//                                t.second.size.toString()
//                            )
//                        )
//                    }

                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = viewModel(viewModelFactory) {
            observe(listUserApporvers) {
                listApproveUsers = it ?: listOf()
            }
            observe(listGood){
                listGoods = it ?: listOf()
                adapterProduct.listgood = listGoods
                adapterProduct.notifyDataSetChanged()
            }
            observe(listInventory){
                listInventorys = it ?: listOf()
                adapterProduct.listInventory = listInventorys
                adapterProduct.notifyDataSetChanged()
            }
            observe(listMills){
                listMill = it ?: listOf()
            }
            observe(listUser){
                listUsers = it ?: listOf()
            }
            observe(listSchedule){
                listSchedules = it ?: listOf()
                request.manufacturingCommands.forEach {
                    it.responsibles = listOf()
                }
                request.arrayWorkerSchedules = mutableListOf()
            }
            observe(listUserFree){
                listUsersFree = it?.map { UserProfileResponse(it._id,it.name,it.email) } ?: listOf()
            }
            observe(updateStatus) {
                if(it == false ) showToast(EventToast(text ="Update thất bại"))
                else {
                    showToast(EventToast(isFail = false, text ="Update thành công"))
                    EventBus.getDefault().post(EventUpdate(EventUpdate.UPDATE_PLAN))
                    onBackPress()
                }
            }
        }
        observeLoading(viewModel)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        request.code = generateCode("KHSX")
        init()
    }

    fun onBackPressNew(){
        if(request.approvers.isNotEmpty() || request.startDate.isNotEmpty() || request.endDate.isNotEmpty() ||
                request.goods.isNotEmpty()){
            MaterialAlertDialogBuilder(requireContext())
                .setTitle("Dữ liệu chưa được lưu")
                .setMessage("Xin lưu ý, bạn chưa lưu lại dữ liệu. Bạn có chắc muốn rời khỏi trang này?")
                .setNegativeButton("Không"){ dialog, which ->
                    dialog.dismiss()
                }
                .setPositiveButton("Thoát") { dialog, which ->
                    dialog.dismiss()
                    onBackPress()
                }
                .show()
        } else onBackPress()
    }

    fun init() {
        binding?.apply {
            ivBack.setOnClickListener {
                onBackPressNew()
            }
            if(page == 1){
                btnPrevious.isVisible = false
            }
            request.creator = configRepository.getUser().id
            edtPlanCode.setText(request.code)
            btnNext.setOnClickListener {
                if(page == 1){
                    if(request.endDate.isEmpty() || request.startDate.isEmpty() || request.approvers.isEmpty()) {
                        showToast(EventToast(text = "Vui lòng nhập đủ thông tin"))
                        return@setOnClickListener
                    } else {
                        val t = adapterProduct.data.filter { it.goodId != "" && it.quantity > 0 }
                        val s = mutableListOf<DataProductInput>()
                        t.forEach { a ->
                            if(s.find { it.goodId == a.goodId } == null) s.add(DataProductInput(a.quantity,a.goodId)) else {
                                s.find { it.goodId == a.goodId }!!.quantity += a.quantity
                            }
                        }
                        if(s.isEmpty()) {
                            showToast(EventToast(text = "Vui lòng chọn sản phẩm"))
                            return@setOnClickListener
                        } else {
                            request.goods = s.map { SubRequestGood(it.goodId, it.quantity) }
                            adapterProduct.data = s
                            gotoScreen2()
                        }
                    }
                } else if(page == 2){
                    request.goods.forEach { k ->
                        val t = listCommandTemp.find{it.goodID == k.good}
                        if(t == null || t.quantity != k.quantity.toString()){
                            showToast(EventToast(text = "Vui lòng phân đủ lệnh sản xuất"))
                            return@setOnClickListener
                        } else {
                            request.manufacturingCommands = listCommandTemp
                            gotoScreen3()
                        }
                    }
                }
            }
            btnPrevious.setOnClickListener {
                if(page == 2){
                    gotoScreen1()
                } else if(page == 3){
                    if(!constraintSchedule.isVisible) gotoScreen2() else {
                        constraintSchedule.isVisible = false
                        constraintPlan.isVisible = true
                        if(commandNowSchedule.responsibles.isEmpty()){
                            scheduleNow?.turns?.forEach { k ->
                                k.forEachIndexed { index, subCommandInWorkSchedule ->
                                    if(subCommandInWorkSchedule?._id?.isEmpty() == true && !subCommandInWorkSchedule.isSave){
                                        k[index] = null
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        viewModel.getListApprovers(configRepository.getCurrentRole().id)
        viewModel.getListSalesOrder(configRepository.getCurrentRole().id)
        viewModel.getListGoodManage(configRepository.getCurrentRole().id)
        viewModel.getManufacturingMills()
        viewModel.getAllUser()
        initForScreen1()
        gotoScreen1()
    }

    fun gotoScreen1(){
        page = 1
        binding?.apply {
            screen1.isVisible = true
            screen2.isVisible = false
            screen3.isVisible = false
            btnPrevious.isVisible = false
        }
    }
    fun gotoScreen2(){
        page = 2
        binding?.apply {
            screen1.isVisible = false
            screen2.isVisible = true
            screen3.isVisible = false
            btnPrevious.isVisible = true
            btnNext.isVisible = true
        }
        initForScreen2()
    }
    fun gotoScreen3(){
        binding?.apply {
            screen1.isVisible = false
            screen2.isVisible = false
            screen3.isVisible = true
            btnPrevious.isVisible = true
            btnNext.isVisible = false
        }
        page = 3
        initForScreen3()
    }

    fun initForScreen1() {
        binding?.apply {
//            edtTimeStart.isEnabled = false
//            edtTimeEnd.isEnabled = false

            edtTimeStart.setOnClickListener {
                showMaterialDatePickerDialog(true)
            }
            edtTimeEnd.setOnClickListener {
                showMaterialDatePickerDialog(false)
            }
            edtUserApproval.setOnClickListener {
                postNormal(
                    EventNextHome(
                        ChooseUserFragment::class.java,
                        bundleOf(
                            Pair("LIST_USER", request.approvers ?: listOf()),
                            Pair("LIST_DATA", listApproveUsers),
                            Pair("USING_FOR", "APPROVE_PLAN")
                        )
                    )
                )
            }
            edtDes.doAfterTextChanged {
                request.description = edtDes.getTextz()
            }
            adapterProduct.listInventory = listInventorys
            adapterProduct.listgood = listGoods
            rcvProduct.adapter = adapterProduct
            if(request.goods.isEmpty()) {
                val data = mutableListOf<DataProductInput>(DataProductInput())
                adapterProduct.data = data
            } else {
                val data = mutableListOf<DataProductInput>()
                request.goods.forEach {
                    data.add(DataProductInput(it.quantity,it.good))
                }
            }
            btnAddProduct.setOnClickListener {
                val t = adapterProduct.data
                t.add(DataProductInput())
                adapterProduct.data = t
            }

        }
    }
    fun initForScreen2(){
        binding?.apply {
            screen1.isVisible = false
            screen2.isVisible = true
            screen3.isVisible = false
            btnPrevious.isVisible = true
        }
        if(listGoodForCommand.size != request.goods.size){
            request.manufacturingCommands = listOf()
            listGoodForCommand = request.goods.map { SubRequestGood(it.good,it.quantity) }
            listCommandTemp = mutableListOf()
        } else {
            for(i in 0 until request.goods.size){
                if(request.goods[i].quantity != listGoodForCommand[i].quantity){
                    request.manufacturingCommands = listOf()
                    listGoodForCommand = request.goods.map { SubRequestGood(it.good,it.quantity) }
                    listCommandTemp = mutableListOf()
                }
            }
        }
        val infoAdapter = InfoProductAdapter()
        val productivityAdapter = InfoProductivityAdapter()
        val createCommandAdapter = CreateCommandAdapter()
        binding?.apply {
            rcvProductPartCommand.adapter = infoAdapter
            rcvProductivity.adapter = productivityAdapter
            rcvCreateCommand.adapter = createCommandAdapter
        }
        infoAdapter.listgood = listGoods
        infoAdapter.listInventory = listInventorys
        val t = request.goods.map{DataInfoProduct(it.quantity,it.good,it.quantity)}
        listCommandTemp.forEach { k ->
            val e = t.find { it.goodId == k.goodID }
            if(e != null) e.quantityNotOrder = e.quantityNotOrder - k.quantity.toInt()
        }
        infoAdapter.data = t

        val listProductivity = mutableListOf<GoodDetailModel>()
        listGoods.forEach { k ->
           if(listGoodForCommand.find { it.good == k._id } != null) {
               val e = k.manufacturingMills?.filter { r ->
                   listMill.find { it._id == r.manufacturingMill?._id } != null
               }
               if(!e.isNullOrEmpty()){
                   e.forEach {
                       listProductivity.add(GoodDetailModel(name = k.name , code = k.code, manufacturingMills = listOf(it)))
                   }
               }
           }
        }
        productivityAdapter.data = listProductivity

        var listProduct = listGoods.filter { a -> request.goods.find { it.good == a._id } != null }

        createCommandAdapter.listgood = listProduct
        createCommandAdapter.listInfoGood = infoAdapter.data
        createCommandAdapter.data = listCommandTemp

        binding?.apply {
            edtCode.setText(commandNow.code)
            val adapter = ArrayAdapter(requireContext(),android.R.layout.simple_dropdown_item_1line,listProduct)
            edtVariantName.setAdapter(adapter)
            edtVariantName.setOnItemClickListener { adapterView, view, i, l ->
                commandNow.goodID = (adapterView.getItemAtPosition(i) as GoodDetailModel)._id
            }
            edtPriceInit.doAfterTextChanged {
                commandNow.quantity = if(edtPriceInit.getTextz().isEmpty()) "0" else edtPriceInit.getTextz()
            }
            edtLineCost.setText(context?.getString(R.string.number_user,commandNow.approvers.size.toString()))
            edtObserver.setText(context?.getString(R.string.number_user,commandNow.accountables.size.toString()))
            edtTester.setText(context?.getString(R.string.number_user,commandNow.qualityControlStaffs.size.toString()))
            edtLineCost.setOnClickListener {
                postNormal(
                    EventNextHome(
                        ChooseUserFragment::class.java,
                        bundleOf(
                            Pair("LIST_USER", commandNow.approvers),
                            Pair("LIST_DATA", listUsers),
                            Pair("USING_FOR", "APPROVE_COMMAND")
                        )
                    )
                )
            }
            edtObserver.setOnClickListener {
                postNormal(
                    EventNextHome(
                        ChooseUserFragment::class.java,
                        bundleOf(
                            Pair("LIST_USER", commandNow.accountables),
                            Pair("LIST_DATA", listUsers),
                            Pair("USING_FOR", "ACCOUNTABLE_COMMAND")
                        )
                    )
                )
            }
            edtTester.setOnClickListener {
                postNormal(
                    EventNextHome(
                        ChooseUserFragment::class.java,
                        bundleOf(
                            Pair("LIST_USER", commandNow.qualityControlStaffs),
                            Pair("LIST_DATA", listUsers),
                            Pair("USING_FOR", "TESTER_COMMAND")
                        )
                    )
                )
            }
            createCommandAdapter.onRemove ={ p , i ->
                listCommandTemp.removeAt(p)
                createCommandAdapter.notifyDataSetChanged()
                val s = request.goods.map{DataInfoProduct(it.quantity,it.good,it.quantity)}
                listCommandTemp.forEach { k ->
                    val e = s.find { it.goodId == k.goodID }
                    if(e != null) e.quantityNotOrder = e.quantityNotOrder - k.quantity.toInt()
                }
                infoAdapter.data = s
            }
            createCommandAdapter.onEdit ={ p , i ->
                listCommandTemp.removeAt(p)
                commandNow = i
                createCommandAdapter.notifyDataSetChanged()
                edtCode.setText(commandNow.code)
                edtPriceInit.setText(commandNow.quantity)
                edtLineCost.setText(context?.getString(R.string.number_user,commandNow.approvers.size.toString()))
                edtObserver.setText(context?.getString(R.string.number_user,commandNow.accountables.size.toString()))
                edtTester.setText(context?.getString(R.string.number_user,commandNow.qualityControlStaffs.size.toString()))
                edtVariantName.setText(listGoods.find{it._id == commandNow.goodID}.toString(),false)
                val s = request.goods.map{DataInfoProduct(it.quantity,it.good,it.quantity)}
                listCommandTemp.forEach { k ->
                    val e = s.find { it.goodId == k.goodID }
                    if(e != null) e.quantityNotOrder = e.quantityNotOrder - k.quantity.toInt()
                }
                infoAdapter.data = s
            }


            btnAddCommand.setOnClickListener {
                if(commandNow.quantity.toInt() == 0 || commandNow.goodID.isEmpty() ||
                        commandNow.accountables.isEmpty() || commandNow.qualityControlStaffs.isEmpty()
                    || commandNow.approvers.isEmpty()) {
                    showToast(EventToast(text = "Vui lòng nhập đủ thông tin"))
                    return@setOnClickListener
                } else {
                    val t = infoAdapter.data
                    if(t.find{it.goodId == commandNow.goodID}?.quantityNotOrder ?: 0 < commandNow.quantity.toInt()){
                        showToast(EventToast(text = "Số lượng không hợp lệ"))
                        return@setOnClickListener
                    } else {
                        listCommandTemp.add(
                            SubRequestCommand(commandNow.code,commandNow.goodID, quantity = commandNow.quantity,
                                good = SubGoodPlan(commandNow.goodID ,commandNow.quantity.toInt(),commandNow.goodID ),
                        approvers = commandNow.approvers, qualityControlStaffs = commandNow.qualityControlStaffs, accountables = commandNow.accountables)
                        )
                        createCommandAdapter.data = listCommandTemp

                        commandNow = SubRequestCommand(generateCode("LSX"))
                        edtCode.setText(commandNow.code)
                        edtLineCost.setText(context?.getString(R.string.number_user,commandNow.approvers.size.toString()))
                        edtObserver.setText(context?.getString(R.string.number_user,commandNow.accountables.size.toString()))
                        edtTester.setText(context?.getString(R.string.number_user,commandNow.qualityControlStaffs.size.toString()))
                        edtPriceInit.setText(commandNow.quantity)
                        edtVariantName.setText("",false)
                        val s = request.goods.map{DataInfoProduct(it.quantity,it.good,it.quantity)}
                        listCommandTemp.forEach { k ->
                            val e = s.find { it.goodId == k.goodID }
                            if(e != null) e.quantityNotOrder = e.quantityNotOrder - k.quantity.toInt()
                        }
                        infoAdapter.data = s
                    }
                }
            }



        }


    }

    fun initForScreen3(){
        val productivityAdapter = InfoProductivityAdapter()
        val commandScheduleAdapter = CreateWorkScheduleAdapter()
        binding?.apply {
            rcvProductivity3.adapter = productivityAdapter
            rcvCommandSchdule.adapter = commandScheduleAdapter
            constraintSchedule.isVisible = false
            constraintPlan.isVisible = true
        }


        val listProductivity = mutableListOf<GoodDetailModel>()
        listGoods.forEach { k ->
            if(listGoodForCommand.find { it.good == k._id } != null) {
                val e = k.manufacturingMills?.filter { r ->
                    listMill.find { it._id == r.manufacturingMill?._id } != null
                }
                if(!e.isNullOrEmpty()){
                    e.forEach {
                        listProductivity.add(GoodDetailModel(name = k.name , code = k.code, manufacturingMills = listOf(it)))
                    }
                }
            }
        }
        productivityAdapter.data = listProductivity
        var listProduct = listGoods.filter { a -> request.goods.find { it.good == a._id } != null }
        commandScheduleAdapter.listgood = listProduct
        commandScheduleAdapter.data = listCommandTemp
        commandScheduleAdapter.onSchedule ={p , i , name->
            binding?.constraintSchedule?.isVisible = true
            binding?.constraintPlan?.isVisible = false
            commandNowSchedule = i
            initScreenSchedule(i,name)
        }

    }

    var month = 0
    var millNow = ""
    var scheduleNow : WorkScheduleModel? = null
    var listScheduleRequest = mutableListOf<WorkerScheduleRequest>()
    fun initScreenSchedule(command : SubRequestCommand , name : String){
        binding?.apply {
            edtCodeSchedule.setText(command.code)
            val adapter = ArrayAdapter(requireContext(),android.R.layout.simple_dropdown_item_1line,listMill)
            edtMill.setAdapter(adapter)
            rcvSchdule.layoutManager = LinearLayoutManager(requireContext(),LinearLayoutManager.HORIZONTAL,false)
            val shiftAdapter = ShiftInMonthAdapter()
            rcvSchdule.adapter = shiftAdapter
            edtMill.setOnItemClickListener { adapterView, view, i, l ->
                val mill = (adapterView.getItemAtPosition(i) as ManufacturingMillModel)
                val listSchedule = listSchedules.filter { it?.manufacturingMill == mill._id }
                command.manufacturingMill = mill._id
                millNow = mill._id
                if(month != 0){
                    tvTitleWorkScheduleInMonth.text = getString(R.string.title_work_schedule_month , month.toString())
                    Log.v("TAG",listSchedules.toString())
                    scheduleNow =  listSchedules.filter { it?.manufacturingMill == millNow  && isDateInMonth(it.month , month)}.firstOrNull()
                    Log.v("TAG",scheduleNow.toString())
                    shiftAdapter.code = command.code
                    if(scheduleNow?.turns != null && scheduleNow!!.turns.isNotEmpty()) {
                        shiftAdapter.data = scheduleNow!!.turns
                        tvNotData.isVisible = false
                    } else {
                        tvNotData.isVisible = true

                    }


                }
            }
            val format = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            var listMonth = mutableListOf<String>()
            justTry {
                val calendar = Calendar.getInstance()
                val s = format.parse(request.startDate)
                val e = format.parse(request.endDate)
                calendar.time = s
                val i = calendar.get(Calendar.MONTH)
                calendar.time = e
                val u = calendar.get(Calendar.MONTH)
                for(i in i..u){
                    listMonth.add("Tháng ${i+1}")
                }
            }
            val adapterMonth = ArrayAdapter(requireContext(),android.R.layout.simple_dropdown_item_1line,listMonth)
            edtMonth.setAdapter(adapterMonth)
            edtMonth.setOnItemClickListener { adapterView, view, i, l ->
                month = (adapterView.getItemAtPosition(i) as String).substringAfter(" ").toInt()
                if(millNow != ""){
                    shiftAdapter.code = command.code
                    tvTitleWorkScheduleInMonth.text = getString(R.string.title_work_schedule_month , month.toString())
                    scheduleNow =  listSchedules.filter { it?.manufacturingMill == millNow  && isDateInMonth(it.month , month)}.firstOrNull()
                    if(scheduleNow?.turns != null && scheduleNow!!.turns.isNotEmpty()) {
                        shiftAdapter.data = scheduleNow!!.turns
                        tvNotData.isVisible = false
                    }
                    else {
                        tvNotData.isVisible = true
                    }
                }
            }
            shiftAdapter.onChooseShift2 = { s ,d,c ->
                if(c) {
                    listScheduleRequest.add(WorkerScheduleRequest(s,d,"0$month-2023"))
                    scheduleNow?.turns?.get(s)
                        ?.set(d-1, SubCommandInWorkSchedule(code = command.code))
                }
                else justTry {
                    listScheduleRequest.removeIf { it.index1 == s && it.index2 == d

                }
                    scheduleNow?.turns?.get(s)
                        ?.set(d-1, null)}
                if(listScheduleRequest.isEmpty()){
                    listUsersFree = listOf()
                } else {
                    viewModel.getFreeUser(listScheduleRequest.map { gson.toJson(it) })
//                    val t = listScheduleRequest.map { gson.toJson(it) }
                }
                shiftAdapter.notifyItemChanged(s+1)
                listTempUsesResponsible = listOf()
            }
            edtUserResponsible.setOnClickListener {
                postNormal(
                    EventNextHome(
                        ChooseUserFragment::class.java,
                        bundleOf(
                            Pair("LIST_USER", listTempUsesResponsible ?: listOf()),
                            Pair("LIST_DATA", listUsersFree),
                            Pair("USING_FOR", "RESPONSIBLE_COMMAND")
                        )
                    )
                )
            }
            btnSaveSchedule.setOnClickListener {
                if(listTempUsesResponsible.isEmpty()) {
                    showToast(EventToast(text = "Vui lòng nhập đủ thông tin"))
                    return@setOnClickListener
                }
                else {
                    commandNowSchedule.responsibles = listTempUsesResponsible
                    listScheduleRequest.sortBy { it.index2 }
                    commandNowSchedule.startDate = "${listScheduleRequest.first().index2}-$month-2023"
                    commandNowSchedule.endDate = "${listScheduleRequest.last().index2}-$month-2023"
                    listScheduleRequest.sortBy { it.index1 }
                    commandNowSchedule.startTurn = listScheduleRequest.first().index1
                    commandNowSchedule.endTurn = listScheduleRequest.last().index1
                    request.listMillSchedules.add(scheduleNow!!)
                    scheduleNow?.turns?.forEachIndexed { index2, k  ->
                        k.forEachIndexed { index, subCommandInWorkSchedule ->
                            if(subCommandInWorkSchedule?._id?.isEmpty() == true ){
                                k[index]?.isSave = true
                                request.arrayWorkerSchedules.add(ArrayWorkerSchedule(
                                    index2,index,"$month-2023",subCommandInWorkSchedule.code, listTempUsesResponsible))
                            }
                        }
                    }
                    initForScreen3()
                }

            }
            if(command.responsibles.isEmpty()){

            }
            btnSave.setOnClickListener {
                println(request.toString())
                viewModel.createManufacturingPlan(request)
            }


        }
    }

    private fun showDateTimePickerDialog(isStart: Boolean) {
        val datePickerDialog = DatePickerDialog(
            requireContext(),
            { _, year, month, dayOfMonth ->
                calendar.set(year, month, dayOfMonth)
                if (isStart) {
                    binding?.edtTimeStart?.setText(
                        SimpleDateFormat(
                            "dd-MM-yyyy",
                            Locale.getDefault()
                        ).format(calendar.time)
                    )
                    request.startDate = SimpleDateFormat(
                        "yyyy-MM-dd",
                        Locale.getDefault()
                    ).format(calendar.time)
                    if(request.startDate.isNotEmpty() && request.endDate.isNotEmpty()) viewModel.getWorkScheduleOfMillByDate(
                        listMill.map { it._id },
                        binding?.edtTimeStart?.getTextz() ?: "",
                        binding?.edtTimeEnd?.getTextz() ?:""
                    )
                } else {
                    binding?.edtTimeEnd?.setText(
                        SimpleDateFormat(
                            "dd-MM-yyyy",
                            Locale.getDefault()
                        ).format(calendar.time)
                    )
                    request.endDate = SimpleDateFormat(
                        "yyyy-MM-dd",
                        Locale.getDefault()
                    ).format(calendar.time)
                    if(request.startDate.isNotEmpty() && request.endDate.isNotEmpty()) viewModel.getWorkScheduleOfMillByDate(
                        listMill.map { it._id },
                        binding?.edtTimeStart?.getTextz() ?: "",
                        binding?.edtTimeEnd?.getTextz() ?:""
                    )
                }
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        )
        datePickerDialog.show()
    }

    private fun showMaterialDatePickerDialog(isStart: Boolean) {

        val selectedDateInMillis = calendar.timeInMillis
        val builder = MaterialDatePicker.Builder.datePicker()
        builder.setSelection(selectedDateInMillis)
        val datePicker = builder.build()

        datePicker.addOnPositiveButtonClickListener { selectedDateInMillis ->
            calendar.timeInMillis = selectedDateInMillis
            if (isStart) {
                binding?.edtTimeStart?.setText(
                    SimpleDateFormat(
                        "dd-MM-yyyy",
                        Locale.getDefault()
                    ).format(calendar.time)
                )
                request.startDate = SimpleDateFormat(
                    "yyyy-MM-dd",
                    Locale.getDefault()
                ).format(calendar.time)
                if(request.startDate.isNotEmpty() && request.endDate.isNotEmpty()) viewModel.getWorkScheduleOfMillByDate(
                    listMill.map { it._id },
                    binding?.edtTimeStart?.getTextz() ?: "",
                    binding?.edtTimeEnd?.getTextz() ?:""
                )
            } else {
                binding?.edtTimeEnd?.setText(
                    SimpleDateFormat(
                        "dd-MM-yyyy",
                        Locale.getDefault()
                    ).format(calendar.time)
                )
                request.endDate = SimpleDateFormat(
                    "yyyy-MM-dd",
                    Locale.getDefault()
                ).format(calendar.time)
                if(request.startDate.isNotEmpty() && request.endDate.isNotEmpty()) viewModel.getWorkScheduleOfMillByDate(
                    listMill.map { it._id },
                    binding?.edtTimeStart?.getTextz() ?: "",
                    binding?.edtTimeEnd?.getTextz() ?:""
                )
            }
        }

        datePicker.show(requireActivity().supportFragmentManager, datePicker.toString())
    }

    // khoi tao cac view cua screen 2


}