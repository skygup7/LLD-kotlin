package lld.splitwise.models

import java.net.URL

// TODO: add audit columns
interface Expense {
    /**
     * Attributes
     */

    /* Expense public Id */
    val publicId: String
    /* userID of the user who paid the expense */
    val paidBy: String
    val amount: Long

    val splits: List<Split>

    val metaData: ExpenseMetaData

    /**
     * Methods
     */
    fun validate(): Boolean

    fun getSplitAmounts(): Map<String, Double>
}

data class EqualExpense(
    override val publicId: String,
    override val paidBy: String,
    override val amount: Long,
    override val splits: List<EqualSplit>,
    override val metaData: ExpenseMetaData
) : Expense {
    override fun validate(): Boolean {
        return true
    }

    override fun getSplitAmounts(): Map<String, Double> {
        return splits.associate { it.userId to (amount / splits.size.toDouble()) }
    }
}

data class ExactExpense(
    override val publicId: String,
    override val paidBy: String,
    override val amount: Long,
    override val splits: List<ExactSplit>,
    override val metaData: ExpenseMetaData
) : Expense {
    override fun validate(): Boolean {

        val total: Long = splits.fold(0L) { acc, split -> acc + split.amount }

        if (total != amount) return false

        return true
    }

    override fun getSplitAmounts(): Map<String, Double> {
        return splits.associate { it.userId to it.amount.toDouble() }
    }
}

data class PercentExpense(
    override val publicId: String,
    override val paidBy: String,
    override val amount: Long,
    override val splits: List<PercentSplit>,
    override val metaData: ExpenseMetaData
) : Expense {
    override fun validate(): Boolean {

        val totalPercentage: Long = splits.fold(0) { acc, split -> acc + split.percent }
        if (totalPercentage != 100L) return false

        return true
    }

    override fun getSplitAmounts(): Map<String, Double> {
        return splits.associate { it.userId to getSplitAmount(it) }
    }

    private fun getSplitAmount(percentSplit: PercentSplit): Double {
        return (percentSplit.percent * this.amount) / 100.0
    }
}

data class ExpenseMetaData(
    val name: String?,
    val imageURL: URL?,
    val notes: String?
)
