package lld.snakeandladder.models

data class Snake(
    val head: Long,
    val tail: Long
)

data class Ladder(
    val bottom: Long,
    val top: Long
)

data class GameBoard(
    val size: Long,

    val ladders: List<Ladder>,
    val snakes: List<Snake>
)

data class RunTimeGameBoard(
    val size: Long,

    val ladders: Map<Long, Long>,
    val snakes: Map<Long, Long>,

    val players: List<Player>,
    val playerPositions: MutableMap<String, Long>,

    val winningOrder: MutableList<Player>
)

data class Player(
    val id: String,
    val name: String,
    val color: PlayerColor
)

enum class PlayerColor {
    Red,
    Blue,
    Green,
    Yellow
}
