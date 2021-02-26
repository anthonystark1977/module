package com.lovely.deer.listener

import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import timber.log.Timber


abstract class EndlessRecyclerViewScrollListener(
    val mLayoutManager: RecyclerView.LayoutManager?,
    var mCurrentPage: Int
) : RecyclerView.OnScrollListener() {
    // 더 로드하기 전에 현재 스크롤 위치 아래에 포함할 항목의 최소 아이템 수.
    private var visibleThreshold = 5

    // 마지막 로드 후 아이템의 총 항목 수
    private var previousTotalItemCount = 0

    // 마지막 데이터가 로드되기를 기다리는 경우 true.
    private var loading = true
    private val startingPageIndex = 0

    init {
        when (mLayoutManager) {
            is GridLayoutManager -> {
                visibleThreshold *= mLayoutManager.spanCount
            }
            is StaggeredGridLayoutManager -> {
                visibleThreshold *= mLayoutManager.spanCount
            }
        }
    }

    private fun getLastVisibleItem(lastVisibleItemPositions: IntArray): Int {
        var maxSize = 0
        for (i in lastVisibleItemPositions.indices) {
            if (i == 0) {
                maxSize = lastVisibleItemPositions[i]
            } else if (lastVisibleItemPositions[i] > maxSize) {
                maxSize = lastVisibleItemPositions[i]
            }
        }
        return maxSize
    }

    // 아래 메소드는 스크롤하는 동안 1초에 여러 번 발생하므로 주의해야 합니다.
    // 데이터를 좀 더 로드해야 할 경우 문제 해결에 도움이 되는 몇 가지 유용한 매개 변수가 제공됩니다.
    // 그러나 먼저 이전 로드가 완료되기를 기다리고 있는지 확인합니다.
    override fun onScrolled(view: RecyclerView, dx: Int, dy: Int) {
        var lastVisibleItemPosition = 0
        val totalItemCount = mLayoutManager?.itemCount ?: 0

        when (mLayoutManager) {
            is StaggeredGridLayoutManager -> {
                val lastVisibleItemPositions =
                    mLayoutManager.findLastVisibleItemPositions(null)
                // get maximum element within the list
                lastVisibleItemPosition = getLastVisibleItem(lastVisibleItemPositions)
            }
            is GridLayoutManager -> {
                lastVisibleItemPosition =
                    (mLayoutManager as GridLayoutManager).findLastVisibleItemPosition()
            }
            is LinearLayoutManager -> {
                lastVisibleItemPosition =
                    (mLayoutManager as LinearLayoutManager).findLastVisibleItemPosition()
            }
        }

        // 총 데이터 수가 0이고, 이전 데이터 수가 아닌 경우.
        // 리스트가 invalidate(무효화)되어 초기 상태로 재설정되어야 한다고 가정.
        // val totalItemCount = mLayoutManager.itemCount
        if (totalItemCount < previousTotalItemCount) {
            mCurrentPage = startingPageIndex
            previousTotalItemCount = totalItemCount
            if (totalItemCount == 0) {
                loading = true
            }
        }
        // 여전히 로드 중인 경우 데이터셋이 변경되었으면, 현재 페이지와 번호화 아이템 수를 로드.
        if (loading && totalItemCount > previousTotalItemCount) {
            loading = false
            previousTotalItemCount = totalItemCount
        }

        // 현재 로딩 중이 아닌 경우, visibleThresHold의 위반 여부와 데이터를 더 불러올 필요가 있는지 확인.
        // 더 많은 데이터를 로드해야 하는 경우, LoadMore 메소드로 데이터 로드.
        // visibleThresHold는 전체 열의 수를 반영해야 함.
        if (!loading && lastVisibleItemPosition + visibleThreshold > totalItemCount) {
            mCurrentPage++
            onLoadMore(mCurrentPage, totalItemCount, view)
            loading = true
        }

        Timber.d("[SCROLL]: currentPage:${mCurrentPage}, lastVisibleItemPosition = $lastVisibleItemPosition, loading = $loading, totalItemCount = $totalItemCount, previousTotalItemCount = $previousTotalItemCount");
    }

    // Call this method whenever performing new searches
    fun resetState() {
        mCurrentPage = startingPageIndex
        previousTotalItemCount = 0
        loading = true
    }

    // Defines the process for actually loading more data based on page
    abstract fun onLoadMore(page: Int, totalItemsCount: Int, view: RecyclerView?)
}