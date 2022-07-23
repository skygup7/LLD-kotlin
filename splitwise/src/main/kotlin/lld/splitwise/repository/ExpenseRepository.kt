package lld.splitwise.repository

import lld.splitwise.models.Balance
import lld.splitwise.models.Expense

interface ExpenseRepository {

    fun saveExpense(expense: Expense)

    /**
     * gets all non-zero pending balances and filters negative balances to avoid duplicate balance from other side
     */
    fun getAllBalances(): List<Balance>

    /**
     * fetches all non-zero pending balances for a [userId]
     */
    fun getUserBalance(userId: String): List<Balance>
}