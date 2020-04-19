package com.huy.fitsu.budgetHistory

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.huy.fitsu.data.model.Budget
import com.huy.fitsu.databinding.BudgetItemBinding

class BudgetsAdapter(
    private val viewModel: DashboardViewModel
): PagedListAdapter<Budget, BudgetsAdapter.BudgetItem>(diffCallback) {

    companion object {
        private val diffCallback = object : DiffUtil.ItemCallback<Budget>() {
            override fun areItemsTheSame(oldItem: Budget, newItem: Budget): Boolean
                    = oldItem.id == newItem.id


            override fun areContentsTheSame(oldItem: Budget, newItem: Budget): Boolean
                    = oldItem == newItem
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BudgetItem
        = BudgetItem.from(parent)

    override fun onBindViewHolder(holder: BudgetItem, position: Int) {
        getItem(position)?.let {
            holder.bind(it, viewModel)
        }
    }



    class BudgetItem private constructor(
        private val binding: BudgetItemBinding
    ): RecyclerView.ViewHolder(binding.root) {

        fun bind(budget: Budget, viewModel: DashboardViewModel) {
            binding.budget = budget
        }

        companion object {
            fun from(parent: ViewGroup) : BudgetItem {
                val inflater = LayoutInflater.from(parent.context)
                val binding = BudgetItemBinding.inflate(inflater, parent, false)
                return BudgetItem(binding)
            }
        }

    }

}