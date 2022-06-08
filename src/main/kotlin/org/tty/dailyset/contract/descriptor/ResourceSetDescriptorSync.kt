package org.tty.dailyset.contract.descriptor

import org.tty.dailyset.contract.dao.sync.ResourceSetDaoCompatSync
import org.tty.dailyset.contract.declare.ResourceSet

interface ResourceSetDescriptorSync<T: ResourceSet<ES>, TE, ES>: ResourceSetDescriptor<T, TE, ES>, ResourceDescriptorSync {
    val resourceSetDaoCompatSync: ResourceSetDaoCompatSync<TE>
}