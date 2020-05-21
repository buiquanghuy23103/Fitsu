package com.huy.fitsu.addEditTransaction

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
import androidx.navigation.fragment.navArgs
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.huy.fitsu.FitsuApplication
import com.huy.fitsu.R
import com.huy.fitsu.data.model.Category
import com.huy.fitsu.data.model.EventObserver
import com.huy.fitsu.databinding.AddEditTransactionFragBinding
import com.huy.fitsu.util.DateConverter
import com.huy.fitsu.util.hideKeyboardFromView
import com.huy.fitsu.util.waitForTransition
import javax.inject.Inject
import kotlin.math.abs

class AddEditTransactionFragment : Fragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private lateinit var binding: AddEditTransactionFragBinding

    private val viewModel by viewModels<AddEditTransactionViewModel> { viewModelFactory }

    private val navArgs: AddEditTransactionFragmentArgs by navArgs()

    private val datePickerTag = "date_picker"


    override fun onAttach(context: Context) {
        super.onAttach(context)
        injectDagger()
    }

    private fun injectDagger() {
        (requireActivity().application as FitsuApplication).appComponent
            .addEditTransactionComponent()
            .create()
            .inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = AddEditTransactionFragBinding.inflate(inflater, container, false).apply {
            executePendingBindings()
            waitForTransition(transactionEditForm)
        }
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        binding.lifecycleOwner = viewLifecycleOwner

        val transactionId = navArgs.transactionId

        viewModel.getTransactionLiveDataById(transactionId)
            .observe(viewLifecycleOwner, Observer {
                it?.let { transaction ->
                    binding.transaction = transaction
                    setupValueEditText()
                    setupDateButton()
                    setupIncomeOutcomeToggleButton()
                    setupUpdateButton()
                    setupDeleteButton()
                }
            })

        viewModel.categoriesLiveData.observe(viewLifecycleOwner, Observer {
            it?.let { categories ->
                setupCategoryPicker(categories)
            }
        })

        viewModel.getCategoryByTransactionId(transactionId).observe(viewLifecycleOwner, Observer {
            it?.let { category ->
                binding.category = category
            }
        })

        viewModel.navigateUp.observe(viewLifecycleOwner, EventObserver {
            findNavController().navigateUp()
        })

    }

    private fun setupValueEditText() = with(binding.transactionValueEditText) {
        setOnFocusChangeListener { view, hasFocus ->
            if (!hasFocus) {
                hideKeyboardFromView(view)

                val floatValue = text.toString().toFloatOrNull() ?: 0f
                binding.transaction = binding.transaction!!.copy(
                    value = -abs(floatValue)
                )
            }
        }
    }

    private fun setupCategoryPicker(categories: List<Category>) {
        val categoriesString: Array<CharSequence> = categories.map { it.title }.toTypedArray()

        binding.transactionCategoryButton.setOnClickListener {
            clearFocusOnEditText()

            val currentIndex = categories.indexOf(binding.category)

            MaterialAlertDialogBuilder(requireContext())
                .setTitle(R.string.transaction_category_picker_title)
                .setSingleChoiceItems(categoriesString, currentIndex) { dialog, which ->
                    val chosenCategory = categories[which]
                    binding.category = chosenCategory
                    binding.transaction = binding.transaction!!.copy(
                        categoryId = chosenCategory.id
                    )
                    dialog.dismiss()
                }
                .show()
        }
    }

    private fun setupDateButton() {
        val currentDate = binding.transaction!!.createdAt
        val epochSeconds = DateConverter.localDateToEpochSeconds(currentDate)

        val datePicker = MaterialDatePicker.Builder.datePicker()
            .setSelection(epochSeconds)
            .setTitleText(R.string.transaction_date_picker_title)
            .build()

        datePicker.addOnPositiveButtonClickListener {
            DateConverter.epochSecondsToLocalDate(it)?.let { date ->
                binding.transaction = binding.transaction!!.copy(
                    createdAt = date
                )
            }
        }

        binding.transactionDateButton.setOnClickListener {
            clearFocusOnEditText()
            datePicker.showNow(this.childFragmentManager, datePickerTag)
        }

    }

    private fun setupIncomeOutcomeToggleButton() {
        binding.incomeOutcomeToggleGroup.addOnButtonCheckedListener { group, _, _ ->
            when (group.checkedButtonId) {
                R.id.outcome -> markAsNegative()
                R.id.income -> markAsPositive()
            }

        }
    }

    private fun markAsNegative() = markSignOfTransaction(false)

    private fun markAsPositive() = markSignOfTransaction(true)

    private fun markSignOfTransaction(positive: Boolean) {
        val sign = if (positive) 1 else -1
        val currentValue = binding.transaction!!.value
        val newValue = sign * abs(currentValue)
        binding.transaction = binding.transaction!!.copy(
            value = newValue
        )
    }

    private fun setupUpdateButton() {
        binding.transactionUpdateButton.setOnClickListener {
            clearFocusOnEditText()
            viewModel.updateTransaction(binding.transaction!!)
        }
    }

    private fun setupDeleteButton() {
        binding.transactionDeleteButton.setOnClickListener {
            clearFocusOnEditText()
            viewModel.deleteTransaction(binding.transaction!!)
        }
    }

    private fun clearFocusOnEditText() {
        binding.transactionValueEditText.clearFocus()
    }

}