package com.zapcom.android.app.data.repository

import com.zapcom.android.app.data.repository.entity.SectionEntity
import com.zapcom.android.app.data.source.remote.SectionDataSource
import javax.inject.Inject
import javax.inject.Singleton

typealias SectionEntityResult = Result<List<SectionEntity>>

/**
 * Section repository
 *
 * @property remote
 * @constructor Create empty Section repository
 */
@Singleton
class SectionRepository @Inject constructor(
    private val remote: SectionDataSource
) {
    /**
     * Currency data
     *
     * @return SectionEntityResult
     */
    suspend fun sectionData(): SectionEntityResult = remote.sectionData()

}

