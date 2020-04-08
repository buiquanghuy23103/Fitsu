package com.huy.fitsu.dashboard

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.huy.fitsu.data.model.Transaction
import com.huy.fitsu.databinding.TransactionItemBinding

class TransactionsAdapter(
    private val viewModel: TransactionsViewModel
): PagedListAdapter<Transaction, TransactionsAdapter.TransactionItem>(TransactionDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TransactionItem {
        return TransactionItem.from(parent)
    }

    override fun onBindViewHolder(holder: TransactionItem, position: Int) {
        val item: Transaction? = getItem(position)
        item?.let { holder.bind(it, viewModel) }
    }

    class TransactionItem private constructor(
        private val binding: TransactionItemBinding
    ): RecyclerView.ViewHolder(binding.root) {

        fun bind(transaction: Transaction, viewModel: TransactionsViewModel) {
            binding.transaction = transaction
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

class TransactionDiffCallback: DiffUtil.ItemCallback<Transaction>() {

    override fun areItemsTheSame(oldItem: Transaction, newItem: Transaction): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Transaction, newItem: Transaction): Boolean {
        return oldItem == newItem
    }
}