package com.indpro.issuetracker.model

import java.util.*

data class Comment(val userID: Int, val comment: String) {
    val createdAt = Date()
    override fun toString(): String {
        return "Comment(comment='$comment', userID=$userID, createdAt=$createdAt)"
    }
}