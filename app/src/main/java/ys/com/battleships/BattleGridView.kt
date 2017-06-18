package ys.com.battleships

import android.content.Context
import android.content.res.TypedArray
import android.graphics.Canvas
import android.graphics.Paint
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.View

/**
 * Created by Yuval Shabtai on 6/17/2017.
 *
 * The BattleGridView is a view of a grid in which squares
 * can be set to Hit, Miss, or Clear.
 */

open class BattleGridView : View {
    var gridSize : Int = 0
    var cellWidth : Float = 0F
    var cellHeight : Float = 0F
    var gridPaint : Paint

    var gridStates : Array<Array<GridState>>? = null
    private var listener : BattleGridViewListener? = null

    //Constructor gets grid size and color
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        val tArr = context.theme.obtainStyledAttributes(attrs, R.styleable.BattleGridView, 0, 0)
        var color : Int

        //Get and apply custom attributes
        try {
            gridSize = tArr.getInteger(R.styleable.BattleGridView_size, 0)
            color = tArr.getColor(R.styleable.BattleGridView_color, 0)
        } finally {
            tArr.recycle()
        }

        //Initialize grid cells array
        gridStates = Array(gridSize, {i -> Array(gridSize, {i -> GridState.CLEAR})})

        //Initialize paint to draw grid
        gridPaint = Paint(Paint.ANTI_ALIAS_FLAG)
        gridPaint.color = color
        gridPaint.strokeWidth = 3F
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        //Calculates the width and height of a single cell
        cellWidth = width / (gridSize.toFloat())
        cellHeight = height / (gridSize.toFloat())

        drawLines(canvas)
        drawSquares(canvas)
    }

    open fun drawLines(canvas: Canvas) {
        /*
         * Draws the lines going from top to bottom and left to right.
         * The x value of each line is width/(gridSize - 1) * i
         * The y values of each lines is height/(gridSize - 1) * i
         *
         * The if-else values are just to make sure the lines are
         * drawn fully and don't get "cut" by crossing the view's bounds.
         */
        for(i in 0..gridSize) {
            //Up to down grid lines
            canvas.drawLine((cellWidth) * i
                                + if(i==0) gridPaint.strokeWidth/2 else 0F
                                + if(i==gridSize) -gridPaint.strokeWidth/2 else 0F,
                            0F,
                            (cellWidth) * i
                                + if(i==0) gridPaint.strokeWidth/2 else 0F
                                + if(i==gridSize) -gridPaint.strokeWidth/2 else 0F,
                    height.toFloat(), gridPaint)

            //--------------------------------------------------

            //Left to right grid lines
            canvas.drawLine(0F,
                            (cellHeight) * i
                                + if(i==0) gridPaint.strokeWidth/2 else 0F
                                + if(i==gridSize) -gridPaint.strokeWidth/2 else 0F,
                            width.toFloat(),
                            (cellHeight) * i
                                + if(i==0) gridPaint.strokeWidth/2 else 0F
                                + if(i==gridSize) -gridPaint.strokeWidth/2 else 0F, gridPaint)
        }
    }

    open fun drawSquares(canvas: Canvas) {
        for(y in 0..gridSize - 1) {
            for(x in 0..gridSize - 1) {
                if(gridStates!![x][y] == GridState.MISS) {
                    canvas.drawLine(x * cellWidth + (cellWidth)/5,
                                    y * cellHeight + (cellHeight)/5,
                                    (x+1) * cellWidth - (cellWidth)/5,
                                    (y+1) * cellHeight - (cellHeight)/5,
                                    gridPaint)
                } else if(gridStates!![x][y] == GridState.HIT) {
                    canvas.drawLine(x * cellWidth + (cellWidth)/5,
                                    y * cellHeight + (cellHeight)/5,
                                    (x+1) * cellWidth - (cellWidth)/5,
                                    (y+1) * cellHeight - (cellHeight)/5,
                                    gridPaint)

                    canvas.drawLine((x+1) * cellWidth - (cellWidth)/5,
                                    y * cellHeight + (cellHeight)/5,
                                    x * cellWidth + (cellWidth)/5,
                                    (y+1) * cellHeight - (cellHeight)/5,
                                    gridPaint)
                }
            }
        }
    }

    /**
     * Returns the state of a square on the grid.
     *
     * @param x The x-coordinate of the square.
     * @param y The y-coordinate of the square.
     */
    fun getState(x: Int, y: Int) : GridState {
        return gridStates!![x][y]
    }

    /**
     * Sets a square on the grid to a state.
     *
     * @param x The x-coordinate of the square.
     * @param y The y-coordinate of the square.
     * @param state The state the grid square will be set to.
     */
    fun setState(x: Int, y: Int, state: GridState) {
        gridStates!![x][y] = state
    }

    /**
     * Sets a listener to the BattleGridView
     *
     * @param listener The listener for the BattleGridView
     */
    fun setBattleGridViewListener(listener: BattleGridViewListener) {
        this.listener = listener
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        if(listener != null) {
            listener!!.gridClicked(((event!!.x / width.toFloat()) * (gridSize)).toInt(),
                                    ((event!!.y / height.toFloat()) * (gridSize)).toInt())
        }

        return super.onTouchEvent(event)
    }
}