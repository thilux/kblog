package com.thilux.kblog.repository

import com.thilux.kblog.dto.DomainDto
import java.util.concurrent.CopyOnWriteArraySet
import java.util.concurrent.atomic.AtomicInteger

/**
 * Created by tsantana on 16/12/17.
 */

open class MemoryRepository<T: DomainDto> {

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

    private fun remove(record: T){
        if (!records.contains(record)){
            throw IllegalArgumentException("Entity not found. Can' be removed.")
        }

        records.remove(record)
    }

    fun remove(id: String) = remove(get(id))

    fun remove(id: Int) = remove(get(id))

    fun clear() = records.clear()

}