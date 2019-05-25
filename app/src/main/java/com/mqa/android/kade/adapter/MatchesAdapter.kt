package com.mqa.android.kade.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.mqa.android.kade.model.MatchesFeed
import com.mqa.android.kade.model.Results
import com.mqa.android.kade.R
import kotlinx.android.synthetic.main.item_layout.view.*

class MatchesAdapter(private val context: Context, private val teamFeed: MatchesFeed, private val listener: (Results) -> Unit)
    : RecyclerView.Adapter<MatchesAdapter.ViewHolder>() {

    override fun onBindViewHolder(p0: ViewHolder, p1: Int) {
        p0.bindItem(teamFeed.event[p1], listener)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
            ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_layout, parent, false))

    override fun getItemCount(): Int = teamFeed.event.count()

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