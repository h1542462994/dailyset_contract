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

    @Test
    fun testRemoveElement() {
        println(" |: removeElement ")
        // add 1 album and 3 musics, same as testApplyNoUid
        vocalEngineSeeder.callToEnd(VocalSeedUntil.ApplyElement)

    }



}