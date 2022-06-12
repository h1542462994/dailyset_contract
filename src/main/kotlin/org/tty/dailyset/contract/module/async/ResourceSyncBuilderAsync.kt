package org.tty.dailyset.contract.module.async

import org.tty.dailyset.contract.declare.*
import org.tty.dailyset.contract.descriptor.*

/**
 * the builder for resource sync module (sync)
 */
interface ResourceSyncBuilderAsync<TS: ResourceSet<ES>, TL: ResourceLink<EC>, TTL: ResourceTemporalLink<EC>, TC: ResourceContent, TV: ResourceSetVisibility, ES, EC> {

    fun <TE> registerSetDescriptor(descriptor: ResourceSetDescriptorAsync<TS, TE, ES>)

    fun <TE> registerLinkDescriptor(descriptor: ResourceLinkDescriptorAsync<TL, TE, EC>)

    fun <TE> registerTemporalLinkDescriptor(descriptor: ResourceTemporalLinkDescriptorAsync<TTL, TE, EC>)

    fun <TE> registerContentDescriptor(descriptor: ResourceContentDescriptorAsync<out TC, TE, EC>)

    fun registerContentDescriptors(vararg descriptor: ResourceContentDescriptorAsync<out TC, *, EC>)

    fun <TE> registerSetVisibilityDescriptor(descriptor: ResourceSetVisibilityDescriptorAsync<TV, TE>)

    fun buildClient(): ResourceSyncClientAsync<TS, TL, TTL, TC, TV, ES, EC>

    fun buildServer(): ResourceSyncServerAsync<TS, TL, TC, ES, EC>
}