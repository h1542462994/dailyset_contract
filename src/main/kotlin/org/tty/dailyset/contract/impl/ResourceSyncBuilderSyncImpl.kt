package org.tty.dailyset.contract.impl

import org.tty.dailyset.contract.declare.*
import org.tty.dailyset.contract.descriptor.*
import org.tty.dailyset.contract.module.sync.ResourceSyncBuilderSync
import org.tty.dailyset.contract.module.sync.ResourceSyncClientSync
import org.tty.dailyset.contract.module.sync.ResourceSyncServerSync

class ResourceSyncBuilderSyncImpl<TS: ResourceSet<ES>, TL: ResourceLink<EC>, TTL: ResourceTemporalLink<EC>, TC: ResourceContent, TV: ResourceSetVisibility, ES, EC>: ResourceSyncBuilderSync<TS, TL, TTL, TC, TV, ES, EC> {
    override fun <TE> registerSetDescriptor(descriptor: ResourceSetDescriptorSync<TS, TE, ES>) {
        TODO("Not yet implemented")
    }

    override fun <TE> registerLinkDescriptor(descriptor: ResourceLinkDescriptorSync<TL, TE, EC>) {
        TODO("Not yet implemented")
    }

    override fun <TE> registerTemporalLinkDescriptor(descriptor: ResourceTemporalLinkDescriptorSync<TTL, TE, EC>) {
        TODO("Not yet implemented")
    }

    override fun <TE> registerContentDescriptor(descriptor: ResourceContentDescriptorSync<out TC, TE, EC>) {
        TODO("Not yet implemented")
    }

    override fun registerContentDescriptors(vararg descriptors: ResourceContentDescriptorSync<out TC, *, EC>) {
        TODO("Not yet implemented")
    }

    override fun <TE> registerSetVisibilityDescriptor(descriptor: ResourceSetVisibilityDescriptorSync<TV, TE>) {
        TODO("Not yet implemented")
    }

    override fun buildClient(): ResourceSyncClientSync<TS, TL, TTL, TC, TV, ES, EC> {
        TODO("Not yet implemented")
    }

    override fun buildServer(): ResourceSyncServerSync<TS, TL, TC, ES, EC> {
        TODO("Not yet implemented")
    }
}