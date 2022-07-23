package lld.splitwise.repository.impls

import lld.splitwise.models.Balance
import lld.splitwise.models.Expense
import lld.splitwise.repository.ExpenseRepository
import kotlin.math.abs

class ExpenseRepositoryInMemImpl: ExpenseRepository {

    companion object {
        private const val MARGIN_ERROR: Double = 0.01
    }

    private val expenses: MutableMap<String, Expense> = mutableMapOf()
    private val balances: MutableMap<String, MutableMap<String, Double>> = mutableMapOf()

    override fun saveExpense(expense: Expense) {
        expenses[expense.publicId] = expense

        val splits = expense.getSplitAmounts()
        val paidBy = expense.paidBy

        splits.filter { it.key != paidBy }.forEach { (userId, amount) ->
            val balance1 = balances[userId] ?: mutableMapOf()
            val balance2 = balances[paidBy] ?: mutableMapOf()

            balance1[paidBy] = balance1.getOrDefault(paidBy, 0.0) + -1 * amount
            balance2[userId] = balance2.getOrDefault(userId, 0.0) + amount

            balances[userId] = balance1
            balances[paidBy] = balance2
        }
    }

    override fun getAllBalances(): List<Balance> {
        return balances.keys.flatMap { getUserBalance(it) }.filter { it.amount > 0 }
    }

    override fun getUserBalance(userId: String): List<Balance> {
        return balances.getOrDefault(userId, emptyMap()).map { (lender, amount) ->
            Balance(
                userId1 = userId,
                userId2 = lender,
                amount = amount
            )
        }.filter { abs(it.amount - 0.0) > MARGIN_ERROR } // non-zero amount check by using round-off margin
    }

}