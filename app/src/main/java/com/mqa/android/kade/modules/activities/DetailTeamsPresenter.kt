package com.mqa.android.kade.modules.activities

import com.mqa.android.kade.utils.CoroutineContextProvider
import com.google.gson.Gson
import com.mqa.android.kade.api.ApiRepository
import com.mqa.android.kade.api.TheSportDBApi
import com.mqa.android.kade.model.PlayerFeed
import com.mqa.android.kade.model.TeamResponse
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class DetailTeamsPresenter(private val view: DetailTeamsView,
                           private val apiRepository: ApiRepository,
                           private val gson: Gson,
                           private val context: CoroutineContextProvider = CoroutineContextProvider()) {

    fun getTeamDetail(idTeam: String?) {
        view.showLoading()

        GlobalScope.launch (context.main){
            val data = gson.fromJson(apiRepository
                    .doRequest(TheSportDBApi.getTeamDetail(idTeam)).await(),
                    TeamResponse::class.java)

            val dataPlayer = gson.fromJson(apiRepository
                    .doRequest(TheSportDBApi.getTeamNames(idTeam)).await(),
                    PlayerFeed::class.java)

            view.showEventList(data.teams, dataPlayer?.player)
            view.hideLoading()
        }
    }
}