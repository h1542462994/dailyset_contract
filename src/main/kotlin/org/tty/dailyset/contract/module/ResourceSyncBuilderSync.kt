package org.tty.dailyset.contract.module

import org.tty.dailyset.contract.declare.ResourceContent
import org.tty.dailyset.contract.descriptor.ResourceContentDescriptor

interface ResourceSyncBuilderSync<TM> {

    fun <T: ResourceContent, TE, EC> registerContentDescriptor(descriptor: ResourceContentDescriptor<T, TE, EC>)

    fun build(): TM

}