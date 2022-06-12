package org.tty.dailyset.contract.module

import org.tty.dailyset.contract.declare.*
import org.tty.dailyset.contract.impl.ResourceSyncBuilderSyncImpl
import org.tty.dailyset.contract.module.sync.ResourceSyncBuilderSync
import org.tty.dailyset.contract.module.sync.ResourceSyncClientSync
import org.tty.dailyset.contract.module.sync.ResourceSyncServerSync

fun <TS: ResourceSet<ES>, TL: ResourceLink<EC>, TTL: ResourceTemporalLink<EC>, TC: ResourceContent, TV: ResourceSetVisibility, ES, EC> resourceSyncClientSync(
    builderLambda: ResourceSyncBuilderSync<TS, TL, TTL, TC, TV, ES, EC>.() -> Unit
): ResourceSyncClientSync<TS, TL, TTL, TC, TV, ES, EC> {
    val builder = ResourceSyncBuilderSyncImpl<TS, TL, TTL, TC, TV, ES, EC>()
    builder.builderLambda()
    return builder.buildClient()
}

fun <TS: ResourceSet<ES>, TL: ResourceLink<EC>, TC: ResourceContent, ES, EC> resourceSyncServerSync(
    builderLambda: ResourceSyncBuilderSync<TS, TL, Nothing, TC, Nothing, ES, EC>.() -> Unit
): ResourceSyncServerSync<TS, TL, TC, ES, EC> {
    val builder = ResourceSyncBuilderSyncImpl<TS, TL, Nothing, TC, Nothing, ES, EC>()
    builder.builderLambda()
    return builder.buildServer()
}