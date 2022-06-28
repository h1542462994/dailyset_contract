package org.tty.dailyset.contract.test.vocal

import org.junit.jupiter.api.*
import org.tty.dailyset.contract.data.applyingReq
import org.tty.dailyset.contract.declare.ResourceDefaults
import org.tty.dailyset.contract.test.vocal.bean.*
import java.time.LocalDateTime
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertNull

class VocalTestServer {
    companion object {
        private val vocalEngine = VocalEngine()
        private val vocalServer = vocalEngine.server
        private val vocalEngineSeeder = VocalEngineSeeder(vocalEngine)

        const val SET_UID = "my_vocal"
    }

    @Test
    fun testCreateSet() {
        println(" |: createSet")
        // seed the engine to init
        vocalEngineSeeder.call(VocalSeedUntil.Init)
        // in start, the set of my_vocal is not created.
        assertNull(vocalServer.readBase(SET_UID))
        assertThrows<IllegalArgumentException> { vocalServer.read(SET_UID) }
        vocalEngineSeeder.call(VocalSeedUntil.CreateSet)
        println(vocalEngine.setsServer)
        // check the version is 1
        var set = assertNotNull(vocalServer.readBase(SET_UID))
        assertEquals(ResourceDefaults.VERSION_INIT, set.version)
        // create the set repeatedly
        vocalEngineSeeder.call(VocalSeedUntil.CreateSet)
        set = assertNotNull(vocalServer.readBase(SET_UID))
        // check the version is 1
        assertEquals(ResourceDefaults.VERSION_INIT, set.version)
    }

    /**
     * add 1 album and 3 musics.
     */
    @Test
    fun testApplyElementNoUid() {
        println(" |: applyElementNoUid")
        // add 1 album and 3 musics
        vocalEngineSeeder.callToEnd(VocalSeedUntil.ApplyElement)
        println(vocalEngine.linksServer)
        println(vocalEngine.musicsServer)
        // test count
        val result = vocalServer.read(SET_UID)
        val musics = result.getVariance<Music>(VocalContentType.Music)
        assertEquals(VocalEngineSeeder.MUSIC_COUNT_1, musics.size)

    }

    /**
     * add 1 album and 3 musics.
     *
     * then apply 1 music (name equal)
     * check data changed.
     */
    @Test
    fun testApplyElementKeySelector() {
        println(" |: applyElementKeySelector")
        // add 1 album and 3 musics, same as testApplyNoUid
        vocalEngineSeeder.callToEnd(VocalSeedUntil.ApplyElement)
        val name = "Tokimeki Runners"
        val newDescription = "@Music..insert"
        // apply a music.
        val writeMusic = Music("value will not write", name, ResourceDefaults.EMPTY_UID, newDescription, 40009641)
        val existedMusic = vocalServer
            .read(SET_UID)
            .getVariance<Music>(VocalContentType.Music)
            .find { it.name == name }!!

        vocalServer.write(applyingReq(SET_UID) {
            apply(VocalContentType.Music, writeMusic)
        }, LocalDateTime.now())
        println(vocalEngine.linksServer)
        println(vocalEngine.musicsServer)
        // check count
        val musics = vocalServer.read(SET_UID).getVariance<Music>(VocalContentType.Music)
        assertEquals(VocalEngineSeeder.MUSIC_COUNT_1, musics.size)
        // check the uid is not changed
        val readMusic = musics.find { it.name == name }!!
        assertEquals(existedMusic.uid, readMusic.uid)
    }

    /**
     * add 1 album and 3 musics.
     *
     * then remove 1 music (name equal)
     * check data changed.
     *
     * then remove 1 music not existed
     * check data changed.
     *
     * then removeAll musics
     * check data changed.
     *
     * then add 1 music not existed but not memory deleted
     * check data changed.
     */
    @Test
    fun testRemoveElement() {
        println(" |: removeElement ")
        // add 1 album and 3 musics, same as testApplyNoUid
        vocalEngineSeeder.callToEnd(VocalSeedUntil.ApplyElement)
        val name = "Tokimeki Runners"
        val musics = vocalServer.read(SET_UID).getVariance<Music>(VocalContentType.Music)
        val music = musics.find { it.name == name }!!

        // :remove 1 music
        vocalServer.write(applyingReq(SET_UID) {
            remove(VocalContentType.Music, music)
        }, LocalDateTime.now())
        val newMusics = vocalServer.read(SET_UID).getVariance<Music>(VocalContentType.Music)

        println(vocalEngine.setsServer)
        println(vocalEngine.linksServer)
        println(vocalEngine.musicsServer)
        // check count
        assertEquals(VocalEngineSeeder.MUSIC_COUNT_1 - 1, newMusics.size)

        val notExistedMusic = Music(ResourceDefaults.EMPTY_UID, "not existed", ResourceDefaults.EMPTY_UID, "Not Existed", 0)
        // :remove 1 music
        vocalServer.write(applyingReq(SET_UID) {
            remove(VocalContentType.Music, notExistedMusic)
        }, LocalDateTime.now())

        println(vocalEngine.setsServer)
        // check count and version
        assertEquals(VocalEngineSeeder.MUSIC_COUNT_1 - 1, vocalServer.read(SET_UID).getVariance<Music>(VocalContentType.Music).size)
        assertEquals(4, vocalServer.readBase(SET_UID)?.version)

        // :removeAll
        vocalServer.write(applyingReq(SET_UID) {
            removeAll(VocalContentType.Music)
        }, LocalDateTime.now())
        assertEquals(0, vocalServer.read(SET_UID).getVariance<Music>(VocalContentType.Music).size)
        println(vocalEngine.linksServer)

        // :apply 1 music
        vocalServer.write(applyingReq(SET_UID) {
            apply(VocalContentType.Music, VocalEngineSeeder.MUSIC_1)
        }, LocalDateTime.now())
        assertEquals(1, vocalServer.read(SET_UID).getVariance<Music>(VocalContentType.Music).size)
        println(vocalEngine.linksServer)
    }

    /**
     * add 1 album and 3 musics
     *
     * then replace with 2 musics, check data changed
     */
    @Test
    fun testReplaceElement() {
        println(" :| replaceElement")
        vocalEngineSeeder.callToEnd(VocalSeedUntil.ApplyElement)
        // :replace 2 musics
        vocalServer.write(applyingReq(SET_UID) {
            replaces(VocalContentType.Music, listOf(VocalEngineSeeder.MUSIC_1, VocalEngineSeeder.MUSIC_4))
        }, LocalDateTime.now())

        // test count
        val musics = vocalServer.read(SET_UID).getVariance<Music>(VocalContentType.Music)
        assertEquals(2, musics.size)

        println(vocalEngine.linksServer)
        println(vocalEngine.musicsServer)
    }

    /**
     * add 1 album and 3 musics
     * single 1 music -> error
     */
    @Test
    fun testSingleElement() {
        println(" :| singleElement")
        vocalEngineSeeder.callToEnd(VocalSeedUntil.ApplyElement)
        // single 3 musics
        assertThrows<IllegalStateException> {
            vocalServer.write(applyingReq(SET_UID) {
                single(VocalContentType.Music, VocalEngineSeeder.MUSIC_1)
            }, LocalDateTime.now())
        }
        // single 1 album
        vocalServer.write(applyingReq(SET_UID) {
            single(VocalContentType.Album, Album("__not assigned__", "新的专辑", emptyList(), "新的专辑"))
        }, LocalDateTime.now())
        // test count
        val albums = vocalServer.read(SET_UID).getVariance<Album>(VocalContentType.Album)
        assertEquals(1, albums.size)

        println(vocalEngine.albumsServer)
    }




}