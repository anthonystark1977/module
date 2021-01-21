package com.tony.stark.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.CallSuper
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import com.tony.stark.util.observeEvent
import com.tony.stark.util.observe
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.kotlin.addTo
import org.koin.androidx.scope.ScopeFragment
import timber.log.Timber


abstract class BaseFragment<
        I : MviIntent,
        S : MviViewState,
        E : MviSingleEvent,
        VM : MviViewModel<I, S, E>,
        VB : ViewDataBinding
        >(
    @LayoutRes private val layoutId: Int
) : ScopeFragment(), MviView<I, S, E> {
    val compositeDisposable = CompositeDisposable()
    abstract val mViewModel: VM
    var mBinding: VB? = null

    @CallSuper
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mBinding = DataBindingUtil.inflate(inflater, layoutId, container, false)
        return mBinding?.root
    }

    @CallSuper
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Timber.d("$this:: onViewCreated()")
        setUpView(view, savedInstanceState)
        bindVM()
    }

    @CallSuper
    override fun onDestroyView() {
        super.onDestroyView()
        Timber.d("$this:: onDestroyView()")
        mBinding = null
        compositeDisposable.clear()
    }

    protected abstract fun setUpView(view: View, savedInstanceState: Bundle?)

    private fun bindVM() {
        mViewModel.state.observe(owner = viewLifecycleOwner, ::render)
        mViewModel.singleEvent.observeEvent(viewLifecycleOwner, ::handleEvent)
        mViewModel.processIntents(viewIntents()).addTo(compositeDisposable)
    }
}
