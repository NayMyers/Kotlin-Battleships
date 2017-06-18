package ys.com.battleships

/**
 * Created by Yuval Shabtai on 6/17/2017.
 *
 * Used to receive information whenever a BattleGridView has been clicked.
 */
interface BattleGridViewListener {
    fun gridClicked(x: Int, y: Int)
}