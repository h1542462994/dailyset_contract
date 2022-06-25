package org.tty.dailyset.contract.data

import org.tty.dailyset.contract.declare.ResourceContent
import org.tty.dailyset.contract.declare.ResourceSet
import kotlinx.serialization.Serializable
import org.tty.dailyset.contract.bean.serializer.LocalDateTimeIso8601Serializer
import java.time.Duration
import java.time.LocalDateTime

@Serializable
data class TemporaryResult<out TC: ResourceContent, ES, EC>(
    val set: ResourceSet<ES>,
    val typedResourcesTemp: List<TypedResourcesTemporary<TC, EC>>,
    @Serializable(with = LocalDateTimeIso8601Serializer::class)
    val timeUpload: LocalDateTime
) {
    fun adjustTick(timeWriting: LocalDateTime): TemporaryResult<TC, ES, EC> {
        val nanoDiff = Duration.between(timeUpload, timeWriting).nano
        return TemporaryResult(
            set,
            typedResourcesTemp.map { typedResourcesTemp ->
                TypedResourcesTemporary(
                    typedResourcesTemp.contentType,
                    typedResourcesTemp.resourceContentsTn.map {
                        ResourceContentTn(it.temporaryLink.copy(lastTick = it.temporaryLink.lastTick.plusNanos(nanoDiff.toLong())), it.content)
                    }
                )
            },
            timeUpload
        )
    }
}