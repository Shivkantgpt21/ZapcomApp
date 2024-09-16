package com.zapcom.android.app.state

import io.mockk.MockKAnnotations
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class SectionViewModelTest {

    @MockK
    lateinit var sectionsStateHolder: SectionsStateHolder

    @Before
    fun setup() {
        MockKAnnotations.init(this, relaxed = true)
    }

    @Test
    fun `initial state is correct`() = runTest {
        val viewModel = SectionViewModel(
            sectionsStateHolder
        )

        val expectedUiState = SectionViewModel.UiState(
            sectionsUiState = sectionsStateHolder.initialValue
        )
        assertEquals(expectedUiState, viewModel.state.value)
    }
}