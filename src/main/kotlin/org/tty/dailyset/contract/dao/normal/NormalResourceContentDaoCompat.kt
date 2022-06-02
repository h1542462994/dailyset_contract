package org.tty.dailyset.contract.dao.normal

import org.tty.dailyset.contract.bean.declare.ResourceContent

/**
 * compat dao for [ResourceContent]
 */
interface NormalResourceContentDaoCompat<TC: ResourceContent> {
    fun findAllByUids(uids: List<String>): List<TC>
    fun applies(contents: List<TC>): Int
}