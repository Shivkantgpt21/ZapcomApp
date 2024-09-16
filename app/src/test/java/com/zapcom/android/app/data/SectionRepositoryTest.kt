package com.zapcom.android.app.data

import com.zapcom.android.app.data.repository.SectionRepository
import com.zapcom.android.app.data.repository.entity.ItemEntity
import com.zapcom.android.app.data.repository.entity.SectionEntity
import com.zapcom.android.app.data.source.remote.SectionDataSource
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class SectionRepositoryTest {

    private lateinit var repository: SectionRepository

    @MockK
    lateinit var remote: SectionDataSource

    @Before
    fun setup() {
        MockKAnnotations.init(this, relaxed = true)
        repository = SectionRepository(remote)
    }

    @Test
    fun `sectionData fetches from api verify with entity`() =
        runTest {
            val sectionEntities = listOf(
                SectionEntity(  sectionType = "banner",
                    items = listOf(
                        ItemEntity(
                            "Jacket",
                            "https://images.pexels.com/photos/789812/pexels-photo-789812.jpeg"
                        )
                    )),
                SectionEntity( sectionType = "horizontalFreeScroll",
                    items = listOf(
                        ItemEntity("Laptop", "https://images.pexels.com/photos/7974/pexels-photo.jpg"),
                        ItemEntity("Hat", "https://images.pexels.com/photos/984619/pexels-photo-984619.jpeg"),
                        ItemEntity(
                            "Sunglasses",
                            "https://images.pexels.com/photos/343720/pexels-photo-343720.jpeg"
                        ),
                        ItemEntity("Watch", "https://images.pexels.com/photos/277390/pexels-photo-277390.jpeg")
                    )),
                SectionEntity( sectionType = "splitBanner",
                    items = listOf(
                        ItemEntity("Camera", "https://images.pexels.com/photos/225157/pexels-photo-225157.jpeg"),
                        ItemEntity("Perfumes", "https://images.pexels.com/photos/264819/pexels-photo-264819.jpeg")
                    )),
                )

            coEvery { remote.sectionData() } returns (Result.success(sectionEntities))

            val result = repository.sectionData()
            assertEquals(true, result.isSuccess)
            assertEquals(sectionEntities, result.getOrNull())
        }

    @Test
    fun `sectionData returns failure result when remote throws exception`() = runTest {
        val exception = Exception("Test exception")
        coEvery { remote.sectionData() } returns Result.failure(exception)

        val result = repository.sectionData()

        assertEquals(true, result.isFailure)
        assertEquals(exception, result.exceptionOrNull())
    }
}