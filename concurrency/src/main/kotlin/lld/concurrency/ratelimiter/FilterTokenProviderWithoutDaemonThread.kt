package lld.concurrency.ratelimiter

import java.lang.Thread.sleep
import java.time.Instant
import java.util.concurrent.Semaphore
import kotlin.math.min

class FilterTokenProviderWithoutDaemonThread(
    private val maxTokenCount: Long,
    private val allowedRequestsPerMilliSecond: Double = 0.001 // default: 1 token per second
) {

    private var lastRequestTime = Instant.now()
    private var lastAvailableTokenCount: Long = 0
    private val minimumMilliTimeBetween2Requests: Double = 1/allowedRequestsPerMilliSecond

    private val lock: Semaphore = Semaphore(1)


    fun getToken(threadName: String, tokenCount: Int) {

        lock.acquire()

        try {

            lastAvailableTokenCount += ((Instant.now().toEpochMilli() -  lastRequestTime.toEpochMilli()) * allowedRequestsPerMilliSecond).toLong()
            lastAvailableTokenCount = min(lastAvailableTokenCount, maxTokenCount)

            if (lastAvailableTokenCount > 0) {
                lastAvailableTokenCount--
            } else {
                val sleepTime = (minimumMilliTimeBetween2Requests - (Instant.now().toEpochMilli() -  lastRequestTime.toEpochMilli()))
                println("sleeping thread: '$threadName' to cover minimum time before getting new token. i.e.: $sleepTime ms")
                sleep(sleepTime.toLong())
            }

            lastRequestTime = Instant.now()

            println("thread: '$threadName' granted access at $lastRequestTime for token count: $tokenCount. available tokens: $lastAvailableTokenCount")

        } finally {
            lock.release()
        }
    }
}