package com.thilux.kblog.dto

import org.jetbrains.squash.definition.TableDefinition
import org.jetbrains.squash.statements.InsertValuesStatement

/**
 * Created by tsantana on 16/12/17.
 */

interface DomainDto<T: TableDefinition> {
    var id: Int?

    fun toEntity(): InsertValuesStatement<T, Unit>
}