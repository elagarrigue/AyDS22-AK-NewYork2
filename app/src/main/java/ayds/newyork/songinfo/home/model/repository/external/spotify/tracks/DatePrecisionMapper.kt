package ayds.newyork.songinfo.home.model.repository.external.spotify.tracks

import ayds.newyork.songinfo.home.model.entities.DatePrecision
import java.lang.Exception

interface DatePrecisionMapper {
    fun getDatePrecisionFromString(str : String) : DatePrecision
}

private const val DAY_PRECISION = "day"
private const val MONTH_PRECISION = "month"
private const val YEAR_PRECISION = "year"

internal class DatePrecisionMapperImpl : DatePrecisionMapper{

    override fun getDatePrecisionFromString(str : String): DatePrecision {
        return when(str){
            DAY_PRECISION -> DatePrecision.DAY
            MONTH_PRECISION -> DatePrecision.MONTH
            YEAR_PRECISION -> DatePrecision.YEAR
            else -> throw Exception("Precision received is not supported.")
        }
    }
}