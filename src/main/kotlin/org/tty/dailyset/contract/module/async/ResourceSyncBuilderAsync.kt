package org.tty.dailyset.contract.module.async

import org.tty.dailyset.contract.declare.*
import org.tty.dailyset.contract.descriptor.*

/**
 * the builder for resource sync module (sync)
 */
interface ResourceSyncBuilderAsync<TS: ResourceSet<ES>, TL: ResourceLink<EC>, TTL: ResourceTemporalLink<EC>, TV: ResourceSetVisibility, ES, EC> {

    fun <TE> registerSetDescriptor(descriptor: ResourceSetDescriptorAsync<TS, TE, ES>)

    fun <TE> registerLinkDescriptor(descriptor: ResourceLinkDescriptorAsync<TL, TE, EC>)

    fun <TE> registerTemporalLinkDescriptor(descriptor: ResourceTemporalLinkDescriptorAsync<TTL, TE, EC>)

    fun <T: ResourceContent, TE> registerContentDescriptor(descriptor: ResourceContentDescriptorAsync<T, TE, EC>)

    fun <TE> registerSetVisibilityDescriptor(descriptor: ResourceSetVisibilityDescriptorAsync<TV, TE>)

    fun buildClient(): ResourceSyncClientAsync<TS, TL, TTL, TV, ES, EC>

    fun buildServer(): ResourceSyncServerAsync<TS, TL, ES, EC>
}