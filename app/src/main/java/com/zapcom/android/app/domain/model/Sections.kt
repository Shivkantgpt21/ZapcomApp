package com.zapcom.android.app.domain.model


/**
 * Section
 *
 * @property sections
 * @constructor Create empty Section
 */
data class Sections(val sections: List<Section>){

    sealed class Section {
        data class Banner(val item: Item) : Section()
        data class HorizontalFreeScroll(val items: List<Item>) : Section()
        data class SplitBanner(val items: List<Item>) : Section()
    }
}
