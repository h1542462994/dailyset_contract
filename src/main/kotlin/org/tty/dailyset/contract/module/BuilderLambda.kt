package org.tty.dailyset.contract.module

fun dailySetResourceSyncModule(builderAction: DailySetResourceSyncBuilder.() -> Unit): DailySetResourceSyncModule {
    val builder = DailySetResourceSyncBuilder()
    builder.builderAction()
    return builder.build()
}