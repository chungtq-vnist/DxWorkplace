package com.example.test.dxworkspace.presentation.ui.home.manufacturing.request

import androidx.lifecycle.MutableLiveData
import com.example.test.dxworkspace.data.entity.good.GoodDetailModel
import com.example.test.dxworkspace.data.entity.product_request.ParamCreateProductRequest
import com.example.test.dxworkspace.data.entity.product_request.ProductRequestManagementModel
import com.example.test.dxworkspace.data.entity.product_request.UpdateProductRequest
import com.example.test.dxworkspace.domain.usecase.manufacturing_manage.CreateProductRequestManufacturingUseCase
import com.example.test.dxworkspace.domain.usecase.manufacturing_manage.GetGoodByTypeUseCase
import com.example.test.dxworkspace.domain.usecase.manufacturing_manage.GetProductRequestManufacturingUseCase
import com.example.test.dxworkspace.domain.usecase.manufacturing_manage.UpdateProductRequestManufacturingUseCase
import com.example.test.dxworkspace.presentation.model.menu.ParamGetProductRequest
import com.example.test.dxworkspace.presentation.ui.BaseViewModel
import javax.inject.Inject
import kotlin.math.hypot

class ManufacturingRequestViewModel @Inject constructor(
    private val getProductRequestManufacturingUseCase: GetProductRequestManufacturingUseCase,
    private val getGoodByTypeUseCase: GetGoodByTypeUseCase,
    private val createProductRequestManufacturingUseCase: CreateProductRequestManufacturingUseCase,
    private val updateProductRequestManufacturingUseCase: UpdateProductRequestManufacturingUseCase
): BaseViewModel() {

    val listRequest = MutableLiveData<List<ProductRequestManagementModel>>()
val listGoods = MutableLiveData<List<GoodDetailModel>>()
    val statusUpdate = MutableLiveData<Boolean>()

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

    fun updateProductRequest(data : UpdateProductRequest,id:String){
        updateProductRequestManufacturingUseCase(Pair(id,data)){
            it.either({

            },{
                statusUpdate.value = it
            })
        }
    }
}