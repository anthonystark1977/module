package com.attractive.deer.base


import androidx.annotation.CallSuper
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

import com.attractive.deer.util.data.Event
import com.attractive.deer.util.data.NotNullLiveData
import com.attractive.deer.util.data.NotNullMutableLiveData

import io.reactivex.rxjava3.disposables.CompositeDisposable
import timber.log.Timber

/**
 * 1) AAC ViewModel().
 *
 *
 * @property MviIntent: Any action that can be performed in app.
 * @property MviViewState: The status of the current screen.
 * @property MviSingleEvent: Toast,Snack,Dialog ...
 *
 * @param intialState: The earliest state of the screen.
 *
 * @author Scarlett
 * @version 1.0.0
 * @since 2021-03-12 오후 1:58
 **/
abstract class BaseViewModel<I : MviIntent, S : MviViewState, E : MviSingleEvent>(
    val intialState: S
) : ViewModel(), MviViewModel<I, S, E> {
    val compositeDisposable = CompositeDisposable()


    /**
     * stateD -> [state]
     *
     * change the current state.
     * Initial state is initialState
     *
     * @author Scarlett
     * @version 1.0.0
     * @since 2021-03-12 오후 2:18
     **/
    private val stateD = NotNullMutableLiveData(intialState)
    override val state: NotNullLiveData<S> get() = stateD

    /**
     * Toast,Snack,Dialog ...
     *
     * @author Scarlett
     * @version 1.0.0
     * @since 2021-03-12 오후 2:20
     **/
    private val singleEventD = MutableLiveData<Event<E>>()
    override val singleEvent: LiveData<Event<E>> get() = singleEventD

    init {
        Timber.d("$this::init")
    }

    /**
     * If the current state and parameter values are not the same, update the new state.
     *
     * @author Scarlett
     * @version 1.0.0
     * @since 2021-03-12 오후 2:23
     **/
    fun setNewState(state: S) {
        if (state != stateD.value) {
            stateD.value = state
        }
    }

    /**
     * Send a dialogue or a message.
     *
     * @author Scarlett
     * @version 1.0.0
     * @since 2021-03-12 오후 2:24
     **/
    fun sendEvent(event: E) {
        singleEventD.value = Event(event)
    }

    /**
     * Dispose of all subscriptions.
     *
     * @author Scarlett
     * @version 1.0.0
     * @since 2021-03-12 오후 2:24
     **/
    @CallSuper
    override fun onCleared() {
        Timber.d("$this::onCleared")
        compositeDisposable.dispose()
    }
}