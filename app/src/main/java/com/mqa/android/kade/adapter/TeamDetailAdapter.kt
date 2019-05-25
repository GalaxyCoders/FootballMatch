package com.mqa.android.kade.adapter

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.mqa.android.kade.model.Players
import com.mqa.android.kade.modules.activities.PlayerDetailActivity
import com.mqa.android.kade.R
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.player_layout.view.*
import org.jetbrains.anko.sdk27.coroutines.onClick
import org.jetbrains.anko.startActivity

class TeamDetailAdapter(private val players: List<Players>)
    : RecyclerView.Adapter<TeamDetailAdapter.ViewHolder>() {

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindItem(players[position])
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
            ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.player_layout, parent, false))

    override fun getItemCount(): Int = players.size

    class ViewHolder(containerView: View) : RecyclerView.ViewHolder(containerView) {

        fun bindItem(team: Players) {
            Picasso.get().load(team.strCutout).into(itemView.playerImg)
            itemView.name.text = team.strPlayer
            itemView.position.text = team.strPosition
            itemView.onClick {
                val ctx = itemView.context
                ctx.startActivity<PlayerDetailActivity>(
                        "id" to team.idPlayer)
            }
        }
    }

}