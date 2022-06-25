package org.tty.dailyset.contract.test

import org.tty.dailyset.contract.bean.entity.*
import org.tty.dailyset.contract.bean.enums.DailySetContentType
import org.tty.dailyset.contract.bean.enums.DailySetType
import org.tty.dailyset.contract.dao.sync.ResourceContentDaoCompatSync
import org.tty.dailyset.contract.descriptor.*
import org.tty.dailyset.contract.module.resourceSyncClientSync
import org.tty.dailyset.contract.module.resourceSyncServerSync

class SampleCodes {

    fun sampleOfBuildClientSync() {
       val client = resourceSyncClientSync<DailySetContent, DailySetType, DailySetContentType> {
            registerSetDescriptor(
                resourceSetDescriptorSync(resourceSetDaoCompatSync = emptyStub())
            )
            registerLinkDescriptor(
                resourceLinkDescriptorSync(resourceLinkDaoCompatSync = emptyStub())
            )
            registerTemporaryLinkDescriptor(
                resourceTemporaryLinkDescriptorSync(resourceTemporaryLinkDaoCompatSync = emptyStub())
            )
            registerSetVisibilityDescriptor(
                resourceSetVisibilityDescriptorSync(resourceSetVisibilityDaoCompatSync = emptyStub())
            )
            registerContentDescriptors(
                resourceContentDescriptorSync(DailySetContentType.Duration, resourceContentDaoCompatSync = emptyStub<ResourceContentDaoCompatSync<DailySetDuration>>()),
                resourceContentDescriptorSync(DailySetContentType.Course, resourceContentDaoCompatSync = emptyStub<ResourceContentDaoCompatSync<DailySetCourse>>()),
                resourceContentDescriptorSync(DailySetContentType.Table, resourceContentDaoCompatSync = emptyStub<ResourceContentDaoCompatSync<DailySetTable>>()),
                resourceContentDescriptorSync(DailySetContentType.Row, resourceContentDaoCompatSync = emptyStub<ResourceContentDaoCompatSync<DailySetRow>>()),
                resourceContentDescriptorSync(DailySetContentType.Cell, resourceContentDaoCompatSync = emptyStub<ResourceContentDaoCompatSync<DailySetCell>>()),
                resourceContentDescriptorSync(DailySetContentType.Basic, resourceContentDaoCompatSync = emptyStub<ResourceContentDaoCompatSync<DailySetBasic>>()),
                resourceContentDescriptorSync(DailySetContentType.Usage, resourceContentDaoCompatSync = emptyStub<ResourceContentDaoCompatSync<DailySetUsage>>()),
                resourceContentDescriptorSync(DailySetContentType.StudentInfo, resourceContentDaoCompatSync = emptyStub<ResourceContentDaoCompatSync<DailySetStudentInfo>>()),
                resourceContentDescriptorSync(DailySetContentType.SchoolInfo, resourceContentDaoCompatSync = emptyStub<ResourceContentDaoCompatSync<DailySetSchoolInfo>>())
            )
            useTransactionSupport(emptyStub())
        }
    }

    fun sampleOfBuildServerSync() {
        val server = resourceSyncServerSync<DailySetContent, DailySetType, DailySetContentType> {
            registerSetDescriptor(
                resourceSetDescriptorSync(resourceSetDaoCompatSync = emptyStub())
            )
            registerLinkDescriptor(
                resourceLinkDescriptorSync(resourceLinkDaoCompatSync = emptyStub())
            )
            registerContentDescriptors(
                resourceContentDescriptorSync(DailySetContentType.Duration, resourceContentDaoCompatSync = emptyStub<ResourceContentDaoCompatSync<DailySetDuration>>()),
                resourceContentDescriptorSync(DailySetContentType.Course, resourceContentDaoCompatSync = emptyStub<ResourceContentDaoCompatSync<DailySetCourse>>()),
                resourceContentDescriptorSync(DailySetContentType.Table, resourceContentDaoCompatSync = emptyStub<ResourceContentDaoCompatSync<DailySetTable>>()),
                resourceContentDescriptorSync(DailySetContentType.Row, resourceContentDaoCompatSync = emptyStub<ResourceContentDaoCompatSync<DailySetRow>>()),
                resourceContentDescriptorSync(DailySetContentType.Cell, resourceContentDaoCompatSync = emptyStub<ResourceContentDaoCompatSync<DailySetCell>>()),
                resourceContentDescriptorSync(DailySetContentType.Basic, resourceContentDaoCompatSync = emptyStub<ResourceContentDaoCompatSync<DailySetBasic>>()),
                resourceContentDescriptorSync(DailySetContentType.Usage, resourceContentDaoCompatSync = emptyStub<ResourceContentDaoCompatSync<DailySetUsage>>()),
                resourceContentDescriptorSync(DailySetContentType.StudentInfo, resourceContentDaoCompatSync = emptyStub<ResourceContentDaoCompatSync<DailySetStudentInfo>>()),
                resourceContentDescriptorSync(DailySetContentType.SchoolInfo, resourceContentDaoCompatSync = emptyStub<ResourceContentDaoCompatSync<DailySetSchoolInfo>>())
            )
            useTransactionSupport(emptyStub())
        }
    }
}