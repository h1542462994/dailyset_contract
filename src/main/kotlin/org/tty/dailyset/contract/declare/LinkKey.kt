package org.tty.dailyset.contract.declare

data class LinkKey<EC>(
    val setUid: String,
    val contentType: EC,
    val contentUid: String
)
