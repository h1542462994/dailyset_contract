package org.tty.dailyset.contract.module

import kotlin.reflect.KType

interface ResourceSyncBuilder<TM> {

    fun registerContentTypes(vararg types: KType)

    fun build(): TM

}