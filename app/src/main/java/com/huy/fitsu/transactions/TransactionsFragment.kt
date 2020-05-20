package com.huy.fitsu.transactions

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.huy.fitsu.databinding.TransactionsFragBinding

class TransactionsFragment: Fragment() {

    private lateinit var binding: TransactionsFragBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = TransactionsFragBinding.inflate(inflater, container, false)
        return binding.root
    }

}