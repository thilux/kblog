package com.thilux.kblog.repository

import com.thilux.kblog.dto.CommentDto

/**
 * Created by tsantana on 16/12/17.
 */

object CommentRepository: MemoryRepository<CommentDto>() {

    fun getFromPostId(postId: String): List<CommentDto> {

        return getAll().filter { it.postId.toString() == postId }

    }
}