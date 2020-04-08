package com.huy.fitsu.addEditTransaction

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.huy.fitsu.databinding.AddEditTransactionFragBinding

class AddEditTransactionFragment: Fragment() {

    private lateinit var binding: AddEditTransactionFragBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = AddEditTransactionFragBinding.inflate(inflater, container, false)
        return binding.root
    }

}