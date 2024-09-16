package com.zapcom.android.networking.hilt

import com.zapcom.android.networking.NetworkConfig
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

/**
 * Singleton module
 *
 * @constructor Create empty Singleton module
 */
@Module
@InstallIn(SingletonComponent::class)
class SingletonModule {

    @Provides
    fun provideNetworkConfig() = NetworkConfig(NetworkConfig.BASE_URL)
}