package com.mqa.android.kade.modules.fragments.Favorite

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import com.mqa.android.kade.database.database
import com.mqa.android.kade.adapter.FavoriteTeamsAdapter
import com.mqa.android.kade.modules.activities.TeamDetailActivity
import com.mqa.android.kade.R
import com.mqa.android.kade.database.FavoriteTeam
import org.jetbrains.anko.*
import org.jetbrains.anko.db.classParser
import org.jetbrains.anko.db.select
import org.jetbrains.anko.recyclerview.v7.recyclerView
import org.jetbrains.anko.support.v4.find
import org.jetbrains.anko.support.v4.onRefresh
import org.jetbrains.anko.support.v4.swipeRefreshLayout

class TeamsFav : Fragment() {

    private var favoTeams: MutableList<FavoriteTeam> = mutableListOf()
    private lateinit var swipeRefreshLayout: SwipeRefreshLayout
    private lateinit var recyclerView: RecyclerView
    private lateinit var progressBar: ProgressBar
    private lateinit var adapter: FavoriteTeamsAdapter

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        initId()

        adapter = FavoriteTeamsAdapter(favoTeams) {
            requireContext().startActivity<TeamDetailActivity>("id" to
                    "${it.teamId}")
        }

        recyclerView.adapter = adapter
        showFavorite()
        swipeRefreshLayout.onRefresh {
            favoTeams.clear()
            showFavorite()
        }
    }

    override fun onResume() {
        super.onResume()
        swipeRefreshLayout.setRefreshing(true)
        favoTeams.clear()
        showFavorite()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return FavoriteTeamsUI().createView(AnkoContext.create(requireContext(), this))
    }

    private fun initId() {
        swipeRefreshLayout = find(R.id.swipeRefreshFavoTeams)
        recyclerView = find(R.id.rvFavoTeams)
        progressBar = find(R.id.pbFavoTeams)
    }

    private fun showFavorite() {
        context?.database?.use {
            swipeRefreshLayout.isRefreshing = false
            progressBar.visibility = View.GONE
            val result = select(FavoriteTeam.TABLE_TEAM)
            val favorite = result.parseList(classParser<FavoriteTeam>())
            favoTeams.addAll(favorite)
            adapter.notifyDataSetChanged()
        }
    }


    class FavoriteTeamsUI : AnkoComponent<TeamsFav> {

        override fun createView(ui: AnkoContext<TeamsFav>) = with(ui) {
            relativeLayout {
                lparams(width = matchParent, height = matchParent)

                swipeRefreshLayout {
                    id = R.id.swipeRefreshFavoTeams
                    setColorSchemeResources(R.color.colorAccent,
                            android.R.color.holo_green_light,
                            android.R.color.holo_orange_light,
                            android.R.color.holo_red_light)

                    recyclerView {
                        lparams(width = matchParent, height = wrapContent)
                        id = R.id.rvFavoTeams
                        layoutManager = LinearLayoutManager(ctx)
//                    addItemDecoration(SpaceItemDecoration(8))
                    }
                }

                progressBar {
                    id = R.id.pbFavoTeams
                }.lparams {
                    centerHorizontally()
                }
            }
        }
    }

    companion object {
        fun teamInstance(): TeamsFav = TeamsFav()
    }

}
