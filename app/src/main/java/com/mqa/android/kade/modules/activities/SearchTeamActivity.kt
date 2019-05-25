package com.mqa.android.kade.modules.activities

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import com.mqa.android.kade.modules.fragments.Team.TeamsPresenter
import com.mqa.android.kade.modules.fragments.Team.TeamsView
import com.mqa.android.kade.utils.invisible
import com.mqa.android.kade.utils.visible
import com.google.gson.Gson
import com.mqa.android.kade.adapter.SearchTeamAdapter
import com.mqa.android.kade.api.ApiRepository
import com.mqa.android.kade.api.TheSportDBApi.getSearchTeam
import com.mqa.android.kade.model.Team
import com.mqa.android.kade.R
import kotlinx.android.synthetic.main.activity_search_team.*
import org.jetbrains.anko.sdk27.coroutines.onClick
import org.jetbrains.anko.startActivity

class SearchTeamActivity : AppCompatActivity(), TeamsView {

    private var teams: MutableList<Team> = mutableListOf()
    private lateinit var presenter: TeamsPresenter
    private lateinit var adapter: SearchTeamAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search_team)
        recyclerView_search2.layoutManager = LinearLayoutManager(this)
        adapter = SearchTeamAdapter(teams) {
            this.startActivity<TeamDetailActivity>("id" to "${it.teamId}")
        }
        recyclerView_search2.adapter = adapter

        val request = ApiRepository()
        val gson = Gson()
        presenter = TeamsPresenter(this, request, gson)
        searchBtn3.onClick {
            val search = searchET2.text.toString()
            println(search)
            presenter.getSearchT(search)
            println(getSearchTeam(search))
        }

    }

    override fun showLoading() {
        progS2.visible()
    }

    override fun hideLoading() {
        progS2.invisible()
    }

    override fun showTeamList(data: List<Team>) {
        teams.clear()
        teams.addAll(data)
        adapter.notifyDataSetChanged()
    }
}
