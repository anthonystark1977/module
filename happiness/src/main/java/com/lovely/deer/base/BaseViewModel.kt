package com.lovely.deer.base



import androidx.annotation.CallSuper
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

import com.lovely.deer.util.data.Event
import com.lovely.deer.util.data.NotNullLiveData
import com.lovely.deer.util.data.NotNullMutableLiveData

import io.reactivex.rxjava3.disposables.CompositeDisposable
import timber.log.Timber

abstract class BaseViewModel<I : MviIntent, S : MviViewState, E : MviSingleEvent>(
    val intialState: S
) : ViewModel(), MviViewModel<I, S, E> {
    val compositeDisposable = CompositeDisposable()

    private val stateD = NotNullMutableLiveData(intialState)
    override val state: NotNullLiveData<S> get() = stateD

    private val singleEventD = MutableLiveData<Event<E>>()
    override val singleEvent: LiveData<Event<E>> get() = singleEventD

    init {
        Timber.d("$this::init")
    }

    fun setNewState(state: S) {
        if (state != stateD.value) {
            stateD.value = state
        }
    }

    fun sendEvent(event: E) {
        singleEventD.value = Event(event)
    }

    @CallSuper
    override fun onCleared() {
        Timber.d("$this::onCleared")
        compositeDisposable.dispose()
    }
}