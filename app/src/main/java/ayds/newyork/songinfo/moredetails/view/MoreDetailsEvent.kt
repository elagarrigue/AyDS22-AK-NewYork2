package ayds.newyork.songinfo.moredetails.view

sealed class MoreDetailsEvent {
    object GetArtistInfo : MoreDetailsEvent()
}