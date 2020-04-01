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
import androidx.navigation.fragment.navArgs
import com.huy.fitsu.FitsuApplication
import com.huy.fitsu.databinding.AddEditCategoryFragBinding
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
        loadCategoryInfo()
    }

    private fun setCategoryId() {
        val categoryId = args.categoryId
        viewModel.setCategoryId(categoryId)
    }

    private fun loadCategoryInfo() {
        viewModel.getCategory().observe(viewLifecycleOwner, Observer {
            it?.let { category -> binding.category = category }
        })
    }

}