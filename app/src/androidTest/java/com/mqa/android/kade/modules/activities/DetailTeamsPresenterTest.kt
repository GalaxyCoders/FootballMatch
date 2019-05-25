package com.mqa.android.kade.modules.activities

import com.mqa.android.kade.utils.CoroutineContextProvider
import com.google.gson.Gson
import com.mqa.android.kade.api.ApiRepository
import com.mqa.android.kade.api.TheSportDBApi
import com.mqa.android.kade.model.PlayerFeed
import com.mqa.android.kade.model.Players
import com.mqa.android.kade.model.Team
import com.mqa.android.kade.model.TeamResponse
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations

class TeamDetailPresenterTest {
    @Mock
    private
    lateinit var view: DetailTeamsView

    @Mock
    private
    lateinit var gson: Gson

    @Mock
    private
    lateinit var apiRepository: ApiRepository

    private lateinit var presenter: DetailTeamsPresenter

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        presenter = DetailTeamsPresenter(view, apiRepository, gson, CoroutineContextProvider())
    }

    @Test
    fun testGetTeamDetail() {
        val teams: MutableList<Team> = mutableListOf()
        val response = TeamResponse(teams)
        val players: MutableList<Players> = mutableListOf()
        val response2 = PlayerFeed(players)
        val id = "1234"

        GlobalScope.launch {
            Mockito.`when`(gson.fromJson(apiRepository
                    .doRequest(TheSportDBApi.getTeams(id)).await(),
                    TeamResponse::class.java
            )).thenReturn(response)
            Mockito.`when`(gson.fromJson(apiRepository
                    .doRequest(TheSportDBApi.getTeamDetail(id)).await(),
                    PlayerFeed::class.java
            )).thenReturn(response2)

            presenter.getTeamDetail(id)

            Mockito.verify(view).showLoading()
            Mockito.verify(view).showEventList(teams, players)
            Mockito.verify(view).hideLoading()
        }
    }

}