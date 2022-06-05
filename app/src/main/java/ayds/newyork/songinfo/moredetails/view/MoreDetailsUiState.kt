package ayds.newyork.songinfo.moredetails.view

import ayds.newyork.songinfo.moredetails.model.entities.Source

data class MoreDetailsUiState (
    val name : String = "",
    val article : String = "",
    val url : String =  "",
    val urlBtnEnabled: Boolean = false,
    val source : Source? = null,
    val image : String = "",
    val currentCardListPosition : Int = 0
)