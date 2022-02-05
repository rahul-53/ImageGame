package com.rahul.imagegame

import android.animation.ArgbEvaluator
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.RadioGroup
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.rahul.imagegame.models.BoardSize
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity() : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var imageGame: ImageGame
    private lateinit var adapter: ImageAdapter

    private var boardSize: BoardSize = BoardSize.EASY

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        recyclerView = findViewById(R.id.recyclerView)
        setupBoard()
    }

    private fun setupBoard(){

        when(boardSize){
            BoardSize.EASY -> {
                tvNumMoves.text = "Easy: 4 X 2"
                tvNumPairs.text = "Pairs: 0 / 4"
            }

            BoardSize.MEDIUM -> {
                tvNumMoves.text = "Medium: 6 X 3"
                tvNumPairs.text = "Pairs: 0 /9"
            }
            BoardSize.HARD -> {
                tvNumMoves.text = "Hard: 6 X 4"
                tvNumPairs.text = "Pairs: 0 / 12"
            }
        }

        tvNumPairs.setTextColor(ContextCompat.getColor(this, R.color.color_progress_none))
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

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_item,menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.mIRefresh ->{
                if (imageGame.getNumberOfMoves() > 0 && !imageGame.haveWonTheGame()){
                    showAlertDialogue("Do you want to quit?", null, View.OnClickListener {
                        setupBoard()
                    })
                }
                else{
                    setupBoard()
                }
                return true
            }
            R.id.new_size ->{
                showNewSizeDialogue()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun showNewSizeDialogue() {
        val boardSizeView = LayoutInflater.from(this).inflate(R.layout.dailogue_board_size, null)
        val radioGroupSize:RadioGroup = boardSizeView.findViewById(R.id.radio_btn_group)

        when(boardSize){
            BoardSize.EASY -> radioGroupSize.check(R.id.radioButtonEasy)
            BoardSize.MEDIUM -> radioGroupSize.check(R.id.radioButtonMedium)
            BoardSize.HARD -> radioGroupSize.check(R.id.radioButtonHard)
        }


        showAlertDialogue("Choose new size", boardSizeView, View.OnClickListener {

           boardSize = when(radioGroupSize.checkedRadioButtonId){
               R.id.radioButtonEasy -> BoardSize.EASY
               R.id.radioButtonMedium -> BoardSize.MEDIUM
               else -> BoardSize.HARD
           }
            setupBoard()
        })
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
            val color = ArgbEvaluator().evaluate(
                imageGame.numberOfPairsFound.toFloat()/boardSize.getNumPairs(),
                ContextCompat.getColor(this, R.color.color_progress_none),
                ContextCompat.getColor(this, R.color.color_progress_full)
            ) as Int
            tvNumPairs.setTextColor(color)
            tvNumPairs.text = "Pairs: ${imageGame.numberOfPairsFound}/${boardSize.getNumPairs()}"
            if (imageGame.haveWonTheGame()){
                Snackbar.make(clRoot, "Congratulations, You WON!!",Snackbar.LENGTH_LONG).show()
            }
        }

       tvNumMoves.text = "Moves ${imageGame.getNumberOfMoves()}"

        adapter.notifyDataSetChanged()
    }
    private fun showAlertDialogue(title:String, view:View?, positiveClickListener:View.OnClickListener){
        AlertDialog.Builder(this)
            .setTitle(title)
            .setView(view)
            .setNegativeButton("Cancel",null)
            .setPositiveButton("OK"){_, _ ->
                positiveClickListener.onClick(null)

            }.show()
    }

}