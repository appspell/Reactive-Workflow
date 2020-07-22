package com.glukharev.framework

import android.view.ViewGroup
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

abstract class View<UIState, UIAction> {

    private var coroutineScope: CoroutineScope? = null

    private val outputActions =
        MutableStateFlow<UIAction?>(null)

    protected var containerView: ViewGroup? = null

    fun bindToScope(coroutineScope: CoroutineScope?) {
        this.coroutineScope = coroutineScope
    }

    fun bind(view: ViewGroup?) {
        containerView = view
        initUserInteraction()
    }

    fun subscribe(observer: (UIAction) -> Unit) {
        coroutineScope?.launch {
            outputActions.collect { action ->
                action?.let { observer.invoke(action) }
            }
        }
    }

    protected fun sendAction(action: UIAction) {
        outputActions.value = action
    }

    abstract fun render(state: UIState)

    protected abstract fun initUserInteraction()

}