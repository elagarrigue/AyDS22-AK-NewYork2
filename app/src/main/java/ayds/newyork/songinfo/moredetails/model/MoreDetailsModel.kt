package ayds.newyork.songinfo.moredetails.model

import ayds.newyork.songinfo.home.model.entities.Song
import ayds.newyork.songinfo.home.model.repository.SongRepository
import ayds.newyork.songinfo.moredetails.model.entities.Artist
import ayds.newyork.songinfo.moredetails.model.repository.ArtistInfoRepository
import ayds.observer.Subject

interface MoreDetailsModel{

    // val ArtistObservable: falta implementar

    fun searchArtist(term: String)

    fun getArtistById(id: String): Artist
}

internal class  MoreDetailsModelImpl (private val repository: ArtistInfoRepository) : MoreDetailsModel {

    // override val ArtistObservable = Subject<ArtistInfo>()

    override fun searchArtist(id: String) {
        repository.getArtistById(id).let{
            // aca va el notify del ArtistObservable
        }
    }

    override fun getArtistById(id : String) : Artist{
        return repository.getArtistById(id)
    }
}