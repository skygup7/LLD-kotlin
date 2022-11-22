package lld.snakeandladder.utils

object DiceUtil {

    fun getDiceRoll(diceCount: Long = 1): Long {
        return (1 * diceCount..6 * diceCount).random()
    }
}
