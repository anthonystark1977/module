package com.anthonystark.module.librarytest

import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.schedulers.Schedulers


/**
 * [exhaustMap]
 * @author 권혁신
 * @version 0.0.8
 * @since 2021-03-10 오후 8:14
 **/

fun exhaustMapTest() {

    val foo1 = Flowable.range(0, 1000000000)
        .map { x ->
            println("[very fast sender] i'm fast. very fast.")
        }

    val foo2 = Observable.range(0, 1000000000)
        .map { x ->
            println("[very fast sender] i'm fast. very fast.")
        }

    foo2.observeOn(Schedulers.computation()).subscribe { x ->
        Thread.sleep(1000)
        println("[very busy receiver] i'm busy. very busy.")
        println(
            java.lang.String.format(
                "receiving id: %s %50.50s",
                Thread.currentThread().name,
                x
            )
        )
    }

    while (true) {
        Thread.sleep(1000)
    }
}