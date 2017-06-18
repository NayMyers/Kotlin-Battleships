package ys.com.battleships

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import ys.com.battleships.ships.Direction
import ys.com.battleships.ships.Ship

/**
 * Created by Yuval Shabtai on 6/17/2017.
 */
class FriendlyBattleGridView : BattleGridView {

    val shipsPaint : Paint
    /*
     * Ship 0 is a Carrier (size 5)
     * Ship 1 is a Battleship (size 4)
     * Ship 2 is a Cruise (size 3)
     * Ship 3 is a Submarine (size 3)
     * Ship 4 is a destroyer (size 4)
     */
    var ships : Array<Ship>? = null

    var shipArr : Array<Array<Ship>>? = null

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        ships = arrayOf(Ship(5, Direction.RIGHT, 0, 7),
                        Ship(4, Direction.DOWN, 1, 0),
                        Ship(3, Direction.DOWN, 2, 0),
                        Ship(3, Direction.DOWN, 3, 0),
                        Ship(2, Direction.DOWN, 4, 0))

        shipsPaint = Paint(Paint.ANTI_ALIAS_FLAG)
        shipsPaint.color = Color.argb(0xFF, 0x2A, 0x74, 0x32)
        shipsPaint.strokeWidth = 20F
    }

    override fun onDraw(canvas: Canvas) {
        cellWidth = width / (gridSize.toFloat())
        cellHeight = height / (gridSize.toFloat())

        for(ship in ships!!) {
            drawShip(ship, canvas)
        }
        drawLines(canvas)
        drawSquares(canvas)
    }

    private fun drawShip(ship: Ship, canvas: Canvas) {
        var x1 : Float = ship.startX.toFloat()
        var y1 : Float = ship.startY.toFloat()

        var x2 : Float = x1
        var y2 : Float = y1

        if(ship.direction == Direction.RIGHT) {
            x2 = x1 + ship.size - 1
        } else if(ship.direction == Direction.LEFT) {
            x2 = x1 - ship.size + 1
        }
        if(ship.direction == Direction.DOWN) {
            y2 = y1 + ship.size - 1
        } else if(ship.direction == Direction.UP) {
            y2 = y1 - ship.size + 1
        }

        x1 *= cellWidth
        y1 *= cellHeight
        x2 *= cellWidth
        y2 *= cellHeight

        x1 += cellWidth/2
        y1 += cellHeight/2
        x2 += cellWidth/2
        y2 += cellHeight/2

        canvas.drawLine(x1, y1, x2, y2, shipsPaint)
    }
}