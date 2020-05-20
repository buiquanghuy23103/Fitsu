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
import com.huy.fitsu.FitsuApplication
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
    }

    private fun setupTransactionsList() {
        val adapter = TransactionsAdapter()
        binding.transactionsList.adapter = adapter
        viewModel.transactionsLiveData.observe(viewLifecycleOwner, Observer {
            it?.let {transactions ->
                adapter.submitList(transactions)
            }
        })
    }

}