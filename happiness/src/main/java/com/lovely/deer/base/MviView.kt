package com.lovely.deer.base
import androidx.annotation.CheckResult
import io.reactivex.rxjava3.core.Observable

interface MviView<I : MviIntent, S : MviViewState, E : MviSingleEvent> {
    fun render(viewState: S)
    fun handleEvent(event: E)
    @CheckResult fun viewIntents(): Observable<I>
}