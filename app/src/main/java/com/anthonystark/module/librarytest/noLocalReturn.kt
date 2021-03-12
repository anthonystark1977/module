package com.anthonystark.module.librarytest

import timber.log.Timber

/**
 * [NON-LOCAL-RETURN]
 *  - 람다함수 내에서 람다함수를 포함하는 함수를 벗어나게하는 기법.
 *  - 코틀린 lamda에서 return을 사용할 수 없다.
 *  - return은 method나 익명함수(fun())에서만 사용가능하다.
 *  - inline 키워드를 붙여주면, 사용 가능하지만 호출함수까지 모두 exit 된다.
 *  - local-return 이 아니기에 [NON-LOCAL-RETURN]이라고 한다.
 *  - label(return@fun)을 사용하거나 fun()을 사용해서 local-return이 가능하다. (inline 없이도 그냥 라벨만 사용해서 리턴가능)
 *
 * @author Scarlett
 * @version 0.0.8
 * @since 2021-03-10 오후 8:00
 **/
data class Person(val name:String,val age:Int)

inline fun findPerson(find:() -> Unit){
    Timber.tag("nonLocalReturnTest").d("Start Function")
    find()
    Timber.tag("nonLocalReturnTest").d("End Function")
}

fun nonLocalReturnTest(){
    val people = listOf(Person("김진우",30),Person("박진희",30))
    findPerson {
        for(item in people){
            if(item.name == "김진우"){
                Timber.tag("nonLocalReturnTest").d("Find!!")
                return
            }
        }
        Timber.tag("nonLocalReturnTest").d("-------------------------")
    }
}