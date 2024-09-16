package com.zapcom.android.app.state

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

/**
 * Section view model
 *
 * @property sectionsStateHolder
 * @constructor Create empty Section view model
 */
@HiltViewModel
class SectionViewModel @Inject constructor(
    private val sectionsStateHolder: SectionsStateHolder

) : ViewModel() {

    val state: StateFlow<UiState> = sectionsStateHolder.state.map { sectionUiState ->
        UiState(
            sectionsUiState = sectionUiState,
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(),
        initialValue = initialValue()
    )

    private fun initialValue() = UiState(
        sectionsUiState = sectionsStateHolder.initialValue
    )

    data class UiState(
        val sectionsUiState: SectionsStateHolder.UiState
    )
}