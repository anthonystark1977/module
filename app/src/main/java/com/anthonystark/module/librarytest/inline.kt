package com.anthonystark.module.librarytest

import android.view.View
import timber.log.Timber

/**
 * [inline]
 *
 * 1) 고차 함수를 사용한다면, 런타임 패널티가 있기 때문에 함수 구현 자체를 코드에 넣음으로써 오버헤드를 없엘 수 있다.
 * ==> 여기서 런타임 오버헤드란 A를 처리하는데 원래 10초가 걸리는데, 중간과정(안전성,부가 처리B)이 있어 15초가 걸렸다면, 오버헤드는 5초이다.
 * ==> 고차 함수는 자바 코드로 변환이 될때, 내부적 객체 생성과 함께 호출하게 되어있어서, 오버헤드가 걸린다.
 * ==> 이러한 문제를 inline이 해결해준다.
 *
 * 2) inline 함수를 사용하게 된다면, 코드가 컴파일될 때 코드를 복사한다. 바이트코드의 양은 많아지겠지만, 객체 생성은 없다.
 * ==> 일반함수보다 성능이 좋다.
 * ==> 하지만 내부적으로 코드를 복사하기때문에 인자로 전달받은 함수는 다른 함수로 전달되거나 참조 할 수 없다.
 * ==> 모든 인자를 inline으로 처리하고 싶지 않을 때 noninline을 사용한다.(noninline은 오버헤드가 발생)
 *
 * 3) inline 함수는 인자로 받은 함수를 다른 실행 컨텍스트를 통해 호출할 때는 함수 안에서 비-로컬 흐름(non-local-return)을 제어할 수 없다.
 * ==> 이럴 경우 사용하는 것이 crossinline이다.
 * ==> 쉽게 말해서 crossinline을 해주지 않으면 함수 inline함수를 호출한 함수가 return 된다.
 * ==> 하지만 crossinline을 하면 return 키워드를 방지할 수 있다.
 * ==> 아래예제에서 shortFunc 함수의 끝까지 가야함. 중간에 끊어질 수 있음.
 *
 * @author Scarlett
 * @version 0.0.8
 * @since 2021-03-10 오후 8:13
 **/


inline fun crossinlined(crossinline body: () -> Unit) {
    doSometing{
        body()
    }
}
fun doSometing(something : () -> Unit) {
    something()
}

inline fun View.click(crossinline block: (View) -> Unit) {
    setOnClickListener { view ->
        block(view)
    }
}

inline fun shortFunc(a: Int, crossinline out: (Int) -> Unit) {
    Timber.tag("crossinlineTest").d("Before calling out()")
    test02{
        out(a)
    }
    Timber.tag("crossinlineTest").d("After calling out()")
}

fun test02(t:()->Unit){
    t()
}

inline fun test01(crossinline t:()->Unit){
    test02{
        t()
    }
}

fun crossinlineTest() {
    shortFunc(5) {
        // out의 바디.
        Timber.tag("crossinlineTest").d("First Call: $it")
    }
}
