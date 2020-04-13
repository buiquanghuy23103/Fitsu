package com.huy.fitsu.addEditCategory

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.divyanshu.colorseekbar.ColorSeekBar
import com.huy.fitsu.FitsuApplication
import com.huy.fitsu.R
import com.huy.fitsu.data.model.BudgetDuration
import com.huy.fitsu.data.model.Category
import com.huy.fitsu.databinding.AddEditCategoryFragBinding
import kotlinx.android.synthetic.main.add_edit_category_frag.*
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
                binding.categoryChangeColorButton.setBackgroundColor(category.color)
                binding.categoryBudgetDurationEditText.setText(category.budgetDuration.name, false)
                binding.categoryResetColorButton.setOnClickListener {
                    binding.categoryColorSeekBar.visibility = View.GONE
                    binding.categoryChangeColorButton.setBackgroundColor(category.color)
                }
                setupUpdateCategoryButton(it)
                setupDeleteCategoryButton(it)
            }
        })

        setupDropDownMenu()
        setupColorSelector()
    }

    private fun setupDropDownMenu() {
        val adapter = ArrayAdapter(
            requireContext(),
            R.layout.dropdown_menu_popup_item,
            BudgetDuration.values()
        )
        binding.categoryBudgetDurationEditText.setAdapter(adapter)
    }

    private fun setupColorSelector() {
        binding.categoryChangeColorButton.setOnClickListener {
            binding.categoryColorSeekBar.visibility = View.VISIBLE
        }
        binding.categoryColorSeekBar.setOnColorChangeListener(
            object : ColorSeekBar.OnColorChangeListener {
                override fun onColorChangeListener(color: Int) {
                    binding.categoryChangeColorButton.setBackgroundColor(color)
                }
            }
        )
    }

    private fun setupUpdateCategoryButton(category: Category) {
        binding.categoryUpdateButton.setOnClickListener {
            val title = category_title_edit_text.text.toString()
            val budgetDuration = category_budget_duration_edit_text.text.toString()
            val color = category_color_seek_bar.getColor()
            val newCategory = category.copy(
                title = title,
                budgetDuration = BudgetDuration.valueOf(budgetDuration)
            )
            if (category_color_seek_bar.isVisible) newCategory.color = color
            viewModel.updateCategory(newCategory)
        }
    }

    private fun setupDeleteCategoryButton(category: Category) {
        binding.categoryDeleteButton.setOnClickListener {
            viewModel.deleteCategory(category.id)
        }
    }

}