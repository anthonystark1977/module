package com.anthonystark.module.step.rx

sealed class RxTestIntent{
    data class TestClick(val data:String):RxTestIntent()
    data class TestClick1(val data:String):RxTestIntent()
    data class TestClick2(val data:String):RxTestIntent()
    data class TestClick3(val data:String):RxTestIntent()
    data class TestClick4(val data:String):RxTestIntent()
}