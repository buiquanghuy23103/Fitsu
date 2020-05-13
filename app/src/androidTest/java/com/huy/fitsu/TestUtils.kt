package com.huy.fitsu

import android.view.View
import android.view.ViewGroup
import androidx.paging.PagedList
import androidx.paging.PositionalDataSource
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.matcher.BoundedMatcher
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.hamcrest.TypeSafeMatcher
import org.mockito.ArgumentMatchers
import org.mockito.Mockito

fun <T> mockPagedList(list: List<T>) : PagedList<T>{
    val pagedList = Mockito.mock(PagedList::class.java) as PagedList<T>
    Mockito.`when`(pagedList[ArgumentMatchers.anyInt()])
        .then{
            val index = it.arguments.first() as Int
            list[index]
        }
    Mockito.`when`(pagedList.size).thenReturn(list.size)
    return pagedList
}

fun <T> MutableList<out T>.toTestPagedList(): PagedList<T> {
    val dataSource = object : PositionalDataSource<T>() {

        override fun loadInitial(params: LoadInitialParams, callback: LoadInitialCallback<T>) {
            val data = this@toTestPagedList
            val position = this@toTestPagedList.size - 1
            val size = this@toTestPagedList.size
            callback.onResult(data, position, size)
        }

        override fun loadRange(params: LoadRangeParams, callback: LoadRangeCallback<T>) {
            throw Exception("loadRange not expected")
        }
    }

    return PagedList.Builder(dataSource, this.size)
        .setFetchExecutor { it.run() }
        .setNotifyExecutor { it.run() }
        .build()
}

fun childAtPosition(
    parentMatcher: Matcher<View>, position: Int
): Matcher<View> {

    return object : TypeSafeMatcher<View>() {
        override fun describeTo(description: Description) {
            description.appendText("Child at position $position in parent ")
            parentMatcher.describeTo(description)
        }

        public override fun matchesSafely(view: View): Boolean {
            val parent = view.parent
            return parent is ViewGroup && parentMatcher.matches(parent)
                    && view == parent.getChildAt(position)
        }
    }
}

fun atPosition(position: Int, itemMatcher: Matcher<View>): Matcher<View> {
    return object : BoundedMatcher<View, RecyclerView>(RecyclerView::class.java) {

        override fun describeTo(description: Description) {
            description.appendText("has item at position: $position, ")
            itemMatcher.describeTo(description)
        }

        override fun matchesSafely(recyclerView: RecyclerView): Boolean {
            val viewHolder = recyclerView.findViewHolderForAdapterPosition(position)
            return if (viewHolder == null) false else itemMatcher.matches(viewHolder)
        }
    }
}