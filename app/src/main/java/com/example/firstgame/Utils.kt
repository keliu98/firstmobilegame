@file:JvmName("Utils")
@file:JvmMultifileClass
package com.example.firstgame

import java.text.SimpleDateFormat
import java.util.*
import kotlin.random.Random

/**
 * Noninstantiable utility class
 * @author jeannie on 2/2/20.
 * Based on the recommendations in "Effective Java" ported to Kotlin
 * Arguments against utility classes are also valid based on
 * testability, but we'll leave the arguments for another day
 */

class Utils private constructor() {

    companion object {
        /**
         * Returns a random four digit number between 1000 and 9999
         * @return
         */
        @JvmStatic
        fun generateRandomFourDigitNumber(): Int
        {
            //TODO logic to return a random four digit number between 1000 and 9999
            //  call Utils.generateRandomFourDigitNumber() in MainActivity
            return Random.nextInt(1000, 9999)
        }

        @JvmStatic
        fun FormatDate(date: Date) : String
        {
            val formatter = SimpleDateFormat("MMM d, yyyy", Locale.getDefault())
            return formatter.format(date)
        }
    }

}