package ys.com.battleships.ships

/**
 * Created by Yuval Shabtai on 6/17/2017.
 *
 * The Ship class represents a Battleships ship, given a starting coordinate,
 * direction, and size.
 */
class Ship(size: Int, direction: Direction, startX: Int, startY: Int) {
    val size : Int
    var direction : Direction
    var startX : Int
    var startY : Int
    var damage : Int

    init {
        this.size = size
        this.direction = direction
        this.startX = startX
        this.startY = startY

        damage = 0
    }

    /**
     * Called whenever a piece of this ship has been hit.
     */
    fun takeHit() {
        damage += 1
    }

    /**
     * Returns whether or not this ship had drowned.
     */
    fun isDrowned() : Boolean {
        return size <= damage
    }

    /**
     * Rotates the ship.
     */
    fun rotate() {
        direction = if(direction == Direction.DOWN) Direction.RIGHT else Direction.DOWN
    }
}