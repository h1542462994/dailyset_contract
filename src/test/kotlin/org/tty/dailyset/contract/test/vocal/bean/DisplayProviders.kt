package org.tty.dailyset.contract.test.vocal.bean

import org.tty.dailyset.contract.test.formatTable

object DisplayProviders {
    @Suppress("UNCHECKED_CAST")
    fun <E> provide(collection: Collection<E>): CharSequence {
        return if (collection.isEmpty()) {
            "ResourceContent@U []"
        } else {
            val element = collection.first()
            when (element) {
                is Song -> {
                    collection as Collection<Song>
                    formatTable(
                        "Song", collection,
                        title = listOf("uid", "name", "albumUid", "description", "contentLength"),
                        selector = { listOf(it.uid, it.name, it.albumUid, it.description, it.contentLength) }
                    )
                }
                is Album -> {
                    collection as Collection<Album>
                    formatTable("Album", collection,
                        title = listOf("uid", "name", "songUids", "description" ),
                        selector = { listOf(it.uid, it.name, it.songUids, it.description) }
                    )
                }
                else -> {
                    "Unknown [???]"
                }
            }
        }
    }
}