package com.mqa.android.kade.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.mqa.android.kade.model.Results
import com.mqa.android.kade.model.SearchFeed
import com.mqa.android.kade.R
import kotlinx.android.synthetic.main.item_layout.view.*

var jumlah: Boolean = false

class SearchAdapter(private val context: Context, private val teamFeed: SearchFeed, private val listener: (Results) -> Unit)
    : RecyclerView.Adapter<SearchAdapter.ViewHolder>() {
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindItem(teamFeed.event[position], listener)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
            ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_layout, parent, false))

    override fun getItemCount(): Int = teamFeed.event.size

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