package com.anthonystark.module.librarytest

import com.attractive.deer.util.data.exhaustMap
import io.reactivex.rxjava3.core.BackpressureStrategy
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.schedulers.Schedulers
import io.reactivex.rxjava3.schedulers.TestScheduler
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.reactivestreams.Subscriber
import org.reactivestreams.Subscription
import timber.log.Timber
import java.util.*
import java.util.concurrent.TimeUnit


/**
 * [exhaustMap]
 *
 * - 이전 출력을 처리하는 동안 새 출력을 모두 취소하려는 경우.
 *
 *
 * @author Scarlett
 * @version 0.0.8
 * @since 2021-03-10 오후 8:14
 **/

/*
- 생산이 소비보다 두배 빠르므로, 점점 소비 작업이 뒤로 밀림.
- 만약 생산하는 양이 많다면, 생산이 먼저 수행되고, 소비는 나중에 몰려서 수행되므로 OutOfMemory가 발생할 수 있음.
* */
fun exhaustMapTest01() = runBlocking<Unit> {
    val observable = Observable.range(1, 10)
    observable.map { // 100ms 단위로 생성.
        val str = "Mapping Item $it"
        runBlocking { delay(100) }
        Timber.tag("exhaustMapTest").d("$str - ${Thread.currentThread().name}")
        str
    }.observeOn(Schedulers.computation())
        .subscribe { // 200ms 단위로 소비.
            run {
                Timber.tag("exhaustMapTest").d("Received $it - ${Thread.currentThread().name}")
                runBlocking { delay(200) }
            }
        }

}

/*
* - 생산이 128개가 되면 일단 멈춤.
* - 그리고 96개가 소비되는 순간 다시 생산이 시작되면서 생산 속도를 적절히 조정하는걸 확인할 수 있음.
* - 내부적으로 32개가 다시 생산 가능하도록 하는 버퍼의 threshold 값인듯.
* - Flowable은 생산속도를 조절 함으로써 memory에러를 방지 할 수 있지만 그만큼 느려짐.
* - Flowable은 생산의 갯수가 수천, 수많개 정도로 많을때 사용하고 그렇지 않은 경우 observable을 사용하는 것이 좋음.
* */
fun exhaustMapTest02() = runBlocking<Unit> {
    val flowable = Flowable.range(1, 150)
    flowable.map {
        val str = "Mapping Item $it"
        Timber.tag("exhaustMapTest").d("$str - ${Thread.currentThread().name}")
        str
    }.observeOn(Schedulers.computation())
        .subscribe {
            run {
                Timber.tag("exhaustMapTest").d("Received $it - ${Thread.currentThread().name}")
                runBlocking { delay(10) }
            }
        }
}

/*
* BackpressureStrategy.BUFFER
* - 무제한 버퍼를 제공 (기본값인 128개가 아님)
* - 따라서 생산하는 데이터 개수가 많고, 수신하여 처리하는 쪽이 너무 느릴 경우 OutOfMemory가 발생할 수 있음.
* - 생산속도는 10ms, 소비속도는 100ms 설정.
* - 결과를 보면 제한 없이 버퍼에 계속 쌓음
* - 중간중간에 데이터를 소비하다가, 데이터 생산이 끝나면 버퍼에 쌓인 데이터를 전부 일괄 처리하고 끝남.
* - 결론적으로 Flowable은 자신의 속도대로 모두 방출하고, 수신자 역시 자신의 처리 속도에 따라 모든 데이터를 처리.
*
*
* BackpressureStrategy.ERROR
* - 소비쪽에서 생산을 따라잡을 수 없는 경우에 ERROR 발생.
* - 단 기본 버퍼 128개 까지는 제공.
*
*
* BackpressureStrategy.DROP
* - 수신자가 처리중에 생산자로부터 전달 받으면 해당 데이터는 버려진다.
* - 개념상으로 10개를 생성하더라도, 1개를 수신중이라면 9개는 버려지는 개념이나, 여기에도 버퍼(128개)가 존재.
* - 128개까지는 버퍼에 쌓여있어 순차적으로 수신자가 처리하나, 버퍼에 담기지 못하는 129개째부터 버려짐.
* - 수신자의 동작을 기준으로 데이터를 처리하고, 처리중에 생산된 데이터는 버려지며, 수신 처리가 끝나면 그 다음으로 수신하는 데이터를 처리.
*
* BackpressureStrategy.LATEST
* - DROP과 유사하게 동작하나 마지막 값인 200을 전달 받아 처리함.
*
*
* BackpressureStrategy.MISSING
* - backpressure을 사용하지 않겠다는 의미.
* */
fun exhaustMapTest03() = runBlocking<Unit> {
    val flowable = Flowable.create<Int>({
        for (i in 1..200) {
            Timber.tag("exhaustMapTest").d("Send Item $it - ${Thread.currentThread().name}")
            it.onNext(i)
            runBlocking { delay(10) }
        }
        it.onComplete()
    }, BackpressureStrategy.DROP).subscribeOn(Schedulers.io())

    // runBlocking을 유지하기 위해 observing이 끝날때까지 대기하기 위해서 사용한다.
    val waitingJob = launch{
        delay(Long.MAX_VALUE)
    }

    flowable.observeOn(Schedulers.computation())
        .subscribe(MySubscriber01(waitingJob))
}


fun exhaustMapTest() = runBlocking<Unit> {
    val scheduler = TestScheduler()
    val observable = Observable.create<Int>{
        for (i in 1..500) {
            Timber.tag("exhaustMapTest").d("Send Item $i - ${Thread.currentThread().name}")
            it.onNext(i)
            runBlocking { delay(10) }
        }
        it.onComplete()
    }
    observable.exhaustMap { s ->
            // val delay = Random().nextInt(10).toLong()
            Observable.just(s.toString() + "x").delay(100, TimeUnit.MILLISECONDS)
        }.subscribe{
            Timber.tag("exhaustMapTest").d("######## Receivce Item $it - ${Thread.currentThread().name}")
            runBlocking { delay(200) }
        }

    scheduler.advanceTimeBy(1, TimeUnit.MINUTES)

}


class MySubscriber01(val job: Job) : Subscriber<Int> {
    override fun onSubscribe(s: Subscription?) {
        s?.request(200)
    }

    override fun onNext(t: Int?) {
        runBlocking { delay(100) }
        Timber.tag("exhaustMapTest").d("Received $t - ${Thread.currentThread().name}")
    }

    override fun onError(t: Throwable?) {
        Timber.tag("exhaustMapTest").d("onError() $t")
        job.cancel()
    }

    override fun onComplete() {
        Timber.tag("exhaustMapTest").d("onComplete()")
        job.cancel()
    }
}