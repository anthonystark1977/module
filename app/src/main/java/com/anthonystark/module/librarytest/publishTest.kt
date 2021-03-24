package com.anthonystark.module.librarytest

import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.ObservableEmitter
import timber.log.Timber
import java.lang.Thread.sleep
import java.util.concurrent.TimeUnit

/**
 * [publish]
 *
 * - Hot Observable을 만드는 방법은 Cold Observable을 변환하는 것.
 * - publish를 사용하면 ConnectableObservable로 변환이 된다..
 * - 원래 Cold Observable은 항상 1,2,3이 차례로 발행되는데, Hot으로 바꾸면 동시에 발행된다.
 * - subscribe 이전의 데이터는 받을 수 없다.
 * - share은 publish().refCount()와 같은 의미이며, 더이상 배출할 Observer가 없을 시 자동으로 자신을 해지(dispose)하고 다시 새로운 Observer오면 자동으로 시작.
 * @author Scarlett
 * @version 1.0.0
 * @since 2021-03-14 오후 1:41
 **/
fun publishTest() {
    val threeRandoms = Observable.interval(1, TimeUnit.SECONDS)
        .publish()
        .refCount()

    threeRandoms.take(5).subscribe { i: Long ->
        Timber.tag("publishTest").d("#1 Send Item $i - ${Thread.currentThread().name}")
    }
    sleep(3000) // 3초동안 잠자기

    threeRandoms.take(2).subscribe { i: Long ->
        Timber.tag("publishTest").d("#2 Send Item $i - ${Thread.currentThread().name}")
    }

    sleep(3000) // 3초동안 잠자기

    threeRandoms.subscribe { i: Long ->
        Timber.tag("publishTest").d("#3 Send Item $i - ${Thread.currentThread().name}")
    }

    sleep(3000) // 3초동안 잠자기

}

fun shareTest01() {
    var emitter:ObservableEmitter<String>? = null
    val srcObservable = Observable.create<String> { emitter = it }.share()

    srcObservable.subscribe{ Timber.tag("publishTest").d("#1 $it") }
    emitter?.onNext("String 1")
    srcObservable.subscribe{ Timber.tag("publishTest").d("#2 $it") }
    emitter?.onNext("String 2")
}

fun shareTest02() {

    val observable = Observable.just("1").share()
    observable.subscribe{ Timber.tag("publishTest").d("#1 $it") }
    observable.subscribe{ Timber.tag("publishTest").d("#2 $it") }
}