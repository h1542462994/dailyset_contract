package org.tty.dailyset.contract.descriptor

import org.tty.dailyset.contract.dao.async.ResourceSetDaoCompatAsync

interface ResourceSetDescriptorAsync<TE: Any, ES>: ResourceSetDescriptor<TE, ES>, ResourceDescriptorAsync {
    val resourceSetDaoCompatAsync: ResourceSetDaoCompatAsync<TE>
}