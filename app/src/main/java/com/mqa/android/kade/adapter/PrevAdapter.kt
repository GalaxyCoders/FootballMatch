package com.mqa.android.kade.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.mqa.android.kade.model.Results
import com.mqa.android.kade.model.TeamFeed
import com.mqa.android.kade.R
import kotlinx.android.synthetic.main.item_layout.view.*

class PrevAdapter(private val context: Context, private val teamFeed: TeamFeed, private val listener: (Results) -> Unit)
    : RecyclerView.Adapter<PrevAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
            ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_layout, parent, false))

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindItem(teamFeed.events[position], listener)
    }

    override fun getItemCount(): Int = teamFeed.events.count()

    class ViewHolder(containerView: View) : RecyclerView.ViewHolder(containerView) {

        fun bindItem(team: Results, listener: (Results) -> Unit) {
            itemView.home_team.text = team.strHomeTeam
            itemView.away_team.text = team.strAwayTeam
            itemView.home_score.text = team.intHomeScore
            itemView.away_score.text = team.intAwayScore
            itemView.tanggal.text = team.dateEvent
            itemView.setOnClickListener {
                listener(team)
            }
        }
    }

}