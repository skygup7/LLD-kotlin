package lld.concurrency.schedulecallback

import java.lang.Thread.sleep
import kotlin.concurrent.thread

fun main() {
    val callbackScheduler = CallbackScheduler()

    sleep(1000)
    val initialGap = 5000

    for (i in 1..10) {
        thread(start = true, isDaemon = true) {
            callbackScheduler.scheduleCallback(1000L*i + initialGap, "schedule job from thread $i")
        }
    }

    for (i in 1..10) {
        thread(start = true, isDaemon = true) {
            callbackScheduler.scheduleCallback(1000L*i + initialGap, "schedule job from thread ${i+10}")
        }
    }

    for (i in 1..10) {
        thread(start = true, isDaemon = true) {
            callbackScheduler.scheduleCallback(1500L*i, "schedule job from thread ${i+20}")
        }
    }

    sleep(20000)
}