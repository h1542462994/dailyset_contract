package org.tty.dailyset.contract.module

import org.tty.dailyset.contract.declare.ResourceLink
import org.tty.dailyset.contract.declare.ResourceSet
import org.tty.dailyset.contract.declare.ResourceSetVisibility
import org.tty.dailyset.contract.declare.ResourceTemporalLink
import org.tty.dailyset.contract.impl.ResourceSyncBuilderSyncImpl
import org.tty.dailyset.contract.module.sync.ResourceSyncBuilderSync
import org.tty.dailyset.contract.module.sync.ResourceSyncClientSync
import org.tty.dailyset.contract.module.sync.ResourceSyncServerSync

fun <TS: ResourceSet<ES>, TL: ResourceLink<EC>, TTL: ResourceTemporalLink<EC>, TV: ResourceSetVisibility, ES, EC> resourceSyncClientSync(
    builderLambda: ResourceSyncBuilderSync<TS, TL, TTL, TV, ES, EC>.() -> Unit
): ResourceSyncClientSync<TS, TL, TTL, TV, ES, EC> {
    val builder = ResourceSyncBuilderSyncImpl<TS, TL, TTL, TV, ES, EC>()
    builder.builderLambda()
    return builder.buildClient()
}

fun <TS: ResourceSet<ES>, TL: ResourceLink<EC>, ES, EC> resourceSyncServerSync(
    builderLambda: ResourceSyncBuilderSync<TS, TL, Nothing, Nothing, ES, EC>.() -> Unit
): ResourceSyncServerSync<TS, TL, ES, EC> {
    val builder = ResourceSyncBuilderSyncImpl<TS, TL, Nothing, Nothing, ES, EC>()
    builder.builderLambda()
    return builder.buildServer()
}