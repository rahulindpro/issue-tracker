# Issue Tracker
## Tools
* IDE: Android Studio 3.4.2
* Language: Kotline - 1.3.41
* Gradle: 5.1.1
* Android Gradle Plugin Version: 3.4.2
* Build Tool Version: 12.0.0

## How to run
* Open Project in Android Studio
* Open [IssueTrackerUnitTest.kt](https://github.com/rahulindpro/issue-tracker/blob/master/app/src/test/java/com/indpro/issuetracker/IssueTrackerUnitTest.kt) file and Run the Test
<img src="https://github.com/rahulindpro/issue-tracker/blob/master/Screenshot%202019-07-24%20at%2017.23.10.png" width="500" style="max-width:500%;">

## Operations has been implemented
* Add Issue
* Remove Issue
* Set Issue State
* Assign User to Issue
* Add Issue comment
* Get list of Issues (Filter Issue)
* Get a Specific Issue
* Add User
* Get list Of User
* Get a specific user

## App Packages
* <b>[model](https://github.com/rahulindpro/issue-tracker/tree/master/app/src/main/java/com/indpro/issuetracker/model)</b>  
    * <b>[User.kt](https://github.com/rahulindpro/issue-tracker/blob/master/app/src/main/java/com/indpro/issuetracker/model/User.kt)</b> - User is a data class, which contains all information about User. 
   * <b>[Issue.kt](https://github.com/rahulindpro/issue-tracker/blob/master/app/src/main/java/com/indpro/issuetracker/model/Issue.kt)</b> - Issue also a data class, which contains all information about Issue.
   
   ### Using Set to keeping records on run time
   Example
   * val users = hashSetOf<User>()
   * val issues = hashSetOf<Issue>()

## Unit Testing
I have implemented unit test into [IssueTrackerUnitTest.kt](https://github.com/rahulindpro/issue-tracker/blob/master/app/src/test/java/com/indpro/issuetracker/IssueTrackerUnitTest.kt) file.

### These sort of testing has been performed 
* Filter Issue 
* Remove User - User will be removed from main list and Issue list 
* removeTask
* randomTest - Pick a random task and assign it to a random user
* userTest - Fetch User list and filter data from the Issue list

### Filter Unit Test Output
<img src="https://github.com/rahulindpro/issue-tracker/blob/master/Screenshot%202019-08-06%20at%2009.55.04.png" width="800" style="max-width:500%;">

<img src="https://github.com/rahulindpro/issue-tracker/blob/master/Screenshot%202019-08-06%20at%2009.55.26.png">


