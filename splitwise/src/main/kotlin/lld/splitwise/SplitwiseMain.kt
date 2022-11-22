package lld.splitwise

import lld.splitwise.models.SplitType
import lld.splitwise.models.User
import lld.splitwise.repository.ExpenseRepository
import lld.splitwise.repository.UserRepository
import lld.splitwise.repository.impls.ExpenseRepositoryInMemImpl
import lld.splitwise.repository.impls.UserRepositoryInMemImpl
import lld.splitwise.service.SplitwiseService
import lld.splitwise.service.impls.SplitwiseServiceImpl
import java.util.logging.Logger

private val LOGGER = Logger.getLogger("Splitwise")

fun main() {
    /**
     * Initialise services and repositories
     */
    val userRepository: UserRepository = UserRepositoryInMemImpl()
    val expenseRepository: ExpenseRepository = ExpenseRepositoryInMemImpl()

    val splitwiseService: SplitwiseService = SplitwiseServiceImpl(
        expenseRepository = expenseRepository,
        userRepository = userRepository
    )

    /**
     * Hydrate user list
     */
    val userList = listOf(
        User(
            id = "sky",
            name = "Akash Gupta",
            email = "sky@splitwise.com",
            phone = null
        ),
        User(
            id = "fire",
            name = "Jwaala Pandey",
            email = "fire@splitwise.com",
            phone = null
        ),
        User(
            id = "cold",
            name = "Sheetal Sharma",
            email = "cold@splitwise.com",
            phone = null
        ),
        User(
            id = "water",
            name = "Salil Singh",
            email = "water@splitwise.com",
            phone = null
        )
    )
    userList.forEach { userRepository.addUser(it) }

    /**
     * Print user instructions
     */
    LOGGER.info("Splitwise initialised")

    LOGGER.info(
        "Expense input format: EXPENSE <paid-by-user-id> <amount> <number-of-users> <space-separated-list-of-users>" +
            " <Equal/Exact/Percent> <space-separated-values-in-case-of-non-equal>"
    )

    LOGGER.info("Show balances for all: Show")

    LOGGER.info("Show balances for a single user: Show <user-id>")
// TODO: Introduce add user feature functionality
//    LOGGER.info("Add a single user: ADDUSER <user-id> <name>")

    LOGGER.info("Exit the application: Exit")

    /**
     * Wait for user input and then process it
     */
    var ioReaderFlag = true
    var it: Int
    while (ioReaderFlag) {
        run ioBlock@{
            LOGGER.info("Ready to store and split expenses. Waiting for user input for next expense")

            val lineInput = readln()

            val inputTokens: List<String> = lineInput.split(" ")

            it = 1
            when (inputTokens[0]) {
                "Expense" -> {
                    val paidBy = inputTokens[it]; it++
                    val amount = inputTokens[it].toLong(); it++
                    val splitUserCount = inputTokens[it].toInt(); it++
                    val splitUsers = inputTokens.slice(it until it + splitUserCount); it += splitUserCount

                    val splitType: SplitType = try {
                        SplitType.valueOf(inputTokens[it])
                    } catch (e: Exception) {
                        LOGGER.severe { "${inputTokens[it]} split type not supported. Only EQUAL/EXACT/PERCENT are supported" }
                        return@ioBlock
                    }
                    it++

                    val splits: List<Pair<String, Long?>> = try {
                        when (splitType) {
                            SplitType.Equal -> {
                                splitUsers.map { Pair(it, null) }
                            }
                            SplitType.Exact -> {
                                splitUsers.mapIndexed { index, userId ->
                                    Pair(userId, inputTokens[it + index].toLong())
                                }
                            }
                            SplitType.Percent -> {
                                splitUsers.mapIndexed { index, userId ->
                                    Pair(userId, inputTokens[it + index].toLong())
                                }
                            }
                        }
                    } catch (e: Exception) {
                        LOGGER.severe { "incorrect split format. couldn't find splits for users." }
                        return@ioBlock
                    }

                    try {
                        splitwiseService.addNewExpense(
                            amount = amount,
                            paidBy = paidBy,
                            splitType = splitType,
                            splits = splits
                        )
                    } catch (e: Exception) {
                        LOGGER.severe { e.message }
                        return@ioBlock
                    }
                }
                "Show" -> {
                    if (inputTokens.size > 1) {
                        try {
                            splitwiseService.showBalanceFor(inputTokens[it])
                        } catch (e: Exception) {
                            LOGGER.severe { e.message }
                            return@ioBlock
                        }
                    } else {
                        splitwiseService.showAllBalances()
                    }
                }
                "Exit" -> {
                    ioReaderFlag = false
                }
                else -> {
                    LOGGER.severe("${inputTokens[0]} command not supported")
                }
            }
        }
    }

    LOGGER.info("Thank you for using the Splitwise application. Come back soon with more expenses. Bye, until then")
}
