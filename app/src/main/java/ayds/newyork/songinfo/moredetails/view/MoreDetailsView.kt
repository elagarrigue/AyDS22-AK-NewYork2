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
import ayds.newyork.songinfo.moredetails.model.entities.EmptyCard
import ayds.newyork.songinfo.moredetails.model.entities.ExternalCard
import ayds.newyork.songinfo.moredetails.model.entities.Source
import ayds.observer.Observable
import ayds.observer.Subject
import com.squareup.picasso.Picasso

interface MoreDetailsView {
    val moreDetailsEventObservable: Observable<MoreDetailsEvent>
    var uiState : MoreDetailsUiState

    fun openExternalLink(url: String)
}

class MoreDetailsViewActivity : AppCompatActivity(), MoreDetailsView {

    private val artistInfoDescriptionHelper: ArtistInfoDescriptionHelper = MoreDetailsViewInjector.artistInfoDescriptionHelper
    private val onActionSubject = Subject<MoreDetailsEvent>()
    override val moreDetailsEventObservable: Observable<MoreDetailsEvent> = onActionSubject
    private lateinit var moreDetailsModel: MoreDetailsModel
    override var uiState = MoreDetailsUiState()

    private lateinit var textAbstract: TextView
    private lateinit var textSource: TextView
    private lateinit var btnUrl: Button
    private lateinit var btnNext: Button
    private lateinit var cardImg: ImageView


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
        btnNext.setOnClickListener { notifyGetNextCard() }
    }

    private fun notifyGetNextCard() {
        onActionSubject.notify(MoreDetailsEvent.UpdateIndex)
        onActionSubject.notify(MoreDetailsEvent.GetNextCard)
    }

    private fun notifyOpenArtistInfoUrl() {
        onActionSubject.notify(MoreDetailsEvent.OpenArtistInfoLink)
    }

    private fun updateUiState(card: Card) {
        when(card){
            is ExternalCard -> updateMoreDetailsUiState(card)
            is EmptyCard -> {
                updateNoResultsUiState()
                disableBtnNext()
            }
        }
    }

    private fun updateMoreDetailsUiState(card : Card) {
        uiState = uiState.copy(
            name = card.artistName,
            article = artistInfoDescriptionHelper.getCardText(card),
            url = card.infoUrl,
            urlBtnEnabled = true,
            source = card.source,
            image = card.sourceLogoUrl,
        )
        updateCard()
    }

    private fun updateNoResultsUiState() {
        uiState = uiState.copy(
            name = "",
            article = artistInfoDescriptionHelper.getCardText(),
            url = "" ,
            urlBtnEnabled = false,
            source = null,
            image = "",
        )
    }

    private fun initProperties() {
        textAbstract = findViewById(R.id.descriptionText)
        textSource = findViewById(R.id.sourceText)
        btnUrl = findViewById(R.id.openUrlButton)
        cardImg = findViewById(R.id.imageView)
        btnNext = findViewById(R.id.nextCardButton)
    }

    private fun notifyGetArtistInfoAction() {
        onActionSubject.notify(MoreDetailsEvent.GetArtistInfo)
    }

    private fun initObservers() {
        moreDetailsModel.artistObservable
            .subscribe{ value -> updateCardInfo(value) }
    }

    private fun updateCardInfo(artistInfoFromRepository : Card) {
        updateUiState(artistInfoFromRepository)
    }

    private fun updateCard(){
        initListeners()
        updateUrlBtnState()
        applyImage()
        applyText()
    }

    private fun applyImage() {
        runOnUiThread {
            Picasso.get().load(uiState.image).into(cardImg)
        }
    }

    private fun applyText() {
        runOnUiThread {
            textSource.text = getSource(uiState.source)
            textAbstract.text = getAbstractAsHtml()
        }
    }

    private fun getSource(source : Source?) : String{
        return when (source) {
            Source.LASTFM -> "LASTFM"
            Source.WIKIPEDIA -> "WIKIPEDIA"
            Source.NEWYORKTIMES -> "NEWYORKTIMES"
            else -> "Not Found"
        }
    }

    private fun getAbstractAsHtml() : Spanned {
        return formatHtml(uiState.article)
    }

    private fun updateUrlBtnState() {
        enableActions(uiState.urlBtnEnabled)
    }

    private fun disableBtnNext(){
        runOnUiThread {
            btnNext.isEnabled = false
        }
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