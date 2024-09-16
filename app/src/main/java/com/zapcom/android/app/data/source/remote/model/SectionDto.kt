package com.zapcom.android.app.data.source.remote.model

import kotlinx.serialization.Serializable

@Serializable
data class SectionDto(val sectionType: String, val items: List<ItemDto>)
