package com.mqa.android.kade.modules.activities

import android.database.sqlite.SQLiteConstraintException
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v7.widget.LinearLayoutManager
import android.view.Menu
import android.view.MenuItem
import android.view.WindowManager
import com.mqa.android.kade.utils.gone
import com.mqa.android.kade.utils.visible
import com.google.gson.Gson
import com.mqa.android.kade.adapter.TeamDetailAdapter
import com.mqa.android.kade.api.ApiRepository
import com.mqa.android.kade.model.*
import com.mqa.android.kade.R
import com.mqa.android.kade.database.FavoriteTeam
import com.mqa.android.kade.database.db
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_detail.*
import kotlinx.android.synthetic.main.activity_team_detail.*
import org.jetbrains.anko.db.classParser
import org.jetbrains.anko.db.delete
import org.jetbrains.anko.db.insert
import org.jetbrains.anko.db.select
import org.jetbrains.anko.design.snackbar
import org.jetbrains.anko.startActivity

class TeamDetailActivity : AppCompatActivity(), DetailTeamsView {

    var idTeam: String = ""
    var teamBadge: String? = ""
    var teamName: String? = ""

    lateinit var mTeams: Team
    private var mPlayer: MutableList<Players> = mutableListOf()
    private var menuItem: Menu? = null
    private var isFavorite: Boolean = false
    private lateinit var presenter: DetailTeamsPresenter
    private lateinit var adapter: TeamDetailAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_team_detail)
        recyclerView_team.layoutManager = LinearLayoutManager(this)

        val intent = intent
        idTeam = intent.getStringExtra("id")
        getTeamDetails()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.detail_menu, menu)
        menuItem = menu
        setFavorite()
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                finish()
                true
            }
            R.id.add_to_favorite -> {
                if (isFavorite) removeFromFavorite() else addToFavorite()

                isFavorite = !isFavorite
                setFavorite()

                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun getTeamDetails() {
        favoriteState()
        recyclerView_team.layoutManager = LinearLayoutManager(this)
        adapter = TeamDetailAdapter(mPlayer)
        recyclerView_team.adapter = adapter

        presenter = DetailTeamsPresenter(this, ApiRepository(), Gson())
        presenter.getTeamDetail(idTeam)
    }

    override fun showEventList(data: List<Team>, player: MutableList<Players>?) {
        mPlayer.clear()
        mTeams = data[0]
        bindView()
        if (player != null) {
            mPlayer.addAll(player)

            if (player.isEmpty()) {
                mPlayer.removeAll(player)
            }
        }
        adapter.notifyDataSetChanged()
    }

    override fun hideLoading() {
        pBarTeam.gone()
        window.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
    }

    override fun showLoading() {
        pBarTeam.visible()
        window.setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
    }

    private fun bindView() {

        supportActionBar?.title = mTeams.teamName
        Picasso.get().load(mTeams.teamBadge).into(teamImgIV)

        teamNameTV.text = mTeams.teamName
        desTV.text = mTeams.teamDescription
    }

    private fun setFavorite() {
        if (isFavorite)
            menuItem?.getItem(0)?.icon = ContextCompat.getDrawable(this, R.drawable.ic_added_to_favorites)
        else
            menuItem?.getItem(0)?.icon = ContextCompat.getDrawable(this, R.drawable.ic_add_to_favorites)
    }

    private fun removeFromFavorite() {
        try {
            db.use {
                delete(FavoriteTeam.TABLE_TEAM, "(TEAM_ID = {id})",
                        "id" to idTeam)
            }
            swipeRefresh?.snackbar("Removed to favorite")?.show()
        } catch (e: SQLiteConstraintException) {
            swipeRefresh?.snackbar(e.localizedMessage)?.show()
        }
    }

    private fun addToFavorite() {
        try {
            db.use {
                insert(FavoriteTeam.TABLE_TEAM,
                        FavoriteTeam.TEAM_ID to mTeams.teamId,
                        FavoriteTeam.TEAM_NAME to mTeams.teamName,
                        FavoriteTeam.TEAM_BADGE to mTeams.teamBadge)
                println(idTeam)
                println(teamName)
                println(teamBadge)
            }
            swipeRefresh?.snackbar("Added to favorite")?.show()
        } catch (e: SQLiteConstraintException) {
            swipeRefresh?.snackbar(e.localizedMessage)?.show()
        }
    }

    private fun favoriteState() {
        db.use {
            val result = select(FavoriteTeam.TABLE_TEAM)
                    .whereArgs("(TEAM_ID = {id})",
                            "id" to idTeam)
            val favorite = result.parseList(classParser<FavoriteTeam>())
            if (!favorite.isEmpty()) isFavorite = true
        }
    }

}
