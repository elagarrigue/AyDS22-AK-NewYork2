package ayds.newyork.songinfo.moredetails.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import ayds.newyork.songinfo.R
import ayds.newyork.songinfo.moredetails.model.MoreDetailsModel
import ayds.newyork.songinfo.moredetails.model.MoreDetailsModelImpl
import ayds.newyork.songinfo.moredetails.model.entities.ArtistInfo
import ayds.newyork.songinfo.moredetails.model.repository.ArtistInfoRepository
import ayds.newyork.songinfo.moredetails.model.repository.ArtistInfoRepositoryImpl
import ayds.observer.Observable
import ayds.observer.Subject

interface MoreDetailsView {
    val moreDetailsEventObservable: Observable<MoreDetailsEvent>
}

private const val ASTERISK = "[*]"
private const val NY_TIMES_IMG = "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRVioI832nuYIXqzySD8cOXRZEcdlAj3KfxA62UEC4FhrHVe0f7oZXp3_mSFG7nIcUKhg&usqp=CAU"


class MoreDetailsViewActivity : AppCompatActivity(), MoreDetailsView {

    private val onActionSubject = Subject<MoreDetailsEvent>()
    override val moreDetailsEventObservable: Observable<MoreDetailsEvent> = onActionSubject
    private lateinit var moreDetailsModel: MoreDetailsModel
    private lateinit var textAbstract: TextView
    private lateinit var btnUrl: Button
    private lateinit var nyTimesImg: ImageView
    private lateinit var artist : ArtistInfo
    private var repository : ArtistInfoRepository = ArtistInfoRepositoryImpl()
    private var model : MoreDetailsModel = MoreDetailsModelImpl(repository)
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_more_details_view)
        initProperties()
    }

    private fun initProperties() {
        textAbstract = findViewById(R.id.textPane2)
        btnUrl = findViewById(R.id.openUrlButton)
        nyTimesImg = findViewById(R.id.imageView)
    }

    private fun notifyGetArtistInfoAction() {
        onActionSubject.notify(MoreDetailsEvent.GetArtistInfo)
    }

    companion object {
        const val ARTIST_NAME_EXTRA = "artistName"
    }
}