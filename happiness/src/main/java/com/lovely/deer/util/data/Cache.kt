package com.lovely.deer.util.data

import androidx.collection.LruCache
import kotlin.time.Duration
import kotlin.time.ExperimentalTime
import kotlin.time.TimeMark
import kotlin.time.TimeSource

@OptIn(ExperimentalTime::class)
class Cache<K : Any, V : Any>(maxSize: Int, private val entryLifetime: Duration) {
   val timeSource = TimeSource.Monotonic
   val cache = LruCache<K, Value<V>>(maxSize)

  @Synchronized operator fun get(key: K): V? {
    val value = cache[key] ?: return null
    return if (value.expiredTime.hasPassedNow()) {
      remove(key)
      null
    } else {
      value.value
    }
  }

  fun remove(key: K) {
    cache.remove(key)
  }

  operator fun set(key: K, value: V) {
    cache.put(
      key,
      Value(
        value,
        timeSource.markNow() + entryLifetime
      )
    )
  }

  data class Value<V : Any>(
    val value: V,
    val expiredTime: TimeMark,
  )
}