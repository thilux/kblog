package com.thilux.kblog.dto

import java.util.*

/**
 * Created by tsantana on 15/12/17.
 */

data class PostDto(val title: String, val content: String, val creationDate: Date): DomainDto {
    override var id: Int? = null
}