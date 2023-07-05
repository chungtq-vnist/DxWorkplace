package com.example.test.dxworkspace.presentation.ui.home

import android.content.SharedPreferences
import androidx.lifecycle.MutableLiveData
import com.example.test.dxworkspace.core.extensions.Either
import com.example.test.dxworkspace.data.entity.manufacturing_command.StockModel
import com.example.test.dxworkspace.data.entity.manufacturing_work.ManufacturingWorkModel
import com.example.test.dxworkspace.data.entity.manufacturing_work.OrganizationUnit
import com.example.test.dxworkspace.data.entity.report.ReportRequestModel
import com.example.test.dxworkspace.data.entity.report.WarehouseReportModel
import com.example.test.dxworkspace.data.entity.role.RoleModel
import com.example.test.dxworkspace.data.entity.user.UserProfileResponse
import com.example.test.dxworkspace.domain.usecase.manufacturing_manage.GetAllOrganizationUnitRemoteUseCase
import com.example.test.dxworkspace.domain.usecase.manufacturing_manage.GetAllStockUseCase
import com.example.test.dxworkspace.domain.usecase.manufacturing_manage.GetAllUsersUseCase
import com.example.test.dxworkspace.domain.usecase.manufacturing_work.GetAllManufacturingWorkRemoteUsecase
import com.example.test.dxworkspace.domain.usecase.manufacturing_work.GetAllManufacturingWorkRemoteWithoutRoleUsecase
import com.example.test.dxworkspace.domain.usecase.report.GetWarehouseReportUseCase
import com.example.test.dxworkspace.domain.usecase.role.GetAllRoleRemoteUseCase
import com.example.test.dxworkspace.domain.usecase.version.CheckVersionUseCase
import com.example.test.dxworkspace.domain.usecase.version.HandleVersionUseCase
import com.example.test.dxworkspace.presentation.model.menu.ManufacturingWorkSelect
import com.example.test.dxworkspace.presentation.ui.BaseViewModel
import com.example.test.dxworkspace.presentation.utils.common.Constants
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class HomeViewModel @Inject constructor(
    private val sharedPreferences: SharedPreferences,
    private val checkVersionUseCase: CheckVersionUseCase,
    private val handleVersionUseCase: HandleVersionUseCase,
    private val getAllOrganizationUnitRemoteUseCase: GetAllOrganizationUnitRemoteUseCase,
    private val getAllRoleRemoteUseCase: GetAllRoleRemoteUseCase,
    private val getWarehouseReportUseCase : GetWarehouseReportUseCase,
    private val getAllUsersUseCase: GetAllUsersUseCase,
    private val getAllStockUseCase: GetAllStockUseCase,
    private val getAllManufacturingWorkRemoteUsecase: GetAllManufacturingWorkRemoteUsecase,
    private val getAllManufacturingWorkRemoteWithoutRoleUsecase: GetAllManufacturingWorkRemoteWithoutRoleUsecase

    ) : BaseViewModel() {

    // fromDate va toDate dung de luu rangetime cho dashboard dieu khien san xuat
    var fromDate: String = "01-06-2023"
    var toDate: String = "01-06-2023"
    var typeTimeReport = ""

    var isCompare =
        true // chỉ có báo cáo theo các thời gian đặc biệt mới có so sánh , báo cáo kiểm soát sản xuất cũng không có so sánh
    var fromDateCompare: String = "01-06-2023"
    var toDateCompare: String = "01-06-2023"

    // luu cac nha may dang duoc chon cho dashboard dieu khien san xuat
    var listWorksSelected = mutableListOf<ManufacturingWorkSelect>()

    // save for manufacturing work
    val listOrganization = MutableLiveData<List<OrganizationUnit>>()
    val listRole = MutableLiveData<List<RoleModel>>()
    val listManufacturingWork = MutableLiveData<List<ManufacturingWorkModel>>()

    // save for warehouse report
    val listDataWarehouseReport = MutableLiveData<List<WarehouseReportModel>>()
    val statusReport = MutableLiveData<String>()

    val listUser = MutableLiveData<List<UserProfileResponse>>()
    val listStock = MutableLiveData<List<StockModel>>()

    init {
        // khoi tao fromDate va toDate cach nhau 1 thang , va toDate lay ngay hien tai
        val dateFormat = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
        val calendar = Calendar.getInstance()

        toDate = dateFormat.format(calendar.time)

        calendar.add(Calendar.MONTH, -1)
        fromDate = dateFormat.format(calendar.time)
        calendar.add(Calendar.DAY_OF_MONTH, -1)
        toDateCompare = dateFormat.format(calendar.time)
        calendar.add(Calendar.MONTH, -1)
        fromDateCompare = dateFormat.format(calendar.time)
        typeTimeReport = Constants.DatePicker.QUICK_30_DAY
    }

    var checkVersionSuccess = false
    fun checkVersion(handle: (() -> Unit)) {
        checkVersionUseCase("") {
            it.either({}, {
                handle.invoke()
            })
        }
    }

    fun checkVersion(handleSuccess: ((Boolean) -> Unit), handleFail: (() -> Unit)) {
        handleVersionUseCase("") {
            it.either({
                handleFail.invoke()
            }, {
                handleSuccess.invoke(it)
            })
        }
    }

    fun getAllOrganizationUnit() {
        showLoading(true)
        getAllOrganizationUnitRemoteUseCase("") {
            it.either({
                showLoading(false)
            }, {
                showLoading(false)
                listOrganization.value = it
            })
        }
    }

    fun getAllRoles() {
        getAllRoleRemoteUseCase("") {
            it.either({

            }, {
                listRole.value = it
            })
        }
    }

    fun getListDataReportWarehouse(params: ReportRequestModel){
        getWarehouseReportUseCase(params){
            it.either({
                handleFailure(it)
                statusReport.value = "FAIL"
            },{
                listDataWarehouseReport.value = it
                statusReport.value = "DONE"
            })
        }
    }

    fun getAllUser(){
        showLoading(true)
        getAllUsersUseCase(""){
            it.either({
                showLoading(false)
                listUser.value = listOf()
            },{
                showLoading(false)
                listUser.value = it
            })
        }
    }

    fun getStock(){
        showLoading(true)
        getAllStockUseCase(""){
            it.either({
                showLoading(false)
                listStock.value = listOf()
            },{
                showLoading(false)
                listStock.value = it
            })
        }

    }

    fun getManufacturingWorks(){
        getAllManufacturingWorkRemoteUsecase(""){
            it.either({},{
                listManufacturingWork.value = it
            })

        }
    }

    fun getManufacturingWorksWithoutRole(){
        getAllManufacturingWorkRemoteWithoutRoleUsecase(""){
            it.either({},{
                listManufacturingWork.value = it
            })

        }
    }
}