package com.rahul.imagegame

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.rahul.imagegame.models.BoardSize
import com.rahul.imagegame.models.MemoryCard
import com.rahul.imagegame.utils.Constants
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity() : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var imageGame: ImageGame
    private lateinit var adapter: ImageAdapter
    private lateinit var tvNumMoves:TextView
    private lateinit var tvNumPairs: TextView

    private var boardSize: BoardSize = BoardSize.EASY

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        recyclerView = findViewById(R.id.recyclerView)

        imageGame = ImageGame(boardSize)
        adapter = ImageAdapter(
            this,boardSize, imageGame.cards,
            object :ImageAdapter.CardClickListener{
                override fun onCardClick(position: Int) {
                   updateGameWithFlip(position)
                }
            })

        recyclerView.adapter = adapter
        recyclerView.setHasFixedSize( true)
        recyclerView.layoutManager = GridLayoutManager(this,boardSize.getWidth())

    }

    private fun updateGameWithFlip(position: Int) {
        //won the game
        if(imageGame.haveWonTheGame()){
            Snackbar.make(clRoot, "won the game",Snackbar.LENGTH_SHORT).show()
            return
        }
        //invalid move alert
        if (imageGame.isCardFacedUp(position)){
            Snackbar.make(clRoot, "invalid move",Snackbar.LENGTH_SHORT).show()
            return
        }
        if(imageGame.flipCard(position)){
            Log.d("tag","Found a match:${imageGame.numberOfPairsFound}")
        }

        adapter.notifyDataSetChanged()
    }
}