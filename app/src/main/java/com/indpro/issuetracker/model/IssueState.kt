package com.indpro.issuetracker.model

import java.util.*

// IssueState contain information about your current state and previous state Exm. TO_DO or IN_PROGRESS or DONE
class IssueState(val state: State, val comment: String? = null, val startDate: Date = Date()) {


    var previousState: IssueState? = null

    //
    val startState: IssueState?
        get() {
            return previousState?.startState
        }

    val hasPreviousState: Boolean get() = previousState != null

    enum class State {
        TO_DO, IN_PROGRESS, DONE
    }

    override fun toString(): String {
        return "\nIssueState(state=$state, comment=$comment, startDate=$startDate) $previousState"
    }
}

