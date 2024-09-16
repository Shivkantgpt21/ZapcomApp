package com.zapcom.android.app.data.source.remote

import com.zapcom.android.app.data.repository.entity.SectionEntity

/**
 * Currency data source
 *
 * @constructor Create empty Currency data source
 */
interface SectionDataSource {
    /**
     * Currency data
     *
     * @return
     */
    suspend fun sectionData(): Result<List<SectionEntity>>
}