package org.tty.dailyset.contract.descriptor

import org.tty.dailyset.contract.dao.async.ResourceContentDaoCompatAsync
import org.tty.dailyset.contract.declare.ResourceContent

interface ResourceContentDescriptorAsync<T: ResourceContent, TE: Any, EC>: ResourceContentDescriptor<T, TE, EC>, ResourceDescriptorAsync {
    val resourceContentDaoCompatAsync: ResourceContentDaoCompatAsync<TE>
}