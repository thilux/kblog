package com.thilux.kblog.repository

import com.thilux.kblog.DatabaseHandler
import com.thilux.kblog.domain.CommentEntity
import com.thilux.kblog.dto.CommentDto
import com.thilux.kblog.fetchIntIdColumnKey
import com.thilux.kblog.valuesForEntityWithIntId
import org.jetbrains.squash.connection.BinaryObject
import org.jetbrains.squash.expressions.eq
import org.jetbrains.squash.query.select
import org.jetbrains.squash.query.where
import org.jetbrains.squash.results.ResultRow
import org.jetbrains.squash.results.get
import org.jetbrains.squash.statements.deleteFrom
import org.jetbrains.squash.statements.insertInto
import java.nio.charset.Charset

/**
 * Created by tsantana on 16/12/17.
 */

object CommentRepository: SquashRepository<CommentDto>() {

    override fun add(into: CommentDto): CommentDto = DatabaseHandler.withTransaction {
        val insertStatement = insertInto(CommentEntity).valuesForEntityWithIntId {
            it[commentator] = into.commentator
            it[content] = BinaryObject.fromByteArray(DatabaseHandler.createTransaction(), into.content.toByteArray())
            it[post] = into.postId
            it[commentDate] = into.commentDate
            CommentEntity
        }

        insertStatement.fetchIntIdColumnKey(CommentEntity.id)

        val createdId = insertStatement.execute()

        into.id = createdId

        into
    }

    override fun getOneById(id: Int): CommentDto = DatabaseHandler.withTransaction {
        val row = CommentEntity.select().where { CommentEntity.id eq id }.execute().single()

        getCommentDtoFromRow(row)
    }

    override fun getAll(): List<CommentDto> = DatabaseHandler.withTransaction {
        val rows = CommentEntity.select().execute()
        val commentDtos = ArrayList<CommentDto>()

        rows.forEach {
            val commentDto = getCommentDtoFromRow(it)
            commentDtos.add(commentDto)
        }

        commentDtos
    }

    override fun remove(id: Int) = DatabaseHandler.withTransaction{
        getOneById(id)
        deleteFrom(CommentEntity).where { CommentEntity.id eq id }.execute()
    }

    fun getAllFromPostId(postId: Int): List<CommentDto> = DatabaseHandler.withTransaction{
        val rows = CommentEntity.select().where { CommentEntity.post eq postId }.execute()
        val commentDtos = ArrayList<CommentDto>()

        rows.forEach {
            commentDtos.add(getCommentDtoFromRow(it))
        }

        commentDtos
    }

    private fun getCommentDtoFromRow(row: ResultRow) : CommentDto {
        val commentDto = CommentDto(
                row[CommentEntity.post],
                row[CommentEntity.commentator],
                row[CommentEntity.content].bytes.toString(Charset.defaultCharset()),
                row[CommentEntity.commentDate])

        commentDto.id = row[CommentEntity.id]

        return commentDto
    }
}