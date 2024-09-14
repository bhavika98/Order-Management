package com.app.incroyable.order_management.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.app.incroyable.order_management.adapter.OrderAdapter
import com.app.incroyable.order_management.bottomsheet.OrderDetailsBottomSheet
import com.app.incroyable.order_management.databinding.FragmentOrderListBinding
import com.app.incroyable.order_management.network.OnConfirmation
import com.app.incroyable.order_management.viewmodel.OrderViewModel
import kotlinx.coroutines.launch

class OrderListFragment : Fragment() {

    private lateinit var binding: FragmentOrderListBinding
    private lateinit var adapter: OrderAdapter
    private val viewModel: OrderViewModel by activityViewModels()
    private var state: String? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentOrderListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        state = arguments?.getString(ARG_STATE)
        adapter = OrderAdapter(emptyList()) { order ->
            OrderDetailsBottomSheet.showDialog(parentFragmentManager,
                order.id,
                object : OnConfirmation<Void?> {
                    override fun onConfirmation(item: Void?) {
                    }

                    override fun onCancel() {

                    }

                })
        }

        binding.recyclerView.layoutManager = LinearLayoutManager(activity)
        binding.recyclerView.adapter = adapter

        lifecycleScope.launch {
            viewModel.orderList.observe(viewLifecycleOwner) { _ ->
                viewModel.getOrdersByState(state!!).observe(viewLifecycleOwner) { orders ->
                    if (orders.isEmpty()) {
                        binding.noOrder.visibility = View.VISIBLE
                    } else {
                        binding.noOrder.visibility = View.GONE
                    }

                    adapter.updateOrders(orders)
                }
            }
        }
    }

    companion object {
        private const val ARG_STATE = "state"
        fun newInstance(state: String): OrderListFragment {
            val fragment = OrderListFragment()
            val args = Bundle()
            args.putString(ARG_STATE, state)
            fragment.arguments = args
            return fragment
        }
    }
}


