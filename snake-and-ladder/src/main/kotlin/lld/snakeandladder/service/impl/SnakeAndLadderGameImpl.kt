package lld.snakeandladder.service.impl

import lld.snakeandladder.models.Player
import lld.snakeandladder.models.RunTimeGameBoard
import lld.snakeandladder.service.SnakeAndLadderGame
import lld.snakeandladder.utils.DiceUtil
import org.slf4j.LoggerFactory

class SnakeAndLadderGameImpl(
    private val gameBoard: RunTimeGameBoard,
    private val diceCount: Long
) : SnakeAndLadderGame {

    private val logger = LoggerFactory.getLogger(SnakeAndLadderGameImpl::class.java)

    override fun startGame() {
        val currentPlayers: MutableSet<Player> = this.gameBoard.players.toMutableSet()

        while (currentPlayers.size > 1) {
            playRound(currentPlayers)
        }

        gameBoard.winningOrder.add(currentPlayers.first())
    }

    private fun playRound(players: MutableSet<Player>) {
        players.forEach {
            if (playChance(it)) {
                logger.info("${it.name} won!!")
                players.remove(it)
                gameBoard.winningOrder.add(it)
            }
        }
    }

    /**
     * @return true when the player has reached the end, otherwise returns false
     */
    private fun playChance(player: Player): Boolean {
        logger.info("It's ${player.name} turn")
        val diceRoll = DiceUtil.getDiceRoll(diceCount = diceCount)
        logger.info("$diceRoll rolled for ${player.name}")

        val oldPos = gameBoard.playerPositions[player.id]!!
        var newPos = oldPos + diceRoll

        if (newPos > 100L) {
            logger.info("Can't move beyond ${gameBoard.size}. Dismissing the dice roll")
            return false
        }

        logger.info("${player.name} reaches on position: $newPos")

        while (true) {
            if (gameBoard.snakes[newPos] != null) {
                newPos = gameBoard.snakes[newPos]!!
                logger.info("Oops! found snake. Taking ${player.name} to position: $newPos")
                continue
            }
            if (gameBoard.ladders[newPos] != null) {
                newPos = gameBoard.ladders[newPos]!!
                logger.info("Yay! found ladder. Taking ${player.name} to position: $newPos")
                continue
            }
            break
        }

        gameBoard.playerPositions[player.id] = newPos
        return newPos == gameBoard.size
    }

    override fun getWinningOrder(): List<Player> {
        return this.gameBoard.winningOrder
    }
}
