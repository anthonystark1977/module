package com.attractive.deer.util.data

import androidx.collection.LruCache
import kotlin.time.Duration
import kotlin.time.ExperimentalTime
import kotlin.time.TimeMark
import kotlin.time.TimeSource

/**
 * Least Recently Used (LRU)
 *
 * - The data structure that removes the oldest referenced object from a limited size.
 * - ex) cacheSize = 4 * 1024 * 1024 = 4MB, Bitmap.
 * - it would be possible to cache four for 1 MB of Bitmap and two for 2 MB of Bitmap.
 *
 * - because of the use of Optin, API usage is not required.
 *   Any usage of a declaration annotated with `@ExperimentalTime` must be accepted either by
 *  annotating that usage with the [OptIn] annotation, e.g. `@OptIn(ExperimentalTime::class)`,
 *  or by using the compiler argument `-Xopt-in=kotlin.time.ExperimentalTime`.
 *
 * @param maxSize: cacheSize
 * @param entryLifetime: cache usage time.
 *
 * @author Scarlett
 * @version 0.0.8
 * @since 2021-03-08 오전 10:56
 **/
@OptIn(ExperimentalTime::class)
class Cache<K : Any, V : Any>(maxSize: Int, private val entryLifetime: Duration) {

    val timeSource = TimeSource.Monotonic
    val cache = LruCache<K, Value<V>>(maxSize)

    /**
     * Only one Thread can execute synchronized instance methods.
     * The conclusion is that only one thread per instance can be accessed if a method using synchronized exists.
     * To put it simply, using synchronized in a method means that the object containing the function is locked.
     *
     * @author Scarlett
     * @version 0.0.8
     * @since 2021-03-08 오전 11:09
     **/
    @Synchronized
    operator fun get(key: K): V? {
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


data class RequestCacheKey(
    val path: String,
    val queryItems: Map<String, String>,
)

fun buildKey(path: String, queryItems: Map<String, Any?>): RequestCacheKey {
    return RequestCacheKey(
        path = path,
        queryItems = queryItems.entries
            .mapNotNull { (k, v) ->
                if (v == null) null
                else k to v.toString()
            }
            .toMap()
    )
}
