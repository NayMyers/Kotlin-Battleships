package ys.com.battleships

import android.content.Context
import android.content.res.TypedArray
import android.graphics.Canvas
import android.graphics.Paint
import android.util.AttributeSet
import android.util.Log
import android.view.View

/**
 * Created by Yuval Shabtai on 6/17/2017.
 */


class BattleGridView : View {
    var gridSize : Int = 0
    var gridPaint : Paint

    var gridStates : Array<Array<GridState>>? = null

    //Constructor gets grid size and color
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        val tArr = context.theme.obtainStyledAttributes(attrs, R.styleable.BattleGridView, 0, 0)
        var color = 0

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
        /*
         * Draws the lines going from top to bottom.
         * The x value of each line is width/(gridSize - 1)
         *
         * The if-else values are just to make sure the lines at the left
         * and right bounds of the grid get drawn fully and don't cross the
         * view's bounds.
         *
         * The same applies for the second for loop,
         * except that one draws the lines going left to right.
         */
        for(i in 0..gridSize-1) {
            canvas.drawLine((width / (gridSize.toFloat() - 1)) * i
                                    + if(i==0) gridPaint.strokeWidth/2 else 0F
                                    + if(i==gridSize-1) -gridPaint.strokeWidth/2 else 0F,
                            0F,
                            (width / (gridSize.toFloat() - 1)) * i
                                    + if(i==0) gridPaint.strokeWidth/2 else 0F
                                    + if(i==gridSize-1) -gridPaint.strokeWidth/2 else 0F,
                            height.toFloat(), gridPaint)

            Log.i("Just checking", i.toString())
        }

        for(i in 0..gridSize-1) {
            canvas.drawLine(0F,
                    (height / (gridSize.toFloat() - 1)) * i
                            + if(i==0) gridPaint.strokeWidth/2 else 0F
                            + if(i==gridSize-1) -gridPaint.strokeWidth/2 else 0F,
                    width.toFloat(),
                    (height / (gridSize.toFloat() - 1)) * i
                            + if(i==0) gridPaint.strokeWidth/2 else 0F
                            + if(i==gridSize-1) -gridPaint.strokeWidth/2 else 0F, gridPaint)
        }
    }

}