package com.zapcom.android.app.state

import app.cash.turbine.test
import com.zapcom.android.app.domain.SectionUseCase
import com.zapcom.android.app.domain.model.Item
import com.zapcom.android.app.domain.model.Sections
import com.zapcom.android.app.state.SectionsStateHolder.UiState
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

@ExperimentalCoroutinesApi
class SectionStateHolderTest {

    // Mock dependencies
    @MockK(relaxed = true, relaxUnitFun = true)
    lateinit var useCase: SectionUseCase

    @MockK(relaxed = true, relaxUnitFun = true)
    lateinit var connectivityHelper: ConnectivityHelper

    private lateinit var sectionsStateHolder: SectionsStateHolder

    @Before
    fun setup() {
        MockKAnnotations.init(this, relaxed = true)
        // Set up the test coroutine dispatcher
        Dispatchers.setMain(StandardTestDispatcher())

        // Create an instance of SectionsStateHolder
        sectionsStateHolder = SectionsStateHolder(useCase, connectivityHelper)
    }

    @Test
    fun `state emits NoInternet when not connected to the internet`() = runTest {
        // Arrange
        every { connectivityHelper.isConnected() } returns false
        sectionsStateHolder = SectionsStateHolder(useCase, connectivityHelper)

        // Act & Assert
        sectionsStateHolder.state.test {
            val state = awaitItem()
            assert(state is UiState.NoInternet)
            assert((state as UiState.NoInternet).message == "No internet please check your internet and try again")
        }
    }

    @Test
    fun `state emits Success when data is successfully fetched`() = runTest {
        // Arrange
        val mockSection = Sections(
            sections = listOf(
                Sections.Section.Banner(
                    item =
                    Item(
                        "Jacket",
                        "https://images.pexels.com/photos/789812/pexels-photo-789812.jpeg"
                    )
                )
            )
        )
        val mockSuccessResult = Result.success(mockSection)

        every { connectivityHelper.isConnected() } returns true
        every { useCase.invoke(Unit) } returns flowOf(mockSuccessResult)

        sectionsStateHolder = SectionsStateHolder(useCase, connectivityHelper)

        // Act & Assert
        sectionsStateHolder.state.test {
            val state = awaitItem()
            // Assert
            assert(state is UiState.Success)
        }
    }

    @Test
    fun `state emits Error when sections list is empty`() = runTest {
        // Arrange
        val emptySections = Sections(sections = emptyList())
        val mockSuccessResult = Result.success(emptySections)

        every { connectivityHelper.isConnected() } returns true
        every { useCase.invoke(Unit) } returns flowOf(mockSuccessResult)

        // Arrange
        sectionsStateHolder = SectionsStateHolder(useCase, connectivityHelper)

        // Act
        sectionsStateHolder.state.test {
            val state = awaitItem()
            // Assert
            assert(state is UiState.Error)
            assertEquals("No data please try after some times", (state as UiState.Error).message)
        }
    }

    @Test
    fun `state emits Loading initially`() = runTest {

        sectionsStateHolder = SectionsStateHolder(useCase, connectivityHelper)

        assert(sectionsStateHolder.initialValue is UiState.Loading)
    }

    @Test
    fun `state updates sections when data is loaded`() = runTest {
        // Arrange
        val mockSection = Sections(
            sections = listOf(
                Sections.Section.Banner(
                    item =
                    Item(
                        "Jacket",
                        "https://images.pexels.com/photos/789812/pexels-photo-789812.jpeg"
                    )
                )
            )
        )

        val mockSuccessResult = Result.success(mockSection)
        every { connectivityHelper.isConnected() } returns true
        every { useCase.invoke(Unit) } returns flowOf(mockSuccessResult)

        // Act
        sectionsStateHolder.state.test {
            val state = awaitItem()
            // Assert
            assert(state is UiState.Success)
            val successState = state as UiState.Success
            assertEquals(1, successState.sectionUiStates.size)
        }
    }

    @After
    fun tearDown() {
        // Reset coroutines dispatcher
        Dispatchers.resetMain()
    }

}