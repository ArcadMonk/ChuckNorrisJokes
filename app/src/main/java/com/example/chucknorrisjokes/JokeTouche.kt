package com.example.chucknorrisjokes


import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView

class JokeTouch(
    private val onJokeRemoved: (Int) ->Unit,
    private val onItemMoved: (Int,Int) -> Boolean
) : ItemTouchHelper(
    object : ItemTouchHelper.SimpleCallback(
        UP or DOWN,
        LEFT or RIGHT
    ) {
        override fun onMove(
            recyclerView: RecyclerView,
            viewHolder: RecyclerView.ViewHolder,
            target: RecyclerView.ViewHolder
        ): Boolean {
            return onItemMoved(viewHolder.adapterPosition, target.adapterPosition)

        }

        override fun onSwiped(viewHolder: RecyclerView.ViewHolder, swipeDir: Int) {
            onJokeRemoved(viewHolder.adapterPosition)
        }
    }
)