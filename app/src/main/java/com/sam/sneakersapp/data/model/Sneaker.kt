package com.sam.sneakersapp.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Sneaker(
    val brand: String? = null,
    val colorway: String? = null,
    val gender: String? = null,
    val id: String? = null,
    val media: Media? = null,
    val name: String? = null,
    val releaseYear: String? = null,
    val retailPrice: Int? = null,
    val shoe: String? = null,
    val styleId: String? = null,
    val title: String? = null,
    val year: Int? = null,
    val size: List<String>? = null,
    val color: List<String>? = null,
    var isAddedToCart: Boolean = false
) : Parcelable {
    @Parcelize
    data class Media(
        val imageUrl: String? = null,
        val smallImageUrl: String? = null,
        val thumbUrl: String? = null
    ) : Parcelable
}