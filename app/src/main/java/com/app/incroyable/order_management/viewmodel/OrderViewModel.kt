package com.app.incroyable.order_management.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.incroyable.order_management.model.OrderData
import com.app.incroyable.order_management.model.OrderStats
import com.app.incroyable.order_management.repository.OrderRepository
import com.app.incroyable.order_management.utils.key_delivered
import com.app.incroyable.order_management.utils.key_trashed
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.concurrent.ConcurrentHashMap

class OrderViewModel(private val repository: OrderRepository) : ViewModel() {

    private val orders = ConcurrentHashMap<String, OrderData>()
    private val _orderList = MediatorLiveData<List<OrderData>>()
    val orderList: LiveData<List<OrderData>> get() = _orderList

    private val _stats = MutableLiveData<OrderStats>()
    val stats: LiveData<OrderStats> get() = _stats

    private val _orderDetails = MutableLiveData<OrderData>()
    val orderDetails: LiveData<OrderData> = _orderDetails

    init {

        _orderList.addSource(repository.orderEvents) { events ->
            if (events.isNullOrEmpty()) {
                _orderList.value = orders.values.toList()
            } else {
                events.forEach { addOrder(it) }
            }
        }

//        viewModelScope.launch {
//            while (true) {
//                repository.fetchOrderEvents()
//                delay(2000)
//            }
//        }
        dummyCode()
    }

    private fun dummyCode() {
        viewModelScope.launch {
            _orderList.value = orders.values.toList()
            delay(2000)
            addOrder(
                OrderData(
                    "fbd68485",
                    "CREATED",
                    1026,
                    "Al pastor tacos",
                    "Jasmine Carroll",
                    "NONE",
                    "NONE",
                    System.currentTimeMillis(),
                    "123 Main Street, Springfield, IL 62701, USA"
                )
            )
            addOrder(
                OrderData(
                    "fbd68487",
                    "COOKING",
                    1145,
                    "Pizza",
                    "Harsh Singh",
                    "COLD",
                    "NONE",
                    System.currentTimeMillis(),
                    "456 Elm Street, Apt 5B, New York, NY 10001, USA"
                )
            )
            addOrder(
                OrderData(
                    "fbd64587",
                    "DELIVERED",
                    1145,
                    "Frittenwork",
                    "Rahul Roy",
                    "HOT",
                    "NONE",
                    System.currentTimeMillis(),
                    "789 Maple Avenue, Suite 300, San Francisco, CA 94107, USA"
                )
            )
            addOrder(
                OrderData(
                    "fbd64533",
                    "WAITING",
                    1145,
                    "Cheese Burger",
                    "Anita Patel",
                    "OVERFLOW",
                    "NONE",
                    System.currentTimeMillis(),
                    "90 Maple Avenue, Suite 300, San Francisco, CA 94107, USA"
                )
            )
            addOrder(
                OrderData(
                    "fbd6432443",
                    "TRASHED",
                    1145,
                    "Cheese Burger",
                    "Anita Patel",
                    "OVERFLOW",
                    "NONE",
                    System.currentTimeMillis(),
                    "90 Maple Avenue, Suite 300, San Francisco, CA 94107, USA"
                )
            )
            addOrder(
                OrderData(
                    "fbd453543",
                    "CANCELLED",
                    1145,
                    "Cheese Burger",
                    "Anita Patel",
                    "OVERFLOW",
                    "NONE",
                    System.currentTimeMillis(),
                    "90 Maple Avenue, Suite 300, San Francisco, CA 94107, USA"
                )
            )
            delay(5000)
            addOrder(
                OrderData(
                    "fbd68485",
                    "COOKING",
                    1026,
                    "Al pastor tacos",
                    "Jasmine Carroll",
                    "NONE",
                    "NONE",
                    System.currentTimeMillis(),
                    "123 Main Street, Springfield, IL 62701, USA"
                )
            )
            delay(2000)
            addOrder(
                OrderData(
                    "fbd68485",
                    "WAITING",
                    1026,
                    "Al pastor tacos",
                    "Jasmine Carroll",
                    "NONE",
                    "NONE",
                    System.currentTimeMillis(),
                    "123 Main Street, Springfield, IL 62701, USA"
                )
            )
            delay(2000)
            addOrder(
                OrderData(
                    "fbd68485",
                    "DELIVERED",
                    1026,
                    "Al pastor tacos",
                    "Jasmine Carroll",
                    "NONE",
                    "NONE",
                    System.currentTimeMillis(),
                    "123 Main Street, Springfield, IL 62701, USA"
                )
            )
        }
    }

    private fun addOrder(order: OrderData) {
        orders[order.id] = order
        _orderList.value = orders.values.toList()
        updateStats()
        if (_orderDetails.value?.id == order.id) {
            _orderDetails.value = order
        }
    }

    fun getOrderDetails(orderId: String) {
        _orderDetails.value = orders[orderId]
    }

    fun getOrdersByState(state: String): LiveData<List<OrderData>> {
        val liveData = MutableLiveData<List<OrderData>>()
        liveData.value = orders.values.filter { it.state == state }
        return liveData
    }

    private fun updateStats() {
        val totalSales = orders.values.filter { it.state == key_delivered }.sumOf { it.price } / 100.0
        val totalWaste = orders.values.filter { it.state == key_trashed }.sumOf { it.price } / 100.0


        val totalSalesFormatted = String.format("%.2f", totalSales)
        val totalWasteFormatted = String.format("%.2f", totalWaste)
        val totalRevenue = totalSales - totalWaste
        val totalRevenueFormatted = String.format("%.2f", totalRevenue)

        _stats.value = OrderStats(
            trashed = orders.values.count { it.state == key_trashed },
            delivered = orders.values.count { it.state == key_delivered },
            totalSales = totalSalesFormatted.toDouble(),
            totalWaste = totalWasteFormatted.toDouble(),
            totalRevenue = totalRevenueFormatted.toDouble()
        )
    }

}



