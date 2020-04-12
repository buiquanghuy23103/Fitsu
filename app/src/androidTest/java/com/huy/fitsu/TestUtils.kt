package com.huy.fitsu

import androidx.paging.PagedList
import androidx.paging.PositionalDataSource
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