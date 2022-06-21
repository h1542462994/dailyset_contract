package org.tty.dailyset.contract.module.async

import org.tty.dailyset.contract.declare.*
import org.tty.dailyset.contract.descriptor.*

/**
 * the builder for resource sync module (sync)
 */
interface ResourceSyncBuilderAsync<TC: ResourceContent,ES, EC> {

    fun <TE: Any> registerSetDescriptor(descriptor: ResourceSetDescriptorAsync<TE, ES>)

    fun <TE: Any> registerLinkDescriptor(descriptor: ResourceLinkDescriptorAsync<TE, EC>)

    fun <TE: Any> registerTemporalLinkDescriptor(descriptor: ResourceTemporalLinkDescriptorAsync<TE, EC>)

    fun <TE: Any> registerContentDescriptor(descriptor: ResourceContentDescriptorAsync<out TC, TE, EC>)

    fun <TE: Any> registerContentDescriptors(vararg descriptor: ResourceContentDescriptorAsync<out TC, in TE, EC>)

    fun <TE: Any> registerSetVisibilityDescriptor(descriptor: ResourceSetVisibilityDescriptorAsync<TE>)

    fun buildClient(): ResourceSyncClientAsync<TC, ES, EC>

    fun buildServer(): ResourceSyncServerAsync<TC, ES, EC>
}