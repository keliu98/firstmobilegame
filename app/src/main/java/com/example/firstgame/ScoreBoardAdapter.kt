package com.example.firstgame

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class ScoreBoardAdapter(private val scores: MutableList<ScoreboardItem>) :
    RecyclerView.Adapter<ScoreBoardAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.scoreboard_item, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int = scores.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val score = scores[position]
        holder.scoreView.text = score.getScore().toString()
        holder.timeView.text = formatElapsedTime(score.getElapsedTime())
    }

    private fun formatElapsedTime(elapsedTime: Long): String {
        val minutes = (elapsedTime / 1000) / 60
        val seconds = (elapsedTime / 1000) % 60
        return String.format("%02d:%02d", minutes, seconds)
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val scoreView: TextView = itemView.findViewById(R.id.scoreboard_score)
        val timeView: TextView = itemView.findViewById(R.id.scoreboard_date)
    }
}
