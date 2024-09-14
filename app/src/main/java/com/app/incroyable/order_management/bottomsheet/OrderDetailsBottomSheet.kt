package com.app.incroyable.order_management.bottomsheet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.activityViewModels
import com.app.incroyable.order_management.R
import com.app.incroyable.order_management.databinding.BottomSheetOrderDetailsBinding
import com.app.incroyable.order_management.model.OrderData
import com.app.incroyable.order_management.network.OnConfirmation
import com.app.incroyable.order_management.utils.convertTimestampToDateTime
import com.app.incroyable.order_management.utils.formatCentsToCurrency
import com.app.incroyable.order_management.utils.key_cancelled
import com.app.incroyable.order_management.utils.key_cooking
import com.app.incroyable.order_management.utils.key_created
import com.app.incroyable.order_management.utils.key_delivered
import com.app.incroyable.order_management.utils.key_trashed
import com.app.incroyable.order_management.utils.key_waiting
import com.app.incroyable.order_management.viewmodel.OrderViewModel
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class OrderDetailsBottomSheet : BottomSheetDialogFragment() {

    companion object {
        fun showDialog(
            fragmentManager: FragmentManager,
            orderId: String,
            onConfirmation: OnConfirmation<Void?>
        ) {
            val pointsDialog = OrderDetailsBottomSheet()
            pointsDialog.onConfirmation = onConfirmation
            val bundle = Bundle().apply {
                putString("orderId", orderId)
            }
            pointsDialog.arguments = bundle
            pointsDialog.isCancelable = true
            pointsDialog.show(
                fragmentManager, OrderDetailsBottomSheet::class.java.simpleName
            )
        }
    }

    private val viewModel: OrderViewModel by activityViewModels()
    private lateinit var onConfirmation: OnConfirmation<Void?>
    private lateinit var binding: BottomSheetOrderDetailsBinding
    private var orderId: String? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = BottomSheetOrderDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.executePendingBindings()

        orderId = arguments?.getString("orderId")
        orderId?.let { viewModel.getOrderDetails(it) }

        viewModel.orderDetails.observe(viewLifecycleOwner) { orderDetails ->
            if (orderDetails == null) return@observe

            updateOrderDetails(orderDetails)
        }
    }

    private fun updateOrderDetails(orderDetails: OrderData) {
        binding.txtId.text = getString(R.string.order_id_format, orderDetails.id.substringBefore("-"))
        binding.txtTimeStamp.text = convertTimestampToDateTime(orderDetails.timestamp)
        binding.txtItemValue.text = orderDetails.item
        binding.txtPriceValue.text = formatCentsToCurrency(orderDetails.price)
        binding.txtCustomerValue.text = orderDetails.customer
        binding.txtShelfValue.text = orderDetails.shelf
        binding.txtDestinationValue.text = orderDetails.destination

        updateStatusViewScroller(orderDetails.state)
    }

    private fun updateStatusViewScroller(state: String) {
        val statuses = mutableListOf("Created", "Cooking", "Waiting")

        binding.statusViewScroller.statusView.apply {
            currentCount = when (state) {
                key_created -> 1
                key_cooking -> 2
                key_waiting -> 3
                else -> 4
            }

            val (circleColor, drawableRes, statusText) = when (state) {
                key_delivered -> Triple(
                    R.color.green,
                    R.drawable.icon_done,
                    R.string.delivered
                )
                key_trashed -> Triple(
                    R.color.red,
                    R.drawable.icon_cross,
                    R.string.trashed
                )
                key_cancelled -> Triple(
                    R.color.yellow,
                    R.drawable.icon_cancel,
                    R.string.cancelled
                )
                else -> Triple(R.color.orange, null, R.string.pending)
            }

            circleFillColorCurrent = ContextCompat.getColor(requireContext(), circleColor)
            if (drawableRes != null) {
                currentDrawable = ContextCompat.getDrawable(requireContext(), drawableRes)
            }

            statuses.add(ContextCompat.getString(requireContext(), statusText))
            setStatusList(statuses)
        }
    }
}
