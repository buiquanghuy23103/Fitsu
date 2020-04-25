package com.huy.fitsu.addEditCategory

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.annotation.ColorInt
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.huy.fitsu.FitsuApplication
import com.huy.fitsu.R
import com.huy.fitsu.data.model.BudgetDuration
import com.huy.fitsu.data.model.Category
import com.huy.fitsu.databinding.AddEditCategoryFragBinding
import kotlinx.android.synthetic.main.add_edit_category_frag.*
import yuku.ambilwarna.AmbilWarnaDialog
import javax.inject.Inject

class AddEditCategoryFragment: Fragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val viewModel by viewModels<AddEditCategoryViewModel> { viewModelFactory }

    private val args: AddEditCategoryFragmentArgs by navArgs()

    private lateinit var binding: AddEditCategoryFragBinding

    override fun onAttach(context: Context) {
        super.onAttach(context)

        (requireActivity().application as FitsuApplication).appComponent
            .addEditCategoryComponent()
            .create()
            .inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = AddEditCategoryFragBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        binding.lifecycleOwner = this.viewLifecycleOwner

        setCategoryId()

        loadView()

        viewModel.navigateBackLiveData().observe(viewLifecycleOwner, Observer {
            findNavController().navigateUp()
        })
    }

    private fun setCategoryId() {
        val categoryId = args.categoryId
        viewModel.setCategoryId(categoryId)
    }

    private fun loadView() {
        viewModel.getCategory().observe(viewLifecycleOwner, Observer {
            it?.let { category ->
                binding.category = category
                setupColorSelector(category.color)
                binding.categoryChangeColorButton.setBackgroundColor(category.color)
                binding.categoryBudgetDurationEditText.setText(category.budgetDuration.name, false)
                setupUpdateCategoryButton(it)
            }
        })

        setupDropDownMenu()
        setupDeleteCategoryButton()
    }

    private fun setupDropDownMenu() {
        val adapter = ArrayAdapter(
            requireContext(),
            R.layout.dropdown_item_style,
            BudgetDuration.values()
        )
        binding.categoryBudgetDurationEditText.setAdapter(adapter)
    }

    private fun setupColorSelector(@ColorInt initColorInt: Int) {
        binding.categoryChangeColorButton.setOnClickListener {
            showColorPicker(initColorInt)
        }
    }

    private fun showColorPicker(initColorInt: Int) {
        AmbilWarnaDialog(
            requireContext(),
            initColorInt,
            object : AmbilWarnaDialog.OnAmbilWarnaListener {
                override fun onOk(dialog: AmbilWarnaDialog?, color: Int) {
                    binding.categoryChangeColorButton.setBackgroundColor(color)
                }

                override fun onCancel(dialog: AmbilWarnaDialog?) { /*DO NOTHING*/ }
            }
        ).show()
    }

    private fun setupUpdateCategoryButton(category: Category) {
        binding.categoryUpdateButton.setOnClickListener {
            val title = category_title_edit_text.text.toString()
            val budgetDuration = category_budget_duration_edit_text.text.toString()
            val newCategory = category.copy(
                title = title,
                budgetDuration = BudgetDuration.valueOf(budgetDuration)
            )
            viewModel.updateCategory(newCategory)
        }
    }

    private fun setupDeleteCategoryButton() {
        binding.categoryDeleteButton.setOnClickListener {
            showDeleteWarningDialog()
        }
    }

    private fun showDeleteWarningDialog() {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle(R.string.delete_category_warning_dialog_title)
            .setMessage(R.string.delete_category_warning_dialog_message)
            .setPositiveButton(R.string.delete_category_warning_dialog_positive_button) {dialog, _ ->
                viewModel.deleteCategory()
                dialog.dismiss()
            }
            .show()
    }

}