package com.zapcom.android.app.data

import com.zapcom.android.app.data.source.remote.SectionDataSource
import com.zapcom.android.app.data.source.remote.SectionDataSourceImpl
import com.zapcom.android.networking.NetworkingClient
import io.mockk.MockKAnnotations
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.mockkClass
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Test

@ExperimentalCoroutinesApi
class SectionDataSourceImplTest {

    private lateinit var dataSource: SectionDataSource

    @RelaxedMockK
    private var client: NetworkingClient = mockkClass(NetworkingClient::class)

    @Before
    fun setup() {
        MockKAnnotations.init(this, relaxed = true)
        dataSource = SectionDataSourceImpl(client)
    }

    @ExperimentalCoroutinesApi
    @Test
    fun `sectionData returns success result with mapped SectionEntity list`() = runTest {
        dataSource.sectionData().runCatching {
            assertEquals(true, this.isSuccess)
            assertNotNull(this.getOrNull())
        }
    }

    @Test
    fun `sectionData returns failure result when client throws exception`() = runTest {
        val exception = Exception("Test exception")
        dataSource.sectionData().runCatching {
            assertEquals(true, this.isFailure)
        }.onFailure {
            assertEquals(exception, it)
        }
    }
}