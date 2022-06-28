@file:Suppress("PrivatePropertyName")

package org.tty.dailyset.contract.test.vocal

import org.tty.dailyset.contract.data.applyingReq
import org.tty.dailyset.contract.declare.ResourceDefaults
import org.tty.dailyset.contract.declare.ResourceSet
import org.tty.dailyset.contract.test.vocal.bean.Album
import org.tty.dailyset.contract.test.vocal.bean.Music
import org.tty.dailyset.contract.test.vocal.bean.VocalContentType
import org.tty.dailyset.contract.test.vocal.bean.VocalType
import java.time.LocalDateTime
import kotlin.reflect.jvm.internal.impl.descriptors.Visibilities.Local


internal class VocalEngineSeeder(private val vocalEngine: VocalEngine) {



    data class OrderedAction(
        val order: Int,
        val func: () -> Unit
    )

    private fun callUnit(value: Int) {
        for (action in ORDERED_ACTIONS) {
            if (action.order == value) {
                action.func.invoke()
            } else if (action.order > value) {
                break
            }
        }
    }

    private fun callRange(range: IntRange) {
        for (action in ORDERED_ACTIONS) {
            if (action.order in range) {
                action.func.invoke()
            } else if (action.order > range.last) {
                break
            }
        }
    }

    fun callToEnd(last: VocalSeedUntil) {
        callRange(VocalSeedUntil.Init.order .. last.order)
    }

    fun callBetween(first: VocalSeedUntil, end: VocalSeedUntil) {
        callRange(first.order .. end.order)
    }

    fun call(until: VocalSeedUntil) {
        callUnit(until.order)
    }

    var ticksRecorded = mutableListOf<LocalDateTime>()
    private fun tick(): LocalDateTime {
        val now = LocalDateTime.now()
        ticksRecorded.add(now)
        return now
    }

    /**
     * orderedAction: 0
     */
    private val INIT_ACTION: OrderedAction = OrderedAction(VocalSeedUntil.Init.order) {
        ticksRecorded.clear()
        vocalEngine.setsServer.clear()
        vocalEngine.linksServer.clear()
        vocalEngine.musicsServer.clear()
        vocalEngine.albumsServer.clear()
    }

    /**
     * orderedAction: 1
     */
    private val ADD_SET_ACTION: OrderedAction = OrderedAction(VocalSeedUntil.CreateSet.order) {
        vocalEngine.server.createIfAbsent(VOCAL_SET)
    }

    private val APPLY_ELEMENT_ACTION: OrderedAction = OrderedAction(VocalSeedUntil.ApplyElement.order) {
        vocalEngine.server.write(applyingReq(SET_UID) {
            apply(VocalContentType.Album, ALBUM)
            applies(VocalContentType.Music, listOf(MUSIC_1, MUSIC_2, MUSIC_3))
        }, tick())
    }



    /**
     * orderedAction: 2
     */
    private val ORDERED_ACTIONS = buildList {
        add(INIT_ACTION)
        add(ADD_SET_ACTION)
        add(APPLY_ELEMENT_ACTION)
    }



    companion object {

        private const val SET_UID = VocalTestServer.SET_UID
        private val VOCAL_SET = ResourceSet(SET_UID, VocalType.Normal, ResourceDefaults.VERSION_ZERO)
        private val ALBUM = Album(ResourceDefaults.EMPTY_UID, "LoveLive虹咲学园偶像同好会-Tokimeki Runners", emptyList(), "@Album")
        val MUSIC_1 = Music(ResourceDefaults.EMPTY_UID, "Tokimeki Runners", ResourceDefaults.EMPTY_UID, "@Music1", 40009641)
        private val MUSIC_2 = Music(ResourceDefaults.EMPTY_UID, "夢への一歩", ResourceDefaults.EMPTY_UID, "@Music2", 37820201)
        private val MUSIC_3 = Music(ResourceDefaults.EMPTY_UID, "CHASE!", ResourceDefaults.EMPTY_UID, "@Music3", 34693281)
        val MUSIC_4 = Music(ResourceDefaults.EMPTY_UID, "ダイアモンド", ResourceDefaults.EMPTY_UID, "@Music4", 31795561)

        const val MUSIC_COUNT_1 = 3
    }



}