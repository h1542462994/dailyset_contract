package org.tty.dailyset.contract.bean.enums

/**
 * enum represents **resource content type.**
 */
enum class DailySetContentType(val value: Int) {
    Table(101),
    Row(102),
    Cell(103),
    Duration(104),
    Course(105),

    Basic(201),
    Usage(202),
    SchoolInfo(203),
    StudentInfo(204),
}
