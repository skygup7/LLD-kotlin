package lld.concurrency.webcrawler

import java.util.LinkedList
import java.util.concurrent.TimeUnit
import java.util.concurrent.locks.ReentrantLock
import kotlin.concurrent.thread

class WebCrawlerMultithreaded {

    private lateinit var hostName: String
    private val seen: HashSet<String> = HashSet()
    private val result: MutableList<String> = mutableListOf()
    private val queue = LinkedList<String>()

    private val lock = ReentrantLock()
    private val cv = lock.newCondition()
    private var threadsCurrentlyCrawling = 0;

    private val threadCount = 10

    @Synchronized
    fun crawl(initialURL: String, htmlParser: HTMLParser): List<String> {
        initializeVariables(initialURL)

        val threads: MutableList<Thread> = mutableListOf()

        for (i in 1..threadCount) {
            threads.add(
                thread(start = true) {
                    var flag = true
                    while (flag) {
                        flag = bfsCrawler(htmlParser)
                    }
                }
            )
        }

        for (thread in threads) {
            thread.join()
        }


        return result
    }

    private fun initializeVariables(initialURL: String) {
        hostName = getHostNameFromURL(initialURL)

        seen.clear()
        seen.add(initialURL)

        result.clear()
        result.add(initialURL)
    }

    private fun getHostNameFromURL(url: String): String {
        TODO()
    }

    private fun checkValidHost(url: String): Boolean {
        TODO()
    }

    private fun bfsCrawler(htmlParser: HTMLParser): Boolean {
        val currentUrl: String

        lock.lock()
        try{
            while (threadsCurrentlyCrawling > 0 && queue.isEmpty()) {
                cv.await(30, TimeUnit.MILLISECONDS)
            }
            currentUrl = if (queue.isEmpty()) { return false } else { queue.pollFirst() }

            result.add(currentUrl)
            threadsCurrentlyCrawling++

        } finally {
            lock.unlock()
        }

        val newURLs = htmlParser.getURLs(currentUrl)


        lock.lock()
        try {
            for (newUrl in newURLs) {
                if (!seen.contains(newUrl) && checkValidHost(newUrl)) {
                    queue.add(newUrl)
                    seen.add(newUrl)
                }
            }

            threadsCurrentlyCrawling--;
            cv.signalAll()
        } finally {
            lock.unlock()
        }

        return true
    }
}