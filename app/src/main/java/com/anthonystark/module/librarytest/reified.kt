package com.anthonystark.module.librarytest

/**
 * [reified]
 *  - 일반적인 제네릭 함수 body에서는 타입 T는 컴파일 타임에 존재하지만, 런타임에는 Type Erasure 때문에 접근할 수 없다.
 *  - 따라서 런타임에 제네릭 함수에 접근하려면, 명시적으로 fun genericFunc(c:Class)와 같이 타입을 파라미터로 전달해주어야한다.
 *  - 하지만 reified와 함께 inline 함수를 추가적으로 만들면, class를 파라미터로 넘겨줄 필요가 없다.
 *  - 따라서 myVar is T처럼 변수가 T인스턴스인지 쉽게 겁사할 수 있다.
 *
 *
 * [Reflection]
 *  - 런타임 시 자신의 프로그램 구조를 조사할 수 있도록 허용하는 언어와 라이브러리의 집합.
 *  - 가장 일반적인 긴으은 val myClass = MyClass::class 와 같이 런타임 참조 클래스를 가져오는 것. (KClass)
 *  - 함수를 참조하는 방법은 예를 들어 ::isOdd = (Int) -> Boolean 식으로 참조.
 *  - val predicate:(Int)->Boolean ==> ::isOdd 로 참조 될 수도 있다.
 * @author Scarlett
 * @version 0.0.8
 * @since 2021-03-11 오전 5:51
 **/

interface TestInterface {
    fun getMethod(): Boolean
}


fun reflectionTest() {
    val testInterface = object : TestInterface {
        override fun getMethod(): Boolean {
            return true
        }
    }
    var isEmptyStringList: TestInterface.() -> Boolean = TestInterface::getMethod
    testInterface.isEmptyStringList()
}


fun <T> noReifiedFunc(clazz: Class<T>) {
    val test = 3
    if (clazz.isInstance(test)) { }
}

inline fun <reified T> reifiedFunc() {
    val test = 4
    if (test is T) { }
}