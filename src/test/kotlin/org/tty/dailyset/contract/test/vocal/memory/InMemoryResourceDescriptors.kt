package org.tty.dailyset.contract.test.vocal.memory

import org.tty.dailyset.contract.declare.*
import org.tty.dailyset.contract.descriptor.*

fun <ES> inMemoryResourceSetDescriptorSync(inMemoryResourceSets: InMemoryResourceSets<ES> = InMemoryResourceSets()): ResourceSetDescriptorSync<ResourceSet<ES>, ES> {
    return resourceSetDescriptorSync(
        inMemoryResourceSets
    )
}

fun <EC> inMemoryResourceLinkDescriptorSync(inMemoryResourceLinks: InMemoryResourceLinks<EC> = InMemoryResourceLinks()): ResourceLinkDescriptorSync<ResourceLink<EC>, EC> {
    return resourceLinkDescriptorSync(
        inMemoryResourceLinks
    )
}

fun <T: ResourceContent, EC> inMemoryResourceContentDescriptorSync(contentType: EC, inMemoryResourceContents: InMemoryResourceContents<T> = InMemoryResourceContents()): ResourceContentDescriptorSync<T, T, EC> {
    return resourceContentDescriptorSync(
        contentType = contentType,
        resourceContentDaoCompatSync = inMemoryResourceContents,
        keySelector = defaultKeySelectorFunc()
    )
}