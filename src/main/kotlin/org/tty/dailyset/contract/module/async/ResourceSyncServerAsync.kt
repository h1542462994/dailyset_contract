package org.tty.dailyset.contract.module.async

import org.tty.dailyset.contract.declare.ResourceContent
import org.tty.dailyset.contract.declare.ResourceLink
import org.tty.dailyset.contract.declare.ResourceSet

interface ResourceSyncServerAsync<TC: ResourceContent, ES, EC>:
    ResourceSyncModuleAsync<TC, ES, EC> {
}