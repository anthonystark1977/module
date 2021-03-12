package com.attractive.deer.base

import androidx.annotation.CheckResult
import androidx.lifecycle.LiveData

import com.attractive.deer.util.data.Event
import com.attractive.deer.util.data.NotNullLiveData

import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.disposables.Disposable

/**
 * MviViewModel
 *
 * @property I: Any action that can be performed in app.
 * @property S: The status of the current screen.
 * @property E: Toast,Snack,Dialog ...
 * @author Scarlett
 * @version 1.0.0
 * @since 2021-03-12 오후 2:25
 **/
interface MviViewModel<I : MviIntent, S : MviViewState, E : MviSingleEvent> {
    val state: NotNullLiveData<S>
    val singleEvent: LiveData<Event<E>>

    /**
     * To subscribe to all intents.
     * @author Scarlett
     * @version 1.0.0
     * @since 2021-03-12 오후 2:27
     **/
    @CheckResult
    fun processIntents(intents: Observable<I>): Disposable
}