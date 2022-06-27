package org.tty.dailyset.contract.test.vocal

import org.junit.jupiter.api.MethodOrderer
import org.junit.jupiter.api.Order
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestMethodOrder
import org.tty.dailyset.contract.data.applyingReq
import org.tty.dailyset.contract.declare.ResourceDefaults
import org.tty.dailyset.contract.declare.ResourceSet
import org.tty.dailyset.contract.test.vocal.bean.Album
import org.tty.dailyset.contract.test.vocal.bean.Music
import org.tty.dailyset.contract.test.vocal.bean.VocalContentType
import org.tty.dailyset.contract.test.vocal.bean.VocalType
import java.time.LocalDateTime
import kotlin.test.assertEquals
import kotlin.test.assertNull

@TestMethodOrder(MethodOrderer.OrderAnnotation::class)
class VocalTestServer {
    companion object {
        private val vocalEngine = VocalEngine()
        private val vocalServer = vocalEngine.server

        private const val setUid = "my_vocal"
    }

    @Test
    @Order(0)
    fun testCreateSet() {
        // in start, the set of my_vocal is not created.
        assertNull(vocalServer.readBase(setUid))

    }

    @Test
    @Order(4)
    fun testElementApply() {
        println("=== element apply:: ===")
        val music1 = Music("_id_neo_sky", "NEO_SKY, NEO MAP!", "_id_nijigasaki", "Ed of nijigasaki s01.", 1007)
        val music2 = Music("_id_susume", "全速ドリーマー", "_id_nijigasaki", "Vocal of distribution.", 9714)
        val album = Album("_id_nijigasaki", "NEO SKY, NEO MAP!", listOf("_id_neo_sky", "id_susume"), "description elements.")

        println(vocalEngine.setsServer)

        vocalServer.write(applyingReq("my_vocal") {
            apply(VocalContentType.Music, music1)
            apply(VocalContentType.Music, music2)
            apply(VocalContentType.Album, album)
        }, LocalDateTime.now())

        println(vocalEngine.linksServer)
        println(vocalEngine.musicsServer)
        println(vocalEngine.albumsServer)
    }

    @Test
    @Order(8)
    fun testElementApply2() {
        println("=== element apply2:: ===")

        val result = vocalServer.read("my_vocal")
        val musicResult = result.getVariance<Music>(VocalContentType.Music)
        val song = musicResult.resourceContents.find { it.uid == "_id_susume" }!!
        vocalServer.write(applyingReq("my_vocal") {
            apply(VocalContentType.Music, song.copy(description = "Des @V2", contentLength = 1000))
        }, LocalDateTime.now())

        println(vocalEngine.linksServer)
        println(vocalEngine.musicsServer)
    }

    @Test
    @Order(12)
    fun testElementApply3() {
        println("=== element apply3:: ===")

        val music = Music(ResourceDefaults.EMPTY_UID, "全速ドリーマー", "_id_nijigasaki", "Des @V3", 1001)
        vocalServer.write(applyingReq("my_vocal") {
            apply(VocalContentType.Music, music)
        }, LocalDateTime.now())

        println(vocalEngine.linksServer)
        println(vocalEngine.musicsServer)
    }

    @Test
    @Order(16)
    fun testGetUpdate() {
        println(vocalServer.readUpdate("my_vocal", ResourceDefaults.VERSION_ZERO))
        println(vocalServer.readUpdate("my_vocal", 2))

    }


}