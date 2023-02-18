package com.example.firstgame

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class ScoreBoardAdapter(private val scores: List<ScoreboardItem>) :
    RecyclerView.Adapter<ScoreBoardAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.scoreboard_item, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int = scores.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val score = scores[position]
        holder.nameView.text = score.name
        holder.scoreView.text = score.score.toString()
        holder.dateView.text = score.date.toString()
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val nameView: TextView = itemView.findViewById(R.id.scoreboard_name)
        val scoreView: TextView = itemView.findViewById(R.id.scoreboard_score)
        val dateView: TextView = itemView.findViewById(R.id.scoreboard_date)
    }
}
