package ayds.newyork.songinfo.home.view

interface SongDatePrecisionHelper {
    fun getPrecisionDate(Precision : String, Date : String) : String
}

class SongDatePrecisionHelperImpl : SongDatePrecisionHelper {

    val months = arrayOf("None", "January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December")

    override fun getPrecisionDate(precision: String, date: String): String =
        when (precision) {
            "year" -> dateWithYearPrecision(date)
            "month" -> dateWithMonthPrecision(date)
            "day" -> dateWithDayPrecision(date)
            else -> "Invalid Precision"
        }

    }