package com.zapcom.android.app.data.di

import com.zapcom.android.app.data.source.remote.SectionDataSource
import com.zapcom.android.app.data.source.remote.SectionDataSourceImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

/**
 * Data module
 *
 * @constructor Create empty Data module
 */
@Module
@InstallIn(SingletonComponent::class)
abstract class DataModule {

    /**
     * Bind section data source
     *
     * @param impl
     * @return SectionDataSource
     */
    @Binds
    abstract fun bindSectionDataSource(impl: SectionDataSourceImpl): SectionDataSource
}