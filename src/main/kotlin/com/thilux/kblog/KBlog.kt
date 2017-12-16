package com.thilux.kblog

/**
 * Created by tsantana on 15/12/17.
 */

import com.thilux.kblog.domain.Comment
import com.thilux.kblog.domain.Post
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