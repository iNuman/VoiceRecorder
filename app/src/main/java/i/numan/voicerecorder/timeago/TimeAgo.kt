package i.numan.voicerecorder.timeago

import java.util.*
import java.util.concurrent.TimeUnit

class TimeAgo {

    fun getTimeAgo(duration: Long): String {
        val now = Date()
        val seconds = TimeUnit.MILLISECONDS.toSeconds(now.time - duration)
        val minutes = TimeUnit.MILLISECONDS.toMinutes(now.time - duration)
        val hours = TimeUnit.MILLISECONDS.toHours(now.time - duration)
        val days = TimeUnit.MILLISECONDS.toDays(now.time - duration)
        when {
            seconds < 60 -> {
                return "just now"
            }
            minutes == 1L -> {
                return "a minute ago"
            }
            // it means if (minutes > 1 && minutes < 60) in java
            minutes in 2..59 -> {
                return "$minutes minutes ago"
            }
            hours == 1L -> {
                return "an hour ago"
            }
            hours in 2..23 -> {
                return "$hours hours ago"
            }
            days == 1L -> {
                return "a day ago"
            }
            else -> {
                return "$days days ago"
            }
        }
    }
}