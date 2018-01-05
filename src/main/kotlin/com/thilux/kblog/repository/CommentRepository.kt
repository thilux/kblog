package com.thilux.kblog.repository

import com.thilux.kblog.dto.Comment

/**
 * Created by tsantana on 16/12/17.
 */

object CommentRepository: MemoryRepository<Comment>() {

    fun getFromPostId(postId: String): List<Comment> {

        return getAll().filter { it.postId.toString() == postId }

    }
}