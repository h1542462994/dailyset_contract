package org.tty.dailyset.contract.module.sync

import org.tty.dailyset.contract.declare.*
import org.tty.dailyset.contract.descriptor.*

/**
 * the builder for resource sync module (sync)
 */
interface ResourceSyncBuilderSync<TS: ResourceSet<ES>, TL: ResourceLink<EC>, TTL: ResourceTemporalLink<EC>, TV: ResourceSetVisibility, ES, EC> {

    fun <TE> registerSetDescriptor(descriptor: ResourceSetDescriptorSync<TS, TE, ES>)

    fun <TE> registerLinkDescriptor(descriptor: ResourceLinkDescriptorSync<TL, TE, EC>)

    fun <TE> registerTemporalLinkDescriptor(descriptor: ResourceTemporalLinkDescriptorSync<TTL, TE, EC>)

    fun <TE> registerContentDescriptor(descriptor: ResourceContentDescriptorSync<out ResourceContent, TE, EC>)

    fun registerContentDescriptors(vararg descriptors: ResourceContentDescriptorSync<out ResourceContent, *, EC>)

    fun <TE> registerSetVisibilityDescriptor(descriptor: ResourceSetVisibilityDescriptorSync<TV, TE>)

    fun buildClient(): ResourceSyncClientSync<TS, TL, TTL, TV, ES, EC>

    fun buildServer(): ResourceSyncServerSync<TS, TL, ES, EC>
}