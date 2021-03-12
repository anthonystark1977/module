package com.attractive.deer.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.CallSuper
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import com.attractive.deer.util.data.observeEvent
import com.attractive.deer.util.data.observe
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.kotlin.addTo
import org.koin.androidx.scope.ScopeFragment
import timber.log.Timber

/**
 *
 * @property I: Any action that can be performed in app.
 * @property S: The status of the current screen.
 * @property E: Toast,Snack,Dialog ...
 * @property VM: MviViewModel.
 * @property VB: DataBinding.
 * @param layoutId: layoutId
 *
 * @author Scarlett
 * @version 1.0.0
 * @since 2021-03-12 오후 2:34
 **/
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

    /**
     * Rendering,EventProcessing,Intent..
     *
     * @author Scarlett
     * @version 1.0.0
     * @since 2021-03-12 오후 2:37
     **/
    private fun bindVM() {
        mViewModel.state.observe(owner = viewLifecycleOwner, ::render)
        mViewModel.singleEvent.observeEvent(viewLifecycleOwner, ::handleEvent)
        mViewModel.processIntents(viewIntents()).addTo(compositeDisposable)
    }
}
