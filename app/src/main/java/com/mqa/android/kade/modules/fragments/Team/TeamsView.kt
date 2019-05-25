package com.mqa.android.kade.modules.fragments.Team

import com.mqa.android.kade.model.Team

interface TeamsView {
    fun showLoading()
    fun hideLoading()
    fun showTeamList(data: List<Team>)
}