package org.tty.dailyset.contract.module

import org.tty.dailyset.contract.bean.entity.DailySet
import org.tty.dailyset.contract.bean.entity.DailySetLink
import org.tty.dailyset.contract.bean.entity.DailySetTemporalLink
import org.tty.dailyset.contract.bean.enums.DailySetContentType
import org.tty.dailyset.contract.bean.enums.DailySetType

class DailySetResourceSyncModule: ResourceSyncModule<DailySetType, DailySetContentType, DailySet, DailySetLink, DailySetTemporalLink> {

}