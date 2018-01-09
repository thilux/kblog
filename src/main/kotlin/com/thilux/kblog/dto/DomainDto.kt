package com.thilux.kblog.dto

import org.jetbrains.squash.definition.TableDefinition

/**
 * Created by tsantana on 16/12/17.
 */

interface DomainDto<T: TableDefinition> {
    var id: Int?;

    fun toEntity(): T
}