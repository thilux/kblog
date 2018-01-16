package com.thilux.kblog.dto

import com.thilux.kblog.DatabaseHandler
import com.thilux.kblog.domain.CommentEntity
import com.thilux.kblog.repository.PostRepository
import org.jetbrains.squash.connection.BinaryObject
import org.jetbrains.squash.statements.InsertValuesStatement
import org.jetbrains.squash.statements.insertInto
import org.jetbrains.squash.statements.values
import java.time.LocalDateTime
import java.util.*

/**
 * Created by tsantana on 15/12/17.
 */

data class CommentDto(val postId: Int, val commentator: String, val content: String, val commentDate: LocalDateTime) : DomainDto<CommentEntity> {
    override var id: Int? = null;

    override fun toEntity(): InsertValuesStatement<CommentEntity, Unit> {
        return insertInto(CommentEntity).values {
            it[commentator] = this@CommentDto.commentator
            it[content] = BinaryObject.fromByteArray(DatabaseHandler.createTransaction(), this@CommentDto.content.toByteArray())
            it[commentDate] = this@CommentDto.commentDate
            it[post] = null
        }
    }

}