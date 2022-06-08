package org.tty.dailyset.contract.module

import org.tty.dailyset.contract.declare.ResourceContent
import org.tty.dailyset.contract.descriptor.ResourceContentDescriptor

class DailySetResourceSyncBuilder: ResourceSyncBuilderSync<DailySetResourceSyncModule> {
    override fun <T : ResourceContent, TE, EC> registerContentDescriptor(descriptor: ResourceContentDescriptor<T, TE, EC>) {
        TODO("Not yet implemented")
    }
    override fun build(): DailySetResourceSyncModule {
        TODO("Not yet implemented")
    }
}