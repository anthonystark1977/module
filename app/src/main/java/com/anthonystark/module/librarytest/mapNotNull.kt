package com.anthonystark.module.librarytest

import timber.log.Timber


/**
 * [mapNotNull]
 *
 * null이면 필터 처리되서 걸러짐.
 *
 * @author Scarlett
 * @version 0.0.8
 * @since 2021-03-10 오후 7:35
 **/
fun mapNotNullTest() {
    val offset = 0
    val limit = 20
    val search = null
    val filter = 4

    val temp = mapOf(
        "offest" to offset,
        "limit" to limit,
        "searchText" to search,
        "filters" to filter
    )

    val result = temp.entries.mapNotNull { (k, v) ->
        if (v == null) null
        else k to v
    }.toMap()

    Timber.tag("mapNotNullTest").d(result.toString())
}