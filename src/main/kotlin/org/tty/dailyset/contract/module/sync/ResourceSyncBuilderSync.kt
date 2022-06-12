package org.tty.dailyset.contract.module.sync

import org.tty.dailyset.contract.dao.sync.TransactionSupportSync
import org.tty.dailyset.contract.declare.*
import org.tty.dailyset.contract.descriptor.*

/**
 * the builder for resource sync module (sync)
 */
interface ResourceSyncBuilderSync<TS: ResourceSet<ES>, TL: ResourceLink<EC>, TTL: ResourceTemporalLink<EC>, TC: ResourceContent, TV: ResourceSetVisibility, ES, EC> {

    fun <TE> registerSetDescriptor(descriptor: ResourceSetDescriptorSync<TS, TE, ES>)

    fun <TE> registerLinkDescriptor(descriptor: ResourceLinkDescriptorSync<TL, TE, EC>)

    fun <TE> registerTemporalLinkDescriptor(descriptor: ResourceTemporalLinkDescriptorSync<TTL, TE, EC>)

    fun <TE> registerContentDescriptor(descriptor: ResourceContentDescriptorSync<out TC, TE, EC>)

    fun registerContentDescriptors(vararg descriptors: ResourceContentDescriptorSync<out TC, *, EC>)

    fun <TE> registerSetVisibilityDescriptor(descriptor: ResourceSetVisibilityDescriptorSync<TV, TE>)

    fun useTransactionSupport(transactionSupport: TransactionSupportSync)

    fun buildClient(): ResourceSyncClientSync<TS, TL, TTL, TC, TV, ES, EC>

    fun buildServer(): ResourceSyncServerSync<TS, TL, TC, ES, EC>
}