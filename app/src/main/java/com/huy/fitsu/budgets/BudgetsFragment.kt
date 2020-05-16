package com.huy.fitsu.budgets

import android.content.Context
import android.os.Bundle
import android.text.SpannableString
import android.text.Spanned
import android.text.style.ForegroundColorSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.github.mikephil.charting.animation.Easing
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.utils.MPPointF
import com.huy.fitsu.FitsuApplication
import com.huy.fitsu.R
import com.huy.fitsu.data.model.EventObserver
import com.huy.fitsu.databinding.BudgetsFragBinding
import com.huy.fitsu.util.toCurrencyString
import javax.inject.Inject
import kotlin.math.abs

class BudgetsFragment: Fragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val viewModel: BudgetsViewModel by viewModels { viewModelFactory }

    private lateinit var binding: BudgetsFragBinding

    override fun onAttach(context: Context) {
        super.onAttach(context)
        injectDagger()
    }

    private fun injectDagger() {
        (requireActivity().application as FitsuApplication).appComponent
            .transactionHistoryComponent()
            .create()
            .inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = BudgetsFragBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        binding.lifecycleOwner = viewLifecycleOwner

        setupCategoryExpenseList()
        setupNewTransactionButton()
        setupPieChart()
        setupPieChartCenterText()
        setupEditTransactionEvent()
    }

    private fun setupEditTransactionEvent() {
        viewModel.editTransactionEvent.observe(viewLifecycleOwner, EventObserver {
            editTransaction(it)
        })
    }

    private fun setupCategoryExpenseList() {
        val categoryExpenseAdapter = CategoryExpenseAdapter()
        binding.categoryExpenseList.adapter = categoryExpenseAdapter

        viewModel.categoryExpensesLiveData.observe(viewLifecycleOwner, Observer { list ->
            if (list != null && list.isNotEmpty()) {
                categoryExpenseAdapter.submitList(list)
            }
        })
    }

    private fun setupNewTransactionButton() {
        binding.addTransButton.setOnClickListener {
            viewModel.addTransaction()
        }
    }


    private fun setupPieChart() {

        designPieChart()

        viewModel.categoryExpensesLiveData.observe(viewLifecycleOwner, Observer { categoryExpenses ->
            if (categoryExpenses != null && categoryExpenses.isNotEmpty()) {

                val yEntries = categoryExpenses.map {
                    PieEntry(abs(it.totalExpense), it.categoryTitle)
                }

                val pieDataSet = PieDataSet(yEntries, "").apply {
                    setDrawIcons(false)
                    sliceSpace = 3f
                    iconsOffset = MPPointF.getInstance(0f, 40f)
                    selectionShift = 0f
                    colors = categoryExpenses.map { it.categoryColor }
                }


                with(binding.categoryPieChart) {

                    data = PieData(pieDataSet).apply {
                        setDrawValues(false)
                    }

                    invalidate()
                }

            }
        })


    }

    private fun designPieChart() {
        with(binding.categoryPieChart) {
            // This chart only shows the circle and the center text
            description.isEnabled = false
            setDrawEntryLabels(false)
            legend.isEnabled = false
            isHighlightPerTapEnabled = false
            isClickable = false

            // No center text, use TextView instead
            setDrawCenterText(false)

            // Draw a big hole so that slices are thin
            isDrawHoleEnabled = true
            holeRadius = 95f

            animateY(1400, Easing.EaseInOutQuad)
        }
    }

    private fun setupPieChartCenterText() {
        viewModel.budgetLiveData.observe(viewLifecycleOwner, Observer {
            it?.let { budget ->
                binding.budget.text = budget.value.toCurrencyString()
            }
        })

        viewModel.budgetLeftLiveData().observe(viewLifecycleOwner, Observer {
            it?.let {budgetLeft ->
                val spannableString = SpannableString(budgetLeft.toCurrencyString())

                if (budgetLeft <= 0) {
                    markAsErrorText(spannableString)
                }

                binding.budgetLeft.text = spannableString
            }
        })
    }

    private fun markAsErrorText(spannableString: SpannableString) {
        val errorColor = ContextCompat.getColor(requireContext(), R.color.errorTextColor)
        spannableString.setSpan(
            ForegroundColorSpan(errorColor),
            0,
            spannableString.length,
            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
        )
    }

    private fun editTransaction(transactionId: String) {
        val action = BudgetsFragmentDirections.toAddEditTransactionFragment(transactionId)
        findNavController().navigate(action)
    }

}