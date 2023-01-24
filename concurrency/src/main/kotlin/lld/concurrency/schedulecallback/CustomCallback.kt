package lld.concurrency.schedulecallback

import java.time.Instant

data class CustomCallback(
    val callbackAt: Instant,
    val message: String
)
