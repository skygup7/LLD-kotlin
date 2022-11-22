package lld.splitwise.service

import lld.splitwise.models.SplitType

interface SplitwiseService {

    /**
     * Unit function to add new expenses.
     * TODO: create payload object or expense create object
     *
     * @throws RuntimeException on validation errors like user not found or splits not valid
     */
    fun addNewExpense(
        amount: Long,
        paidBy: String,
        splitType: SplitType,
        splits: List<Pair<String, Long?>>
    )

    fun showAllBalances()

    fun showBalanceFor(userId: String)
}
