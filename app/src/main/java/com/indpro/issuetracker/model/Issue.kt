package com.indpro.issuetracker.model

import android.os.Build
import androidx.annotation.RequiresApi
import java.util.*
import kotlin.random.Random

data class Issue(val id: Int, val name: String, val createdAt: Date = Date()) {

    //assignTo is the id of user
    private var assignTo: Int? = null

    //just want to make make sure task will assign to user only
    fun assignToUser(user: User?) {
        assignTo = user?.id
    }

    fun isAssignTo(user: User): Boolean {
        return isAssignTo(user.id)
    }

    fun isAssignTo(userID: Int): Boolean {
        return userID == assignTo
    }

    // current state of issue
    var issueState: IssueState = IssueState(IssueState.State.TO_DO, startDate = createdAt)
        set(value) {
            value.previousState = issueState

            if (startDate == null && value.state == IssueState.State.IN_PROGRESS) {
                startDate = value.startDate
            }
            //println("Issue $id state $state changed to ${value.state}")
            field = value
        }

    // current state of issue
    val state: IssueState.State get() = issueState.state

    //list of comments
    private var comments = arrayListOf<Comment>()

    fun addComment(userID: Int, comment: String) {
        comments.add(Comment(userID, comment))
        // println("User $userID recently comment on issue $id")
    }

    // startDate -> when user first time move the task into IN_PROGRESS
    var startDate: Date? = null

    //endDate will be date when task is done if task is still IN_PROGRESS or TO_DO in that case endDate will be null
    val endDate: Date?
        get() {
            val currentState = issueState
            return if (currentState.state == IssueState.State.DONE) currentState.startDate else null
        }

    override fun equals(other: Any?): Boolean {
        return (other as? Issue)?.hashCode() == hashCode()
    }

    override fun hashCode(): Int {
        return id.hashCode()
    }

    override fun toString(): String {
        return "Issue(id=$id, name='$name', assignTo=$assignTo,  createdAt=$createdAt, startDate=$startDate, endDate=$endDate, issueState=$state)"
    }

    //    If both startDate and endDate are specified,
//    only issues within the defined time range are returned. If only startDate is specified, all issues created after that date are returned.
//    If only endDate is specified, all issues created up to that date are returned.
    private fun valid(
        state: IssueState.State?,
        userID: Int?,
        startDate: Date?,
        endDate: Date?
    ): Boolean {

        //println("startDate- ${this.startDate} => ${if(startDate == null) 0 else this.startDate?.compareTo(startDate)}")
        //println("ISSUE -> ${id}, ${assignTo?.id}, ${this.startDate}, ${this.endDate}, ${this.state}")

        return (userID == null || assignTo == userID)
                && (state == null || this.state == state)
                && (startDate == null || this.startDate != null && (this.startDate?.compareTo(startDate) ?: 0) >= 0)
                && (endDate == null || this.endDate != null && (this.endDate?.compareTo(endDate) ?: 0) <= 0)

    }


    companion object {
        private val issues = hashSetOf<Issue>()

        // get a random issue
        fun getRandomIssue(): Issue? {
            return getIssue(Random.nextInt(0, issues.size))
        }

        // check if issue is exist or not
        fun issueExist(issueID: Int): Boolean {
            return getIssue(issueID) != null
        }

        // add new issue and return id
        fun addIssue(name: String, createdAt: Date = Date()): Int {
            val issue = Issue(issues.size, name, createdAt = createdAt)
            issues.add(issue)
            //println("$issue added")
            return issue.id
        }

        // get issue by id
        fun getIssue(id: Int): Issue? {
            return issues.firstOrNull { id == it.id }
        }

        //remove a specific issue
        @RequiresApi(Build.VERSION_CODES.N)
        fun removeIssue(id: Int) {
            issues.removeIf { issue -> issue.id == id }
        }

        //remove issue
        fun removeIssue(issue: Issue) {
            issues.remove(issue)
        }

        // count number of issue
        fun count(): Int {
            return issues.count()
        }

        // get all issue (list is depend on filter)
        fun getIssues(
            state: IssueState.State? = null,
            userID: Int? = null,
            startDate: Date? = null,
            endDate: Date? = null
        ): List<Issue> {
            // println("filter (state = ${state}, userID = ${userID}, startDate = ${startDate}, endDate = ${endDate})")
            return issues.filter { it.valid(state, userID, startDate, endDate) }
        }

    }

}

