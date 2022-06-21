package org.tty.dailyset.contract.declare

import kotlinx.serialization.Serializable

@Serializable
data class ResourceSetVisibility(
    val uid: String,
    val userUid: String,
    val visible: Boolean
)