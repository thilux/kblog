package com.thilux.kblog.dto

import java.util.*

/**
 * Created by tsantana on 15/12/17.
 */

data class Post(val title: String, val content: String, val creationDate: Date): Domain {
    override var id: Int? = null
}