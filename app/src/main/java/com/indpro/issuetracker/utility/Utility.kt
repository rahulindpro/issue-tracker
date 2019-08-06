package com.indpro.issuetracker.utility

import android.annotation.SuppressLint
import java.text.SimpleDateFormat
import java.util.*
import kotlin.random.Random

class Utility {
    companion object {

        @SuppressLint("SimpleDateFormat")
        val DateFormat = SimpleDateFormat("dd-MM-yyyy HH:mm")

        val Calendar = GregorianCalendar()

        fun getRandom(from: Int = 1, until: Int): Int {
            return Random.nextInt(from, until)
        }

        //get a random date
        fun getRandomDateForTest(): Date {
            val day = getRandom(until = 31)

            //for all test filter using date dd-07-2019 00:01 (dd is date)
            val date = "${String.format("%02d", day)}-07-2019 00:01"
            return DateFormat.parse(date) ?: Date()
        }
    }
}