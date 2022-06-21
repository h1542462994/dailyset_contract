package org.tty.dailyset.contract.declare

import kotlinx.serialization.Serializable
/**
 * **resource set.** the set of the resource contents. it holds multi resource link.
 * @see ResourceContent
 */

@Serializable
data class ResourceSet<ES>(
    val uid: String,
    val setType: ES,
    val version: Int
) {
    fun increaseVersionCopy(): ResourceSet<ES> {
        return copy(
            version = version + 1
        )
    }

    fun increasedVersion(): Int {
        return version + 1
    }
}