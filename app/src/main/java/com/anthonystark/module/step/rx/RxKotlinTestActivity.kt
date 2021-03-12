package com.anthonystark.module.step.rx

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.anthonystark.module.R
import com.anthonystark.module.databinding.ActivityRxKotilnBinding
import com.jakewharton.rxbinding4.view.clicks
import com.jakewharton.rxrelay3.PublishRelay
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.kotlin.addTo
import io.reactivex.rxjava3.kotlin.ofType
import io.reactivex.rxjava3.kotlin.subscribeBy
import io.reactivex.rxjava3.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_rx_kotiln.*
import timber.log.Timber
import java.util.concurrent.TimeUnit

/**
 * [RxKotlin]
 *  subscribeOn: Observable이 emit하는 thread를 지정. (onNext()가 이루어지는 thread를 지정)
- Observable에 관련한 데이터 처리가 이루어지고, emit하는 thread
- subscribeOn을 설정안하면, mainThread에서 최초로 onNext()가 호출됨.

Observable.just(1,2,3) // creation of observable happens on Computational-Threads
.subscribeOn(Schedulers.computation()) // subscribeOn happens only once in chain. Nearest to source wins
.map(integer -> integer) // map happens on Computational-Threads
.observeOn(AndroidSchedulers.mainThread()) // Will switch every onNext to Main-Thread
.subscribe(integer -> {
// called from mainThread
});

 *  - 위의 예제에서
 *
 * http://tomstechnicalblog.blogspot.com/2016/02/rxjava-understanding-observeon-and.html
 * @author 권혁신
 * @version 0.0.8
 * @since 2021-03-11 오후 8:51
 **/
class RxKotlinTestActivity : AppCompatActivity() {

    private lateinit var mBinding: ActivityRxKotilnBinding
    private val compositeDisposable = CompositeDisposable()
    private var mResult: String = ""
    private var intentS = PublishRelay.create<RxTestIntent>()
    private var testInteger: Int = 0
    private val main = AndroidSchedulers.mainThread()
    private val io = Schedulers.io()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_rx_kotiln)

        intentS.publish {
            Observable.mergeArray(
                it.ofType<RxTestIntent>()
            )
        }
            .observeOn(io)
            .subscribeBy(onNext = {
                Timber.tag(TAG).d("subscribeBy():: $it, thread: ${Thread.currentThread().name}")
            })
            .addTo(compositeDisposable)

        initListener()
    }

    private fun initListener() {
        btn_test
            .clicks()
            .throttleFirst(300, TimeUnit.MILLISECONDS)
            .map {
                testInteger += 1
                RxTestIntent.TestClick(testInteger.toString())
            }
            .doOnNext {
                Timber.tag(TAG).d("Intent::$it, thread: ${Thread.currentThread().name}")
            }
            .subscribe(intentS)
            .addTo(compositeDisposable)
    }

    companion object {
        val TAG = RxKotlinTestActivity::class.simpleName
    }

}