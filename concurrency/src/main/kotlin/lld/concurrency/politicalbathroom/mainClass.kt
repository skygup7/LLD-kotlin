package lld.concurrency.politicalbathroom

import java.util.*;
import java.util.concurrent.locks.ReentrantLock
import kotlin.concurrent.thread

class CustomBathroom(
    private val capacity: Int
) {

    private val dQ: LinkedList<String> = LinkedList<String>()
    private val rQ: LinkedList<String> = LinkedList<String>()

    private var inUse: Party = Party.NONE
    private var useCount = 0

    private val lock = ReentrantLock()
    private val demSignal = lock.newCondition()
    private val repSignal = lock.newCondition()

    init {

    }

    fun bgExecuterDem() {
        val name: String

        lock.lock()

        try {

            while (dQ.isEmpty()) {
                demSignal.await()
            }

            while (inUse == Party.REP) {
                demSignal.await()
            }

            while (useCount == capacity) {
                demSignal.await()
            }

            inUse = Party.DEM
            useCount++

            name = dQ.pollFirst()

        } finally {
            lock.unlock()
        }

        thread(start = true, isDaemon = true) {
            useBathroom(name)

            lock.lock()
            useCount--

            if (useCount == 0) {
                if (!dQ.isEmpty()) {
                    demSignal.signalAll()
                } else {
                    inUse = Party.NONE
                    repSignal.signalAll()
                }
            }
            lock.unlock()
        }

    }

    fun bgExecuterRep() {
        val name: String

        lock.lock()

        try {

            while (rQ.isEmpty()) {
                repSignal.await()
            }

            while (inUse == Party.DEM) {
                repSignal.await()
            }

            while (useCount == capacity) {
                repSignal.await()
            }

            inUse = Party.REP
            useCount++

            name = dQ.pollFirst()

        } finally {
            lock.unlock()
        }

        thread(start = true, isDaemon = true) {
            useBathroom(name)

            lock.lock()
            useCount--

            if (useCount == 0) {
                if (!rQ.isEmpty()) {
                    repSignal.signalAll()
                } else {
                    inUse = Party.NONE
                    demSignal.signalAll()
                }
            }
            lock.unlock()
        }

    }

    fun republicanUseBathroom(name: String) {
        lock.lock()

        try {

            rQ.add(name)
            repSignal.signalAll()

        } finally {
            lock.unlock()
        }
    }

    fun democratUseBathroom(name: String) {
        lock.lock()

        try {

            dQ.add(name)
            demSignal.signalAll()

        } finally {
            lock.unlock()
        }
    }

    enum class Party {
        NONE,
        REP,
        DEM
    }
}

fun useBathroom(name: String) {
    Thread.sleep(10)
    println("$name used bathroom")
}

// Interview Question below

// Two types of people

// Democrats   D
// Republicans R
// Unique Name

// name, D/R


// PoliticalBathroom : capacity 3

// def use_bathroom(name: str):
//   # blocking
//   return


// 2 D
// 3 R
// 1 D/ 1R NA


// CommonQueue:

// (Name, D), (Name, R).....


// t = 0
// B = 1  / 2 remaining  [D [t=10] D[t=14] -]

// Queue:

// R  t =2
// R t =5

// D t =9


// D R D D R...

// class PoliticalBathroom:

//       def republican_use_bathroom(self, name):
//           pass

//       def democrat_use_bathroom(self, name):
//           pass



// main():
//   b= PoliticalBathroom()
//   b.republican_use_bathroom(name1)
//   b.democrat_use_bathroom(name2)



// for every person call

// use_bathroom(person)  / Blocking
