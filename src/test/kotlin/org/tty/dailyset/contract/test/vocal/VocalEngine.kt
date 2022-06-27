package org.tty.dailyset.contract.test.vocal

import org.tty.dailyset.contract.descriptor.*
import org.tty.dailyset.contract.module.resourceSyncClientSync
import org.tty.dailyset.contract.module.resourceSyncServerSync
import org.tty.dailyset.contract.test.vocal.bean.*
import org.tty.dailyset.contract.test.vocal.memory.*

class VocalEngine {
    // define your dao compats.
    val setsServer = InMemoryResourceSets<VocalType>()
    val linksServer = InMemoryResourceLinks<VocalContentType>()

    val musicsServer = InMemoryResourceContents<Music>()
    val albumsServer = InMemoryResourceContents<Album>()

    val setsClient = InMemoryResourceSets<VocalType>()
    val linksClient = InMemoryResourceLinks<VocalContentType>()
    val temporaryLinksClient = InMemoryResourceTemporaryLinks<VocalContentType>()
    val musicsClient = InMemoryResourceContents<Music>()
    val albumsClient = InMemoryResourceContents<Album>()
    val setVisibilitiesClient = InMemoryResourceSetVisibilities()

    // create the server
    val server = resourceSyncServerSync<VocalContent, VocalType, VocalContentType> {
        registerSetDescriptor(
            resourceSetDescriptorSync(setsServer)
        )
        registerLinkDescriptor(
            resourceLinkDescriptorSync(linksServer)
        )
        registerContentDescriptors(
            resourceContentDescriptorSync(
                contentType = VocalContentType.Music,
                resourceContentDaoCompatSync = musicsServer
            ),
            resourceContentDescriptorSync(
                contentType = VocalContentType.Album,
                resourceContentDaoCompatSync = albumsServer
            )
        )
    }

    val client = resourceSyncClientSync<VocalContent, VocalType, VocalContentType> {
        registerSetDescriptor(
            resourceSetDescriptorSync(setsClient)
        )
        registerLinkDescriptor(
            resourceLinkDescriptorSync(linksClient)
        )
        registerTemporaryLinkDescriptor(
            resourceTemporaryLinkDescriptorSync(temporaryLinksClient)
        )
        registerContentDescriptors(
            resourceContentDescriptorSync(
                contentType = VocalContentType.Music,
                resourceContentDaoCompatSync = musicsClient
            ),
            resourceContentDescriptorSync(
                contentType = VocalContentType.Album,
                resourceContentDaoCompatSync = albumsClient
            )
        )
        registerSetVisibilityDescriptor(
            resourceSetVisibilityDescriptorSync(setVisibilitiesClient)
        )
    }
}