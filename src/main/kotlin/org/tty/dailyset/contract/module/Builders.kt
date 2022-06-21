package org.tty.dailyset.contract.module

import org.tty.dailyset.contract.declare.*
import org.tty.dailyset.contract.impl.ResourceSyncBuilderSyncImpl
import org.tty.dailyset.contract.module.sync.ResourceSyncBuilderSync
import org.tty.dailyset.contract.module.sync.ResourceSyncClientSync
import org.tty.dailyset.contract.module.sync.ResourceSyncServerSync

fun <TC: ResourceContent, ES, EC> resourceSyncClientSync(
    builderLambda: ResourceSyncBuilderSync<TC, ES, EC>.() -> Unit
): ResourceSyncClientSync<TC, ES, EC> {
    val builder = ResourceSyncBuilderSyncImpl<TC, ES, EC>()
    builder.builderLambda()
    return builder.buildClient()
}

fun <TC: ResourceContent, ES, EC> resourceSyncServerSync(
    builderLambda: ResourceSyncBuilderSync<TC, ES, EC>.() -> Unit
): ResourceSyncServerSync<TC, ES, EC> {
    val builder = ResourceSyncBuilderSyncImpl<TC, ES, EC>()
    builder.builderLambda()
    return builder.buildServer()
}