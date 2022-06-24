package org.tty.dailyset.contract.data

import org.tty.dailyset.contract.declare.ResourceSet
import kotlinx.serialization.Serializable
import org.tty.dailyset.contract.declare.ResourceContent

/**
 * the update result for **transport**, it is provided from **server**, and used for **client**.
 *
 * when the **client** is prepared for **download** data from **server**. it should call *ResourceModuleClient::writeUpdate*
 * with the *return* of *ResourceModuleServer::readUpdate*
 *
 * **notice:** the **client** will sync the version same as the transport data. and you should ensure the [UserContext] is not empty.
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