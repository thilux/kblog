package com.thilux.kblog.repository

import com.thilux.kblog.DatabaseHandler
import com.thilux.kblog.dto.DomainDto
import com.thilux.kblog.dto.PostDto
import org.jetbrains.squash.definition.Table
import org.jetbrains.squash.statements.InsertValuesStatement
import org.jetbrains.squash.results.ResultRow

/**
 * Created by tsantana on 09/01/18.
 */

abstract class SquashRepository<T: DomainDto<*>> {

    abstract fun add(into: T): T
    abstract fun getOneById(id: Int): T
    abstract fun getAll(): List<T>
    abstract fun remove(id: Int)

}