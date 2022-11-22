package lld.splitwise.service.impls

import lld.splitwise.models.Balance
import lld.splitwise.models.EqualExpense
import lld.splitwise.models.EqualSplit
import lld.splitwise.models.ExactExpense
import lld.splitwise.models.ExactSplit
import lld.splitwise.models.Expense
import lld.splitwise.models.ExpenseMetaData
import lld.splitwise.models.PercentExpense
import lld.splitwise.models.PercentSplit
import lld.splitwise.models.SplitType
import lld.splitwise.repository.ExpenseRepository
import lld.splitwise.repository.UserRepository
import lld.splitwise.service.SplitwiseService
import java.util.UUID

class SplitwiseServiceImpl(
    private val expenseRepository: ExpenseRepository,
    private val userRepository: UserRepository
) : SplitwiseService {
    override fun addNewExpense(amount: Long, paidBy: String, splitType: SplitType, splits: List<Pair<String, Long?>>) {
        validateUser(paidBy)
        splits.forEach { validateUser(it.first) }

        val expensePublicId = getPublicId("Expense-")
        val expenseMetaData = ExpenseMetaData(null, null, null) // TODO: add meta data
        val expense: Expense = when (splitType) {
            SplitType.Equal -> EqualExpense(
                publicId = expensePublicId,
                paidBy = paidBy,
                amount = amount,
                splits = splits.map { EqualSplit(userId = it.first) },
                metaData = expenseMetaData
            )
            SplitType.Exact -> ExactExpense(
                publicId = expensePublicId,
                paidBy = paidBy,
                amount = amount,
                splits = splits.map { ExactSplit(userId = it.first, amount = it.second!!) },
                metaData = expenseMetaData
            )
            SplitType.Percent -> PercentExpense(
                publicId = expensePublicId,
                paidBy = paidBy,
                amount = amount,
                splits = splits.map { PercentSplit(userId = it.first, percent = it.second!!) },
                metaData = expenseMetaData
            )
        }

        if (!expense.validate())
            throw RuntimeException("expense details not valid")

        expenseRepository.saveExpense(expense)
    }

    private fun validateUser(userID: String) {
        if (userRepository.getUser(userID) == null)
            throw RuntimeException("User: $userID} not found. please register first")
    }

    private fun getPublicId(prefix: String): String {
        return prefix + UUID.randomUUID().toString()
    }

    override fun showAllBalances() {
        expenseRepository.getAllBalances().forEach { printBalance(it) }
    }

    override fun showBalanceFor(userId: String) {
        validateUser(userId)

        expenseRepository.getUserBalance(userId).forEach { printBalance(it) }
    }

    private fun printBalance(balance: Balance) {
        if (balance.amount < 0) {
            println("${balance.userId1} owes ${-1 * balance.amount} to ${balance.userId2}")
        } else if (balance.amount > 0) {
            println("${balance.userId1} gets back ${balance.amount} from ${balance.userId2}")
        }
    }
}
