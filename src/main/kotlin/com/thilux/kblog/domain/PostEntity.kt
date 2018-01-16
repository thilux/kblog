package com.thilux.kblog.domain

import org.jetbrains.squash.definition.*

/**
 * Created by tsantana on 05/01/18.
 */

object PostEntity : TableDefinition() {
    val id = integer("id").autoIncrement()
    val title = varchar("title", 256)
    val content = blob("content")
    val creationDate = datetime("creation_date").nullable()
}