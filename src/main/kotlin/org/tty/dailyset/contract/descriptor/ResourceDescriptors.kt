/**
 * factory functions to create resource descriptor.
 */

package org.tty.dailyset.contract.descriptor

import org.tty.dailyset.contract.dao.async.*
import org.tty.dailyset.contract.dao.sync.*
import org.tty.dailyset.contract.declare.*
import kotlin.reflect.KType
import kotlin.reflect.full.isSubtypeOf
import kotlin.reflect.typeOf

class ResourceContentDescriptorSyncImpl<T: ResourceContent, TE, EC>(
    override val type: KType,
    override val contentType: EC,
    override val equality: ResourceEquality<T>,
    override val converter: ResourceConverter<T, TE>,
    override val resourceContentDaoCompatSync: ResourceContentDaoCompatSync<TE>
): ResourceContentDescriptorSync<T, TE, EC>

class ResourceContentDescriptorAsyncImpl<T: ResourceContent, TE, EC>(
    override val type: KType,
    override val contentType: EC,
    override val equality: ResourceEquality<T>,
    override val converter: ResourceConverter<T, TE>,
    override val resourceContentDaoCompatAsync: ResourceContentDaoCompatAsync<TE>
): ResourceContentDescriptorAsync<T, TE, EC>

class ResourceSetDescriptorSyncImpl<T: ResourceSet<ES>, TE, ES>(
    override val type: KType,
    override val converter: ResourceConverter<T, TE>,
    override val resourceSetDaoCompatSync: ResourceSetDaoCompatSync<TE>
): ResourceSetDescriptorSync<T, TE, ES>

class ResourceSetDescriptorAsyncImpl<T: ResourceSet<ES>, TE, ES>(
    override val type: KType,
    override val converter: ResourceConverter<T, TE>,
    override val resourceSetDaoCompatAsync: ResourceSetDaoCompatAsync<TE>
): ResourceSetDescriptorAsync<T, TE, ES>

class ResourceLinkDescriptorSyncImpl<T: ResourceLink<EC>, TE, EC>(
    override val type: KType,
    override val converter: ResourceConverter<T, TE>,
    override val resourceLinkDaoCompatSync: ResourceLinkDaoCompatSync<EC, TE>
): ResourceLinkDescriptorSync<T, TE, EC>

class ResourceLinkDescriptorAsyncImpl<T: ResourceLink<EC>, TE, EC>(
    override val type: KType,
    override val converter: ResourceConverter<T, TE>,
    override val resourceLinkDaoCompatAsync: ResourceLinkDaoCompatAsync<EC, TE>
): ResourceLinkDescriptorAsync<T, TE, EC>

class ResourceTemporalLinkDescriptorSyncImpl<T: ResourceTemporalLink<EC>, TE, EC>(
    override val type: KType,
    override val converter: ResourceConverter<T, TE>,
    override val resourceTemporalLinkDaoCompatSync: ResourceTemporalLinkDaoCompatSync<EC, TE>
): ResourceTemporalLinkDescriptorSync<T, TE, EC>

class ResourceTemporalLinkDescriptorAsyncImpl<T: ResourceTemporalLink<EC>, TE, EC>(
    override val type: KType,
    override val converter: ResourceConverter<T, TE>,
    override val resourceTemporalLinkDaoCompatAsync: ResourceTemporalLinkDaoCompatAsync<EC, TE>
): ResourceTemporalLinkDescriptorAsync<T, TE, EC>

class ResourceSetVisibilityDescriptorSyncImpl<T: ResourceSetVisibility, TE>(
    override val type: KType,
    override val converter: ResourceConverter<T, TE>,
    override val resourceSetVisibilityDaoCompatSync: ResourceSetVisibilityDaoCompatSync<TE>
): ResourceSetVisibilityDescriptorSync<T, TE>

class ResourceSetVisibilityDescriptorAsyncImpl<T: ResourceSetVisibility, TE>(
    override val type: KType,
    override val converter: ResourceConverter<T, TE>,
    override val resourceSetVisibilityDaoCompatAsync: ResourceSetVisibilityDaoCompatAsync<TE>
): ResourceSetVisibilityDescriptorAsync<T, TE>

/**
 * if [ResourceEquality] is not assigned, equality by [ResourceEquals] will be the default.
 */
inline fun <reified T> defaultEquality(): ResourceEquality<T> {
    val type = typeOf<T>()
    if (type.isSubtypeOf(typeOf<ResourceEquals<T>>())) {
        @Suppress("UNCHECKED_CAST")
        return resourceEquality { first, second ->
            (first as ResourceEquals<T>).resourceEqual(second)
        }
    } else {
        return resourceEquality { first, second -> first == second }
    }
}

/**
 * a default [ResourceConverter] if the storage data and the used data are same.
 */
inline fun <reified T> defaultConverter(): ResourceConverter<T, T> {
    return resourceSameConverter()
}

inline fun <reified T: ResourceContent, EC> resourceContentDescriptorSync(
    contentType: EC,
    resourceContentDaoCompatSync: ResourceContentDaoCompatSync<T>,
    equality: ResourceEquality<T> = defaultEquality()
): ResourceContentDescriptorSync<T, T, EC> {
    return ResourceContentDescriptorSyncImpl(
        type = typeOf<T>(),
        contentType = contentType,
        equality = equality,
        converter = defaultConverter(),
        resourceContentDaoCompatSync = resourceContentDaoCompatSync
    )
}

inline fun <reified T: ResourceContent, TE, EC> resourceContentDescriptorSyncWithConverter(
    contentType: EC,
    resourceContentDaoCompatSync: ResourceContentDaoCompatSync<TE>,
    converter: ResourceConverter<T, TE>,
    equality: ResourceEquality<T> = defaultEquality(),
): ResourceContentDescriptorSync<T, TE, EC> {
    return ResourceContentDescriptorSyncImpl(
        type = typeOf<T>(),
        contentType = contentType,
        equality = equality,
        converter = converter,
        resourceContentDaoCompatSync = resourceContentDaoCompatSync
    )
}

inline fun <reified T: ResourceContent, EC> resourceContentDescriptorAsync(
    contentType: EC,
    resourceContentDaoCompatAsync: ResourceContentDaoCompatAsync<T>,
    equality: ResourceEquality<T> = defaultEquality()
): ResourceContentDescriptorAsync<T, T, EC> {
    return ResourceContentDescriptorAsyncImpl(
        type = typeOf<T>(),
        contentType = contentType,
        equality = equality,
        converter = defaultConverter(),
        resourceContentDaoCompatAsync = resourceContentDaoCompatAsync
    )
}

inline fun <reified T: ResourceContent, TE, EC> resourceContentDescriptorAsyncWithConverter(
    contentType: EC,
    resourceContentDaoCompatAsync: ResourceContentDaoCompatAsync<TE>,
    converter: ResourceConverter<T, TE>,
    equality: ResourceEquality<T> = defaultEquality()
): ResourceContentDescriptorAsync<T, TE, EC> {
    return ResourceContentDescriptorAsyncImpl(
        type = typeOf<T>(),
        contentType = contentType,
        equality = equality,
        converter = converter,
        resourceContentDaoCompatAsync = resourceContentDaoCompatAsync
    )
}

inline fun <reified T: ResourceSet<ES>, ES> resourceSetDescriptorSync(
    resourceSetDaoCompatSync: ResourceSetDaoCompatSync<T>,
): ResourceSetDescriptorSync<T, T, ES> {
    return ResourceSetDescriptorSyncImpl(
        type = typeOf<T>(),
        converter = defaultConverter(),
        resourceSetDaoCompatSync = resourceSetDaoCompatSync
    )
}

inline fun <reified T: ResourceSet<ES>, TE, ES> resourceSetDescriptorSyncWithConverter(
    converter: ResourceConverter<T, TE>,
    resourceSetDaoCompatSync: ResourceSetDaoCompatSync<TE>
): ResourceSetDescriptorSync<T, TE, ES> {
    return ResourceSetDescriptorSyncImpl(
        type = typeOf<T>(),
        converter = converter,
        resourceSetDaoCompatSync = resourceSetDaoCompatSync
    )
}

inline fun <reified T: ResourceSet<ES>, ES> resourceSetDescriptorAsync(
    setType: ES,
    resourceSetDaoCompatAsync: ResourceSetDaoCompatAsync<T>
): ResourceSetDescriptorAsync<T, T, ES> {
    return ResourceSetDescriptorAsyncImpl(
        type = typeOf<T>(),
        converter = defaultConverter(),
        resourceSetDaoCompatAsync = resourceSetDaoCompatAsync
    )
}

inline fun <reified T: ResourceSet<ES>, TE, ES> resourceSetDescriptorAsyncWithConverter(
    setType: ES,
    converter: ResourceConverter<T, TE>,
    resourceSetDaoCompatAsync: ResourceSetDaoCompatAsync<TE>
): ResourceSetDescriptorAsync<T, TE, ES> {
    return ResourceSetDescriptorAsyncImpl(
        type = typeOf<T>(),
        converter = converter,
        resourceSetDaoCompatAsync = resourceSetDaoCompatAsync
    )
}

inline fun <reified T: ResourceLink<EC>, EC> resourceLinkDescriptorSync(
    resourceLinkDaoCompatSync: ResourceLinkDaoCompatSync<EC, T>
): ResourceLinkDescriptorSync<T, T, EC> {
    return ResourceLinkDescriptorSyncImpl(
        type = typeOf<T>(),
        converter = defaultConverter(),
        resourceLinkDaoCompatSync = resourceLinkDaoCompatSync
    )
}

inline fun <reified T: ResourceLink<EC>, TE, EC> resourceLinkDescriptorSyncWithConverter(
    contentType: EC,
    converter: ResourceConverter<T, TE>,
    resourceLinkDaoCompatSync: ResourceLinkDaoCompatSync<EC, TE>
): ResourceLinkDescriptorSync<T, TE, EC> {
    return ResourceLinkDescriptorSyncImpl(
        type = typeOf<T>(),
        converter = converter,
        resourceLinkDaoCompatSync = resourceLinkDaoCompatSync
    )
}

inline fun <reified T: ResourceLink<EC>, EC> resourceLinkDescriptorAsync(
    contentType: EC,
    resourceLinkDaoCompatAsync: ResourceLinkDaoCompatAsync<EC, T>
): ResourceLinkDescriptorAsync<T, T, EC> {
    return ResourceLinkDescriptorAsyncImpl(
        type = typeOf<T>(),
        converter = defaultConverter(),
        resourceLinkDaoCompatAsync = resourceLinkDaoCompatAsync
    )
}

inline fun <reified T: ResourceLink<EC>, TE, EC> resourceLinkDescriptorAsyncWithConverter(
    contentType: EC,
    converter: ResourceConverter<T, TE>,
    resourceLinkDaoCompatAsync: ResourceLinkDaoCompatAsync<EC, TE>
): ResourceLinkDescriptorAsync<T, TE, EC> {
    return ResourceLinkDescriptorAsyncImpl(
        type = typeOf<T>(),
        converter = converter,
        resourceLinkDaoCompatAsync = resourceLinkDaoCompatAsync
    )
}

inline fun <reified T: ResourceTemporalLink<EC>, EC> resourceTemporalLinkDescriptorSync(
    resourceTemporalLinkDaoCompatSync: ResourceTemporalLinkDaoCompatSync<EC, T>
): ResourceTemporalLinkDescriptorSync<T, T, EC> {
    return ResourceTemporalLinkDescriptorSyncImpl(
        type = typeOf<T>(),
        converter = defaultConverter(),
        resourceTemporalLinkDaoCompatSync = resourceTemporalLinkDaoCompatSync
    )
}

inline fun <reified T: ResourceTemporalLink<EC>, TE, EC> resourceTemporalLinkDescriptorSyncWithConverter(
    converter: ResourceConverter<T, TE>,
    resourceTemporalLinkDaoCompatSync: ResourceTemporalLinkDaoCompatSync<EC, TE>
): ResourceTemporalLinkDescriptorSync<T, TE, EC> {
    return ResourceTemporalLinkDescriptorSyncImpl(
        type = typeOf<T>(),
        converter = converter,
        resourceTemporalLinkDaoCompatSync = resourceTemporalLinkDaoCompatSync
    )
}

inline fun <reified T: ResourceTemporalLink<EC>, EC> resourceTemporalLinkDescriptorAsync(
    resourceTemporalLinkDaoCompatAsync: ResourceTemporalLinkDaoCompatAsync<EC, T>
): ResourceTemporalLinkDescriptorAsync<T, T, EC> {
    return ResourceTemporalLinkDescriptorAsyncImpl(
        type = typeOf<T>(),
        converter = defaultConverter(),
        resourceTemporalLinkDaoCompatAsync = resourceTemporalLinkDaoCompatAsync
    )
}

inline fun <reified T: ResourceTemporalLink<EC>, TE, EC> resourceTemporalLinkDescriptorAsyncWithConverter(
    converter: ResourceConverter<T, TE>,
    resourceTemporalLinkDaoCompatAsync: ResourceTemporalLinkDaoCompatAsync<EC, TE>
): ResourceTemporalLinkDescriptorAsync<T, TE, EC> {
    return ResourceTemporalLinkDescriptorAsyncImpl(
        type = typeOf<T>(),
        converter = converter,
        resourceTemporalLinkDaoCompatAsync = resourceTemporalLinkDaoCompatAsync
    )
}

inline fun <reified T: ResourceSetVisibility> resourceSetVisibilityDescriptorSync(
    resourceSetVisibilityDaoCompatSync: ResourceSetVisibilityDaoCompatSync<T>
): ResourceSetVisibilityDescriptorSync<T, T> {
    return ResourceSetVisibilityDescriptorSyncImpl(
        type = typeOf<T>(),
        converter = defaultConverter(),
        resourceSetVisibilityDaoCompatSync = resourceSetVisibilityDaoCompatSync
    )
}

inline fun <reified T: ResourceSetVisibility, TE> resourceSetVisibilityDescriptorSyncWithConverter(
    converter: ResourceConverter<T, TE>,
    resourceSetVisibilityDaoCompatSync: ResourceSetVisibilityDaoCompatSync<TE>
): ResourceSetVisibilityDescriptorSync<T, TE> {
    return ResourceSetVisibilityDescriptorSyncImpl(
        type = typeOf<T>(),
        converter = converter,
        resourceSetVisibilityDaoCompatSync = resourceSetVisibilityDaoCompatSync
    )
}

inline fun <reified T: ResourceSetVisibility> resourceSetVisibilityDescriptorAsync(
    resourceSetVisibilityDaoCompatAsync: ResourceSetVisibilityDaoCompatAsync<T>
): ResourceSetVisibilityDescriptorAsync<T, T> {
    return ResourceSetVisibilityDescriptorAsyncImpl(
        type = typeOf<T>(),
        converter = defaultConverter(),
        resourceSetVisibilityDaoCompatAsync = resourceSetVisibilityDaoCompatAsync
    )
}

inline fun <reified T: ResourceSetVisibility, TE> resourceSetVisibilityDescriptorAsyncWithConverter(
    converter: ResourceConverter<T, TE>,
    resourceSetVisibilityDaoCompatAsync: ResourceSetVisibilityDaoCompatAsync<TE>
): ResourceSetVisibilityDescriptorAsync<T, TE> {
    return ResourceSetVisibilityDescriptorAsyncImpl(
        type =  typeOf<T>(),
        converter = converter,
        resourceSetVisibilityDaoCompatAsync = resourceSetVisibilityDaoCompatAsync
    )
}
