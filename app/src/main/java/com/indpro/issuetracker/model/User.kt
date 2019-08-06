package com.indpro.issuetracker.model

import kotlin.random.Random


data class User(val id: Int, val name: String) {

    //hashCode will make sure user will have unique identifier
    override fun hashCode(): Int {
        return id.hashCode()
    }

    //you can compare id as well
    override fun equals(other: Any?): Boolean {
        return (other as? User)?.hashCode() == hashCode()
    }

    companion object {
        //store unique value
        private val users = hashSetOf<User>()

        //add 10 test user if user list is empty (Only use for testing purpose)
        fun testUsers() {
            if (users.isEmpty()) {
                for (i in 0 until 10) {
                    addUser("User ${i + 1}")
                }
            }
        }

        // count number of user
        fun count(): Int {
            return users.count()
        }

        //add a new user and return its id
        fun addUser(name: String): Int {
            val user = User(users.size, name)
            users.add(user)
            println("$user added successfully")
            return user.id
        }

        //check user is exist or not
        fun userExist(userID: Int): Boolean {
            return getUser(userID) != null
        }

        // get specific user by id
        fun getUser(id: Int): User? {
            return users.firstOrNull { id == it.id }
        }

        // get a list of user
        fun getUsers(): Set<User> {
            return users
        }

        //remove a specific user
        fun removeUser(userID: Int) {
            val user = getUser(userID) ?: return
            //make sure not any issue assign to that user userID
            Issue.getIssues(userID = userID).forEach {
                it.assignToUser(null)
            }
            users.remove(user)
        }

        //get a random user from the list
        fun getRandomUser(): User? {
            return getUser(Random.nextInt(0, users.size))
        }
    }

}