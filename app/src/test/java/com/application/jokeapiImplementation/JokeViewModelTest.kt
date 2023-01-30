package com.application.jokeapiImplementation

import android.app.Application
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.application.jokeapiImplementation.model.JokeResponse
import com.application.jokeapiImplementation.viewModel.JokeViewModel
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.ArgumentCaptor
import org.mockito.Mockito
import org.mockito.Mockito.*

class JokeViewModelTest {
//   private val context =  Mockito.mock(Application::class.java)
//    @Mock
//    lateinit var viewModel : JokeViewModel
//    companion object {
//        @ClassRule
//        @JvmField
//        val schedulers = RxImmediateSchedulerRule()
//    }
//
//    @Rule
//    @JvmField
//    val rule = InstantTaskExecutorRule()
//    @Test
//    fun jokeAPITest() {
//        givenJokeViewModelIsInitialized()
//        whenJokeAPIAreReadAndParsed()
//        thenTheJokesReceived()
//    }
//
//    private fun givenJokeViewModelIsInitialized() {
//         viewModel = JokeViewModel(context)
//    }
//
//    private  fun whenJokeAPIAreReadAndParsed() = runBlocking{
//        viewModel.randomJokeAPICall()
//    }
//
//    private fun thenTheJokesReceived() {
//        assertNotNull(viewModel.randomJoke?.value)
//    }
//
@get:Rule
val rule = InstantTaskExecutorRule()
    private val context =  mock(Application::class.java)
    private lateinit var viewModel: JokeViewModel

    private val observer: Observer<JokeResponse> = mock()

    @Before
    fun before() {
        viewModel = JokeViewModel(context)
        viewModel.randomJoke?.observeForever(observer)
    }

    @Test
    fun fetchUser_ShouldReturnUser() {
        val expectedUser = JokeResponse(true)

        viewModel.randomJokeAPICall()

        val captor = ArgumentCaptor.forClass(JokeResponse::class.java)
        captor.run {
            verify(observer, times(1)).onChanged(capture())
            assertEquals(expectedUser, value)
        }
    }
}