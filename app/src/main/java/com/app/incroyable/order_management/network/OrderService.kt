package com.app.incroyable.order_management.network

import com.app.incroyable.order_management.model.OrderData
import retrofit2.Call
import retrofit2.http.GET

interface OrderService {
    @GET("/order_events")
    fun getOrderEvents(): Call<List<OrderData>>
}