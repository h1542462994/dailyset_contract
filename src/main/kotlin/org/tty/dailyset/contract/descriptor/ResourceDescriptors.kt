package org.tty.dailyset.contract.descriptor

import org.tty.dailyset.contract.dao.async.ResourceDaoCompatAsync
import org.tty.dailyset.contract.dao.sync.ResourceContentDaoCompatSync
import org.tty.dailyset.contract.declare.ResourceContent
import org.tty.dailyset.contract.declare.ResourceConverter
import org.tty.dailyset.contract.declare.ResourceEquality
import kotlin.reflect.KType
import kotlin.reflect.typeOf

class ResourceContentDescriptorSyncImpl<T: ResourceContent, TE, EC>(
    override val type: KType,
    override val contentType: EC,
    override val equality: ResourceEquality<T>?,
    override val converter: ResourceConverter<T, TE>?,
    override val resourceContentDaoCompatSync: ResourceContentDaoCompatSync<TE>
): ResourceContentDescriptorSync<T, TE, EC>

inline fun <reified T: ResourceContent, EC> resourceContentDescriptorSync(
    contentType: EC,
    resourceContentDaoCompatSync: ResourceContentDaoCompatSync<T>,
    equality: ResourceEquality<T>? = null
): ResourceContentDescriptorSync<T, T, EC> {
    return ResourceContentDescriptorSyncImpl(
        type = typeOf<T>(),
        contentType = contentType,
        equality = equality,
        converter = null,
        resourceContentDaoCompatSync = resourceContentDaoCompatSync
    )
}