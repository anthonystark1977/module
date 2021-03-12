package com.attractive.deer.util.data

import androidx.lifecycle.LiveData

/**
 * Live data that does not allow null.
 * @author Scarlett
 * @version 1.0.0
 * @since 2021-03-12 오후 2:33
 **/
open class NotNullLiveData<T : Any>(value: T) : LiveData<T>(value) {
    override fun getValue(): T = super.getValue()!!

    @Suppress("RedundantOverride")
    override fun setValue(value: T) = super.setValue(value)

    @Suppress("RedundantOverride")
    override fun postValue(value: T) = super.postValue(value)
}

/**
 * Live data changed by a value that does not allow null.
 * @author Scarlett
 * @version 1.0.0
 * @since 2021-03-12 오후 2:33
 **/
class NotNullMutableLiveData<T : Any>(value: T) : NotNullLiveData<T>(value) {
    public override fun setValue(value: T) = super.setValue(value)

    public override fun postValue(value: T) = super.postValue(value)
}