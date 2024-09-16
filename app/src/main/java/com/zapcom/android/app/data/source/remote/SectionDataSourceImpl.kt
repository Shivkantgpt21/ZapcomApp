package com.zapcom.android.app.data.source.remote

import com.zapcom.android.app.data.repository.entity.ItemEntity
import com.zapcom.android.app.data.repository.entity.SectionEntity
import com.zapcom.android.app.data.source.remote.model.ItemDto
import com.zapcom.android.app.data.source.remote.model.SectionDto
import com.zapcom.android.networking.NetworkConfig
import com.zapcom.android.networking.NetworkingClient
import javax.inject.Inject

/**
 * Section data source impl
 *
 * @property client
 * @constructor Create empty Section data source impl
 */
class SectionDataSourceImpl @Inject constructor(
    private val client: NetworkingClient
) : SectionDataSource {
    override suspend fun sectionData() =
        kotlin.runCatching {
            client.get<List<SectionDto>>(NetworkConfig.URL_PATH).map {
                it.toEntity()
            }
        }.onFailure {
           println("shiva:"+it.message)
        }
}

private fun SectionDto.toEntity() = SectionEntity(
    sectionType = sectionType,
    items = items.map {
        it.toEntity()
    },
)

private fun ItemDto.toEntity() = ItemEntity(
    title = title,
    image = image
)