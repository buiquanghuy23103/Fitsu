package com.huy.fitsu.addEditCategory

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
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.huy.fitsu.FitsuApplication
import com.huy.fitsu.R
import com.huy.fitsu.databinding.AddEditCategoryFragBinding
import com.huy.fitsu.util.hideKeyboardFromView
import yuku.ambilwarna.AmbilWarnaDialog
import javax.inject.Inject

class AddEditCategoryFragment : Fragment() {

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

        loadView()

        viewModel.navigateBackLiveData().observe(viewLifecycleOwner, Observer {
            findNavController().navigateUp()
        })
    }

    private fun loadView() {
        viewModel.getCategoryLiveDataById(args.categoryId)
            .observe(viewLifecycleOwner, Observer {
                it?.let { category ->
                    binding.category = category
                    setupTitleEditText()
                    setupColorSelector()
                    setupUpdateCategoryButton()
                    setupDeleteCategoryButton()
                }
            })

    }

    private fun setupTitleEditText() = with(binding.categoryTitleEditText) {
        setOnFocusChangeListener { view, hasFocus ->
            if (!hasFocus) {
                hideKeyboardFromView(view)
                binding.category = binding.category!!.copy(
                    title = text.toString()
                )
            }
        }
    }


    private fun setupColorSelector() {
        binding.categoryChangeColorButton.setOnClickListener {
            clearFocusFromTitleEditText()
            showColorPicker()
        }
    }

    private fun showColorPicker() {
        AmbilWarnaDialog(
            requireContext(),
            binding.category!!.color,
            object : AmbilWarnaDialog.OnAmbilWarnaListener {
                override fun onOk(dialog: AmbilWarnaDialog?, color: Int) {
                    binding.category = binding.category!!.copy(
                        color = color
                    )
                }

                override fun onCancel(dialog: AmbilWarnaDialog?) { /*DO NOTHING*/
                }
            }
        ).show()
    }

    private fun setupUpdateCategoryButton() {
        binding.categoryUpdateButton.setOnClickListener {
            clearFocusFromTitleEditText()
            viewModel.updateCategory(binding.category!!)
        }
    }

    private fun setupDeleteCategoryButton() {
        binding.categoryDeleteButton.setOnClickListener {
            clearFocusFromTitleEditText()
            showDeleteWarningDialog()
        }
    }

    private fun showDeleteWarningDialog() {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle(R.string.delete_category_warning_dialog_title)
            .setMessage(R.string.delete_category_warning_dialog_message)
            .setPositiveButton(R.string.delete) { dialog, _ ->
                val categoryId = binding.category!!.id
                viewModel.deleteCategoryById(categoryId)
                dialog.dismiss()
            }
            .show()
    }

    private fun clearFocusFromTitleEditText() {
        binding.categoryTitleEditText.clearFocus()
    }

}