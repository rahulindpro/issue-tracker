package com.indpro.issuetracker

import com.indpro.issuetracker.model.Issue
import com.indpro.issuetracker.model.IssueState
import com.indpro.issuetracker.model.User
import com.indpro.issuetracker.utility.Operation.Companion.assignUser
import com.indpro.issuetracker.utility.Operation.Companion.setIssueState
import com.indpro.issuetracker.utility.Utility
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import java.util.*

class IssueTrackerUnitTest {

    private val formatter = Utility.DateFormat
    private val gc = Utility.Calendar

    init {
        // add test user if list is empty
        User.testUsers()

        //add 10 random issue when test is run
        for (i in 0 until 10) {
            val user = User.getRandomUser()
            val issueID = Issue.addIssue("Issue ${i + 1}", Utility.getRandomDateForTest())
            Issue.getIssue(issueID)?.assignToUser(user)
        }

        for (i in 0 until 5) {

            val user = User.getRandomUser() ?: continue
            for (j in 0 until 10) {
                val issue = Issue.getRandomIssue() ?: continue
                issue.assignToUser(user)

                gc.time = issue.createdAt
                gc.add(Calendar.DATE, 2)
                issue.issueState = IssueState(IssueState.State.IN_PROGRESS, "In Progress", gc.time ?: Date())
                issue.addComment(user.id, "moved In Progress by ${user.name}")
            }

            for (j in 0 until 10) {
                val issue = Issue.getRandomIssue() ?: continue
                gc.time = issue.startDate ?: issue.createdAt
                gc.add(Calendar.DATE, 1)
                issue.issueState = IssueState(IssueState.State.DONE, "This task has been completed", gc.time ?: Date())
                issue.addComment(user.id, "completed by ${user.name}")
            }

            val issue = Issue.getRandomIssue() ?: continue

            gc.time = issue.startDate ?: issue.createdAt
            gc.add(Calendar.DATE, 2)
            issue.issueState = IssueState(IssueState.State.IN_PROGRESS, "In Progress", gc.time ?: Date())

        }
    }

    @Before
    fun setUp() {
        println("---------- Before (Setup)---------------")
        println("Users - ${User.getUsers().count()}")
        println("ISSUES - ${Issue.getIssues().count()}")
    }

    // fetch a random user and an issue. Assign that issue to the user and make it IN_PROGRESS
    @Test
    fun randomTest() {
        val userID = User.getRandomUser()?.id ?: return
        val issueID = Issue.getRandomIssue()?.id ?: return
        assignUser(userID, issueID)
        setIssueState(issueID, IssueState.State.IN_PROGRESS, "I'm on it!")
    }

    @Test
    fun testScenario1() {
        //add a user
        val userID = User.addUser("Steve")

        //add an issue
        val issueID = Issue.addIssue("The app crashes on login.")
        // assign issue to the user
        assignUser(userID, issueID)

        //fetch issue by id
        val issue = Issue.getIssue(issueID)

        //check issue is null or not
        assertNotNull(issue)

        //check issue is assign to user or not
        assertTrue(issue?.isAssignTo(userID) ?: false)

        //change a state of issue
        setIssueState(issueID, IssueState.State.IN_PROGRESS, "I'm on it!")

        //check if state of issue is changes or not
        assertTrue(issue?.state == IssueState.State.IN_PROGRESS)

        //change a state of issue
        setIssueState(issueID, IssueState.State.DONE)

        //check if state of issue is changes or not
        assertTrue(issue?.state == IssueState.State.DONE)

        setIssueState(issueID, IssueState.State.TO_DO, "still have problem")
        assertTrue(issue?.state == IssueState.State.TO_DO)

        //check user is null or not
        assertNotNull(User.getUser(userID))

    }


    @Test // remove 10 random issue and make assert test its actually removed from list or not
    fun removeRandomIssue() {
        for (i in 0 until 10) {
            val issue = Issue.getRandomIssue() ?: continue
            Issue.removeIssue(issue.id)
            assertNull(Issue.getIssue(issue.id))
        }
    }

    @Test // remove 5 random user and make assert test its actually removed from list or not and make sure no issue should assign to this user
    fun removeUser() {
        for (i in 0 until 5) {
            val user = User.getRandomUser() ?: continue
            println(Issue.getIssues(userID = user.id).count())
            User.removeUser(user.id)
            assertNull(User.getUser(user.id))
            assertTrue(Issue.getIssues(userID = user.id).isEmpty())
        }
    }

    @Test
    fun userTest() {

        User.getUsers().forEach { user ->
            testFilter(userID = user.id)
            testFilter(userID = user.id, state = IssueState.State.DONE)
            testFilter(userID = user.id, state = IssueState.State.IN_PROGRESS)
            testFilter(userID = user.id, state = IssueState.State.TO_DO)
        }
    }

    @Test
    fun userTestWithDateFilter() {

        var startDate = formatter.parse("19-07-2019 00:01")
        User.getUsers().forEach { user ->

            testFilter(userID = user.id, startDate = startDate)
            testFilter(userID = user.id, state = IssueState.State.DONE)
            testFilter(userID = user.id, state = IssueState.State.IN_PROGRESS)
            testFilter(userID = user.id, state = IssueState.State.TO_DO)

            val endDate = formatter.parse("30-07-2019 00:01")

            testFilter(userID = user.id, endDate = startDate)

            startDate = formatter.parse("10-07-2019 00:01")

            testFilter(startDate = startDate, endDate = endDate, state = IssueState.State.DONE)
        }
    }

    @Test
    fun issueTest() {

        testFilter()

        testFilter(state = IssueState.State.DONE)
        testFilter(state = IssueState.State.IN_PROGRESS)
        testFilter(state = IssueState.State.TO_DO)

        var startDate = formatter.parse("19-07-2019 00:01")
        assertTrue(startDate != null)

        testFilter(startDate = startDate)

        testFilter(endDate = startDate)

        startDate = formatter.parse("10-07-2019 00:01")

        val endDate = formatter.parse("30-07-2019 00:01")

        testFilter(startDate = startDate, endDate = endDate)

        testFilter(state = IssueState.State.IN_PROGRESS, startDate = startDate, endDate = endDate)

    }

    private fun testFilter(
        state: IssueState.State? = null,
        userID: Int? = null,
        startDate: Date? = null,
        endDate: Date? = null
    ) {
        println("\nFilter Query => (state = $state, userID = $userID, startDate = $startDate, endDate = $endDate)")
        println("---------- RESULT ---------------")
        val list = Issue.getIssues(userID = userID, state = state, startDate = startDate, endDate = endDate)
        println("item found ${list.count()}")
        list.forEach { issue ->
            println(issue)
            // start date check
            assertTrue(startDate == null || issue.startDate != null)

            // end date check
            assertTrue(endDate == null || issue.endDate != null)

            // check state of issue
            assertTrue(state == null || issue.state == state)

            //check user id
            assertTrue(userID == null || issue.isAssignTo(userID))

            assertTrue(
                (startDate == null || (issue.startDate?.compareTo(startDate) ?: 0) >= 0)
                        && (endDate == null || (issue.endDate?.compareTo(endDate) ?: 0) <= 0)
            )
        }
        println()
    }

    @After
    fun tearDown() {
        println("---------- AFTER (TearDown)---------------")
        println("Users - ${User.count()}")
        println("ISSUES - ${Issue.count()}")
    }
}
