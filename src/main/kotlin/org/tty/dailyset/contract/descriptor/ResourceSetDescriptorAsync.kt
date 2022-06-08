package org.tty.dailyset.contract.descriptor

import org.tty.dailyset.contract.dao.async.ResourceSetDaoCompatAsync
import org.tty.dailyset.contract.declare.ResourceSet

interface ResourceSetDescriptorAsync<T: ResourceSet<ES>, TE, ES>: ResourceSetDescriptor<T, TE, ES>, ResourceDescriptorAsync {
    val resourceSetDaoCompatAsync: ResourceSetDaoCompatAsync<TE>
}