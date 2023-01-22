package lld.concurrency.producerconsumer

import org.slf4j.LoggerFactory
import java.util.LinkedList
import java.util.concurrent.Semaphore

class BlockingQueueWithSemaphore<T>(
    private val maxSize: Int
) {

    private val logger = LoggerFactory.getLogger(BlockingQueueWithSemaphore::class.java)

    private val producerSemaphore: Semaphore = Semaphore(maxSize)
    private val consumerSemaphore: Semaphore = Semaphore(0)
    private val lock: Semaphore = Semaphore(1)

    private var size = 0;
    private val q = LinkedList<T>()

    fun checkIfAllSet() {

        println("available producer permits: ${producerSemaphore.availablePermits()}")
        println("available consumer permits: ${consumerSemaphore.availablePermits()}")
        println("available lock permits: ${lock.availablePermits()}")
    }

    fun enqueue(threadName: String, value: T) {

        producerSemaphore.acquire()
        lock.acquire() // critical section begins

        q.add(value)
        size++

        if (size > maxSize) logger.warn("current size of the queue is $size") // should never happen
        println("producer thread: $threadName enqueued val: '$value'")

        lock.release() // end of critical section
        consumerSemaphore.release()
    }

    fun dequeue(threadName: String): T {

        consumerSemaphore.acquire()
        lock.acquire() // critical section begins

        val result = q.first()
        q.removeFirst()
        size--
        if (size < 0) logger.warn("current size of the queue is $size") // should never happen
        println("consumer thread: $threadName dequeued val: '$result'")

        lock.release() // end of critical section
        producerSemaphore.release()

        return result
    }
}