package com.thilux.kblog.domain

import org.jetbrains.squash.definition.*

/**
 * Created by tsantana on 05/01/18.
 */

object CommentEntity : TableDefinition() {
    val id = integer("id").autoIncrement().uniqueIndex()
    val commentator = varchar("commentator", 100)
    val content = blob("content")
    val post = reference(PostEntity.id, "post_id")
    val commentDate = datetime("comment_date")
}