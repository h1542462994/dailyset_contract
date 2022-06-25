package org.tty.dailyset.contract.data
import kotlinx.serialization.Serializable

@Serializable
data class WrapData<T>(
    val data: List<T>
)