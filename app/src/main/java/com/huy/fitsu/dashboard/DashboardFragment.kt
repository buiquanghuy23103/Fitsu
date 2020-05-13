package com.huy.fitsu.dashboard

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import com.huy.fitsu.FitsuApplication
import com.huy.fitsu.R
import com.huy.fitsu.databinding.AccountBalanceEditDialogBinding
import com.huy.fitsu.databinding.DashboardFragBinding
import com.huy.fitsu.util.toCurrencyString
import org.jetbrains.annotations.NotNull
import java.lang.NullPointerException
import java.lang.NumberFormatException
import javax.inject.Inject

class DashboardFragment : Fragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val viewModel by viewModels<DashboardViewModel> { viewModelFactory }

    private lateinit var binding: DashboardFragBinding

    override fun onAttach(context: Context) {
        super.onAttach(context)
        (requireActivity().application as FitsuApplication).appComponent
            .dashboardComponent()
            .create()
            .inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DashboardFragBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = viewModel

        binding.dashboardAccountText.setOnClickListener {
            showAccountBalanceEditDialog()
        }

    }

    private fun showAccountBalanceEditDialog() {
        val dialogBinding = AccountBalanceEditDialogBinding.inflate(
            layoutInflater, null, false
        )

        MaterialAlertDialogBuilder(requireContext())
            .setView(R.layout.account_balance_edit_dialog)
            .setPositiveButton(android.R.string.ok) {dialog, _ ->
                dialogBinding.accountBalanceEditText.saveAccountBalance()
                dialog.dismiss()
            }
            .setNegativeButton(R.string.cancel) {dialog, _ ->
                dialog.cancel()
            }
            .create()
            .show()

    }

    private fun EditText.saveAccountBalance() {
        try {
            val accountBalance = this.toString().toFloat()
            viewModel.saveAccountBalance(accountBalance)
        } catch (e: NumberFormatException) {
            Snackbar.make(
                this,
                R.string.field_must_be_a_number,
                Snackbar.LENGTH_SHORT
            )
                .show()
        } catch (e: NullPointerException) {
            Snackbar.make(
                this,
                R.string.field_must_not_be_empty,
                Snackbar.LENGTH_SHORT
            )
                .show()
        }
    }

}