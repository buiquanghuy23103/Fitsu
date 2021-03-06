package com.huy.fitsu.dashboard

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.huy.fitsu.FitsuApplication
import com.huy.fitsu.R
import com.huy.fitsu.databinding.DashboardFragBinding
import com.huy.fitsu.databinding.SimpleEditDialogBinding
import com.huy.fitsu.util.toFloat
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

        setupAccountBalanceText()

        setupAccountBalanceClickListener()

    }

    private fun setupAccountBalanceText() {
        viewModel.accountBalanceString.observe(viewLifecycleOwner, Observer {
            it?.let { accountBalanceString ->
                binding.dashboardAccountText.text = accountBalanceString
            }
        })
    }

    private fun setupAccountBalanceClickListener() {
        binding.dashboardAccountText.setOnClickListener {
            showAccountBalanceEditDialog()
        }
    }

    private fun showAccountBalanceEditDialog() {
        val dialogBinding: SimpleEditDialogBinding =
            DataBindingUtil.inflate(layoutInflater, R.layout.simple_edit_dialog, null, false)

        dialogBinding.title = getString(R.string.account_balance_label)

        viewModel.accountBalance.observe(viewLifecycleOwner, Observer {
            it?.let {accountFloatValue ->
                dialogBinding.editTextString = accountFloatValue.toString()
            }
        })

        MaterialAlertDialogBuilder(requireContext())
            .setView(dialogBinding.root)
            .setPositiveButton(android.R.string.ok) {dialog, _ ->
                try {
                    val newValue = dialogBinding.editDialogEditText.toFloat()
                    viewModel.saveAccountBalance(newValue)
                    dialog.dismiss()
                } catch (e: NumberFormatException) {
                    dialogBinding.errorText = getString(R.string.field_must_be_a_number)
                } catch (e: NullPointerException) {
                    dialogBinding.errorText = getString(R.string.field_must_not_be_empty)
                }
            }
            .setNegativeButton(R.string.cancel) {dialog, _ ->
                dialog.cancel()
            }
            .create()
            .show()

    }

}