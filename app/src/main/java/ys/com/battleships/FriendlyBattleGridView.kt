package ys.com.battleships

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
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

    var selectedShip : Ship? = null
    var origStartX : Int = 0
    var origStartY : Int = 0
    var selectX : Int = 0
    var selectY : Int = 0
    var hasMoved : Boolean = false

    var shipArr : Array<Array<Ship?>>? = null

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        ships = arrayOf(Ship(5, Direction.DOWN, 6, 0),
                        Ship(4, Direction.RIGHT, 0, 1),
                        Ship(3, Direction.RIGHT, 0, 2),
                        Ship(3, Direction.RIGHT, 0, 3),
                        Ship(2, Direction.RIGHT, 0, 4))

        shipsPaint = Paint(Paint.ANTI_ALIAS_FLAG)
        shipsPaint.color = Color.argb(0xFF, 0x2A, 0x74, 0x32)
        shipsPaint.strokeWidth = 20F

        reloadShipArr()
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
        } else if(ship.direction == Direction.DOWN) {
            y2 = y1 + ship.size - 1
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

    /**
     * Locks up the grid such that no ships can be moved.
     * Sets up the ship reference grid for future use.
     */
    //TODO
    fun lock() {

    }

    /**
     * Places ships on the references array.
     */
    fun reloadShipArr() {
        //Reset array
        shipArr = Array<Array<Ship?>>(gridSize, {i -> Array<Ship?>(gridSize, {i -> null})})

        //Place ships
        for(ship in ships!!) {
            placeShip(ship)
        }
    }

    /**
     * Writes a ship to shipArr.
     */
    private fun placeShip(ship: Ship) {
        var x = ship.startX
        var y = ship.startY

        var xDir = 0
        var yDir = 0
        if(ship.direction == Direction.RIGHT) {
            xDir = 1
        } else if(ship.direction == Direction.DOWN) {
            yDir = 1
        }

        for(i in 0..(ship.size - 1)) {
            shipArr!![x][y] = ship

            x += xDir
            y += yDir
        }
    }

    /**
     * Fixes the position of a placed ship such that it doesn't occupy the space of an existing ship
     * as well as doesn't exit the boundaries of the grid.
     */
    fun unclipSelectedShip() {
        if(selectedShip == null) {
            return
        }

        var newStartX = selectedShip!!.startX
        var newStartY = selectedShip!!.startY

        var offset = 0
        while(isClipped(selectedShip!!)) {
            for(y in offset downTo -offset) {
                for(x in offset downTo -offset) {
                    selectedShip!!.startX = newStartX + x
                    selectedShip!!.startY = newStartY + y

                    if(!isClipped(selectedShip!!)) {
                        return
                    }
                }
            }

            offset += 1
        }
    }

    /**
     * Determines whether a ship occupies the space of another ship
     * or goes beyond the grid boundaries.
     */
    private fun isClipped(ship: Ship) : Boolean {
        var checkX = ship!!.startX
        var checkY = ship!!.startY

        var xDir = 0
        var yDir = 0

        if(ship.direction == Direction.DOWN) {
            yDir = 1
        } else {
            xDir = 1
        }

        for(i in 0..ship.size - 1) {
            if((checkX >= gridSize || checkY >= gridSize) || (checkX < 0 || checkY < 0) ||
                    (shipArr!![checkX][checkY] != null && shipArr!![checkX][checkY] != ship)) {
                return true
            }

            checkX += xDir
            checkY += yDir
        }

        return false;
    }


    override fun onTouchEvent(event: MotionEvent?): Boolean {
        //When the screen is initially pressed, a ship is selected
        //When the user moves the ship, it moves to the best of its ability
        //to wherever the user places it
        if(event!!.action == MotionEvent.ACTION_DOWN) {
            hasMoved = false
            selectX = xToGrid(event.x)
            selectY = yToGrid(event.y)
            selectedShip = shipArr!![selectX][selectY]

            if(selectedShip != null) {
                origStartX = selectedShip!!.startX
                origStartY = selectedShip!!.startY
            }
        } else if(event!!.action == MotionEvent.ACTION_MOVE) {
            if(selectedShip != null) {
                val newX = xToGrid(event.x)
                val newY = yToGrid(event.y)

                selectedShip!!.startX = origStartX + (newX - selectX)
                selectedShip!!.startY = origStartY + (newY - selectY)

                if(!hasMoved && (selectedShip!!.startX != origStartX || selectedShip!!.startY != origStartY)) {
                    hasMoved = true
                }

                postInvalidate()
            }
        } else if(event!!.action == MotionEvent.ACTION_UP) {
            if(!hasMoved && selectedShip != null) {
                selectedShip!!.rotate()
            }

            unclipSelectedShip()
            selectedShip = null
            hasMoved = false
            reloadShipArr()
            postInvalidate()
        }


        return super.onTouchEvent(event)
    }
}