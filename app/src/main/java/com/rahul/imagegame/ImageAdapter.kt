package com.rahul.imagegame

import android.content.Context
import android.content.res.ColorStateList
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.recyclerview.widget.RecyclerView
import com.rahul.imagegame.utils.Constants.Companion.MARGIN_SIZE
import com.rahul.imagegame.models.BoardSize
import com.rahul.imagegame.models.MemoryCard
import com.rahul.imagegame.utils.Constants.Companion.DEFAULT_ICONS
import kotlin.math.*

class ImageAdapter(
    private val context: Context,
    private val boardSize: BoardSize,
    private val cardImages: List<MemoryCard>,
    private val cardClickListener: CardClickListener
)
    :RecyclerView.Adapter<ImageAdapter.ImageViewHolder>() {

    interface CardClickListener{
        fun onCardClick(position: Int)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder {

        val cardWidth:Int = parent.width /boardSize.getWidth()-(2*MARGIN_SIZE)
        val cardHeight:Int = parent.height/boardSize.getHeight() -(2*MARGIN_SIZE)
        val cardSideLength:Int = min(cardWidth,cardHeight)

        val view:View = LayoutInflater.from(context)
            .inflate(R.layout.item_layout,parent, false)

        val layoutParams = view.findViewById<CardView>(R.id.cardView)
            .layoutParams as ViewGroup.MarginLayoutParams
        layoutParams.width = cardSideLength
        layoutParams.height = cardSideLength

        layoutParams.setMargins(MARGIN_SIZE, MARGIN_SIZE, MARGIN_SIZE, MARGIN_SIZE)
        return ImageViewHolder(view)
    }

    override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {
        holder.bind(position)
    }

    override fun getItemCount(): Int {
        return boardSize.numCards
    }
    
    inner class ImageViewHolder(itemView: View):RecyclerView.ViewHolder(itemView){
        val imageBtn = itemView.findViewById<ImageButton>(R.id.imageButton)

         fun bind(position: Int){
             val memoryCard: MemoryCard = cardImages[position]
             imageBtn.setImageResource(
                 if (memoryCard.isFaceUp) memoryCard.identifier
                 else R.drawable.ic_launcher_foreground)
             imageBtn.alpha = if(memoryCard.isMatched) .4f else 1.0f
             val colorStateList: ColorStateList? =
                 if (memoryCard.isMatched) ContextCompat.getColorStateList(context, R.color.color_grey)
             else
                 null

             ViewCompat.setBackgroundTintList(imageBtn, colorStateList)

            imageBtn.setOnClickListener {
                Log.d("tag","clicked on position $position")
                cardClickListener.onCardClick(position)
            }
        }
    }
}











