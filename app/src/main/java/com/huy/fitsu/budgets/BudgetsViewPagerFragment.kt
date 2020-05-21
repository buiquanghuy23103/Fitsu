package com.huy.fitsu.budgets

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.google.android.material.tabs.TabLayoutMediator
import com.huy.fitsu.FitsuApplication
import com.huy.fitsu.data.model.Budget
import com.huy.fitsu.databinding.ViewPagerFragBinding
import com.huy.fitsu.util.toReadableString
import javax.inject.Inject

class BudgetsViewPagerFragment : Fragment() {

    lateinit var binding: ViewPagerFragBinding

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    val viewModel: BudgetsViewPagerViewModel by viewModels { viewModelFactory }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        injectDagger()
    }

    private fun injectDagger() {
        (requireActivity().application as FitsuApplication).appComponent
            .budgetsComponent()
            .create()
            .inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = ViewPagerFragBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        viewModel.budgetsLiveData.observe(viewLifecycleOwner, Observer { budgets ->
            if (!budgets.isNullOrEmpty()) {
                setupViewPager(budgets)
                setupTabTitle(budgets)
            }
        })

    }

    private fun setupTabTitle(budgets: List<Budget>) {
        TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
            val item = budgets[position]
            val itemText = item.yearMonth.toReadableString()
            tab.text = itemText
        }.attach()
    }

    private fun setupViewPager(budgets: List<Budget>) {
        val adapter = BudgetsViewPagerAdapter(this, budgets)
        binding.viewPager.adapter = adapter
    }

    class BudgetsViewPagerAdapter(
        fragment: BudgetsViewPagerFragment,
        private val budgets: List<Budget>
    ) : FragmentStateAdapter(fragment) {

        override fun getItemCount(): Int = budgets.size

        override fun createFragment(position: Int): Fragment {
            val budget = budgets[position]
            return BudgetsFragment.getInstanceByBudgetId(budget.id)
        }
    }

}