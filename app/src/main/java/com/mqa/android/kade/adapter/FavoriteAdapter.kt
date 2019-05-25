package com.mqa.android.kade.adapter

import android.content.Context
import android.graphics.Color
import android.graphics.Typeface
import android.support.v7.appcompat.R.attr.colorPrimary
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import com.mqa.android.kade.database.Favorite
import com.mqa.android.kade.R
import com.mqa.android.kade.R.id.*
import org.jetbrains.anko.*

class FavoriteAdapter(private val context: Context, private val favorite: List<Favorite>, private val listener: (Favorite) -> Unit)
    : RecyclerView.Adapter<FavoriteAdapter.FavoriteViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoriteViewHolder = FavoriteViewHolder(LayoutInflater.from(context).inflate(R.layout.item_layout, parent, false))


    override fun onBindViewHolder(holder: FavoriteViewHolder, position: Int) {
        holder.bindItem(favorite[position], listener)
    }

    override fun getItemCount(): Int = favorite.size

    class TeamUI : AnkoComponent<ViewGroup> {
        override fun createView(ui: AnkoContext<ViewGroup>): View {
            return with(ui) {
                linearLayout {
                    padding = dip(10)
                    backgroundColor = Color.WHITE
                    orientation = LinearLayout.VERTICAL
                    lparams(width = matchParent)
                    textView {
                        id = tanggal3
                        gravity = Gravity.CENTER_HORIZONTAL
                        text = "tanggal"
                        textColor = colorPrimary
                        textSize = dip(18).toFloat()
                    }.lparams(width = matchParent)
                    linearLayout {
                        textView {
                            id = home_team3
                            gravity = Gravity.CENTER_HORIZONTAL
                            text = "home"
                            textSize = dip(18).toFloat()
                        }.lparams(width = matchParent) {
                            weight = 1f
                        }
                        textView {
                            id = home_score3
                            gravity = Gravity.CENTER_HORIZONTAL
                            text = "score"
                            textSize = dip(20).toFloat()
                            setTypeface(typeface, Typeface.BOLD)
                        }.lparams(width = matchParent) {
                            weight = 1.3f //not support value
                        }
                        textView {
                            gravity = Gravity.CENTER_HORIZONTAL
                            text = "vs"
                            textSize = dip(22).toFloat()
                        }.lparams(width = matchParent) {
                            weight = 1.3f //not support value
                        }
                        textView {
                            id = away_score3
                            gravity = Gravity.CENTER_HORIZONTAL
                            text = "score"
                            textSize = dip(20).toFloat()
                            setTypeface(typeface, Typeface.BOLD)
                        }.lparams(width = matchParent) {
                            weight = 1.3f //not support value
                        }
                        textView {
                            id = away_team3
                            gravity = Gravity.CENTER_HORIZONTAL
                            text = "away"
                            textSize = dip(18).toFloat()
                        }.lparams(width = matchParent) {
                            weight = 1f
                        }
                    }.lparams(width = matchParent)
                }
            }

        }

    }


    class FavoriteViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        private val teamHome: TextView = view.find(home_team)
        private val teamAway: TextView = view.find(away_team)
        private val homeScore: TextView = view.find(home_score)
        private val awayScore: TextView = view.find(away_score)
        private val date: TextView = view.find(tanggal)

        fun bindItem(favorite: Favorite, listener: (Favorite) -> Unit) {
            teamHome.text = favorite.strHomeTeam
            teamAway.text = favorite.strAwayTeam
            homeScore.text = favorite.intHomeScore
            awayScore.text = favorite.intAwayScore
            date.text = favorite.dateEvent
            println(favorite.strHomeTeam)
            Log.v("", "" + favorite.strAwayTeam)
            itemView.setOnClickListener {
                listener(favorite)
            }
        }
    }
}