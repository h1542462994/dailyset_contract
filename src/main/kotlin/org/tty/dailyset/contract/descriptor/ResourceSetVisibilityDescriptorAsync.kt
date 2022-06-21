package org.tty.dailyset.contract.descriptor

import org.tty.dailyset.contract.dao.async.ResourceSetVisibilityDaoCompatAsync

interface ResourceSetVisibilityDescriptorAsync<TE: Any>: ResourceSetVisibilityDescriptor<TE>, ResourceDescriptorAsync {
    val resourceSetVisibilityDaoCompatAsync: ResourceSetVisibilityDaoCompatAsync<TE>
}