package com.huy.fitsu

import androidx.paging.PagedList
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