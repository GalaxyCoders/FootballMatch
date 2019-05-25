package com.mqa.android.kade.adapter

import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.mqa.android.kade.R
import com.mqa.android.kade.database.FavoriteTeam
import com.squareup.picasso.Picasso
import org.jetbrains.anko.*
import org.jetbrains.anko.sdk27.coroutines.onClick


class FavoriteTeamsAdapter(private val favoriteTeam: List<FavoriteTeam>,
                           private val listener: (FavoriteTeam) -> Unit) :
        RecyclerView.Adapter<FavoriteTeamsAdapter.TeamHolder>() {

    override fun onBindViewHolder(p0: TeamHolder, p1: Int) {
        p0.bindItem(favoriteTeam[p1], listener)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TeamHolder {
        return TeamHolder(ItemTeamsUI().createView(AnkoContext.create(parent.context,
                parent)))
    }

    override fun getItemCount(): Int = favoriteTeam.size

    class TeamHolder(view: View) : RecyclerView.ViewHolder(view) {

        private val teamBadge: ImageView = view.find(R.id.team_badge)
        private val teamName: TextView = view.find(R.id.team_name)

        fun bindItem(favorite: FavoriteTeam, listener: (FavoriteTeam) -> Unit) {
            Picasso.get().load(favorite.teamBadge).into(teamBadge)
            teamName.text = favorite.teamName

            itemView.onClick {
                listener(favorite)
            }
        }
    }

}

class ItemTeamsUI : AnkoComponent<ViewGroup> {
    override fun createView(ui: AnkoContext<ViewGroup>) = with(ui) {
        linearLayout {
            lparams(width = matchParent, height = wrapContent)
            padding = dip(16)
            orientation = LinearLayout.HORIZONTAL

            imageView {
                id = R.id.team_badge
            }.lparams {
                height = dip(50)
                width = dip(50)
            }

            textView {
                id = R.id.team_name
                textSize = 16f
            }.lparams {
                margin = dip(15)
            }
        }
    }
}