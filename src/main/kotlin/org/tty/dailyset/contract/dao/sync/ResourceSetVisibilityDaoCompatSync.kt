package org.tty.dailyset.contract.dao.sync

interface ResourceSetVisibilityDaoCompatSync<TV> {
    fun findAllByUserUid(userUid: String)
    fun applies(visibilities: TV): Int
}