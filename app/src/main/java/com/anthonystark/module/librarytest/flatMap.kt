package com.anthonystark.module.librarytest

import com.anthonystark.module.DomainError
import com.anthonystark.module.DomainResult
import com.anthonystark.module.SuccessResponse
import com.attractive.deer.util.data.*
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.schedulers.Schedulers
import timber.log.Timber


/**
 * [flatMap]
 *
 * flatMap error시 밖으로 빠져나감.
 * map도 마찬가지 단순히 flatMap에서 right() 파싱해준 것.
 * @author Scarlett
 * @version 0.0.8
 * @since 2021-03-08 오전 11:54
 **/
fun flatMapTest01() {
    val result: DomainResult<SuccessResponse> = SuccessResponse("Success Message").right()
    result.flatMap {
        val inlineResult = DomainError(404).left()
        inlineResult.map {
            SuccessResponse(
                "inline Success!!"
            )
        }
    }.fold({
        it
    }, {
        it
    }).let {

    }
}


/**
 * [flatMap]
 *
 * - map과의 차이점은 return 값이 Observable이라는 것이 가장 중요하다.
 * - map은 하나의 item성분에 의해 하나의 변형된 item만 내놓을 수 있지만,
 * - 실제 flatmap()함수를 보면 map()을 먼저한 다음 merge로 감싸주어서 Observable을 리턴한다.
 * - 최종 Observable을 만들어서 가공하는 것.
 * - 구독에 쓰이는 스레드 수 = maxConcurrency
 * @author Scarlett
 * @version 1.0.0
 * @since 2021-03-14 오전 11:36
 **/
fun testFlatMap() {
    Observable.range(0, Int.MAX_VALUE)
        .map { i: Int -> "lineOfData-$i" } // simulate the file with many lines
        .buffer(100) // buffer up chunks of work before processing them
        .flatMap({ chunk: List<String> ->
            Observable.just(chunk)
                .subscribeOn(Schedulers.io()) // put each group on a new thread (io scheduler for blocking IO)
                .doOnNext { t: List<String> ->
                    // do work here
                    try {
                        Thread.sleep(1000)
                    } catch (e: Exception) {
                    }
                    // write out on blocking IO as a side-effect
                    println("Emitting to IO: $t")
                }
        }, 3).forEach {
            Timber.tag("testFlatMap").d("Send Item $it - ${Thread.currentThread().name}")
        }
}
