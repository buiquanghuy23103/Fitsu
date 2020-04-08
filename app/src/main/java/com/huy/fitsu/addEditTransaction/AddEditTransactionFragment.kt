package com.huy.fitsu.addEditTransaction

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.huy.fitsu.FitsuApplication
import com.huy.fitsu.databinding.AddEditTransactionFragBinding

class AddEditTransactionFragment: Fragment() {

    private lateinit var binding: AddEditTransactionFragBinding

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

}