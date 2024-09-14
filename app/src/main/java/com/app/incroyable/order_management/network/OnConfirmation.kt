package com.app.incroyable.order_management.network

interface OnConfirmation<T> {
    fun onConfirmation(item: T)
    fun onCancel()
}