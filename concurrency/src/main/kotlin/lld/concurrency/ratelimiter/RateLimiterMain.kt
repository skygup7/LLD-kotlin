package lld.concurrency.ratelimiter

import java.lang.Thread.sleep
import kotlin.concurrent.thread

fun main() {

//    testWithoutDaemonThread()

    testWithDaemonThread()
}

fun testWithoutDaemonThread() {
    val filterTokenProvider = FilterTokenProviderWithoutDaemonThread(maxTokenCount = 5)

    println("giving some time to create some tokens before hand")
    sleep(5000)

    val thread1 = thread(start = true, isDaemon = true) {
        for (i in 1..10) {
            filterTokenProvider.getToken(threadName = "A", tokenCount = i)
            sleep(2000)
        }
    }

    val thread2 = thread(start = true, isDaemon = true) {
        for (i in 1..10) {
            filterTokenProvider.getToken(threadName = "B", tokenCount = i)
            sleep(2500)
        }
    }

    val thread3 = thread(start = true, isDaemon = true) {
        for (i in 1..10) {
            filterTokenProvider.getToken(threadName = "C", tokenCount = i)
            sleep(3500)
        }
    }

    sleep(50000)
}

fun testWithDaemonThread() {
    val filterTokenProvider = FilterTokenProviderWithDaemonThread(maxTokenCount = 5)

    println("giving some time to create some tokens before hand")
    sleep(5000)

    val thread1 = thread(start = true, isDaemon = true) {
        for (i in 1..10) {
            filterTokenProvider.getToken(threadName = "A", tokenCount = i)
            sleep(1000)
        }
    }

    val thread2 = thread(start = true, isDaemon = true) {
        for (i in 1..10) {
            filterTokenProvider.getToken(threadName = "B", tokenCount = i)
            sleep(1500)
        }
    }

    sleep(21000)
}