package com.app.incroyable.order_management.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.app.incroyable.order_management.R
import com.app.incroyable.order_management.model.OrderData
import com.app.incroyable.order_management.utils.convertTimestampToDateTime
import com.app.incroyable.order_management.utils.formatCentsToCurrency
import com.app.incroyable.order_management.utils.key_cold
import com.app.incroyable.order_management.utils.key_frozen
import com.app.incroyable.order_management.utils.key_hot
import com.app.incroyable.order_management.utils.key_none
import com.app.incroyable.order_management.utils.key_overflow

class OrderAdapter(private var orders: List<OrderData>, private val onClick: (OrderData) -> Unit) :
    RecyclerView.Adapter<OrderAdapter.OrderViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_order, parent, false)
        return OrderViewHolder(view)
    }

    override fun onBindViewHolder(holder: OrderViewHolder, position: Int) {
        val order = orders[position]
        holder.bind(order)
        holder.itemView.setOnClickListener { onClick(order) }
    }

    override fun getItemCount() = orders.size

    fun updateOrders(newOrders: List<OrderData>) {
        orders = newOrders
        notifyDataSetChanged()
    }

    class OrderViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val txtId: TextView = itemView.findViewById(R.id.txtId)
        private val txtItem: TextView = itemView.findViewById(R.id.txtItem)
        private val txtPrice: TextView = itemView.findViewById(R.id.txtPrice)
        private val txtCustomer: TextView = itemView.findViewById(R.id.txtCustomer)
        private val txtDestination: TextView = itemView.findViewById(R.id.txtDestination)
        private val txtTimeStamp: TextView = itemView.findViewById(R.id.txtTimeStamp)
        private val txtShelf: TextView = itemView.findViewById(R.id.txtShelf)

        fun bind(order: OrderData) {
            txtId.text = itemView.context.getString(R.string.order_id_format, order.id.substringBefore("-"))
            txtItem.text = order.item
            txtCustomer.text = order.customer
            txtDestination.text = order.destination
            txtPrice.text = formatCentsToCurrency(order.price)
            txtTimeStamp.text = convertTimestampToDateTime(order.timestamp)
            when (order.shelf) {
                key_hot -> txtShelf.text = "\uD83D\uDD25"
                key_cold -> txtShelf.text = "❄\uFE0F"
                key_frozen -> txtShelf.text = "\uD83E\uDDCA"
                key_overflow -> txtShelf.text = "\uD83D\uDCE6"
                key_none -> txtShelf.text = "⛔"
            }
        }
    }
}

