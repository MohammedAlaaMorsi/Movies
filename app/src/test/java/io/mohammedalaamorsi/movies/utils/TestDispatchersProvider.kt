package io.mohammedalaamorsi.movies.utils

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher

/**
 * Test implementation of [DispatchersProvider] that uses test dispatchers
 * for predictable and immediate execution in unit tests.
 */
@OptIn(ExperimentalCoroutinesApi::class)
class TestDispatchersProvider : DispatchersProvider() {
    private val testDispatcher = UnconfinedTestDispatcher()
    
    override val immediate: CoroutineDispatcher = testDispatcher
    override val main: CoroutineDispatcher = testDispatcher
    override val io: CoroutineDispatcher = testDispatcher
    override val default: CoroutineDispatcher = testDispatcher
    override val unconfined: CoroutineDispatcher = testDispatcher
}
