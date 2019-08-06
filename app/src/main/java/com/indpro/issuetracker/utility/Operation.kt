package com.indpro.issuetracker.utility

import com.indpro.issuetracker.model.Issue
import com.indpro.issuetracker.model.IssueState
import com.indpro.issuetracker.model.User

class Operation {
    companion object {

        // assign a task to user
        fun assignUser(userID: Int, issueID: Int) {
            Issue.getIssue(issueID)?.apply {
                this.assignToUser(User.getUser(userID))
            }
        }

        fun setIssueState(issueID: Int, issueState: IssueState.State, comment: String? = null) {
            Issue.getIssue(issueID)?.apply {
                this.issueState = IssueState(issueState, comment)
            }
        }
    }
}