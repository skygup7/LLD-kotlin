package lld.concurrency.schedulecallback

import java.time.Instant
import java.util.PriorityQueue
import java.util.concurrent.TimeUnit
import java.util.concurrent.locks.Lock
import java.util.concurrent.locks.ReentrantLock
import kotlin.concurrent.thread

class CallbackScheduler {

    private val scheduleQueue = PriorityQueue<CustomCallback>() { a, b ->
        (a.callbackAt.toEpochMilli() - b.callbackAt.toEpochMilli()).toInt()
    }
    private val lock: Lock = ReentrantLock()
    private val newCallbackAdded = lock.newCondition()

    init {
        thread(start = true, isDaemon = true) {
            while (true) {
                this.bgExecutor()
            }
        }
    }

    private fun executeCallback(callback: CustomCallback) {
        println("Your callback scheduled for: ${callback.callbackAt} has been executed with message: '${callback.message}' at: ${Instant.now()}")
    }

    private fun getSleepTimeInMillis(callback: CustomCallback): Long {
        return callback.callbackAt.toEpochMilli() - Instant.now().toEpochMilli()
    }

    private fun bgExecutor() {
        lock.lock()

        while (scheduleQueue.isEmpty()) {
            newCallbackAdded.await()
        }

        while (scheduleQueue.isNotEmpty()) {

            val sleepTime = getSleepTimeInMillis(scheduleQueue.peek())

            if (sleepTime <= 0) break

            newCallbackAdded.await(sleepTime, TimeUnit.MILLISECONDS)
        }

        executeCallback(scheduleQueue.poll())

        lock.unlock()
    }

    public fun scheduleCallback(scheduleAfterMillis: Long, message: String) {
        lock.lock()

        scheduleQueue.add(
            CustomCallback(
                callbackAt = Instant.now().plusMillis(scheduleAfterMillis),
                message = message
            )
        )
        newCallbackAdded.signal()
        println("Your callback with message: '$message' has been scheduled. current time: ${Instant.now()}")

        lock.unlock()
    }
}