/**
 * factory functions to create resource descriptor.
 */

@file:Suppress("unused")

package org.tty.dailyset.contract.descriptor

import org.tty.dailyset.contract.dao.async.*
import org.tty.dailyset.contract.dao.sync.*
import org.tty.dailyset.contract.declare.*

internal class ResourceContentDescriptorSyncImpl<T: ResourceContent, TE: Any, EC>(
    override val contentType: EC,
    override val keySelector: KeySelector<T, Any>,
    override val converter: ResourceConverter<T, TE>,
    override val resourceContentDaoCompatSync: ResourceContentDaoCompatSync<TE>
): ResourceContentDescriptorSync<T, TE, EC>

internal class ResourceContentDescriptorAsyncImpl<T: ResourceContent, TE: Any, EC>(
    override val contentType: EC,
    override val keySelector: KeySelector<T, Any>,
    override val converter: ResourceConverter<T, TE>,
    override val resourceContentDaoCompatAsync: ResourceContentDaoCompatAsync<TE>
): ResourceContentDescriptorAsync<T, TE, EC>

internal class ResourceSetDescriptorSyncImpl<TE: Any, ES>(
    override val converter: ResourceConverter<ResourceSet<ES>, TE>,
    override val resourceSetDaoCompatSync: ResourceSetDaoCompatSync<TE>
): ResourceSetDescriptorSync<TE, ES>

internal class ResourceSetDescriptorAsyncImpl<TE: Any, ES>(
    override val converter: ResourceConverter<ResourceSet<ES>, TE>,
    override val resourceSetDaoCompatAsync: ResourceSetDaoCompatAsync<TE>
): ResourceSetDescriptorAsync<TE, ES>

internal class ResourceLinkDescriptorSyncImpl<TE: Any, EC>(
    override val converter: ResourceConverter<ResourceLink<EC>, TE>,
    override val resourceLinkDaoCompatSync: ResourceLinkDaoCompatSync<EC, TE>
): ResourceLinkDescriptorSync<TE, EC>

internal class ResourceLinkDescriptorAsyncImpl<TE: Any, EC>(
    override val converter: ResourceConverter<ResourceLink<EC>, TE>,
    override val resourceLinkDaoCompatAsync: ResourceLinkDaoCompatAsync<EC, TE>
): ResourceLinkDescriptorAsync<TE, EC>

internal class ResourceTemporaryLinkDescriptorSyncImpl<TE: Any, EC>(
    override val converter: ResourceConverter<ResourceTemporaryLink<EC>, TE>,
    override val resourceTemporaryLinkDaoCompatSync: ResourceTemporaryLinkDaoCompatSync<EC, TE>
): ResourceTemporaryLinkDescriptorSync<TE, EC>

internal class ResourceTemporaryLinkDescriptorAsyncImpl<TE: Any, EC>(
    override val converter: ResourceConverter<ResourceTemporaryLink<EC>, TE>,
    override val resourceTemporaryLinkDaoCompatAsync: ResourceTemporaryLinkDaoCompatAsync<EC, TE>
): ResourceTemporaryLinkDescriptorAsync<TE, EC>

internal class ResourceSetVisibilityDescriptorSyncImpl<TE: Any>(
    override val converter: ResourceConverter<ResourceSetVisibility, TE>,
    override val resourceSetVisibilityDaoCompatSync: ResourceSetVisibilityDaoCompatSync<TE>
): ResourceSetVisibilityDescriptorSync<TE>

internal class ResourceSetVisibilityDescriptorAsyncImpl<TE: Any>(
    override val converter: ResourceConverter<ResourceSetVisibility, TE>,
    override val resourceSetVisibilityDaoCompatAsync: ResourceSetVisibilityDaoCompatAsync<TE>
): ResourceSetVisibilityDescriptorAsync<TE>

/**
 * a default [ResourceConverter] if the storage data and the used data are same.
 */
fun <T> defaultConverter(): ResourceConverter<T, T> {
    return resourceSameConverter()
}

fun <T: ResourceContent, TE: Any, EC> resourceContentDescriptorSync(
    contentType: EC,
    converter: ResourceConverter<T, TE>,
    resourceContentDaoCompatSync: ResourceContentDaoCompatSync<TE>,
    keySelector: KeySelector<T, Any> = DefaultKeySelector(),
): ResourceContentDescriptorSync<T, TE, EC> {
    return ResourceContentDescriptorSyncImpl(
        contentType, keySelector, converter, resourceContentDaoCompatSync
    )
}

fun <T: ResourceContent, TE: Any, EC> resourceContentDescriptorAsync(
    contentType: EC,
    converter: ResourceConverter<T, TE>,
    resourceContentDaoCompatAsync: ResourceContentDaoCompatAsync<TE>,
    keySelector: KeySelector<T, Any> = DefaultKeySelector()
): ResourceContentDescriptorAsync<T, TE, EC> {
    return ResourceContentDescriptorAsyncImpl(
        contentType, keySelector, converter, resourceContentDaoCompatAsync
    )
}

fun <T: ResourceContent, EC> resourceContentDescriptorSync(
    contentType: EC,
    resourceContentDaoCompatSync: ResourceContentDaoCompatSync<T>,
    keySelector: KeySelector<T, Any> = DefaultKeySelector(),
): ResourceContentDescriptorSync<T, T, EC> {
    return resourceContentDescriptorSync(
        contentType = contentType,
        keySelector = keySelector,
        converter = defaultConverter(),
        resourceContentDaoCompatSync = resourceContentDaoCompatSync
    )
}

fun <T: ResourceContent, TE: ResourceContent, EC> resourceContentDescriptorSyncWithConverter(
    contentType: EC,
    resourceContentDaoCompatSync: ResourceContentDaoCompatSync<TE>,
    converter: ResourceConverter<T, TE>,
    keySelector: KeySelector<T, Any> = DefaultKeySelector()
): ResourceContentDescriptorSync<T, TE, EC> {
    return resourceContentDescriptorSync(
        contentType = contentType,
        keySelector = keySelector,
        converter = converter,
        resourceContentDaoCompatSync = resourceContentDaoCompatSync
    )
}

fun <T: ResourceContent, EC> resourceContentDescriptorAsync(
    contentType: EC,
    resourceContentDaoCompatAsync: ResourceContentDaoCompatAsync<T>,
    keySelector: KeySelector<T, Any> = DefaultKeySelector()
): ResourceContentDescriptorAsync<T, T, EC> {
    return resourceContentDescriptorAsync(
        contentType = contentType,
        keySelector = keySelector,
        converter = defaultConverter(),
        resourceContentDaoCompatAsync = resourceContentDaoCompatAsync
    )
}

fun <T: ResourceContent, TE: ResourceContent, EC> resourceContentDescriptorAsyncWithConverter(
    contentType: EC,
    resourceContentDaoCompatAsync: ResourceContentDaoCompatAsync<TE>,
    converter: ResourceConverter<T, TE>,
    keySelector: KeySelector<T, Any> = DefaultKeySelector()
): ResourceContentDescriptorAsync<T, TE, EC> {
    return resourceContentDescriptorAsync(
        contentType = contentType,
        keySelector = keySelector,
        converter = converter,
        resourceContentDaoCompatAsync = resourceContentDaoCompatAsync
    )
}
fun <ES> resourceSetDescriptorSync(
    resourceSetDaoCompatSync: ResourceSetDaoCompatSync<ResourceSet<ES>>,
): ResourceSetDescriptorSync<ResourceSet<ES>, ES> {
    return ResourceSetDescriptorSyncImpl(
        converter = defaultConverter(),
        resourceSetDaoCompatSync = resourceSetDaoCompatSync
    )
}

fun <TE: Any, ES> resourceSetDescriptorSyncWithConverter(
    converter: ResourceConverter<ResourceSet<ES>, TE>,
    resourceSetDaoCompatSync: ResourceSetDaoCompatSync<TE>
): ResourceSetDescriptorSync<TE, ES> {
    return ResourceSetDescriptorSyncImpl(
        converter = converter,
        resourceSetDaoCompatSync = resourceSetDaoCompatSync
    )
}

fun <ES> resourceSetDescriptorAsync(
    resourceSetDaoCompatAsync: ResourceSetDaoCompatAsync<ResourceSet<ES>>
): ResourceSetDescriptorAsync<ResourceSet<ES>, ES> {
    return ResourceSetDescriptorAsyncImpl(
        converter = defaultConverter(),
        resourceSetDaoCompatAsync = resourceSetDaoCompatAsync
    )
}

fun <TE: Any, ES> resourceSetDescriptorAsyncWithConverter(
    converter: ResourceConverter<ResourceSet<ES>, TE>,
    resourceSetDaoCompatAsync: ResourceSetDaoCompatAsync<TE>
): ResourceSetDescriptorAsync<TE, ES> {
    return ResourceSetDescriptorAsyncImpl(
        converter = converter,
        resourceSetDaoCompatAsync = resourceSetDaoCompatAsync
    )
}

fun <EC> resourceLinkDescriptorSync(
    resourceLinkDaoCompatSync: ResourceLinkDaoCompatSync<EC, ResourceLink<EC>>
): ResourceLinkDescriptorSync<ResourceLink<EC>, EC> {
    return ResourceLinkDescriptorSyncImpl(
        converter = defaultConverter(),
        resourceLinkDaoCompatSync = resourceLinkDaoCompatSync
    )
}

fun <TE: Any, EC> resourceLinkDescriptorSyncWithConverter(
    converter: ResourceConverter<ResourceLink<EC>, TE>,
    resourceLinkDaoCompatSync: ResourceLinkDaoCompatSync<EC, TE>
): ResourceLinkDescriptorSync<TE, EC> {
    return ResourceLinkDescriptorSyncImpl(
        converter = converter,
        resourceLinkDaoCompatSync = resourceLinkDaoCompatSync
    )
}

fun <EC> resourceLinkDescriptorAsync(
    resourceLinkDaoCompatAsync: ResourceLinkDaoCompatAsync<EC, ResourceLink<EC>>
): ResourceLinkDescriptorAsync<ResourceLink<EC>, EC> {
    return ResourceLinkDescriptorAsyncImpl(
        converter = defaultConverter(),
        resourceLinkDaoCompatAsync = resourceLinkDaoCompatAsync
    )
}

fun <TE: Any, EC> resourceLinkDescriptorAsyncWithConverter(
    converter: ResourceConverter<ResourceLink<EC>, TE>,
    resourceLinkDaoCompatAsync: ResourceLinkDaoCompatAsync<EC, TE>
): ResourceLinkDescriptorAsync<TE, EC> {
    return ResourceLinkDescriptorAsyncImpl(
        converter = converter,
        resourceLinkDaoCompatAsync = resourceLinkDaoCompatAsync
    )
}

fun <EC> resourceTemporaryLinkDescriptorSync(
    resourceTemporaryLinkDaoCompatSync: ResourceTemporaryLinkDaoCompatSync<EC, ResourceTemporaryLink<EC>>
): ResourceTemporaryLinkDescriptorSync<ResourceTemporaryLink<EC>, EC> {
    return ResourceTemporaryLinkDescriptorSyncImpl(
        converter = defaultConverter(),
        resourceTemporaryLinkDaoCompatSync = resourceTemporaryLinkDaoCompatSync
    )
}

fun <TE: Any, EC> resourceTemporaryLinkDescriptorSyncWithConverter(
    converter: ResourceConverter<ResourceTemporaryLink<EC>, TE>,
    resourceTemporaryLinkDaoCompatSync: ResourceTemporaryLinkDaoCompatSync<EC, TE>
): ResourceTemporaryLinkDescriptorSync<TE, EC> {
    return ResourceTemporaryLinkDescriptorSyncImpl(
        converter = converter,
        resourceTemporaryLinkDaoCompatSync = resourceTemporaryLinkDaoCompatSync
    )
}

fun <EC> resourceTemporaryLinkDescriptorAsync(
    resourceTemporaryLinkDaoCompatAsync: ResourceTemporaryLinkDaoCompatAsync<EC, ResourceTemporaryLink<EC>>
): ResourceTemporaryLinkDescriptorAsync<ResourceTemporaryLink<EC>, EC> {
    return ResourceTemporaryLinkDescriptorAsyncImpl(
        converter = defaultConverter(),
        resourceTemporaryLinkDaoCompatAsync = resourceTemporaryLinkDaoCompatAsync
    )
}

fun <TE: Any, EC> resourceTemporaryLinkDescriptorAsyncWithConverter(
    converter: ResourceConverter<ResourceTemporaryLink<EC>, TE>,
    resourceTemporalLinkDaoCompatAsync: ResourceTemporaryLinkDaoCompatAsync<EC, TE>
): ResourceTemporaryLinkDescriptorAsync<TE, EC> {
    return ResourceTemporaryLinkDescriptorAsyncImpl(
        converter = converter,
        resourceTemporaryLinkDaoCompatAsync = resourceTemporalLinkDaoCompatAsync
    )
}

fun resourceSetVisibilityDescriptorSync(
    resourceSetVisibilityDaoCompatSync: ResourceSetVisibilityDaoCompatSync<ResourceSetVisibility>
): ResourceSetVisibilityDescriptorSync<ResourceSetVisibility> {
    return ResourceSetVisibilityDescriptorSyncImpl(
        converter = defaultConverter(),
        resourceSetVisibilityDaoCompatSync = resourceSetVisibilityDaoCompatSync
    )
}

fun <TE: Any> resourceSetVisibilityDescriptorSyncWithConverter(
    converter: ResourceConverter<ResourceSetVisibility, TE>,
    resourceSetVisibilityDaoCompatSync: ResourceSetVisibilityDaoCompatSync<TE>
): ResourceSetVisibilityDescriptorSync<TE> {
    return ResourceSetVisibilityDescriptorSyncImpl(
        converter = converter,
        resourceSetVisibilityDaoCompatSync = resourceSetVisibilityDaoCompatSync
    )
}

fun resourceSetVisibilityDescriptorAsync(
    resourceSetVisibilityDaoCompatAsync: ResourceSetVisibilityDaoCompatAsync<ResourceSetVisibility>
): ResourceSetVisibilityDescriptorAsync<ResourceSetVisibility> {
    return ResourceSetVisibilityDescriptorAsyncImpl(
        converter = defaultConverter(),
        resourceSetVisibilityDaoCompatAsync = resourceSetVisibilityDaoCompatAsync
    )
}

fun <TE: Any> resourceSetVisibilityDescriptorAsyncWithConverter(
    converter: ResourceConverter<ResourceSetVisibility, TE>,
    resourceSetVisibilityDaoCompatAsync: ResourceSetVisibilityDaoCompatAsync<TE>
): ResourceSetVisibilityDescriptorAsync<TE> {
    return ResourceSetVisibilityDescriptorAsyncImpl(
        converter = converter,
        resourceSetVisibilityDaoCompatAsync = resourceSetVisibilityDaoCompatAsync
    )
}
