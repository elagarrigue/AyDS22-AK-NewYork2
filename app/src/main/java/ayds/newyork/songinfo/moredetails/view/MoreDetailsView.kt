package ayds.newyork.songinfo.moredetails.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import ayds.newyork.songinfo.R
import ayds.newyork.songinfo.home.view.HomeUiEvent
import ayds.newyork.songinfo.moredetails.model.MoreDetailsModel
import ayds.observer.Observable
import ayds.observer.Subject

interface MoreDetailsView {
    val moreDetailsEventObservable: Observable<MoreDetailsEvent>
}

class MoreDetailsViewActivity : AppCompatActivity(), MoreDetailsView {

    private val onActionSubject = Subject<MoreDetailsEvent>()
    override val moreDetailsEventObservable: Observable<MoreDetailsEvent> = onActionSubject
    private lateinit var moreDetailsModel: MoreDetailsModel


    private lateinit var textAbstract: TextView
    private lateinit var btnUrl: Button
    private lateinit var nyTimesImg: ImageView

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