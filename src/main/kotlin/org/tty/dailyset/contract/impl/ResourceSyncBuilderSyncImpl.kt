package org.tty.dailyset.contract.impl

import org.tty.dailyset.contract.dao.sync.TransactionSupportSync
import org.tty.dailyset.contract.declare.*
import org.tty.dailyset.contract.descriptor.*
import org.tty.dailyset.contract.module.sync.ResourceSyncBuilderSync
import org.tty.dailyset.contract.module.sync.ResourceSyncClientSync
import org.tty.dailyset.contract.module.sync.ResourceSyncServerSync

class ResourceSyncBuilderSyncImpl<TC: ResourceContent, ES, EC>: ResourceSyncBuilderSync<TC, ES, EC> {
    private var setDescriptor: ResourceSetDescriptorSync<*, ES>? = null
    private var linkDescriptor: ResourceLinkDescriptorSync<*, EC>? = null
    private var temporaryLinkDescriptor: ResourceTemporaryLinkDescriptorSync<*, EC>? = null
    private var contentDescriptors: MutableList<ResourceContentDescriptorSync<out TC, *, EC>> = mutableListOf()
    private var setVisibilityDescriptor: ResourceSetVisibilityDescriptorSync<*>? = null
    private var transactionSupport: TransactionSupportSync? = null

    override fun <TE: Any> registerSetDescriptor(descriptor: ResourceSetDescriptorSync<TE, ES>) {
        this.setDescriptor = descriptor
    }

    override fun <TE: Any> registerLinkDescriptor(descriptor: ResourceLinkDescriptorSync<TE, EC>) {
        this.linkDescriptor = descriptor
    }

    override fun <TE: Any> registerTemporaryLinkDescriptor(descriptor: ResourceTemporaryLinkDescriptorSync<TE, EC>) {
        this.temporaryLinkDescriptor = descriptor
    }

    override fun <TE: Any> registerContentDescriptor(descriptor: ResourceContentDescriptorSync<out TC, TE, EC>) {
        this.contentDescriptors.add(descriptor)
    }

    override fun <TE : Any> registerContentDescriptors(vararg descriptors: ResourceContentDescriptorSync<out TC, in TE, EC>) {
        this.contentDescriptors = mutableListOf(*descriptors)
    }


    override fun <TE: Any> registerSetVisibilityDescriptor(descriptor: ResourceSetVisibilityDescriptorSync<TE>) {
        this.setVisibilityDescriptor = descriptor
    }

    override fun useTransactionSupport(transactionSupport: TransactionSupportSync) {
        this.transactionSupport = transactionSupport
    }

    override fun buildClient(): ResourceSyncClientSync<TC, ES, EC> {
        val descriptorSet = DescriptorSetSync(
            setDescriptor = setDescriptor!!,
            linkDescriptor = linkDescriptor!!,
            temporaryLinkDescriptor = temporaryLinkDescriptor!!,
            contentDescriptors = contentDescriptors,
            setVisibilityDescriptor = setVisibilityDescriptor!!
        )
        return ResourceSyncClientSyncImpl(descriptorSet, transactionSupport)
    }

    override fun buildServer(): ResourceSyncServerSync<TC, ES, EC> {
        val descriptorSet = DescriptorSetSync(
            setDescriptor = setDescriptor!!,
            linkDescriptor = linkDescriptor!!,
            temporaryLinkDescriptor = null,
            contentDescriptors = contentDescriptors,
            setVisibilityDescriptor = null
        )
        return ResourceSyncServerSyncImpl(descriptorSet, transactionSupport)
    }
}