package com.huy.fitsu.transactionHistory

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.huy.fitsu.data.model.TransactionDetail
import com.huy.fitsu.databinding.TransactionItemBinding
import javax.inject.Inject

class TransactionHistoryAdapter @Inject constructor(
    private val viewModel: TransactionHistoryViewModel
): PagedListAdapter<TransactionDetail, TransactionHistoryAdapter.TransactionItem>(TransactionDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TransactionItem {
        return TransactionItem.from(parent)
    }

    override fun onBindViewHolder(holder: TransactionItem, position: Int) {
        val item: TransactionDetail? = getItem(position)
        item?.let { holder.bind(it, viewModel) }
    }

    class TransactionItem private constructor(
        private val binding: TransactionItemBinding
    ): RecyclerView.ViewHolder(binding.root) {

        fun bind(transaction: TransactionDetail, viewModel: TransactionHistoryViewModel) {
            binding.transaction = transaction
            binding.root.setOnClickListener {
                viewModel.editTransaction(transaction.id)
            }
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