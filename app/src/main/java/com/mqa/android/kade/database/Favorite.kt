package com.mqa.android.kade.database

data class Favorite(val id: Long?,
                    val idEvent: String?,
                    val idAwayTeam: String?,
                    val idHomeTeam: String?,
                    val strHomeTeam: String?,
                    val strAwayTeam: String?,
                    val intAwayScore: String?,
                    val intHomeScore: String?,
                    val dateEvent: String?) {

    companion object {
        const val TABLE_FAVORITE: String = "TABLE_FAVORITE"
        const val ID: String = "ID_"
        const val EVENT_ID: String = "EVENT_ID"
        const val AWAY_ID: String = "AWAY_ID"
        const val HOME_ID: String = "HOME_ID"
        const val TEAM_AWAY: String = "TEAM_AWAY"
        const val TEAM_HOME: String = "TEAM_HOME"
        const val TEAM_HOME_SCORE: String = "TEAM_HOME_SCORE"
        const val TEAM_AWAY_SCORE: String = "TEAM_AWAY_SCORE"
        const val DATE: String = "TEAM_BADGE"
    }
}