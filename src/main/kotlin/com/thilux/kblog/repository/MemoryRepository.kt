package com.thilux.kblog.repository

import com.thilux.kblog.domain.Domain
import java.util.concurrent.CopyOnWriteArraySet
import java.util.concurrent.atomic.AtomicInteger

/**
 * Created by tsantana on 16/12/17.
 */

open class MemoryRepository<T: Domain> {

    private val idCounter = AtomicInteger()
    private val records = CopyOnWriteArraySet<T>()

    fun add(record: T): T {
        if (records.contains(record)){
            return records.find { it == record }!!
        }

        record.id = idCounter.incrementAndGet()
        records.add(record)
        return record
    }

    fun getAll() = records.toList()

    fun get(id: String) = records.find { it.id.toString() == id } ?: throw IllegalArgumentException("No record found with id $id")

    fun get(id: Int) = get(id.toString())

}