package com.zapcom.android.app.domain.base

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.flow.flow
import kotlin.experimental.ExperimentalTypeInference

/**
 * Use case
 *
 * @param P
 * @param R
 * @constructor Create empty Use case
 */
abstract class UseCase<P, R> {

    /**
     * Invoke
     *
     * @param params
     * @return
     */
    operator fun invoke(params: P): Flow<R> = flow {
        emit(doWork(params))
    }

    /**
     * Do work
     *
     * @param params
     * @return
     */
    protected abstract suspend fun doWork(params: P): R
}

/**
 * Launch flow
 *
 * @param block
 * @receiver
 * @return
 */
@OptIn(ExperimentalTypeInference::class)
fun launchFlow(@BuilderInference block: suspend FlowCollector<Unit>.() -> Unit): Flow<Unit> = flow {
    emit(Unit)
    block()
}
