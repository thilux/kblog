package com.thilux.kblog.dto

import java.util.*

/**
 * Created by tsantana on 15/12/17.
 */

data class Comment(val postId: Int, val commentator: String, val content: String, val commentDate: Date) : Domain {
    override var id: Int? = null;
}