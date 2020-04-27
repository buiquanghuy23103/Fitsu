package com.huy.fitsu.categories

import android.content.res.ColorStateList
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.navigation.findNavController
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.huy.fitsu.data.model.Category
import com.huy.fitsu.databinding.CategoryItemBinding
import com.huy.fitsu.util.toTransitionMap
import timber.log.Timber

class CategoriesAdapter :
    ListAdapter<Category, CategoriesAdapter.CategoryItem>(CategoryDiffCallback()) {

    override fun onBindViewHolder(holder: CategoryItem, position: Int) {
        val item = getItem(position)
        holder.bind(item)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryItem {
        return CategoryItem.from(parent)
    }

    class CategoryItem private constructor(
        private val binding: CategoryItemBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(category: Category) =
            with(binding) {
                this.category = category
                categoryItemIcon.backgroundTintList = ColorStateList.valueOf(category.color)
                root.setOnClickListener {
                    navigateToAddEditCategoryFrag(category, it)
                }
                executePendingBindings()
            }

        private fun navigateToAddEditCategoryFrag(
            category: Category,
            view: View
        ) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                Timber.i("Transition is on")
                navigateWithTransition(category, view)
            } else {
                navigateWithoutTransition(category, view)
            }
        }

        private fun navigateWithoutTransition(
            category: Category,
            view: View
        ) {
            val destination = CategoriesFragmentDirections.toAddEditCategoryFragment(
                categoryId = category.id
            )
            view.findNavController().navigate(destination)
        }

        @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
        private fun navigateWithTransition(
            category: Category,
            view: View
        ) {
            val extras = FragmentNavigatorExtras(
                binding.categoryItemContainer.toTransitionMap()
            )
            val destination = CategoriesFragmentDirections.toAddEditCategoryFragment(
                categoryId = category.id
            )
            view.findNavController().navigate(destination, extras)
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