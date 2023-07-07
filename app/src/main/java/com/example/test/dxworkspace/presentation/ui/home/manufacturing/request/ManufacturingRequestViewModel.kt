package com.example.test.dxworkspace.presentation.ui.home.manufacturing.request

import androidx.lifecycle.MutableLiveData
import com.example.test.dxworkspace.data.entity.good.GoodDetailModel
import com.example.test.dxworkspace.data.entity.product_request.ParamCreateProductRequest
import com.example.test.dxworkspace.data.entity.product_request.ParamUpdateRequest
import com.example.test.dxworkspace.data.entity.product_request.ProductRequestManagementModel
import com.example.test.dxworkspace.data.entity.product_request.UpdateProductRequest
import com.example.test.dxworkspace.domain.usecase.manufacturing_manage.*
import com.example.test.dxworkspace.presentation.model.menu.ParamGetProductRequest
import com.example.test.dxworkspace.presentation.ui.BaseViewModel
import javax.inject.Inject
import kotlin.math.hypot

class ManufacturingRequestViewModel @Inject constructor(
    private val getProductRequestManufacturingUseCase: GetProductRequestManufacturingUseCase,
    private val getGoodByTypeUseCase: GetGoodByTypeUseCase,
    private val createProductRequestManufacturingUseCase: CreateProductRequestManufacturingUseCase,
    private val updateProductRequestManufacturingUseCase: UpdateProductRequestManufacturingUseCase,
    private val createManyProductRequestManufacturingUseCase: CreateManyProductRequestManufacturingUseCase,
    private val getRequestByIdsUseCase: GetListProductRequestByIdsUseCase,
    private val updateStatusProductRequestManufacturingUseCase: UpdateStatusProductRequestManufacturingUseCase
): BaseViewModel() {

    val listRequest = MutableLiveData<List<ProductRequestManagementModel>>()
val listGoods = MutableLiveData<List<GoodDetailModel>>()
    val statusUpdate = MutableLiveData<Boolean>()
    var detailRequest = MutableLiveData<ProductRequestManagementModel>()

    fun getProductRequest(from:String,to:String){
        showLoading(true)
        getProductRequestManufacturingUseCase(ParamGetProductRequest(1,"manufacturing",from,to)){
            it.either({
                showLoading(false)
                handleFailure(it)
                listRequest.value = emptyList()
            },{
                showLoading(false)
                listRequest.value = it
            })
        }
    }

    fun getGoodByType(type:String){
        getGoodByTypeUseCase(type){
            it.either({
                listGoods.value = listOf()

            },{
                listGoods.value = it
            })
        }
    }

    fun createProductRequest(data : ParamCreateProductRequest){
        createProductRequestManufacturingUseCase(data){
            it.either({},{
                statusUpdate.value = it
            })
        }
    }

    fun createManyProductRequest(data : List<ParamCreateProductRequest>){
        showLoading(true)
        createManyProductRequestManufacturingUseCase(data){
            it.either({
                      showLoading(false)
                statusUpdate.value = false
            },{
                showLoading(false)
                statusUpdate.value = it
            })
        }
    }

    fun updateProductRequest(data : ParamCreateProductRequest,id:String){
        updateProductRequestManufacturingUseCase(Pair(id,data)){
            it.either({

            },{
                statusUpdate.value = it
            })
        }
    }

    fun updateStatusProductRequest(data : ParamUpdateRequest, id:String){
        updateStatusProductRequestManufacturingUseCase(Pair(id,data)){
            it.either({

            },{
                statusUpdate.value = it
            })
        }
    }

    fun getRequestDetail(id :String){
        showLoading(true)
        getRequestByIdsUseCase(listOf(id)){
            it.either({
                handleFailure(it)
                showLoading(false)
            },{
                if(it.isNotEmpty()) detailRequest.value = it[0]
                showLoading(false)
            })
        }
    }
}