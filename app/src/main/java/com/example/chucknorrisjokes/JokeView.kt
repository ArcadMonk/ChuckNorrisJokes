package com.example.chucknorrisjokes

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout

class JokeView (context: Context, attrs: AttributeSet?, defStyleAttr: Int) :
    ConstraintLayout(context, attrs, defStyleAttr) {



    data class Model(val text :String , val fav : Boolean)
    lateinit var textView : TextView
    lateinit var shareButton : ImageButton
    lateinit var favButton : ImageButton
    var layout : Int = R.layout.joke_layout
    var favState : Boolean = false
    init {
        View.inflate(context, layout, this)
        textView = findViewById(R.id.text_view_id)
        shareButton  = findViewById(R.id.shareButton_id)
        favButton = findViewById(R.id.favButton_id)
    }



    fun setupView(model : Model){


        textView.text = model.text
        favState = model.fav
        favButton.setImageResource(R.drawable.share_icon)
        if(model.fav)
            favButton.setImageResource(R.drawable.icon_fav_full)
        else
            favButton.setImageResource(R.drawable.icon_fav_empty)

    }

}