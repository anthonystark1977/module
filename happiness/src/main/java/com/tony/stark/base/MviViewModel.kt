package com.tony.stark.base
import androidx.annotation.CheckResult
import androidx.lifecycle.LiveData

import com.tony.stark.util.Event
import com.tony.stark.util.NotNullLiveData

import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.disposables.Disposable

interface MviViewModel<I : MviIntent, S : MviViewState, E : MviSingleEvent> {
    val state: NotNullLiveData<S>
    val singleEvent: LiveData<Event<E>>
    @CheckResult fun processIntents(intents: Observable<I>): Disposable
}