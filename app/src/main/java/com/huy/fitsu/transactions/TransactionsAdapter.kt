package com.huy.fitsu.transactions


import android.os.Build
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.navigation.findNavController
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.huy.fitsu.data.model.TransactionDetail
import com.huy.fitsu.databinding.TransactionItemBinding
import com.huy.fitsu.util.toTransitionMap

class TransactionsAdapter: ListAdapter<TransactionDetail, TransactionsAdapter.TransactionItem>(TransactionDiffCallback()) {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TransactionItem {
        return TransactionItem.from(parent)
    }

    override fun onBindViewHolder(holder: TransactionItem, position: Int) {
        val item = getItem(position)
        holder.bind(item)
    }

    class TransactionItem private constructor(
        private val binding: TransactionItemBinding
    ): RecyclerView.ViewHolder(binding.root) {

        private lateinit var transactionDetail: TransactionDetail

        fun bind(transactionDetail: TransactionDetail) {
            this.transactionDetail = transactionDetail
            binding.transactionDetail = transactionDetail
            binding.root.setOnClickListener {
                navigateToAddEditTransactionFrag()
            }
            binding.executePendingBindings()
        }

        private fun navigateToAddEditTransactionFrag() {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                navigateWithTransition()
            } else {
                navigateWithoutTransition()
            }
        }

        @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
        private fun navigateWithTransition() {
            val extras = FragmentNavigatorExtras(
                binding.transactionItemContainer.toTransitionMap()
            )
            val destination = TransactionsFragmentDirections
                .toAddEditTransactionFragment(transactionDetail.id)
            binding.root.findNavController().navigate(destination, extras)
        }

        private fun navigateWithoutTransition() {
            val destination = TransactionsFragmentDirections
                .toAddEditTransactionFragment(transactionDetail.id)
            binding.root.findNavController().navigate(destination)
        }


        companion object {
            fun from(parent: ViewGroup) : TransactionItem {
                val inflater = LayoutInflater.from(parent.context)
                val binding = TransactionItemBinding.inflate(inflater, parent, false)
                return TransactionItem(binding)
            }
        }

    }

}

class TransactionDiffCallback: DiffUtil.ItemCallback<TransactionDetail>() {

    override fun areItemsTheSame(oldItem: TransactionDetail, newItem: TransactionDetail): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(
        oldItem: TransactionDetail,
        newItem: TransactionDetail
    ): Boolean {
        return oldItem == newItem
    }
}