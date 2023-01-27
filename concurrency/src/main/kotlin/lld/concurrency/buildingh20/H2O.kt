package lld.concurrency.buildingh20

import java.util.concurrent.locks.ReentrantLock

internal class H2O {

    private val lock: ReentrantLock = ReentrantLock()
    private val waitForReconstruction = lock.newCondition()
    private val waitForOthers = lock.newCondition()
    private var hCount = 0
    private var oCount = 0
    private var hComplete = false
    private var oComplete = false
    private var isConstructing = false

    fun hydrogen() {
        lock.lock()

        while (hComplete) {
            waitForReconstruction.await()
        }
        hCount++
        if (hCount == 2) hComplete = true
        if (!hComplete || !oComplete) {
            while (!isConstructing) {
                waitForOthers.await()
            }
        } else {
            isConstructing = true
            waitForOthers.signalAll()
        }
        lock.unlock()

        // releaseHydrogen.run() outputs "H". Do not change or remove this line.


        lock.lock()
        hCount--
        if (hCount == 0 && oCount == 0) {
            isConstructing = false
            oComplete = false
            hComplete = false
            waitForReconstruction.signalAll()
        }
        lock.unlock()
    }

    fun oxygen(releaseOxygen: Runnable) {
        lock.lock()
        while (oComplete) {
            waitForReconstruction.await()
        }
        oCount++
        oComplete = true
        if (!hComplete || !oComplete) {
            while (!isConstructing) {
                waitForOthers.await()
            }
        } else {
            isConstructing = true
            waitForOthers.signalAll()
        }
        lock.unlock()

        // releaseOxygen.run() outputs "O". Do not change or remove this line.
        releaseOxygen.run()
        lock.lock()
        oCount--
        if (hCount == 0 && oCount == 0) {
            isConstructing = false
            oComplete = false
            hComplete = false
            waitForReconstruction.signalAll()
        }
        lock.unlock()
    }
}