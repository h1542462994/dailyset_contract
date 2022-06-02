package org.tty.dailyset.contract.bean.enums

/**
 * enum represents **resource set type.**
 */
enum class DailySetType(val value: Int) {
    Normal(0),
    Clazz(1),
    ClazzAuto(2),
    Task(3),
    Global(4),
    Generated(5),
    User(6)
}