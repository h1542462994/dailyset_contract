package org.tty.dailyset.contract.bean.serializer

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import java.time.LocalTime

// FIXME: use kotlinx.localTime instead. it is not support on multiplatform(kmm).
/**
 * a serializer for [LocalTime] that uses the ISO-8601 representation.
 *
 * @see LocalTime.parse
 * @see LocalTime.toString
 */
@OptIn(ExperimentalSerializationApi::class)
@Serializer(forClass = LocalTime::class)
object LocalTimeIso8601Serializer: KSerializer<LocalTime> {

    override val descriptor: SerialDescriptor =  PrimitiveSerialDescriptor("LocalTime", PrimitiveKind.STRING)

    override fun deserialize(decoder: Decoder): LocalTime {
        return LocalTime.parse(decoder.decodeString())
    }

    override fun serialize(encoder: Encoder, value: LocalTime) {
        encoder.encodeString(value.toString())
    }

}