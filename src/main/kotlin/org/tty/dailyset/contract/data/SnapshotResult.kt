package org.tty.dailyset.contract.data

import org.tty.dailyset.contract.declare.ResourceContent
import org.tty.dailyset.contract.declare.ResourceSet
import kotlinx.serialization.Serializable

@Serializable
data class SnapshotResult<TS: ResourceSet<ES>, TC: ResourceContent, ES, EC>(
    val set: TS,
    val typedResources: List<TypedResources<TC, EC>>
)
