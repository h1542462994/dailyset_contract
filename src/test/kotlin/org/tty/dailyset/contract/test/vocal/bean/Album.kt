package org.tty.dailyset.contract.test.vocal.bean

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.tty.dailyset.contract.declare.ResourceContent

@Serializable
@SerialName("album")
data class Album(
    override val uid: String,
    val name: String,
    val songUids: List<String>,
    val description: String
): VocalContent {
    override fun copyByUid(uid: String): ResourceContent {
        return copy(uid = uid)
    }
}