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
import androidx.navigation.fragment.navArgs
import com.huy.fitsu.FitsuApplication
import com.huy.fitsu.databinding.AddEditTransactionFragBinding
import javax.inject.Inject

class AddEditTransactionFragment: Fragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private lateinit var binding: AddEditTransactionFragBinding

    private val viewModel by viewModels<AddEditTransactionViewModel> { viewModelFactory }

    private val navArgs : AddEditTransactionFragmentArgs by navArgs()


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

        viewModel.withTransactionId(navArgs.transactionId)
        binding.lifecycleOwner = viewLifecycleOwner

        viewModel.transaction.observe(viewLifecycleOwner, Observer {
            it?.let { transaction ->
                binding.transaction = transaction
            }
        })
    }

}