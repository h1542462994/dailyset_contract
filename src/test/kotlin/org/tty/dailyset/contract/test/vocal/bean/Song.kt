package org.tty.dailyset.contract.test.vocal.bean

import org.tty.dailyset.contract.declare.KeySelector
import org.tty.dailyset.contract.declare.ResourceContent

data class Song(
    override val uid: String,
    val name: String,
    val albumUid: String,
    val description: String,
    val contentLength: Long
): VocalContent, KeySelector<Song, String> {
    override fun copyByUid(uid: String): ResourceContent {
        return copy(uid = uid)
    }

    override fun selectKey(value: Song): String {
        return name
    }
}