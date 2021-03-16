package com.anthonystark.module.librarytest

import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.schedulers.TestScheduler
import timber.log.Timber
import java.util.*
import java.util.concurrent.TimeUnit



/**
 * [switchMap]
 *
 * - 순서를 보장하기 위해 기존의 수행중이던 작업을 바로 중단한다.
 * - 여러개의 값이 발행되었을 때, 마지막에 들어온 값만 처리하고 싶을 때 사용.
 * - 중간에 끊기더라도 마지막 데이터의 처리는 보장하기 떄문.
 * @author Scarlett
 * @version 1.0.0
 * @since 2021-03-14 오후 12:44
 **/
fun switchMapTest() {
    val items: List<String> = arrayListOf("a", "b", "c", "d", "e", "f")
    val scheduler = TestScheduler()

    Observable.interval(1,TimeUnit.SECONDS)
        .switchMap { s ->
            val delay = Random().nextInt(10).toLong()
            Observable.just(s.toString() + "x").delay(delay,TimeUnit.SECONDS)
        }.doOnNext {
            Timber.tag("switchMapTest").d("Send Item $it - ${Thread.currentThread().name}")
        }
        .subscribe()

    scheduler.advanceTimeBy(1, TimeUnit.MINUTES)
}