package org.tty.dailyset.contract.data

import org.tty.dailyset.contract.declare.ResourceSet
import kotlinx.serialization.Serializable
import org.tty.dailyset.contract.declare.ResourceContent

/**
 * update result, which is provided from server, and used in client.
 * result contains set, link, temporalLink.
 */
@Serializable
data class UpdateResult<out TC: ResourceContent, ES, EC>(
    /**
     * set and it's version
     */
    val set: ResourceSet<ES>,

    /**
     * typed resource update.
     */
    val typedResourcesUpdate: List<TypedResourcesUpdate<TC, EC>>

)