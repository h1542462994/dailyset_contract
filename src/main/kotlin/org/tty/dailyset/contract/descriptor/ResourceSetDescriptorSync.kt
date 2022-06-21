package org.tty.dailyset.contract.descriptor

import org.tty.dailyset.contract.dao.sync.ResourceSetDaoCompatSync
import org.tty.dailyset.contract.declare.ResourceSet
interface ResourceSetDescriptorSync<TE: Any, ES>: ResourceSetDescriptor<TE, ES>, ResourceDescriptorSync {
    val resourceSetDaoCompatSync: ResourceSetDaoCompatSync<TE>
}