package com.attractive.deer.util.data

/**
 * Android SingleEvent
 * - MessageEvent.
 * - Toast,Snackbar...
 * @author Scarlett
 * @version 0.0.8
 * @since 2021-03-08 오후 1:00
 **/
open class Event<out T>(private val content: T) {

    /**
     * Read is allowed but not written.
     * @author Scarlett
     * @version 0.0.8
     * @since 2021-03-08 오후 1:03
     **/
    var hasBeenHandled = false
        private set

    /**
    * Process unprocessed events and return the contents.
    * @author Scarlett
    * @version 0.0.8
    * @since 2021-03-08 오후 1:04
    **/
    fun getContentIfNotHandled(): T? {
        return if (hasBeenHandled) {
            null
        } else {
            hasBeenHandled = true
            content
        }
    }

    fun peekContent(): T = content
}