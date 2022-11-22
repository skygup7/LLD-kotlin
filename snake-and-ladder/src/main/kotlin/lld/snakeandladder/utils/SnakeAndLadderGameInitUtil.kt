package lld.snakeandladder.utils

import lld.snakeandladder.models.GameBoard
import lld.snakeandladder.models.Ladder
import lld.snakeandladder.models.Player
import lld.snakeandladder.models.PlayerColor
import lld.snakeandladder.models.Snake
import org.slf4j.LoggerFactory
import java.util.UUID

object SnakeAndLadderGameInitUtil {

    private val LOGGER = LoggerFactory.getLogger(SnakeAndLadderGameInitUtil::class.java)

    fun getGameBoard(): GameBoard {
        LOGGER.info("Waiting for user input. Format: <boardSize> <snakeCount> <ladderCount>")
        var input = readln().split(" ")

        val boardSize = input[0].toLong()
        val snakeCount = input[1].toLong()
        val ladderCount = input[2].toLong()

        LOGGER.info("Waiting for $snakeCount user inputs. Format: <snakeHead> <snakeTail>")

        val snakes: MutableList<Snake> = mutableListOf()
        for (i in 1..snakeCount) {
            input = readln().split(" ")
            snakes.add(
                Snake(
                    head = input[0].toLong(),
                    tail = input[1].toLong()
                )
            )
        }

        LOGGER.info("Waiting for $ladderCount user inputs. Format: <ladderBottom> <ladderTop>")

        val ladders: MutableList<Ladder> = mutableListOf()
        for (i in 1..ladderCount) {
            input = readln().split(" ")
            ladders.add(
                Ladder(
                    bottom = input[0].toLong(),
                    top = input[1].toLong()
                )
            )
        }

        return GameBoard(
            size = boardSize,
            ladders = ladders,
            snakes = snakes
        )
    }

    fun getPlayers(): List<Player> {
        LOGGER.info("Waiting for user input. Format: <playerCount>")
        var input = readln().split(" ")

        val playersCount = input[0].toLong()

        LOGGER.info("Waiting for $playersCount user inputs. Format: <playerName> <playerColor>")

        val players: MutableList<Player> = mutableListOf()
        for (i in 1..playersCount) {
            input = readln().split(" ")
            players.add(
                Player(
                    id = getPublicId("player-"),
                    name = input[0],
                    color = PlayerColor.valueOf(input[1])
                )
            )
        }

        return players
    }

    private fun getPublicId(prefix: String): String {
        return prefix + UUID.randomUUID().toString()
    }
}
