package com.zapcom.android.app.domain

import com.zapcom.android.app.data.repository.SectionRepository
import com.zapcom.android.app.data.repository.entity.ItemEntity
import com.zapcom.android.app.data.repository.entity.SectionEntity
import com.zapcom.android.app.domain.model.Item
import com.zapcom.android.app.domain.model.Sections
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Test

class SectionsUseCaseTest {

    @MockK
    lateinit var repository: SectionRepository

    @Before
    fun setup() {
        MockKAnnotations.init(this, relaxed = true)
    }

    @Test
    fun `doWork returns success result when repository emits SectionDataEntity`() = runTest {
        val sectionEntities = listOf(
            SectionEntity(
                sectionType = "banner",
                items = listOf(
                    ItemEntity(
                        "Jacket",
                        "https://images.pexels.com/photos/789812/pexels-photo-789812.jpeg"
                    )
                )
            ),
            SectionEntity(
                sectionType = "horizontalFreeScroll",
                items = listOf(
                    ItemEntity("Laptop", "https://images.pexels.com/photos/7974/pexels-photo.jpg"),
                    ItemEntity(
                        "Hat",
                        "https://images.pexels.com/photos/984619/pexels-photo-984619.jpeg"
                    ),
                    ItemEntity(
                        "Sunglasses",
                        "https://images.pexels.com/photos/343720/pexels-photo-343720.jpeg"
                    ),
                    ItemEntity(
                        "Watch",
                        "https://images.pexels.com/photos/277390/pexels-photo-277390.jpeg"
                    )
                )
            ),
            SectionEntity(
                sectionType = "splitBanner",
                items = listOf(
                    ItemEntity(
                        "Camera",
                        "https://images.pexels.com/photos/225157/pexels-photo-225157.jpeg"
                    ),
                    ItemEntity(
                        "Perfumes",
                        "https://images.pexels.com/photos/264819/pexels-photo-264819.jpeg"
                    )
                )
            ),
        )
        coEvery { repository.sectionData() } returns Result.success(sectionEntities)

        val useCase = SectionUseCase(repository)
        val result = useCase(Unit)

        assertEquals(true, result.first().isSuccess)
        val sectionDomain = Sections(
            sections =
            sectionEntities.mapNotNull { it.toDomain() }
        )
        assertNotNull(result.first().getOrNull())
        assertEquals(sectionDomain.sections.size, result.first().getOrNull()?.sections?.size)
    }

    @Test
    fun `doWork returns failure result when repository throws exception`() = runTest {
        val exception = Exception("Test exception")
        coEvery { (repository.sectionData()) } returns Result.failure(exception)

        val useCase = SectionUseCase(repository)
        val result = useCase(Unit)

        assertEquals(true, result.first().isFailure)
        assertEquals(exception, result.first().exceptionOrNull())
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

    companion object {
        private const val BANNER = "banner"
        private const val HORIZONTAL_FREE_SCROLL = "horizontalFreeScroll"
        private const val SPLIT_BANNER = "splitBanner"
    }
}