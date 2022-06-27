package org.tty.dailyset.contract.test.vocal.bean

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.tty.dailyset.contract.declare.Key
import org.tty.dailyset.contract.declare.ResourceContent

@SerialName("music")
@Serializable
data class Music(
    override val uid: String,
    val name: String,
    val albumUid: String,
    val description: String,
    val contentLength: Long
): VocalContent, Key<String> {
    override fun copyByUid(uid: String): ResourceContent {
        return copy(uid = uid)
    }

    override fun key(): String {
        return name
    }
}