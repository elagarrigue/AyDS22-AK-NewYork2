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
    private fun dateWithDayPrecision(date : String) : String{
        var splitDate = date.split('-')
        return splitDate[2]+"/"+splitDate[1]+"/"+splitDate[0]
    }

    private fun dateWithMonthPrecision(date : String) : String {
        var splitDate = date.split('-')
        return months[splitDate[1].toInt()] +", "+splitDate[0]
    }

    private fun dateWithYearPrecision(date : String) : String = if(isLeapYear(date.toInt())) "$date (leap year)" else "$date (not a leap year)"

    private fun isLeapYear(year : Int) = (year % 4 == 0) && (year % 100 != 0 || year % 400 == 0)
    
}