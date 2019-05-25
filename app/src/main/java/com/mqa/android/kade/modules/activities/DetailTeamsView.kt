package com.mqa.android.kade.modules.activities

import com.mqa.android.kade.model.Players
import com.mqa.android.kade.model.Team

interface DetailTeamsView {
    fun hideLoading()
    fun showLoading()
    fun showEventList(data: List<Team>, player: MutableList<Players>?)
}