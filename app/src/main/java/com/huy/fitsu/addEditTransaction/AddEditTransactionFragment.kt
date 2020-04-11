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
import timber.log.Timber
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

        viewModel.transaction.observe(viewLifecycleOwner, Observer {
            it?.let { transaction ->
                binding.transaction = transaction
                setupDateButton(transaction.date)
            }
        })

        viewModel.category.observe(viewLifecycleOwner, Observer {
            it?.let {category ->
                binding.category = category
            }
        })

        viewModel.categories.observe(viewLifecycleOwner, Observer {
            it?.let { categories ->
                setupCategoryPicker(categories)

            }
        })

        viewModel.navigateUp.observe(viewLifecycleOwner, EventObserver {
            findNavController().navigateUp()
        })

    }

    private fun setupCategoryPicker(categories: List<Category>) {
        val categoriesString : Array<CharSequence> = categories.map { it.title }.toTypedArray()

        val dialog = MaterialAlertDialogBuilder(requireContext())
            .setTitle(R.string.transaction_category_picker_title)
            .setNegativeButton(R.string.cancel) { dialog, _ ->
                dialog.dismiss()
            }
            .setPositiveButton(R.string.transaction_category_picker_positive_button_title) { dialog, _ ->

                dialog.dismiss()
            }
            .setSingleChoiceItems(categoriesString, -1) { _, which ->
                Timber.i("chosenIndex=$which")
                val chosenCategory = categories[which]
                viewModel.updateTransactionCategoryId(chosenCategory.id)
            }

        binding.transactionCategoryButton.setOnClickListener {
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
            viewModel.updateTransaction()
        }
    }

}