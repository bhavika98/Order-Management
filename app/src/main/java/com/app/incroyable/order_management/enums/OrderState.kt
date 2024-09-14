package com.app.incroyable.order_management.enums

enum class OrderState(val displayName: String) {
    CREATED("Created"),
    COOKING("Cooking"),
    WAITING("Waiting"),
    DELIVERED("Delivered"),
    TRASHED("Trashed"),
    CANCELLED("Cancelled");

    companion object {
        fun fromString(state: String): OrderState {
            return entries.find { it.name == state } ?: CREATED
        }
    }
}

