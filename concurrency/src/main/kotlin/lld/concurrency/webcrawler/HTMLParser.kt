package lld.concurrency.webcrawler

interface HTMLParser {

    fun getURLs(fromURL: String): List<String>
}