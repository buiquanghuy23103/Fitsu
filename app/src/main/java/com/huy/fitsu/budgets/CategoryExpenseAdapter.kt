package com.huy.fitsu.budgets

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.huy.fitsu.data.model.CategoryExpense
import com.huy.fitsu.databinding.CategoryExpenseItemBinding

class CategoryExpenseAdapter: ListAdapter<CategoryExpense, CategoryExpenseAdapter.CategoryExpenseItem>(diffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryExpenseItem {
        return CategoryExpenseItem.from(parent)
    }

    override fun onBindViewHolder(holder: CategoryExpenseItem, position: Int) {
        val item = getItem(position)
        holder.bind(item)
    }

    companion object {
        val diffCallback = object : DiffUtil.ItemCallback<CategoryExpense>() {

            override fun areItemsTheSame(
                oldItem: CategoryExpense,
                newItem: CategoryExpense
            ): Boolean {
                return oldItem.categoryTitle == newItem.categoryTitle
            }

            override fun areContentsTheSame(
                oldItem: CategoryExpense,
                newItem: CategoryExpense
            ): Boolean {
                return oldItem.categoryTitle == newItem.categoryTitle &&
                        oldItem.categoryColor == newItem.categoryColor &&
                        oldItem.totalExpense == newItem.totalExpense
            }
        }
    }

    class CategoryExpenseItem private constructor(
        private val binding: CategoryExpenseItemBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(categoryExpense: CategoryExpense) {
            binding.categoryExpense = categoryExpense
        }

        companion object {
            fun from(container: ViewGroup): CategoryExpenseItem {
                val inflater = LayoutInflater.from(container.context)
                val binding = CategoryExpenseItemBinding.inflate(inflater, container, false)
                return CategoryExpenseItem(binding)
            }
        }

    }

}