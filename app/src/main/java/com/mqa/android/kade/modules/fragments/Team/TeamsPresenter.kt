package com.mqa.android.kade.modules.fragments.Team

import com.mqa.android.kade.api.ApiRepository
import com.mqa.android.kade.api.TheSportDBApi
import com.mqa.android.kade.model.TeamResponse
import com.mqa.android.kade.utils.CoroutineContextProvider
import com.google.gson.Gson
import com.mqa.android.kade.model.SearchTeamFeed
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class TeamsPresenter(private val view: TeamsView,
                     private val apiRepository: ApiRepository,
                     private val gson: Gson, private val context: CoroutineContextProvider = CoroutineContextProvider()) {

    fun getTeamList(league: String?) {
        view.showLoading()

        GlobalScope.launch(context.main) {
            val data = gson.fromJson(apiRepository
                    .doRequest(TheSportDBApi.getTeams(league)).await(),
                    TeamResponse::class.java)

            view.showTeamList(data.teams)
            view.hideLoading()
        }
    }

    fun getSearchT(league: String?) {
        view.showLoading()

        GlobalScope.launch(context.main) {
            val data = gson.fromJson(apiRepository
                    .doRequest(TheSportDBApi.getSearchTeam(league)).await(),
                    SearchTeamFeed::class.java)

            view.showTeamList(data.teams)
            view.hideLoading()
        }
    }
}