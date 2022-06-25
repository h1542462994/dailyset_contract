package org.tty.dailyset.contract.declare

interface LocalSuffixSupport {

    fun addLocalSuffix(uid: String): String

    fun removeLocalSuffix(uid: String): String
}