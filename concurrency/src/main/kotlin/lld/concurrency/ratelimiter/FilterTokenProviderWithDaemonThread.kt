package lld.concurrency.ratelimiter

import java.lang.Thread.sleep
import java.time.Instant
import java.util.concurrent.Semaphore
import java.util.concurrent.locks.Lock
import java.util.concurrent.locks.ReentrantLock
import kotlin.concurrent.thread
import kotlin.math.min

class FilterTokenProviderWithDaemonThread(
    private val maxTokenCount: Long,
    private val allowedRequestsPerMilliSecond: Double = 0.001 // default: 1 token per second
) {

    private val minimumMilliTimeBetween2Requests: Double = 1/allowedRequestsPerMilliSecond

    private val tokenSemaphore = Semaphore(0)

    init {
        thread(start = true, isDaemon = true) {

            while (true) {
                if (tokenSemaphore.availablePermits() < maxTokenCount) {
                    println("available tokens: ${tokenSemaphore.availablePermits()}. releasing one more at: ${Instant.now()}")
                    tokenSemaphore.release()
                }

                sleep(minimumMilliTimeBetween2Requests.toLong())
            }
        }
    }


    fun getToken(threadName: String, tokenCount: Int) {

        println("thread: '$threadName' trying to get token at ${Instant.now()}")

        tokenSemaphore.acquire()

        println("thread: '$threadName' granted access at ${Instant.now()} for token count: $tokenCount.")
    }
}