package lld.snakeandladder.service

import lld.snakeandladder.models.Player

interface SnakeAndLadderGame {

    /**
     * Starts the game, prints all the rounds and chances of the players.
     */
    fun startGame()

    /**
     * Once the game has finished after calling the above method, returns the winning order of the players
     */
    fun getWinningOrder(): List<Player>
}
