package org.tty.dailyset.contract.bean.annotation

import org.tty.dailyset.contract.bean.enums.DailySetContentType

/**
 * bind a **resource content** to specified contentType.
 */
@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.CLASS)
annotation class ContentBean(
    val contentType: DailySetContentType
)
