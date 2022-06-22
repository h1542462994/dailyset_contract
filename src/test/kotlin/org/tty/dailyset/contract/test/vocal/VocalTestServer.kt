package org.tty.dailyset.contract.test.vocal

import org.junit.jupiter.api.MethodOrderer
import org.junit.jupiter.api.Order
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestMethodOrder
import org.tty.dailyset.contract.data.TypedResources
import org.tty.dailyset.contract.data.applyingReq
import org.tty.dailyset.contract.declare.ResourceDefaults
import org.tty.dailyset.contract.declare.ResourceSet
import org.tty.dailyset.contract.module.resourceSyncServerSync
import org.tty.dailyset.contract.test.vocal.bean.*
import org.tty.dailyset.contract.test.vocal.memory.*
import java.time.LocalDateTime
import kotlin.test.assertEquals

@TestMethodOrder(MethodOrderer.OrderAnnotation::class)
class VocalTestServer {
    companion object {
        private val inMemoryResourceSets = InMemoryResourceSets<VocalType>()
        private val inMemoryResourceLinks = InMemoryResourceLinks<VocalContentType>()
        private val inMemorySongs = InMemoryResourceContents<Song>()
        private val inMemoryAlbums = InMemoryResourceContents<Album>()

        private val vocalSyncServer = resourceSyncServerSync<VocalContent, VocalType, VocalContentType> {
            registerSetDescriptor(inMemoryResourceSetDescriptorSync(inMemoryResourceSets))
            registerLinkDescriptor(inMemoryResourceLinkDescriptorSync(inMemoryResourceLinks))
            registerContentDescriptors(
                inMemoryResourceContentDescriptorSync(VocalContentType.Song, inMemorySongs),
                inMemoryResourceContentDescriptorSync(VocalContentType.Album, inMemoryAlbums)
            )

        }
    }

    @Test
    @Order(0)
    fun testCreateSet() {
        println("=== create:: ===")

        val set = ResourceSet("my_vocal", VocalType.Normal, ResourceDefaults.VERSION_ZERO)
        val created = vocalSyncServer.createIfAbsent(set)

        assertEquals(1, created.version)
        assertEquals(1, inMemoryResourceSets.size)
        val element = inMemoryResourceSets["my_vocal"]!!
        assertEquals(1, element.version)

        println(inMemoryResourceSets)
    }

    @Test
    @Order(4)
    fun testElementApply() {
        println("=== element apply:: ===")
        val song1 = Song("_id_neo_sky", "NEO_SKY, NEO MAP!", "_id_nijigasaki", "Ed of nijigasaki s01.", 1007)
        val song2 = Song("_id_susume", "全速ドリーマー", "_id_nijigasaki", "Vocal of distribution.", 9714)
        val album = Album("_id_nijigasaki", "NEO SKY, NEO MAP!", listOf("_id_neo_sky", "id_susume"), "description elements.")

        println(inMemoryResourceSets)

        vocalSyncServer.write(applyingReq("my_vocal") {
            apply(VocalContentType.Song, song1)
            apply(VocalContentType.Song, song2)
            apply(VocalContentType.Album, album)
        }, LocalDateTime.now())

        println(inMemoryResourceLinks)
        println(inMemorySongs)
        println(inMemoryAlbums)
    }

    @Test
    @Order(8)
    fun testElementApply2() {
        println("=== element apply2:: ===")

        val result = vocalSyncServer.read("my_vocal")
        val songResult = result[VocalContentType.Song] as TypedResources<Song, VocalContentType>
        val song = songResult.resourceContents.find { it.uid == "_id_susume" }!!
        vocalSyncServer.write(applyingReq("my_vocal") {
            apply(VocalContentType.Song, song.copy(description = "Des @V2", contentLength = 1000))
        }, LocalDateTime.now())

        println(inMemoryResourceLinks)
        println(inMemorySongs)
    }

    @Test
    @Order(12)
    fun testElementApply3() {
        println("=== element apply3:: ===")

        val song = Song(ResourceDefaults.EMPTY_UID, "NEO_SKY, NEO MAP!", "_id_nijigasaki", "Des @V3", 1001)
        vocalSyncServer.write(applyingReq("my_vocal") {
            apply(VocalContentType.Song, song)
        }, LocalDateTime.now())

        println(inMemoryResourceLinks)
        println(inMemorySongs)
    }


}