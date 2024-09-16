package com.zapcom.android.app.domain

import com.zapcom.android.app.data.repository.SectionRepository
import com.zapcom.android.app.data.repository.entity.ItemEntity
import com.zapcom.android.app.data.repository.entity.SectionEntity
import com.zapcom.android.app.domain.base.UseCase
import com.zapcom.android.app.domain.model.Item
import com.zapcom.android.app.domain.model.Sections
import javax.inject.Inject

/**
 * Section use case
 *
 * @property repository
 * @constructor Create empty Section use case
 */
class SectionUseCase @Inject constructor(
    private val repository: SectionRepository,
) : UseCase<Unit, Result<Sections>>() {
    override suspend fun doWork(params: Unit): Result<Sections> =

        repository.sectionData().mapCatching {
            Sections(
                sections = it.mapNotNull { section ->
                    section.toDomain()
                }
            )
        }

    companion object {
        private const val BANNER = "banner"
        private const val HORIZONTAL_FREE_SCROLL = "horizontalFreeScroll"
        private const val SPLIT_BANNER = "splitBanner"
    }

    private fun SectionEntity.toDomain() = when (sectionType) {
        BANNER -> Sections.Section.Banner(item = items.first().toDomain())
        HORIZONTAL_FREE_SCROLL -> Sections.Section.HorizontalFreeScroll(items = items.map { it.toDomain() })
        SPLIT_BANNER -> Sections.Section.SplitBanner(items = items.map { it.toDomain() })
        else -> {
            null
        }
    }

    private fun ItemEntity.toDomain() = Item(
        title = title,
        image = image
    )
}

