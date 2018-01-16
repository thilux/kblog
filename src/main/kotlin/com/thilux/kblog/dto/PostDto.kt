package com.thilux.kblog.dto

import com.thilux.kblog.DatabaseHandler
import com.thilux.kblog.domain.PostEntity
import org.jetbrains.squash.connection.BinaryObject
import org.jetbrains.squash.query.select
import org.jetbrains.squash.statements.InsertValuesStatement
import org.jetbrains.squash.statements.insertInto
import org.jetbrains.squash.statements.values
import java.time.LocalDateTime

/**
 * Created by tsantana on 15/12/17.
 */

data class PostDto(val title: String, val content: String, val creationDate: LocalDateTime?): DomainDto<PostEntity> {
    override var id: Int? = null

    override fun toEntity(): InsertValuesStatement<PostEntity, Unit> {
        return insertInto(PostEntity).values {
            it[title] = this@PostDto.title
            it[content] = BinaryObject.fromByteArray(DatabaseHandler.createTransaction(), this@PostDto.content.toByteArray())
            it[creationDate] = this@PostDto.creationDate
        }
    }
}