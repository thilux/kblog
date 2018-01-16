package com.thilux.kblog.repository

import com.thilux.kblog.DatabaseHandler
import com.thilux.kblog.LOG
import com.thilux.kblog.domain.PostEntity
import com.thilux.kblog.dto.PostDto
import com.thilux.kblog.fetchIntIdColumnKey
import com.thilux.kblog.valuesForEntityWithIntId
import org.jetbrains.squash.connection.BinaryObject
import org.jetbrains.squash.expressions.eq
import org.jetbrains.squash.query.select
import org.jetbrains.squash.query.where
import org.jetbrains.squash.results.get
import org.jetbrains.squash.statements.deleteFrom
import org.jetbrains.squash.statements.insertInto
import java.nio.charset.Charset

/**
 * Created by tsantana on 15/12/17.
 */

object PostRepository: SquashRepository<PostDto>() {

    override fun add(into: PostDto): PostDto = DatabaseHandler.withTransaction {
        val insertStatement = insertInto(PostEntity).valuesForEntityWithIntId {
            it[PostEntity.title] = into.title
            it[PostEntity.content] = BinaryObject.fromByteArray(DatabaseHandler.createTransaction(), into.content.toByteArray())
            it[PostEntity.creationDate] = into.creationDate
            PostEntity
        }

        insertStatement.fetchIntIdColumnKey(PostEntity.id)

        val createdId = insertStatement.execute()

        into.id = createdId

        into
    }

    override fun getOneById(id: Int): PostDto = DatabaseHandler.withTransaction {
        val row = PostEntity.select().where { PostEntity.id eq id }.execute().single()
        val postDto = PostDto(
                row[PostEntity.title],
                row[PostEntity.content].bytes.toString(Charset.defaultCharset()),
                row[PostEntity.creationDate])
        postDto.id = row[PostEntity.id]
        postDto
    }

    override fun getAll(): List<PostDto>  = DatabaseHandler.withTransaction {
        val rows = PostEntity.select().execute().toList()
        val postDtos = ArrayList<PostDto>()

        rows.forEach {
            val postDto = PostDto(
                    it[PostEntity.title],
                    it[PostEntity.content].bytes.toString(Charset.defaultCharset()),
                    it[PostEntity.creationDate])
            postDto.id = it[PostEntity.id]
            postDtos.add(postDto)
        }

        postDtos
    }

    override fun remove(id: Int)  {
        DatabaseHandler.withTransaction {
            LOG.debug("Deleting post with ID=>$id")
            getOneById(id).id ?: throw IllegalArgumentException("No post record found with ID $id")
            deleteFrom(PostEntity).where { PostEntity.id eq id }.execute()
        }
    }

}