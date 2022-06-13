package ayds.newyork.songinfo.moredetails.model.repository.external

import ayds.newyork.songinfo.moredetails.model.entities.Card

interface Proxy {
    fun getCard(name : String) : Card?
}