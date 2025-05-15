package io.mohammedalaamorsi.movies.data

import io.mohammedalaamorsi.movies.utils.MainDispatcherRule
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Rule

/**
 * Base class for data layer tests that provides common setup for coroutine testing.
 */
@OptIn(ExperimentalCoroutinesApi::class)
abstract class BaseDataLayerTest {
    
    /**
     * Rule to set the Dispatchers.Main to a TestDispatcher 
     * for unit testing coroutines.
     */
    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()
}
