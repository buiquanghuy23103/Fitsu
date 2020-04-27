package com.huy.fitsu.addEditTransaction

import android.content.Context
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.transition.TransitionInflater
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.huy.fitsu.FitsuApplication
import com.huy.fitsu.R
import com.huy.fitsu.data.model.Category
import com.huy.fitsu.data.model.EventObserver
import com.huy.fitsu.data.model.Transaction
import com.huy.fitsu.databinding.AddEditTransactionFragBinding
import com.huy.fitsu.util.DateConverter
import com.huy.fitsu.util.waitForTransition
import java.time.LocalDate
import javax.inject.Inject

class AddEditTransactionFragment: Fragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private lateinit var binding: AddEditTransactionFragBinding

    private val viewModel by viewModels<AddEditTransactionViewModel> { viewModelFactory }

    private val navArgs : AddEditTransactionFragmentArgs by navArgs()

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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupTransition()
    }

    private fun setupTransition() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            setupEnterTransition()
        }
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    private fun setupEnterTransition() {
        sharedElementEnterTransition = TransitionInflater.from(requireContext())
            .inflateTransition(android.R.transition.move)
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

        viewModel.loadTransactionWithId(navArgs.transactionId)

        binding.lifecycleOwner = viewLifecycleOwner

        viewModel.transaction.observe(viewLifecycleOwner, Observer {
            it?.let { transaction ->
                binding.transaction = transaction
                setupDeleteButton(transaction)
                setupDateButton(transaction.createdAt)
            }
        })

        viewModel.categoriesAndChosenCategory().observe(viewLifecycleOwner, Observer {
            it?.let { pair ->
                val categories = pair.first
                val category = pair.second
                val currentCategoryIndex = categories.indexOf(category)

                binding.category = category
                setupCategoryPicker(categories, category)
                viewModel.selectedCategoryIndex = currentCategoryIndex
                setupUpdateButton(categories)
            }
        })

        viewModel.navigateUp.observe(viewLifecycleOwner, EventObserver {
            findNavController().navigateUp()
        })

    }

    private fun setupCategoryPicker(categories: List<Category>, selectedCategory: Category) {
        val categoriesString : Array<CharSequence> = categories.map { it.title }.toTypedArray()

        val dialog = MaterialAlertDialogBuilder(requireContext())
            .setTitle(R.string.transaction_category_picker_title)

        binding.transactionCategoryButton.setOnClickListener {
            val currentCategoryIndex = categories.indexOf(selectedCategory)

            dialog.setPositiveButton(android.R.string.ok) { dialog, _ ->
                val chosenCategory = categories[viewModel.selectedCategoryIndex]
                binding.transactionCategoryButton.text = chosenCategory.title
                dialog.dismiss()
            }.setSingleChoiceItems(categoriesString, currentCategoryIndex) { _, which ->
                viewModel.selectedCategoryIndex = which
            }

            dialog.show()
        }
    }

    private fun setupDateButton(currentDate: LocalDate) {
        val epochSeconds = DateConverter.localDateToEpochSeconds(currentDate)

        val datePicker = MaterialDatePicker.Builder.datePicker()
            .setSelection(epochSeconds)
            .setTitleText(R.string.transaction_date_picker_title)
            .build()

        binding.transactionDateButton.setOnClickListener {
            datePicker.addOnPositiveButtonClickListener {
                val date = DateConverter.epochSecondsToLocalDate(it)
                val dateText = DateConverter.localDateToString(date)
                binding.transactionDateButton.text = dateText
            }
            datePicker.showNow(this.childFragmentManager, datePickerTag)
        }

    }

    private fun setupUpdateButton(categories: List<Category>) {
        binding.transactionUpdateButton.setOnClickListener {
            with(binding) {

                val moneyValue = transactionValueEditText.text.toString().toFloatOrNull()
                if (moneyValue == null) {
                    warn(R.string.invalid_money_value)
                    return@with
                }

                val dateText = transactionDateButton.text.toString()
                val date = DateConverter.stringToLocalDate(dateText)
                if (date == null) {
                    warn(R.string.invalid_date)
                    return@with
                }

                val category = categories[viewModel.selectedCategoryIndex]

                val newTransaction = Transaction(
                    value = moneyValue,
                    createdAt = date,
                    categoryId = category.id
                )

                viewModel.updateTransaction(newTransaction)
            }
        }
    }

    private fun setupDeleteButton(transaction: Transaction) {
        binding.transactionDeleteButton.setOnClickListener {
            viewModel.deleteTransaction(transaction)
        }
    }

    private fun warn(@StringRes stringRes: Int) {
        Toast.makeText(requireContext(), stringRes, Toast.LENGTH_SHORT).show()
    }

}