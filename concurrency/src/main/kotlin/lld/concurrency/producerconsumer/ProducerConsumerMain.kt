package lld.concurrency.producerconsumer

import lld.concurrency.producerconsumer.BlockingQueueWithSemaphore
import java.lang.Thread.sleep
import kotlin.concurrent.thread


fun main () {
    val blockingQueue = BlockingQueueWithSemaphore<String>(5)

    blockingQueue.checkIfAllSet()

    // 2 producers
    val producer1 = thread(start = true, isDaemon = true) {
        for (i in 1..100) {
            blockingQueue.enqueue(threadName = "A", value = "thread: A, val: $i")
        }
    }
    val producer2 = thread(start = true, isDaemon = true) {
        for (i in 1..100) {
            blockingQueue.enqueue(threadName = "B", value = "thread: B, val: $i")
        }
    }

    // 2 consumers
    val consumer1 = thread(start = true, isDaemon = true) {
        for (i in 1..100) {
            blockingQueue.dequeue(threadName = "C")
        }
    }
    val consumer2 = thread(start = true, isDaemon = true) {
        for (i in 1..100) {
            blockingQueue.dequeue(threadName = "D")
        }
    }

    sleep(10000)

    println("hope your semaphore concepts are clear")

}