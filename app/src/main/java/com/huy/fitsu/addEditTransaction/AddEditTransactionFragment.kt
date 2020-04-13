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
import java.util.*
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
        binding = AddEditTransactionFragBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        viewModel.loadTransactionWithId(navArgs.transactionId)

        binding.lifecycleOwner = viewLifecycleOwner

        setupUpdateButton()
        updateTransactionValue()

        viewModel.transaction.observe(viewLifecycleOwner, Observer {
            it?.let { transaction ->
                binding.transaction = transaction
                setupDateButton(transaction.date)
            }
        })

        viewModel.categoriesAndChosenCategory().observe(viewLifecycleOwner, Observer {
            it?.let { pair ->
                val categories = pair.first
                val category = pair.second
                val currentCategoryIndex = categories.indexOf(category)

                binding.category = category
                setupCategoryPicker(categories, category)
                viewModel.saveCategoryIndex(currentCategoryIndex)
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
            .setNegativeButton(R.string.cancel) { dialog, _ ->
                dialog.dismiss()
            }

        binding.transactionCategoryButton.setOnClickListener {
            val currentCategoryIndex = categories.indexOf(selectedCategory)

            dialog.setPositiveButton(R.string.ok) { dialog, _ ->
                viewModel.updateTransactionCategoryId()
                dialog.dismiss()
            }.setSingleChoiceItems(categoriesString, currentCategoryIndex) { _, which ->
                viewModel.saveCategoryIndex(which)
            }

            dialog.show()
        }
    }

    private fun setupDateButton(currentDate: Date) {
        val currentDateString = DateConverter.dateToString(currentDate)
        binding.transactionDateButton.text = currentDateString

        val datePicker = MaterialDatePicker.Builder.datePicker()
            .setSelection(currentDate.time)
            .setTitleText(R.string.transaction_date_picker_title)
            .build()

        binding.transactionDateButton.setOnClickListener {
            datePicker.addOnPositiveButtonClickListener {
                    viewModel.updateTransactionDate(Date(it))
            }
            datePicker.showNow(this.childFragmentManager, datePickerTag)
        }

    }

    private fun setupUpdateButton() {
        binding.transactionUpdateButton.setOnClickListener {
            viewModel.updateTransactionToDb()
        }
    }

    private fun updateTransactionValue() {
        with(binding.transactionValueEditText) {
            setOnFocusChangeListener { _, hasFocus ->
                if (hasFocus) {
                    val str = this.text.toString()
                    val value = str.toInt()
                    viewModel.updateTransactionValue(value)
                }
            }
        }
    }

}