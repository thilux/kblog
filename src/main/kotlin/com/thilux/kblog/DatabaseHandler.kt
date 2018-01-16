package com.thilux.kblog

import org.jetbrains.squash.connection.DatabaseConnection
import org.jetbrains.squash.connection.Transaction
import org.jetbrains.squash.definition.TableDefinition
import org.jetbrains.squash.dialects.h2.H2Connection

/**
 * Created by tsantana on 09/01/18.
 */

interface DatabaseCommands {

    fun createConnection(): DatabaseConnection
    fun createTransaction(): Transaction = createConnection().createTransaction()

    fun <R> withTransaction(statement: Transaction.() -> R): R = createTransaction().use(statement)

}

object DatabaseHandler: DatabaseCommands {

    override fun createConnection(): DatabaseConnection = H2Connection.createMemoryConnection()

    private val mainConnection = createConnection()

    override fun createTransaction(): Transaction {
        return mainConnection.createTransaction()
    }

    fun createTables(vararg tables: TableDefinition) = withTransaction {

        databaseSchema().create(tables.toList())
    }

}