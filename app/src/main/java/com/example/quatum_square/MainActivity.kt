package com.example.quatum_square
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.GridLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    private lateinit var grid: Array<Array<Int>>
    private lateinit var buttons: Array<Array<Button>>
    private var controlledSquares = intArrayOf(0, 0)
    private var currentPlayer = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        grid = Array(5) { Array(5) { 0 } }
        buttons = Array(5) { Array(5) { Button(this) } }

        setupGrid()
    }

    private fun setupGrid() {
        val gameGrid = findViewById<GridLayout>(R.id.gameGrid)
        for (i in 0..4) {
            for (j in 0..4) {
                buttons[i][j] = Button(this)
                buttons[i][j].setBackgroundColor(0xFFCCCCCC.toInt())
                buttons[i][j].setOnClickListener { addParticle(i, j) }
                gameGrid.addView(buttons[i][j], GridLayout.LayoutParams().apply {
                    width = 0
                    height = 0
                    rowSpec = GridLayout.spec(i, 1f)
                    columnSpec = GridLayout.spec(j, 1f)
                })
            }
        }
        updatePlayerTurn()
    }

    private fun addParticle(x: Int, y: Int) {
        if (grid[x][y] < 4) {
            grid[x][y]++
            checkCollapses(x, y)
            checkControlledSquares()
            switchPlayer()
            updatePlayerTurn()
        }
    }

    private fun checkCollapses(x: Int, y: Int) {
        if (grid[x][y] == 4) {
            grid[x][y] = 0
            for (dx in -1..1) {
                for (dy in -1..1) {
                    if (Math.abs(dx) != Math.abs(dy)) {
                        val nx = x + dx
                        val ny = y + dy
                        if (nx in 0..4 && ny in 0..4) {
                            grid[nx][ny]++
                            checkCollapses(nx, ny)
                        }
                    }
                }
            }
        }
    }

    private fun checkControlledSquares() {
        for (i in 0..4) {
            for (j in 0..4) {
                if (grid[i][j] == 4) {
                    controlledSquares[currentPlayer]++
                    grid[i][j] = 0
                    buttons[i][j].setBackgroundColor(0xFFCCCCCC.toInt())
                }
                updateButton(i, j)
            }
        }
    }

    private fun updateButton(x: Int, y: Int) {
        buttons[x][y].text = grid[x][y].toString()
        val color = when (grid[x][y]) {
            0 -> 0xFFCCCCCC.toInt()
            1 -> 0xFF00FF00.toInt()
            2 -> 0xFFFFFF00.toInt()
            3 -> 0xFFFF0000.toInt()
            else -> 0xFF000000.toInt() // Collapsed
        }
        buttons[x][y].setBackgroundColor(color)
    }

    private fun switchPlayer() {
        currentPlayer = 1 - currentPlayer
    }

    private fun updatePlayerTurn() {
        val playerTurn = findViewById<TextView>(R.id.playerTurn)
        playerTurn.text = "Player ${currentPlayer + 1}'s Turn"
    }
}
