package org.tty.dailyset.contract.dao.sync

interface ResourceSetVisibilityDaoCompatSync<TV> {
    fun findAllByUserUid(userUid: String): List<TV>
    fun applies(visibilities: List<TV>): Int

}