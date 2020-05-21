package com.huy.fitsu.transactions

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
import com.huy.fitsu.databinding.TransactionsFragBinding
import javax.inject.Inject

class TransactionsFragment: Fragment() {

    private lateinit var binding: TransactionsFragBinding

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private val viewModel : TransactionsViewModel by viewModels { viewModelFactory }

    override fun onAttach(context: Context) {
        super.onAttach(context)

        injectDagger()
    }

    private fun injectDagger() {
        (requireActivity().application as FitsuApplication).appComponent
            .transactionsComponent()
            .create()
            .inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = TransactionsFragBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        binding.lifecycleOwner = this.viewLifecycleOwner
        setupTransactionsList()
        setupAddTransactionFab()
        setupEditTransactionEvent()
    }

    private fun setupTransactionsList() {
        val adapter = TransactionsAdapter()
        binding.transactionsList.adapter = adapter
        viewModel.transactionsLiveData.observe(viewLifecycleOwner, Observer {
            binding.noTransactions = it.isNullOrEmpty()

            if (!it.isNullOrEmpty()) {
                adapter.submitList(it)
            }
        })
    }

    private fun setupAddTransactionFab() {
        binding.addTransactionFab.setOnClickListener {
            viewModel.addTransaction()
        }
    }

    private fun setupEditTransactionEvent() {
        viewModel.editTransaction.observe(viewLifecycleOwner, EventObserver {
            navigateToAddEditTransactionFrag(it)
        })
    }

    private fun navigateToAddEditTransactionFrag(transactionId: String) {
        val destination = TransactionsFragmentDirections.toAddEditTransactionFragment(transactionId)
        findNavController().navigate(destination)
    }

}