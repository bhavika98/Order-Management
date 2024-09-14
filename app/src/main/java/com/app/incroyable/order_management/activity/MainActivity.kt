package com.app.incroyable.order_management.activity

import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.MarginLayoutParams
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.app.incroyable.order_management.R
import com.app.incroyable.order_management.databinding.ActivityMainBinding
import com.app.incroyable.order_management.fragment.OrderListFragment
import com.app.incroyable.order_management.repository.OrderRepository
import com.app.incroyable.order_management.utils.capitalizeFirstLetter
import com.app.incroyable.order_management.utils.key_cancelled
import com.app.incroyable.order_management.utils.key_cooking
import com.app.incroyable.order_management.utils.key_created
import com.app.incroyable.order_management.utils.key_delivered
import com.app.incroyable.order_management.utils.key_other
import com.app.incroyable.order_management.utils.key_trashed
import com.app.incroyable.order_management.utils.key_waiting
import com.app.incroyable.order_management.viewmodel.OrderViewModel
import com.app.incroyable.order_management.viewmodel.OrderViewModelFactory
import com.google.android.material.tabs.TabLayoutMediator
import kotlin.math.abs


class MainActivity : AppCompatActivity() {

    private val viewModel: OrderViewModel by viewModels {
        OrderViewModelFactory(OrderRepository())
    }
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        setupViewPager()
        setupTabLayout()
        observeViewModel()
    }

    private fun setupViewPager() {
        val sectionsPagerAdapter = SectionsPagerAdapter(this)
        binding.viewPager.adapter = sectionsPagerAdapter
    }

    private fun setupTabLayout() {
        TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
            tab.text = capitalizeFirstLetter(getTabTitle(position))
        }.attach()
        for (i in 0 until binding.tabLayout.tabCount - 1) {
            val tab = (binding.tabLayout.getChildAt(0) as ViewGroup).getChildAt(i)
            val p = tab.layoutParams as MarginLayoutParams
            p.setMargins(0, 0, 50, 0)
            tab.requestLayout()
        }
    }

    private fun getTabTitle(position: Int): String {
        return when (position) {
            0 -> key_created
            1 -> key_cooking
            2 -> key_waiting
            3 -> key_delivered
            4 -> key_trashed
            5 -> key_cancelled
            else -> key_other
        }
    }

    private fun observeViewModel() {
        viewModel.orderList.observe(this) { orders ->
            val isEmpty = orders.isEmpty()
            binding.placeholderView.visibility = if (isEmpty) View.VISIBLE else View.GONE
        }

        viewModel.stats.observe(this) { stats ->
            binding.txtDelivered.text = stats.delivered.toString()
            binding.txtTrashed.text = stats.trashed.toString()
            binding.txtSales.text = getString(R.string.dollar, stats.totalSales.toString())
            binding.txtWaste.text = getString(R.string.dollar, stats.totalWaste.toString())
            val amount = stats.totalRevenue
            binding.txtRevenue.text =
                if (amount < 0)
                    getString(R.string.dollar_minus, abs(amount).toString())
                else
                    getString(R.string.dollar, amount.toString())
        }
    }

    inner class SectionsPagerAdapter(activity: FragmentActivity) :
        FragmentStateAdapter(activity) {

        override fun getItemCount(): Int = 6

        override fun createFragment(position: Int): Fragment {
            return OrderListFragment.newInstance(getTabTitle(position))
        }
    }

}
