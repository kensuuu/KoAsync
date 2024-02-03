package com.kensuuu.koasync

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.withTimeoutOrNull

/**
 * Parallel async tasks
 * @param scope CoroutineScope
 * @param tasks vararg suspend () -> T
 * @return List<T>
 */
suspend fun <T> parallelAsync(
    scope: CoroutineScope,
    vararg tasks: suspend () -> T
): List<T> {
    val deferredTasks = tasks.map { task ->
        scope.async { task() }
    }
    return deferredTasks.awaitAll()
}

/**
 * Parallel async tasks with timeout and progress and error handling
 * @param scope CoroutineScope
 * @param tasks vararg suspend () -> T
 * @param timeoutMillis Long?
 * @param onProgress (Int, Int) -> Unit
 * @param onError (Exception, Int) -> Unit
 * @return List<T?>
 */
suspend fun <T> parallelAsync(
    scope: CoroutineScope,
    vararg tasks: suspend () -> T,
    timeoutMillis: Long? = null,
    onProgress: ((Int, Int) -> Unit)? = null,
    onError: ((Exception, Int) -> Unit)? = null
): List<T?> {
    var completedTasks = 0
    val totalTasks = tasks.size

    val deferredTasks = tasks.mapIndexed { index, task ->
        scope.async {
            try {
                withTimeoutOrNull(timeoutMillis ?: Long.MAX_VALUE) {
                    task().also {
                        onProgress?.invoke(++completedTasks, totalTasks)
                    }
                }
            } catch (e: Exception) {
                onError?.invoke(e, index)
                null
            }
        }
    }
    return deferredTasks.awaitAll()
}
