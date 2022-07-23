package lld.snakeandladder

import lld.snakeandladder.models.RunTimeGameBoard
import lld.snakeandladder.service.SnakeAndLadderGame
import lld.snakeandladder.service.impl.SnakeAndLadderGameImpl
import lld.snakeandladder.utils.SnakeAndLadderGameInitUtil
import org.slf4j.LoggerFactory
import java.util.logging.Logger


private  val logger = LoggerFactory.getLogger("SnakeAndLadder")

fun main() {
    logger.info("Welcome to Snake and Ladder")

    val gameBoard = SnakeAndLadderGameInitUtil.getGameBoard()
    val players = SnakeAndLadderGameInitUtil.getPlayers()

    logger.info("Waiting for user input. Format: <number of dices>")
    val diceCount = readln().toLong()

    val runTimeGameBoard = RunTimeGameBoard(
        size = gameBoard.size,
        ladders = gameBoard.ladders.associate { Pair(it.bottom, it.top) },
        snakes = gameBoard.snakes.associate { Pair(it.head, it.tail) },
        players = players,
        playerPositions = players.associate { Pair(it.id, 0L) }.toMutableMap(),
        winningOrder = mutableListOf()
    )

    val snakeAndLadderGame: SnakeAndLadderGame = SnakeAndLadderGameImpl(
        gameBoard = runTimeGameBoard,
        diceCount = diceCount
    )

    snakeAndLadderGame.startGame()

    logger.info (
        "Winning order is: " + snakeAndLadderGame.getWinningOrder().joinToString(", ") { it.name }
    )

    logger.info(
        "Thanks for playing the game. See you next time!!!"
    )
}
