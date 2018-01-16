package com.thilux.kblog

import org.jetbrains.squash.connection.Transaction
import org.jetbrains.squash.definition.Column
import org.jetbrains.squash.definition.Table
import org.jetbrains.squash.definition.TableDefinition
import org.jetbrains.squash.statements.InsertStatementSeed
import org.jetbrains.squash.statements.InsertValuesStatement

/**
 * Created by tsantana on 09/01/18.
 */


fun <T : Table> Transaction.tableExists(table: T): Boolean {
    return databaseSchema().tables().any{ String.CASE_INSENSITIVE_ORDER.compare(it.name, table.compoundName.id) == 0}
}

fun tableFromDefinitionExists(tableDefinition: TableDefinition): Boolean = DatabaseHandler.withTransaction {
    databaseSchema().tables().any { String.CASE_INSENSITIVE_ORDER.compare(it.name, tableDefinition.compoundName.id) == 0 }
}

fun <T : Table> InsertStatementSeed<T>.valuesForEntityWithIntId(body: T.(InsertValuesStatement<T, Int>) -> TableDefinition): InsertValuesStatement<T, Int> {
    val values = InsertValuesStatement<T, Int>(table)
    table.body(values)
    return values
}

fun <T : Table, R> InsertValuesStatement<T, Int>.fetchIntIdColumnKey(column: Column<R>): InsertValuesStatement<T, R> {
    require(generatedKeyColumn == null) { "Only one fetch column is supported."}
    @Suppress("UNCHECKED_CAST")
    return (this as InsertValuesStatement<T, R>).apply {
        generatedKeyColumn = column
    }
}