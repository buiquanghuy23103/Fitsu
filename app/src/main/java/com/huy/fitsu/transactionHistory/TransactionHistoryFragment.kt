package com.huy.fitsu.transactionHistory

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
import com.huy.fitsu.databinding.TransactionHistoryFragBinding
import javax.inject.Inject

class TransactionHistoryFragment: Fragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val viewModel: TransactionHistoryViewModel by viewModels { viewModelFactory }

    private lateinit var binding: TransactionHistoryFragBinding

    override fun onAttach(context: Context) {
        super.onAttach(context)
        (requireActivity().application as FitsuApplication).appComponent
            .transactionHistoryComponent()
            .create()
            .inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = TransactionHistoryFragBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        binding.lifecycleOwner = viewLifecycleOwner

        setupTransactionList()
        setupAddTransactionFab()

        viewModel.editTransactionEvent.observe(viewLifecycleOwner, EventObserver {
            editTransaction(it)
        })
    }

    private fun setupTransactionList() {
        val adapter = TransactionHistoryAdapter(viewModel)
        binding.transactionHistoryList.adapter = adapter

        viewModel.transactions.observe(viewLifecycleOwner, Observer {
            it?.let { list -> adapter.submitList(list) }
        })
    }

    private fun setupAddTransactionFab() {
        binding.transactionHistoryAddTransFab.setOnClickListener {
            viewModel.addTransaction()
        }
    }

    private fun editTransaction(transactionId: String) {
        val action = TransactionHistoryFragmentDirections.toAddEditTransactionFragment(transactionId)
        findNavController().navigate(action)
    }

}