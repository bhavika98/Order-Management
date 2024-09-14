package com.app.incroyable.order_management.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.app.incroyable.order_management.model.OrderData
import com.app.incroyable.order_management.network.OrderService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class OrderRepository {
    private val orderService: OrderService
    private val baseUrl = "http://192.168.178.133:8080"

    init {
        val retrofit = Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        orderService = retrofit.create(OrderService::class.java)
    }

    private val _orderEvents = MutableLiveData<List<OrderData>>()
    val orderEvents: LiveData<List<OrderData>> get() = _orderEvents

    fun fetchOrderEvents() {
        _orderEvents.value = emptyList()
        orderService.getOrderEvents().enqueue(object : Callback<List<OrderData>> {
            override fun onResponse(
                call: Call<List<OrderData>>,
                response: Response<List<OrderData>>
            ) {
                if (response.isSuccessful) {
                    _orderEvents.postValue(response.body())
                }
            }

            override fun onFailure(call: Call<List<OrderData>>, t: Throwable) {

            }
        })
    }
}
