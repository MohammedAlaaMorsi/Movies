package io.mohammedalaamorsi.movies.domain.usecase

import io.mohammedalaamorsi.movies.utils.MainDispatcherRule
import io.mohammedalaamorsi.movies.utils.TestDispatchersProvider
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Rule

/**
 * Base class for use case tests that provides common setup for 
 * coroutines and dispatchers.
 */
@OptIn(ExperimentalCoroutinesApi::class)
abstract class BaseUseCaseTest {
    
    /**
     * Rule to set the Dispatchers.Main to a TestDispatcher 
     * for unit testing coroutines.
     */
    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()
    
    /**
     * Test implementation of DispatchersProvider for unit tests.
     */
    val testDispatchersProvider = TestDispatchersProvider()
}
