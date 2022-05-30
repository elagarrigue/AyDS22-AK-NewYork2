package ayds.newyork.songinfo.moredetails.view

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Spanned
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.core.text.HtmlCompat
import ayds.newyork.songinfo.R
import ayds.newyork.songinfo.moredetails.model.MoreDetailsModel
import ayds.newyork.songinfo.moredetails.model.MoreDetailsModelInjector
import ayds.newyork.songinfo.moredetails.model.entities.Card
import ayds.newyork.songinfo.moredetails.model.entities.ExternalCard
import ayds.newyork.songinfo.moredetails.model.entities.EmptyCard
import ayds.observer.Observable
import ayds.observer.Subject
import com.squareup.picasso.Picasso

interface MoreDetailsView {
    val moreDetailsEventObservable: Observable<MoreDetailsEvent>
    var uiState : MoreDetailsUiState

    fun openExternalLink(url: String)
}

private const val NY_TIMES_IMG = "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRVioI832nuYIXqzySD8cOXRZEcdlAj3KfxA62UEC4FhrHVe0f7oZXp3_mSFG7nIcUKhg&usqp=CAU"

class MoreDetailsViewActivity : AppCompatActivity(), MoreDetailsView {

    private val artistInfoDescriptionHelper: ArtistInfoDescriptionHelper = MoreDetailsViewInjector.artistInfoDescriptionHelper
    private val onActionSubject = Subject<MoreDetailsEvent>()
    override val moreDetailsEventObservable: Observable<MoreDetailsEvent> = onActionSubject
    private lateinit var moreDetailsModel: MoreDetailsModel
    private lateinit var textAbstract: TextView
    private lateinit var textSource: TextView
    private lateinit var btnUrl: Button
    private lateinit var nyTimesImg: ImageView
    override var uiState = MoreDetailsUiState()
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_more_details_view)
        initModule()
        initUiStateWithArtistName()
        initProperties()
        initObservers()
        notifyGetArtistInfoAction()
    }

    private fun initUiStateWithArtistName() {
        uiState = uiState.copy(name=intent.getStringExtra(ARTIST_NAME_EXTRA).toString())
    }

    private fun initModule() {
        MoreDetailsViewInjector.init(this)
        moreDetailsModel = MoreDetailsModelInjector.getMoreDetailsModel()
    }

    private fun initListeners(){
        btnUrl.setOnClickListener { notifyOpenArtistInfoUrl() }
    }

    private fun notifyOpenArtistInfoUrl() {
        onActionSubject.notify(MoreDetailsEvent.OpenArtistInfoLink)
    }

    private fun updateUiState(artist: Card) {
        when (artist) {
            is ExternalCard -> updateMoreDetailsUiState(artist)
            EmptyCard -> updateNoResultsUiState()
        }
    }

    private fun updateMoreDetailsUiState(artist : Card) {
        uiState = uiState.copy(
            name = artist.artistName,
            article = artistInfoDescriptionHelper.getArtistInfoText(artist),
            url = artist.infoUrl,
            urlBtnEnabled = true
        )
    }

    private fun updateNoResultsUiState() {
        uiState = uiState.copy(
            name = "",
            article = artistInfoDescriptionHelper.getArtistInfoText(),
            url = "" ,
            urlBtnEnabled = false
        )
    }

    private fun initProperties() {
        textAbstract = findViewById(R.id.textPane2)
        textSource = findViewById(R.id.textPaneSource)
        btnUrl = findViewById(R.id.openUrlButton)
        nyTimesImg = findViewById(R.id.imageView)
    }

    private fun notifyGetArtistInfoAction() {
        onActionSubject.notify(MoreDetailsEvent.GetArtistInfo)
    }

    private fun initObservers() {
        moreDetailsModel.artistObservable
            .subscribe{ value -> updateArtistInfo(value) }
    }

    private fun updateArtistInfo(artistInfoFromRepository : Card) {
        updateUiState(artistInfoFromRepository)
        initListeners()
        updateUrlBtnState()
        applyImage()
        applyText()
    }

    private fun applyImage() {
        runOnUiThread {
            Picasso.get().load(NY_TIMES_IMG).into(nyTimesImg)
        }
    }

    private fun applyText() {
        runOnUiThread {
            textSource.text = "New York Times"
            textAbstract.text = getAbstractAsHtml()
        }
    }

    private fun getAbstractAsHtml() : Spanned {
        return formatHtml(uiState.article)
    }

    private fun updateUrlBtnState() {
        enableActions(uiState.urlBtnEnabled)
    }

    private fun enableActions(enable: Boolean) {
        runOnUiThread {
            btnUrl.isEnabled = enable
        }
    }

    override fun openExternalLink(url: String) {
        val intent = Intent(Intent.ACTION_VIEW)
        intent.data = Uri.parse(uiState.url)
        startActivity(intent)
    }

    private fun formatHtml(abstractHtml : String) : Spanned {
        return HtmlCompat.fromHtml(
            abstractHtml,
            HtmlCompat.FROM_HTML_MODE_LEGACY
        )
    }

    companion object {
        const val ARTIST_NAME_EXTRA = "artistName"
    }
}