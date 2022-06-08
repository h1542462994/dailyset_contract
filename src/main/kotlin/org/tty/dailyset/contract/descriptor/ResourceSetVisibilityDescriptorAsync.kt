package org.tty.dailyset.contract.descriptor

import org.tty.dailyset.contract.dao.async.ResourceSetVisibilityDaoCompatAsync
import org.tty.dailyset.contract.declare.ResourceSetVisibility

interface ResourceSetVisibilityDescriptorAsync<T: ResourceSetVisibility, TE>: ResourceSetVisibilityDescriptor<T, TE>, ResourceDescriptorAsync {
    val resourceSetVisibilityDaoCompatAsync: ResourceSetVisibilityDaoCompatAsync<TE>
}