package ayds.newyork.songinfo.home.view

import ayds.newyork.songinfo.home.model.entities.EmptySong
import ayds.newyork.songinfo.home.model.entities.Song
import ayds.newyork.songinfo.home.model.entities.SpotifySong

interface SongDescriptionHelper {
    fun getSongDescriptionText(song: Song = EmptySong): String
}

internal class SongDescriptionHelperImpl : SongDescriptionHelper {
    var precisionDate : SongDatePrecisionHelper = HomeViewInjector.songDatePrecisionHelper
    override fun getSongDescriptionText(song: Song): String {
        return when (song) {
            is SpotifySong ->
                "${
                    "Song: ${song.songName} " +
                            if (song.isLocallyStored) "[*]" else ""
                }\n" +
                        "Artist: ${song.artistName}\n" +
                        "Album: ${song.albumName}\n" +
                        "Release date: ${precisionDate.getPrecisionDate(song.releaseDatePrecision,song.releaseDate)}"

            else -> "Song not found"
        }
    }
}