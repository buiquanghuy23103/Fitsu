package com.huy.fitsu.categories

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
import com.huy.fitsu.FitsuApplication
import com.huy.fitsu.data.model.EventObserver
import com.huy.fitsu.databinding.CategoriesFragBinding
import timber.log.Timber
import javax.inject.Inject

class CategoriesFragment: Fragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val viewModel by viewModels<CategoriesViewModel> { viewModelFactory }

    private lateinit var binding: CategoriesFragBinding

    private lateinit var listAdapter: CategoriesAdapter

    override fun onAttach(context: Context) {
        super.onAttach(context)
        (requireActivity().application as FitsuApplication).appComponent
            .categoriesComponent()
            .create()
            .inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = CategoriesFragBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        binding.lifecycleOwner = this.viewLifecycleOwner
        viewModel.createDummyCategories()
        setupListAdapter()
        setupNavigation()
    }

    private fun setupListAdapter() {
        listAdapter = CategoriesAdapter(viewModel)
        binding.categoriesList.adapter = listAdapter

        viewModel.getAllCategories().observe(viewLifecycleOwner, Observer {
            if (it.isEmpty()) Timber.i("category list is empty")
            it?.let { categories ->
                listAdapter.submitList(categories)
            }
        })
    }

    private fun setupNavigation() {
        viewModel.editCategoryEventLiveData().observe(viewLifecycleOwner, EventObserver{
            val action = CategoriesFragmentDirections.toAddEditCategoryFragment(it)
            findNavController().navigate(action)
        })
    }

}