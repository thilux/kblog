package com.thilux.kblog

/**
 * Created by tsantana on 15/12/17.
 */

import com.thilux.kblog.dto.Comment
import com.thilux.kblog.dto.Post
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
                val postObject = call.receive<Post>()
                LOG.debug("Received HTTP_POST request for Post entity: $postObject")

                call.respond(PostRepository.add(postObject))
            }

        }

        post(COMMENT_ENDPOINT) {

            errorAware {
                val commentObject = call.receive<Comment>()
                LOG.debug("Received HTTP_POST request for Comment entity: $commentObject")

                call.respond(CommentRepository.add(commentObject))
            }

        }

        get(POSTS_ENDPOINT) {

            errorAware {
                LOG.debug("HTTP_GET request for all Post entities")
                call.respond(PostRepository.getAll())
            }

        }

        get(COMMENTS_ENDPOINT) {
            errorAware {
                LOG.debug("HTTP_GET request for all Comment entities")
                call.respond(CommentRepository.getAll())
            }
        }

        get(POST_ENDPOINT + "/{id}") {
            errorAware {
                val id = call.parameters["id"] ?: throw IllegalArgumentException("Parameter id not found")
                LOG.debug("HTTP_GET request for Post entity with id: $id")
                call.respond(PostRepository.get(id))
            }
        }

        get(POST_ENDPOINT + "/{id}/comments") {
            errorAware {
                val id = call.parameters["id"] ?: throw IllegalArgumentException("Parameter id not found")
                LOG.debug("HTTP_GET request for Comment entities with postId: $id")
                call.respond(CommentRepository.getFromPostId(id))
            }
        }

        get(COMMENT_ENDPOINT + "/{id}") {
            errorAware {
                val id = call.parameters["id"] ?: throw IllegalArgumentException("Parameter id not found")
                LOG.debug("HTTP_GET request for Comment entity with id: $id")
                call.respond(CommentRepository.get(id))
            }
        }

        delete(POST_ENDPOINT + "/{id}") {
            errorAware {
                val id = call.parameters["id"] ?: throw IllegalArgumentException("Parameter id not found")
                LOG.debug("HTTP_DELETE request for Post entity with id: $id")
                PostRepository.remove(id)
                call.respondSuccessJson()
            }
        }

        delete(COMMENT_ENDPOINT + "/{id}") {
            errorAware {
                val id = call.parameters["id"] ?: throw IllegalArgumentException("Parameter id not found")
                LOG.debug("HTTP_DELETE request for Comment entity with id: $id")
                CommentRepository.remove(id)
                call.respondSuccessJson()
            }
        }

    }

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