package com.thilux.kblog.repository

import com.thilux.kblog.dto.DomainDto

/**
 * Created by tsantana on 09/01/18.
 */

abstract class SquashRepository<T: DomainDto<*>> {

    abstract fun add(into: T): T
    abstract fun getOneById(id: Int): T
    abstract fun getAll(): List<T>
    abstract fun remove(id: Int)

}