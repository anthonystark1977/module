package com.attractive.deer.base

import androidx.annotation.CheckResult
import io.reactivex.rxjava3.core.Observable

interface MviView<I : MviIntent, S : MviViewState, E : MviSingleEvent> {

    /**
     * Rendering all current screen status data values.
     *
     * @author Scarlett
     * @version 1.0.0
     * @since 2021-03-12 오후 2:35
     **/
    fun render(viewState: S)

    /**
     * Process all singleEvent.
     *
     * @author Scarlett
     * @version 1.0.0
     * @since 2021-03-12 오후 2:36
     **/
    fun handleEvent(event: E)

    /**
     * Save all allowed intents on the screen.
     * @author Scarlett
     * @version 1.0.0
     * @since 2021-03-12 오후 2:37
     **/
    @CheckResult
    fun viewIntents(): Observable<I>
}