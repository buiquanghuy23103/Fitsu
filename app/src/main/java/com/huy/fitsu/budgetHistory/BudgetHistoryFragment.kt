package com.huy.fitsu.budgetHistory

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.huy.fitsu.FitsuApplication
import com.huy.fitsu.databinding.DashboardFragBinding
import javax.inject.Inject

class BudgetHistoryFragment: Fragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val viewModel by viewModels<BudgetHistoryViewModel> { viewModelFactory }

    private lateinit var binding: DashboardFragBinding

    override fun onAttach(context: Context) {
        super.onAttach(context)
        (requireActivity().application as FitsuApplication).appComponent
            .dashboardComponent()
            .create()
            .inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DashboardFragBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        binding.lifecycleOwner = viewLifecycleOwner

        setupBudgetList()

    }

    private fun setupBudgetList() {
        val adapter = BudgetsAdapter(viewModel)
        binding.budgetList.adapter = adapter

        viewModel.budgets.observe(viewLifecycleOwner, Observer {
            it?.let { list -> adapter.submitList(list) }
        })
    }

}