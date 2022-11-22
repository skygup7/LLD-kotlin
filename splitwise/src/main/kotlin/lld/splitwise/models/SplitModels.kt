package lld.splitwise.models

interface Split {
    val userId: String
}

data class EqualSplit(
    override val userId: String
) : Split

data class ExactSplit(
    override val userId: String,
    val amount: Long
) : Split

data class PercentSplit(
    override val userId: String,
    val percent: Long
) : Split
