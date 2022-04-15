package ayds.newyork.songinfo.home.view

import ayds.newyork.songinfo.home.model.entities.Song
import ayds.newyork.songinfo.home.model.entities.DatePrecision

interface SongDatePrecisionHelper {
    fun getPrecisionDate(song : Song) : String
}

internal class SongDatePrecisionHelperImpl : SongDatePrecisionHelper {

    private val months = arrayOf("January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December")

    override fun getPrecisionDate(song : Song): String {
        val precision = song.releaseDatePrecision
        return when (precision) {
            DatePrecision.YEAR -> dateWithYearPrecision(song.releaseDate)
            DatePrecision.MONTH -> dateWithMonthPrecision(song.releaseDate)
            DatePrecision.DAY -> dateWithDayPrecision(song.releaseDate)
        }
    }

    private fun dateWithDayPrecision(date : String) : String{
        val splitDate = date.split('-')
        val day = splitDate[2]
        val month = splitDate[1]
        val year = splitDate[0]
        return "$day/$month/$year"
    }

    private fun dateWithMonthPrecision(date : String) : String {
        val splitDate = date.split('-')
        val month = months[splitDate[1].toInt()-1]
        val year = splitDate[0]
        return "$month, $year "
    }

    private fun dateWithYearPrecision(date : String) : String {
        val year = date.toInt()
        val toReturn : String
        if(isLeapYear(year)) {
            toReturn = "$year (leap year)"
        }else{
            toReturn = "$year (not leap year)"
        }
        return toReturn
    }

    private fun isLeapYear(year : Int) = (year % 4 == 0) && (year % 100 != 0 || year % 400 == 0)
}