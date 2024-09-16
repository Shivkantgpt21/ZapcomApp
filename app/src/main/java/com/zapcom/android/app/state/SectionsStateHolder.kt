package com.zapcom.android.app.state

import com.zapcom.android.app.domain.SectionUseCase
import com.zapcom.android.app.domain.base.launchFlow
import com.zapcom.android.app.domain.model.Item
import com.zapcom.android.app.domain.model.Sections
import com.zapcom.android.app.ui.event.Event
import dagger.hilt.android.scopes.ViewModelScoped
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.update
import javax.inject.Inject

/**
 * Currency menu state holder
 *
 * @property useCase
 * @constructor Create empty Currency menu state holder
 */
@ViewModelScoped
class SectionsStateHolder @Inject constructor(
    private val useCase: SectionUseCase,
    private val connectivityHelper: ConnectivityHelper
) {

    private val initialUiState = InternalState()

    val initialValue: UiState = initialUiState.toUiState()

    private val _state = MutableStateFlow(initialUiState)

    private val sectionData = launchFlow {
        if (connectivityHelper.isConnected()) {
            useCase(Unit).collect { result ->
                if (result.isSuccess) {
                    _state.update {
                        it.copy(
                            sections = result.getOrNull(),
                            noInternet = false,
                            isLoading = false
                        )
                    }
                }
            }
        } else {
            _state.update {
                it.copy(
                    noInternet = true,
                    isLoading = false
                )
            }
        }
    }

    internal

    val state: Flow<UiState> = combine(_state, sectionData) { internalState, _ ->
        internalState.toUiState()
    }

    private fun InternalState.toUiState() = if (isLoading) {
        UiState.Loading
    } else if (noInternet) {
        UiState.NoInternet(NO_INTERNET_MSG)
    } else if (sections?.sections.isNullOrEmpty()) {
        UiState.Error(ERROR_MSG)
    } else {
        UiState.Success(
            sectionUiStates =
            sections?.sections?.map {
                it.toUiState()
            }.orEmpty()
        )
    }

    private fun Sections.Section.toUiState() =
        when (this) {
            is Sections.Section.Banner -> UiState.SectionType.Banner(
                item = item.toUiState()
            )

            is Sections.Section.HorizontalFreeScroll -> UiState.SectionType.HorizontalFreeScroll(
                items = items.map { it.toUiState() })

            is Sections.Section.SplitBanner -> UiState.SectionType.SplitBanner(items = items.map { it.toUiState() })
        }


    private fun Item.toUiState() = UiState.ItemUiState(
        title = title,
        image = image
    )

    data class InternalState(
        val sections: Sections? = null,
        val noInternet: Boolean = false,
        val isLoading: Boolean = true
    )

    /**
     * Ui state
     *
     * @constructor Create empty Ui state
     */
    sealed class UiState() {
        data object Loading : UiState()
        data class Success(val sectionUiStates: List<SectionType>) : UiState()
        data class Error(val message: String) : UiState()
        data class NoInternet(val message: String) : UiState()
        sealed class SectionType {
            data class Banner(val item: ItemUiState) : SectionType()
            data class HorizontalFreeScroll(val items: List<ItemUiState>) : SectionType()
            data class SplitBanner(val items: List<ItemUiState>) : SectionType()
        }

        /**
         * Item
         *
         * @property title
         * @property image
         * @constructor Create empty Item
         */
        data class ItemUiState(val title: String, val image: String)

        companion object {
            fun preview() = UiState.Success(
                sectionUiStates = listOf(
                    SectionType.Banner(
                        item = ItemUiState(
                            title = "Jacket",
                            image = "https://images.pexels.com/photos/789812/pexels-photo-789812.jpeg"
                        )
                    ),
                    SectionType.SplitBanner(
                        items = listOf(
                            ItemUiState(
                                "Camera",
                                "https://images.pexels.com/photos/225157/pexels-photo-225157.jpeg"
                            ),
                            ItemUiState(
                                "Perfumes",
                                "https://images.pexels.com/photos/264819/pexels-photo-264819.jpeg"
                            )
                        )
                    ),
                    SectionType.HorizontalFreeScroll(
                        items = listOf(
                            ItemUiState(
                                "Laptop", "https://images.pexels.com/photos/7974/pexels-photo.jpg"
                            ),
                            ItemUiState(
                                "Hat",
                                "https://images.pexels.com/photos/984619/pexels-photo-984619.jpeg"
                            )
                        )
                    )
                )
            )
        }
    }

    companion object {
        private const val ERROR_MSG = "No data please try after some times"
        private const val NO_INTERNET_MSG = "No internet please check your internet and try again"
    }

    sealed interface UiEvent:Event{
        data object OnRetry: UiEvent
    }
}