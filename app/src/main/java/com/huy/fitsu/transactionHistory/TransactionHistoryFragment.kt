package com.huy.fitsu.transactionHistory

import android.content.Context
import android.graphics.Color
import android.graphics.Typeface
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
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.formatter.PercentFormatter
import com.github.mikephil.charting.utils.MPPointF
import com.huy.fitsu.FitsuApplication
import com.huy.fitsu.data.model.EventObserver
import com.huy.fitsu.databinding.TransactionHistoryFragBinding
import timber.log.Timber
import javax.inject.Inject

class TransactionHistoryFragment: Fragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val viewModel: TransactionHistoryViewModel by viewModels { viewModelFactory }

    private lateinit var binding: TransactionHistoryFragBinding

    override fun onAttach(context: Context) {
        super.onAttach(context)
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
        val adapter = TransactionHistoryAdapter(viewModel)
        binding.transactionHistoryList.adapter = adapter

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
            Timber.i(list.toString())
            list?.let {categoryReports ->

                val yEntries = categoryReports.map {
                    PieEntry(it.transactionSum, it.categoryTitle)
                }

                val pieDataSet = PieDataSet(yEntries, "Category sum").apply {
                    setDrawIcons(false)
                    sliceSpace = 3f
                    iconsOffset = MPPointF.getInstance(0f, 40f)
                    selectionShift = 50f
                    colors = categoryReports.map { it.categoryColor }
                }

                binding.categoryPieChart.legend.apply {
                    form = Legend.LegendForm.CIRCLE
                }

                with(binding.categoryPieChart) {

                    setUsePercentValues(true)
                    description.isEnabled = false
                    setExtraOffsets(5f, 10f, 5f, 5f)

                    dragDecelerationFrictionCoef = 0.95f

                    setDrawCenterText(false)

                    isRotationEnabled = true
                    rotationAngle = 0f

                    isDrawHoleEnabled = false

                    setTransparentCircleColor(Color.WHITE)
                    setTransparentCircleAlpha(50)
                    transparentCircleRadius = 61f

                    animateY(1400, Easing.EaseInOutQuad)

                    setEntryLabelColor(Color.WHITE)
                    setEntryLabelTypeface(Typeface.DEFAULT)
                    setEntryLabelTextSize(12f)

                    legend.apply {
                        textSize = 14f
                        textColor = Color.WHITE
                        verticalAlignment = Legend.LegendVerticalAlignment.TOP
                        horizontalAlignment = Legend.LegendHorizontalAlignment.RIGHT
                        orientation = Legend.LegendOrientation.VERTICAL
                        setDrawInside(false)
                        xEntrySpace = 7f
                        yEntrySpace = 0f
                        yOffset = 0f
                    }

                    data = PieData(pieDataSet).apply {
                        setValueFormatter(PercentFormatter(this@with))
                        setValueTextSize(14f)
                        setValueTextColor(Color.WHITE)
                        setValueTypeface(Typeface.DEFAULT)
                    }

                    highlightValue(null)

                    invalidate()
                }
            }
        })
    }

    private fun editTransaction(transactionId: String) {
        val action = TransactionHistoryFragmentDirections.toAddEditTransactionFragment(transactionId)
        findNavController().navigate(action)
    }

}