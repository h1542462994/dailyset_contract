package org.tty.dailyset.contract.module.async

import org.tty.dailyset.contract.declare.*

interface ResourceSyncClientAsync<TC: ResourceContent, ES, EC>:
    ResourceSyncModuleAsync<TC, ES, EC> {
}