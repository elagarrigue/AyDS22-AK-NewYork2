package ayds.newyork.songinfo.moredetails.controller

import ayds.newyork.songinfo.moredetails.model.MoreDetailsModel
import ayds.newyork.songinfo.moredetails.view.MoreDetailsEvent
import ayds.newyork.songinfo.moredetails.view.MoreDetailsUiState
import ayds.newyork.songinfo.moredetails.view.MoreDetailsView
import ayds.observer.Subject
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.Before
import org.junit.Test

class MoreDetailsControllerImplTest {

    private val moreDetailsModel: MoreDetailsModel = mockk(relaxUnitFun = true)

    private val onActionSubject = Subject<MoreDetailsEvent>()
    private val moreDetailsView: MoreDetailsView = mockk(relaxUnitFun = true) {
        every { moreDetailsEventObservable } returns onActionSubject
    }

    private val moreDetailsController by lazy {
        MoreDetailsControllerImpl(moreDetailsModel)
    }

    @Before
    fun setup() {
        moreDetailsController.setMoreDetailsView(moreDetailsView)
    }

    @Test
    fun `on search event should search artist`() {
        every { moreDetailsView.uiState } returns MoreDetailsUiState(name = "name")

        onActionSubject.notify(MoreDetailsEvent.GetArtistInfo)

        verify { moreDetailsModel.searchCards("name") }
    }

    @Test
    fun `on open artist info link event should open external link`() {
        every { moreDetailsView.uiState } returns MoreDetailsUiState(url = "url")

        onActionSubject.notify(MoreDetailsEvent.OpenArtistInfoLink)

        verify { moreDetailsView.openExternalLink("url") }
    }
}