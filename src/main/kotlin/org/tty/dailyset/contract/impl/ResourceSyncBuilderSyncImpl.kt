package org.tty.dailyset.contract.impl

import org.tty.dailyset.contract.declare.*
import org.tty.dailyset.contract.descriptor.*
import org.tty.dailyset.contract.module.sync.ResourceSyncBuilderSync
import org.tty.dailyset.contract.module.sync.ResourceSyncClientSync
import org.tty.dailyset.contract.module.sync.ResourceSyncServerSync

class ResourceSyncBuilderSyncImpl<TS: ResourceSet<ES>, TL: ResourceLink<EC>, TTL: ResourceTemporalLink<EC>, TC: ResourceContent, TV: ResourceSetVisibility, ES, EC>: ResourceSyncBuilderSync<TS, TL, TTL, TC, TV, ES, EC> {
    private var setDescriptor: ResourceSetDescriptorSync<TS, *, ES>? = null
    private var linkDescriptor: ResourceLinkDescriptorSync<TL, *, EC>? = null
    private var temporalLinkDescriptor: ResourceTemporalLinkDescriptorSync<TTL, *, EC>? = null
    private var contentDescriptors: MutableList<ResourceContentDescriptorSync<out TC, *, EC>> = mutableListOf()
    private var setVisibilityDescriptor: ResourceSetVisibilityDescriptorSync<TV, *>? = null

    override fun <TE> registerSetDescriptor(descriptor: ResourceSetDescriptorSync<TS, TE, ES>) {
        this.setDescriptor = descriptor
    }

    override fun <TE> registerLinkDescriptor(descriptor: ResourceLinkDescriptorSync<TL, TE, EC>) {
        this.linkDescriptor = descriptor
    }

    override fun <TE> registerTemporalLinkDescriptor(descriptor: ResourceTemporalLinkDescriptorSync<TTL, TE, EC>) {
        this.temporalLinkDescriptor = descriptor
    }

    override fun <TE> registerContentDescriptor(descriptor: ResourceContentDescriptorSync<out TC, TE, EC>) {
        this.contentDescriptors.add(descriptor)
    }

    override fun registerContentDescriptors(vararg descriptors: ResourceContentDescriptorSync<out TC, *, EC>) {
        this.contentDescriptors = mutableListOf(*descriptors)
    }

    override fun <TE> registerSetVisibilityDescriptor(descriptor: ResourceSetVisibilityDescriptorSync<TV, TE>) {
        this.setVisibilityDescriptor = descriptor
    }

    override fun buildClient(): ResourceSyncClientSync<TS, TL, TTL, TC, TV, ES, EC> {
        val descriptorSet = DescriptorSetSync(
            setDescriptor = setDescriptor!!,
            linkDescriptor = linkDescriptor!!,
            temporalLinkDescriptor = temporalLinkDescriptor!!,
            contentDescriptors = contentDescriptors,
            setVisibilityDescriptor = setVisibilityDescriptor!!
        )
        return ResourceSyncClientSyncImpl(descriptorSet)
    }

    override fun buildServer(): ResourceSyncServerSync<TS, TL, TC, ES, EC> {
        val descriptorSet = DescriptorSetSync(
            setDescriptor = setDescriptor!!,
            linkDescriptor = linkDescriptor!!,
            temporalLinkDescriptor = temporalLinkDescriptor!!,
            contentDescriptors = contentDescriptors,
            setVisibilityDescriptor = setVisibilityDescriptor!!
        )
        return ResourceSyncServerSyncImpl(descriptorSet)
    }
}