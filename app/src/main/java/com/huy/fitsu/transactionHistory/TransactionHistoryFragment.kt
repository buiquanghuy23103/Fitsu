package com.huy.fitsu.transactionHistory

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
import com.huy.fitsu.data.model.CategoryReport
import com.huy.fitsu.data.model.EventObserver
import com.huy.fitsu.databinding.TransactionHistoryFragBinding
import com.huy.fitsu.util.waitForTransition
import javax.inject.Inject
import kotlin.math.abs

class TransactionHistoryFragment: Fragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val viewModel: TransactionHistoryViewModel by viewModels { viewModelFactory }

    private lateinit var binding: TransactionHistoryFragBinding

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
        binding = TransactionHistoryFragBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        binding.lifecycleOwner = viewLifecycleOwner

        setupTransactionList()
        setupAddTransactionFab()
        setupCategoryPieChart()

        viewModel.editTransactionEvent.observe(viewLifecycleOwner, EventObserver {
            editTransaction(it)
        })
    }

    private fun setupTransactionList() {
        val adapter = TransactionHistoryAdapter()
        binding.transactionHistoryList.adapter = adapter

        waitForTransition(binding.transactionHistoryList)

        viewModel.transactions.observe(viewLifecycleOwner, Observer {
            it?.let { list -> adapter.submitList(list) }
        })
    }

    private fun setupAddTransactionFab() {
        binding.transactionHistoryAddTransFab.setOnClickListener {
            viewModel.addTransaction()
        }
    }

    private fun setupCategoryPieChart() {
        viewModel.transactionCountByCategory.observe(viewLifecycleOwner, Observer { list ->
            if (list != null && list.isNotEmpty()) {
                drawPieChart(list)
            }
        })
    }

    private fun drawPieChart(categoryReports: List<CategoryReport>) {
        val yEntries = categoryReports.map {
            PieEntry(abs(it.transactionSum), it.categoryTitle)
        }

        val pieDataSet = PieDataSet(yEntries, "").apply {
            setDrawIcons(false)
            sliceSpace = 3f
            iconsOffset = MPPointF.getInstance(0f, 40f)
            selectionShift = 50f
            colors = categoryReports.map { it.categoryColor }
        }


        with(binding.categoryPieChart) {

            // This chart only shows the circle and the center text
            description.isEnabled = false
            setDrawEntryLabels(false)
            legend.isEnabled = false
            isHighlightPerTapEnabled = false
            isClickable = false

            // Set up center text
            setDrawCenterText(true)
            val sum = categoryReports
                .map { it.transactionSum }
                .reduce{ prev, next -> prev + next }
            centerText = sum.toString()
            setCenterTextSize(64f)

            // Draw a big hole so that slices are thin
            isDrawHoleEnabled = true
            holeRadius = 90f

            animateY(1400, Easing.EaseInOutQuad)

            data = PieData(pieDataSet).apply {
                setDrawValues(false)
            }

            invalidate()
        }
    }

    private fun editTransaction(transactionId: String) {
        val action = TransactionHistoryFragmentDirections.toAddEditTransactionFragment(transactionId)
        findNavController().navigate(action)
    }

}