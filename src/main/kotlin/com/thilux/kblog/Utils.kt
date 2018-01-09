package com.thilux.kblog

import org.jetbrains.squash.connection.Transaction
import org.jetbrains.squash.definition.Table

/**
 * Created by tsantana on 09/01/18.
 */


fun <T : Table> Transaction.tableExists(table: T): Boolean {
    return databaseSchema().tables().any{ String.CASE_INSENSITIVE_ORDER.compare(it.name, table.compoundName.id) == 0}
}