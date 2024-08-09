package com.yuta.domain.model.dto

data class UserMiniatureUpdateDto(
    val userId: Int,
    val containerWidth: Int,
    val containerHeight: Int,
    val croppedWidth: Int,
    val croppedHeight: Int,
    val offsetX: Int,
    val offsetY: Int
)
