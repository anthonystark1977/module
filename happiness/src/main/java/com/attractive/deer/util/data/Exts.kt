@file:Suppress("SpellCheckingInspection", "unused")

package com.attractive.deer.util.data

import android.annotation.SuppressLint
import android.content.ContentResolver
import android.content.Context
import android.content.res.Configuration.ORIENTATION_PORTRAIT
import android.net.Uri
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.view.animation.Interpolator
import android.widget.EditText
import android.widget.Toast
import androidx.annotation.*
import androidx.core.content.ContextCompat
import androidx.core.content.res.use
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.BaseTransientBottomBar
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.QuerySnapshot
import com.jakewharton.rxbinding4.InitialValueObservable
import com.jakewharton.rxrelay3.Relay
import com.jaredrummler.materialspinner.MaterialSpinner
import com.miguelcatalan.materialsearchview.MaterialSearchView
import io.reactivex.rxjava3.android.MainThreadDisposable
import io.reactivex.rxjava3.android.MainThreadDisposable.verifyMainThread
import io.reactivex.rxjava3.annotations.SchedulerSupport
import io.reactivex.rxjava3.core.BackpressureStrategy
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.ObservableEmitter
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.kotlin.ofType
import io.reactivex.rxjava3.subjects.Subject
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.onStart
import timber.log.Timber
import java.io.File
import java.io.IOException
import java.io.InputStream
import kotlin.math.roundToInt
import androidx.lifecycle.Observer as LiveDataObserver

/**
 * Refied can only be used in combination with inline function. These functions allow the compiler to copy the function's byte code wherever the function is used.
 * When an inline function is called along with the refied type, the compiler knows the actual type used as an argument(T) and replaces the byte code created with direct response to the class.
 *
 * @author Scarlett
 * @version 0.0.8
 * @since 2021-03-08 오후 1:06
 **/
@CheckResult
@SchedulerSupport(SchedulerSupport.NONE)
inline fun <reified U : Any, T : Any> Observable<T>.notOfType() = filter { it !is U }!!

@Suppress("nothing_to_inline")
inline fun <T : Any> Relay<T>.asObservable(): Observable<T> = this

@Suppress("nothing_to_inline")
inline fun <T : Any> Subject<T>.asObservable(): Observable<T> = this

/**
 * If a consumer fails to keep up with the producer's speed, the following situation occurs
 *
 * 1. busy waiting.
 * 2. out of memory exception.
 *
 * When using Flowable, control flow when the element is stacked in the default buffer size (128) or higher. *default = 16.
 *
 * - BackpressureStrategy.BUFFER: it provides unlimited buffers. Therefore, there is a lot of data that is produced and OOM can occur if consumption is slow.
 * - BackpressureStrategy.ERROR: generate an error when the consumer cannot keep up with production.
 * - BackpressureStrategy.DROP: When the recipient receives the data from the producer during processing, the data is discarded.
For example, even if 10 are created, 9 are discarded if one is received. Up to 128 buffers are stacked in the buffer, which is processed sequentially by the receiver,
but since it is produced during receiving from the 129th, it is discarded.
 * - BackpressureStrategy.LATEST: Similar to Drop, data received during incoming processing is ignored. However, we keep the final value by saving the last ejected value when ignoring it.
When the receive side is processed, one of the ignored values is passed and subsequently discharged.
 * - BackpressureStrategy.MISSING: The Missing value basically means that you do not want to use the backpressure provided by Flowable
 *
 * @author Scarlett
 * @version 0.0.8
 * @since 2021-03-08 오후 1:09
 **/
@CheckResult
inline fun <T : Any, R : Any> Observable<T>.exhaustMap(crossinline transform: (T) -> Observable<R>): Observable<R> {
    return this
        .toFlowable(BackpressureStrategy.DROP)
        .flatMap({ transform(it).toFlowable(BackpressureStrategy.MISSING) }, 1)
        .toObservable()
}


/**
 *  The Observable value is an optional value after parsing (NULL check), and then returns the value.
 * @author Scarlett
 * @version 0.0.8
 * @since 2021-03-08 오후 1:37
 **/
@CheckResult
inline fun <T : Any, R : Any> Observable<T>.mapNotNull(crossinline transform: (T) -> R?): Observable<R> {
    return map { transform(it).toOptional() }
        .ofType<Some<R>>()
        .map { it.value }
}

@Suppress("nothing_to_inline")
inline infix fun ViewGroup.inflate(layoutRes: Int) =
    LayoutInflater.from(context).inflate(layoutRes, this, false)!!

val Context.isOrientationPortrait get() = this.resources.configuration.orientation == ORIENTATION_PORTRAIT

@Suppress("nothing_to_inline")
@ColorInt
inline fun Context.getColorBy(@ColorRes id: Int) = ContextCompat.getColor(this, id)

@Suppress("nothing_to_inline")
inline fun Context.getDrawableBy(@DrawableRes id: Int) = ContextCompat.getDrawable(this, id)

/**
 * Get uri from any resource type
 * @receiver Context
 * @param resId - Resource id
 * @return - Uri to resource by given id or null
 */
fun Context.uriFromResourceId(@AnyRes resId: Int): Uri? {
    return runCatching {
        val res = this@uriFromResourceId.resources
        Uri.parse(
            ContentResolver.SCHEME_ANDROID_RESOURCE +
                    "://" + res.getResourcePackageName(resId)
                    + '/' + res.getResourceTypeName(resId)
                    + '/' + res.getResourceEntryName(resId)
        )
    }.getOrNull()
}

fun Context.dpToPx(dp: Int): Int {
    val displayMetrics = resources.displayMetrics
    return (dp * (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT)).roundToInt()
}

@Suppress("nothing_to_inline")
inline fun Context.toast(
    @StringRes messageRes: Int,
    short: Boolean = true,
) = this.toast(getString(messageRes), short)

@Suppress("nothing_to_inline")
inline fun Context.toast(
    message: String,
    short: Boolean = true,
) =
    Toast.makeText(
        this,
        message,
        if (short) Toast.LENGTH_SHORT else Toast.LENGTH_LONG
    ).apply { show() }!!


enum class SnackbarLength {
    SHORT {
        override val length = Snackbar.LENGTH_SHORT
    },
    LONG {
        override val length = Snackbar.LENGTH_LONG
    },
    INDEFINITE {
        override val length = Snackbar.LENGTH_INDEFINITE
    };

    abstract val length: Int
}

@SuppressLint("Recycle")
fun Context.themeInterpolator(@AttrRes attr: Int): Interpolator {
    return AnimationUtils.loadInterpolator(
        this,
        obtainStyledAttributes(intArrayOf(attr)).use {
            it.getResourceId(0, android.R.interpolator.fast_out_slow_in)
        }
    )
}


inline fun View.snack(
    @StringRes messageRes: Int,
    length: SnackbarLength = SnackbarLength.SHORT,
    crossinline f: Snackbar.() -> Unit = {},
) = snack(resources.getString(messageRes), length, f)

inline fun View.snack(
    message: String,
    length: SnackbarLength = SnackbarLength.SHORT,
    crossinline f: Snackbar.() -> Unit = {},
) = Snackbar.make(this, message, length.length).apply {
    f()
    show()
}

fun Snackbar.action(
    @StringRes actionRes: Int,
    color: Int? = null,
    listener: (View) -> Unit,
) = action(view.resources.getString(actionRes), color, listener)

fun Snackbar.action(
    action: String,
    color: Int? = null,
    listener: (View) -> Unit,
) = apply {
    setAction(action, listener)
    color?.let { setActionTextColor(color) }
}


fun Snackbar.onDismissed(f: () -> Unit) {
    addCallback(object : BaseTransientBottomBar.BaseCallback<Snackbar?>() {
        override fun onDismissed(transientBottomBar: Snackbar?, event: Int) {
            super.onDismissed(transientBottomBar, event)
            f()
            removeCallback(this)
        }
    })
}

/**
 * Register Observer.
 * @author Scarlett
 * @version 0.0.8
 * @since 2021-03-08 오후 1:44
 **/
inline fun <T : Any> NotNullLiveData<T>.observe(
    owner: LifecycleOwner,
    crossinline observer: (T) -> Unit,
) = Observer { value: T -> observer(value) }
    .also { observe(owner, it) }

fun <T : Any> LiveData<T>.toObservable(fallbackNullValue: (() -> T)? = null): Observable<T> {
    return Observable.create { emitter: ObservableEmitter<T> ->
        verifyMainThread()

        val observer = LiveDataObserver<T> { value: T? ->
            if (!emitter.isDisposed) {
                val notnullValue: T =
                    value ?: fallbackNullValue?.invoke() ?: return@LiveDataObserver
                emitter.onNext(notnullValue)
            }
        }
        observeForever(observer)

        emitter.setDisposable(object : MainThreadDisposable() {
            override fun onDispose() {
                removeObserver(observer)
            }
        })
    }
}

/**
 * Register Event Observer.
 * @author Scarlett
 * @version 0.0.8
 * @since 2021-03-08 오후 1:45
 **/
inline fun <T : Any> LiveData<Event<T>>.observeEvent(
    owner: LifecycleOwner,
    crossinline observer: (T) -> Unit,
) = Observer { event: Event<T>? ->
    event?.getContentIfNotHandled()?.let(observer)
}.also { observe(owner, it) }

typealias RxObserver<T> = io.reactivex.rxjava3.core.Observer<T>

private fun checkMainThread(observer: RxObserver<*>): Boolean {
    if (Looper.myLooper() != Looper.getMainLooper()) {
        observer.onSubscribe(Disposable.empty())
        observer.onError(
            IllegalStateException(
                "Expected to be called on the main thread but was ${Thread.currentThread().name}"
            )
        )
        return false
    }
    return true
}

@CheckResult
fun MaterialSearchView.textChanges(): Observable<String> {
    return MaterialSearchViewObservable(this)
}

@CheckResult
fun <T : Any> MaterialSpinner.itemSelections(): InitialValueObservable<T> {
    return MaterialSpinnerSelectionObservable(this)
}
/**
 * MaterialSpinnerSelectionObservable
 *
 * @author Scarlett
 * @version 0.0.8
 * @since 2021-03-08 오후 1:58
 **/
internal class MaterialSpinnerSelectionObservable<T : Any>(private val view: MaterialSpinner) :
    InitialValueObservable<T>() {
    override val initialValue get() = view.getItems<T>()[view.selectedIndex]!!

    override fun subscribeListener(observer: RxObserver<in T>) {
        if (!checkMainThread(observer)) {
            return
        }
        Listener(view, observer).let { listener ->
            view.setOnItemSelectedListener(listener)
            observer.onSubscribe(listener)
        }
    }

    private class Listener<T : Any>(
        private val view: MaterialSpinner,
        private val observer: RxObserver<in T>,
    ) : MaterialSpinner.OnItemSelectedListener<T>, MainThreadDisposable() {
        override fun onDispose() = view.setOnItemSelectedListener(null)

        override fun onItemSelected(view: MaterialSpinner?, position: Int, id: Long, item: T) {
            if (!isDisposed) {
                observer.onNext(item)
            }
        }
    }
}

/**
 * MaterialSearchViewObservable
 *
 * @author Scarlett
 * @version 0.0.8
 * @since 2021-03-08 오후 1:58
 **/
internal class MaterialSearchViewObservable(private val view: MaterialSearchView) :
    Observable<String>() {
    override fun subscribeActual(observer: RxObserver<in String>) {
        if (!checkMainThread(observer)) {
            return
        }
        Listener(view, observer).let { listener ->
            observer.onSubscribe(listener)
            view.setOnQueryTextListener(listener)
        }
    }

    private class Listener(
        private val view: MaterialSearchView,
        private val observer: RxObserver<in String>,
    ) : MainThreadDisposable(), MaterialSearchView.OnQueryTextListener {
        override fun onQueryTextChange(newText: String?): Boolean {
            return newText?.let {
                if (!isDisposed) {
                    observer.onNext(it)
                }
                true
            } == true
        }

        override fun onQueryTextSubmit(query: String?): Boolean {
            query?.let {
                if (!isDisposed) {
                    observer.onNext(it)
                }
            }
            return false
        }

        override fun onDispose() = view.setOnQueryTextListener(null)
    }
}

/**
 * - If the file exists and is not overwritten ==> true, else In case of delete failure ==> false
 * - Create a parent directory, copy the file, and return the target.
 *
 * @param file: file.
 * @param overwrite: overwrite
 * @param bufferSize:  8 * 1024 = 8192b = 8kb = 0.0078mb
 *
 * @author Scarlett
 * @version 1.0.0
 * @since 2021-02-26 오후 2:12
 **/
fun InputStream.copyTo(
    target: File,
    overwrite: Boolean = false,
    bufferSize: Int = DEFAULT_BUFFER_SIZE,
): File {
    if (target.exists()) {
        val stillExists = if (!overwrite) true else !target.delete()
        if (stillExists) {
            throw IllegalAccessException("The destination file already exists.")
        }
    }
    target.parentFile?.mkdirs()
    this.use { input ->
        target.outputStream().use { output ->
            input.copyTo(output, bufferSize)
        }
    }

    return target
}

fun <A, B, C, R> LiveData<A>.combineLatest(
    b: LiveData<B>,
    c: LiveData<C>,
    combine: (A, B, C) -> R,
): LiveData<R> {
    return MediatorLiveData<R>().apply {
        var lastA: A? = null
        var lastB: B? = null
        var lastC: C? = null

        addSource(this@combineLatest) { v ->
            if (v == null && value != null) value = null
            lastA = v

            lastA?.let { a ->
                lastB?.let { b ->
                    lastC?.let { value = combine(a, b, it) }
                }
            }
        }

        addSource(b) { v ->
            if (v == null && value != null) value = null
            lastB = v

            lastA?.let { a ->
                lastB?.let { b ->
                    lastC?.let { value = combine(a, b, it) }
                }
            }
        }

        addSource(c) { v ->
            if (v == null && value != null) value = null
            lastC = v

            lastA?.let { a ->
                lastB?.let { b ->
                    lastC?.let { value = combine(a, b, it) }
                }
            }
        }
    }
}

/**
 * If there is a method that needs to be repeated asynchronously, run the block below.
 *
 * @param times: Number of times to repeat.
 * @param initialDelay: Delay time every iteration.
 * @param factor: Delay time multiplied by argument.
 * @param maxDelay: Maximum delay time by coerceAtMost.
 * @author Scarlett
 * @version 1.0.0
 * @since 2021-02-26 오후 1:29
 **/
suspend fun <T> retryIO(
    times: Int,
    initialDelay: Long,
    factor: Double,
    maxDelay: Long = Long.MAX_VALUE,
    block: suspend () -> T,
): T {
    var currentDelay = initialDelay
    repeat(times - 1) {
        try {
            return block()
        } catch (e: IOException) {
        }
        delay(currentDelay)
        currentDelay = (currentDelay * factor).toLong().coerceAtMost(maxDelay)
    }
    return block() // last attempt
}

fun DocumentReference.snapshots(): Observable<DocumentSnapshot> {
    return Observable.create { emitter: ObservableEmitter<DocumentSnapshot> ->
        val registration = addSnapshotListener listener@{ documentSnapshot, exception ->
            if (exception !== null && !emitter.isDisposed) {
                return@listener emitter.onError(exception)
            }
            if (documentSnapshot != null && !emitter.isDisposed) {
                emitter.onNext(documentSnapshot)
            }
        }
        emitter.setCancellable {
            registration.remove()
            Timber.d("Remove snapshot listener $this")
        }
    }
}

fun Query.snapshots(): Observable<QuerySnapshot> {
    return Observable.create { emitter: ObservableEmitter<QuerySnapshot> ->
        val registration = addSnapshotListener listener@{ querySnapshot, exception ->
            if (exception !== null && !emitter.isDisposed) {
                return@listener emitter.onError(exception)
            }
            if (querySnapshot != null && !emitter.isDisposed) {
                emitter.onNext(querySnapshot)
            }
        }
        emitter.setCancellable {
            registration.remove()
            Timber.d("Remove snapshot listener $this")
        }
    }
}

@Suppress("unused")
inline val Any?.unit
    get() = Unit

inline val ViewGroup.inflater: LayoutInflater get() = LayoutInflater.from(context)


inline fun <reified T : Any, B : ViewDataBinding> RecyclerView.ViewHolder.onlyBind(
    item: Any,
    binding: B,
    crossinline bind: B.(T) -> Unit
) {
    check(item is T) { "${this::class.java.simpleName}::bind only accept ${T::class.java.simpleName}, but item=$item" }
    binding.bind(item)
}


/**
 * Escape file name: only allow letters, numbers, dots and dashes
 */
private fun String.escapeFileName(): String {
    return replace(
        "[^a-zA-Z0-9.\\-]".toRegex(),
        replacement = "_"
    )
}


@ExperimentalCoroutinesApi
@CheckResult
fun EditText.textChanges(): Flow<CharSequence?> {
    return callbackFlow<CharSequence?> {
        val listener = object : TextWatcher {
            override fun afterTextChanged(s: Editable?) = Unit
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) = Unit
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                offer(s)
            }
        }
        addTextChangedListener(listener)
        awaitClose { removeTextChangedListener(listener) }
    }.onStart { emit(text) }
}