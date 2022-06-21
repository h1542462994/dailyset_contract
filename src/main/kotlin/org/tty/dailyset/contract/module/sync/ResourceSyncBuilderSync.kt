package org.tty.dailyset.contract.module.sync

import org.tty.dailyset.contract.dao.sync.TransactionSupportSync
import org.tty.dailyset.contract.declare.*
import org.tty.dailyset.contract.descriptor.*

/**
 * the builder for resource sync module (sync)
 */
interface ResourceSyncBuilderSync<TC: ResourceContent, ES, EC> {

    fun <TE: Any> registerSetDescriptor(descriptor: ResourceSetDescriptorSync<TE, ES>)

    fun <TE: Any> registerLinkDescriptor(descriptor: ResourceLinkDescriptorSync<TE, EC>)

    fun <TE: Any> registerTemporalLinkDescriptor(descriptor: ResourceTemporalLinkDescriptorSync<TE, EC>)

    fun <TE: Any> registerContentDescriptor(descriptor: ResourceContentDescriptorSync<out TC, TE, EC>)

    fun <TE: Any> registerContentDescriptors(vararg descriptors: ResourceContentDescriptorSync<out TC, in TE, EC>)

    fun <TE: Any> registerSetVisibilityDescriptor(descriptor: ResourceSetVisibilityDescriptorSync<TE>)

    fun useTransactionSupport(transactionSupport: TransactionSupportSync)

    fun buildClient(): ResourceSyncClientSync<TC, ES, EC>

    fun buildServer(): ResourceSyncServerSync<TC, ES, EC>
}