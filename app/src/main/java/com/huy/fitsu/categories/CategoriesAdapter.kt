package com.huy.fitsu.categories

import android.content.res.ColorStateList
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.huy.fitsu.data.model.Category
import com.huy.fitsu.databinding.CategoryItemBinding

class CategoriesAdapter(
    private val viewModel: CategoriesViewModel
) : ListAdapter<Category, CategoriesAdapter.CategoryItem>(CategoryDiffCallback()) {

    override fun onBindViewHolder(holder: CategoryItem, position: Int) {
        val item = getItem(position)
        holder.bind(viewModel, item)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryItem {
        return CategoryItem.from(parent)
    }

    class CategoryItem private constructor(
        private val binding: CategoryItemBinding
    ): RecyclerView.ViewHolder(binding.root) {

        fun bind(viewModel: CategoriesViewModel, category: Category) {
            binding.category = category
            binding.categoryItemIcon.backgroundTintList = ColorStateList.valueOf(category.color)
            binding.root.setOnClickListener {
                viewModel.editCategoryWithId(category.id)
            }
        }

        companion object {
            fun from(parent: ViewGroup): CategoryItem {
                val inflater = LayoutInflater.from(parent.context)
                val binding = CategoryItemBinding.inflate(inflater, parent, false)
                return CategoryItem(binding)
            }

        }

    }

}

class CategoryDiffCallback : DiffUtil.ItemCallback<Category>() {
    override fun areItemsTheSame(oldItem: Category, newItem: Category): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Category, newItem: Category): Boolean {
        return oldItem == newItem
    }
}