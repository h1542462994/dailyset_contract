package org.tty.dailyset.contract.dao.sync

import org.tty.dailyset.contract.declare.ResourceContent

/**
 * compat dao for [ResourceContent]
 * @param TC type for content. it will be resource content (T) or the storage entity data (TE).
 * @see org.tty.dailyset.contract.descriptor.ResourceContentDescriptor
 */
interface ResourceContentDaoCompatSync<TC> {
    fun findAllByUids(uids: List<String>): List<TC>
    fun applies(contents: List<TC>): Int
}