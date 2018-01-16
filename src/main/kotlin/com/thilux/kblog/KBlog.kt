package com.thilux.kblog

/**
 * Created by tsantana on 15/12/17.
 */

import com.thilux.kblog.domain.CommentEntity
import com.thilux.kblog.domain.PostEntity
import com.thilux.kblog.dto.CommentDto
import com.thilux.kblog.dto.PostDto
import com.thilux.kblog.repository.CommentRepository
import com.thilux.kblog.repository.PostRepository
import io.ktor.application.*
import io.ktor.features.DefaultHeaders
import io.ktor.features.CORS
import io.ktor.features.ContentNegotiation
import io.ktor.gson.*
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.pipeline.PipelineContext
import io.ktor.request.receive
import io.ktor.response.*
import io.ktor.routing.*
import java.text.DateFormat
import java.time.Duration

const val API_ENDPOINT  = "/api/v1"
const val POST_ENDPOINT = "$API_ENDPOINT/post"
const val COMMENT_ENDPOINT = "$API_ENDPOINT/comment"
const val POSTS_ENDPOINT = "$API_ENDPOINT/posts"
const val COMMENTS_ENDPOINT = "$API_ENDPOINT/comments"

fun Application.main(){

    LOG.debug("Starting kblog application.")

    connectToDatabase()

    createTablesIfNotExisting()

    /*DatabaseHandler.withTransaction {

        databaseSchema().create(PostEntity, CommentEntity)

    }*/

    install(DefaultHeaders)
    install(CORS) {
        maxAge = Duration.ofDays(1)
    }
    install(ContentNegotiation) {
        gson {
            setDateFormat(DateFormat.LONG)
            setPrettyPrinting()
        }
    }

    routing {

        post(POST_ENDPOINT) {

            errorAware {
                val postObject = call.receive<PostDto>()
                LOG.debug("Received HTTP_POST request for PostDto entity: $postObject")

                //call.respond(PostRepository.add(postObject.toEntity()))
                call.respond(PostRepository.add(postObject))
            }

        }

        post(COMMENT_ENDPOINT) {

            errorAware {
                val commentObject = call.receive<CommentDto>()
                LOG.debug("Received HTTP_POST request for CommentDto entity: $commentObject")

                call.respond(CommentRepository.add(commentObject))
            }

        }

        get(POSTS_ENDPOINT) {

            errorAware {
                LOG.debug("HTTP_GET request for all PostDto entities")
                call.respond(PostRepository.getAll())
            }

        }

        get(COMMENTS_ENDPOINT) {
            errorAware {
                LOG.debug("HTTP_GET request for all CommentDto entities")
                call.respond(CommentRepository.getAll())
            }
        }

        get(POST_ENDPOINT + "/{id}") {
            errorAware {
                val id = call.parameters["id"] ?: throw IllegalArgumentException("Parameter id not found")
                LOG.debug("HTTP_GET request for PostDto entity with id: $id")

                call.respond(PostRepository.getOneById(id.toInt()))
            }
        }

        get(POST_ENDPOINT + "/{id}/comments") {
            errorAware {
                val id = call.parameters["id"] ?: throw IllegalArgumentException("Parameter id not found")
                LOG.debug("HTTP_GET request for CommentDto entities with postId: $id")
                call.respond(CommentRepository.getAllFromPostId(id.toInt()))
            }
        }

        get(COMMENT_ENDPOINT + "/{id}") {
            errorAware {
                val id = call.parameters["id"] ?: throw IllegalArgumentException("Parameter id not found")
                LOG.debug("HTTP_GET request for CommentDto entity with id: $id")
                call.respond(CommentRepository.getOneById(id.toInt()))
            }
        }

        delete(POST_ENDPOINT + "/{id}") {
            errorAware {
                val id = call.parameters["id"] ?: throw IllegalArgumentException("Parameter id not found")
                LOG.debug("HTTP_DELETE request for PostDto entity with id: $id")
                PostRepository.remove(id.toInt())
                call.respondSuccessJson()
            }
        }

        delete(COMMENT_ENDPOINT + "/{id}") {
            errorAware {
                val id = call.parameters["id"] ?: throw IllegalArgumentException("Parameter id not found")
                LOG.debug("HTTP_DELETE request for CommentDto entity with id: $id")
                CommentRepository.remove(id.toInt())
                call.respondSuccessJson()
            }
        }

    }

}

fun connectToDatabase() {
    DatabaseHandler.createConnection()
    LOG.debug("Database connection established!")
}

fun createTablesIfNotExisting() {

    if (!tableFromDefinitionExists(PostEntity)) DatabaseHandler.createTables(PostEntity)
    if (!tableFromDefinitionExists(CommentEntity)) DatabaseHandler.createTables(CommentEntity)

    LOG.debug("Database tables created!")


}

private suspend fun <R> PipelineContext<*, ApplicationCall>.errorAware(block: suspend  () -> R): R? {
    return try {
        block()
    } catch (e: Exception) {
        call.respondText("""{"error":"$e"}""", ContentType.parse("application/json"), HttpStatusCode.InternalServerError)
        null
    }
}

private suspend fun ApplicationCall.respondSuccessJson(value: Boolean = true) = respond("""{"success":"$value"}""")
