package com.mqa.android.kade.modules.activities

import android.annotation.SuppressLint
import android.database.sqlite.SQLiteConstraintException
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import com.google.gson.GsonBuilder
import com.mqa.android.kade.database.database
import com.mqa.android.kade.model.DetailEvent
import com.mqa.android.kade.model.EventFeed
import com.mqa.android.kade.R
import com.mqa.android.kade.R.id.*
import com.mqa.android.kade.database.Favorite
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_detail.*
import okhttp3.Call
import okhttp3.OkHttpClient
import okhttp3.Request
import org.jetbrains.anko.db.classParser
import org.jetbrains.anko.db.delete
import org.jetbrains.anko.db.insert
import org.jetbrains.anko.db.select
import org.jetbrains.anko.design.snackbar
import org.jetbrains.anko.startActivity
import org.json.JSONObject
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*
import javax.security.auth.callback.Callback

class DetailActivity : AppCompatActivity() {

    var idHome: String = ""
    var idAway: String = ""
    var idEvent: String = ""
    var lambangHome: String? = ""
    var homeTeam: String = ""
    var awayTeam: String = ""
    var homeScore: String = ""
    var awayScore: String = ""
    var dateEvent: String = ""
    var homeT: String = ""
    var lambangAway: String? = ""

    private var menuItem: Menu? = null
    private var isFavorite: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        homeT = home_team2.text.toString()

        val intent = intent
        idEvent = intent.getStringExtra("id_event")
//        idAway = intent.getStringExtra("id_away")
//        idHome = intent.getStringExtra("id_home")

        val url = "https://www.thesportsdb.com/api/v1/json/1/lookupevent.php?id=$idEvent"
        val request = Request.Builder().url(url).build()
        val client = OkHttpClient()
        client.newCall(request).enqueue(object : Callback, okhttp3.Callback {
            override fun onResponse(call: okhttp3.Call?, response: okhttp3.Response?) {
                val body = response?.body()?.string()
                println(body)

                val gson = GsonBuilder().create()
                val eventFeed = gson.fromJson(body, EventFeed::class.java)

                runOnUiThread {
                    parseEventData(eventFeed.events.single())

                    parseImageData()
                }
            }

            override fun onFailure(call: okhttp3.Call?, e: IOException) {
                println("failed")
            }

        })

        favoriteState()

    }

    private fun parseImageData() {
        val url = "https://www.thesportsdb.com/api/v1/json/1/lookupteam.php?id=$idAway"
        val request = Request.Builder().url(url).build()
        val client = OkHttpClient()
        client.newCall(request).enqueue(object : Callback, okhttp3.Callback {
            override fun onFailure(call: Call, e: IOException) {
                println("failed")
            }

            override fun onResponse(call: okhttp3.Call?, response: okhttp3.Response?) {
                val body = response?.body()?.string()
                println(body)
                val jsonObject = JSONObject(body)
                if (!jsonObject.isNull("teams")){
                val jsonArray = jsonObject.getJSONArray("teams")
                    for (i in 0 until jsonArray.length()) {
                        val jsonObject2 = jsonArray.getJSONObject(i)
                        lambangAway = jsonObject2.getString("strTeamBadge")

                    }
                }else lambangAway = "https://upload.wikimedia.org/wikipedia/commons/thumb/a/ac/No_image_available.svg/600px-No_image_available.svg.png"
                Log.v("lambang_away", "" + lambangAway)
                runOnUiThread {
                    Picasso.get().load(lambangAway).placeholder(R.drawable.ic_search).into(badge_home)
                }
            }
        })

        val url2 = "https://www.thesportsdb.com/api/v1/json/1/lookupteam.php?id=$idHome"
        val request2 = Request.Builder().url(url2).build()
        val client2 = OkHttpClient()
        client2.newCall(request2).enqueue(object : Callback, okhttp3.Callback {
            override fun onFailure(call: Call, e: IOException) {
                println("failed")
            }

            override fun onResponse(call: okhttp3.Call?, response: okhttp3.Response?) {
                val body = response?.body()?.string()
                println(body)
                val jsonObject = JSONObject(body)
                println(jsonObject)
                if (!jsonObject.isNull("teams")){
                val jsonArray = jsonObject.getJSONArray("teams")
                println(jsonArray)
                    for (i in 0 until jsonArray.length()) {
                        val jsonObject2 = jsonArray.getJSONObject(i)
                        lambangHome = jsonObject2.getString("strTeamBadge")
                    }
                } else lambangHome = "https://upload.wikimedia.org/wikipedia/commons/thumb/a/ac/No_image_available.svg/600px-No_image_available.svg.png"
                Log.v("lambang_home", "" + lambangHome)
                runOnUiThread {
                    Picasso.get().load(lambangHome).placeholder(R.drawable.ic_search).into(badge_away)
                }
            }
        })

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
            add_to_favorite -> {
                if (isFavorite) removeFromFavorite() else addToFavorite()

                isFavorite = !isFavorite
//                addToFavorite()
                setFavorite()

                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun setFavorite() {
        if (isFavorite)
            menuItem?.getItem(0)?.icon = ContextCompat.getDrawable(this, R.drawable.ic_added_to_favorites)
        else
            menuItem?.getItem(0)?.icon = ContextCompat.getDrawable(this, R.drawable.ic_add_to_favorites)
    }

    private fun removeFromFavorite() {
        try {
            database.use {
                delete(Favorite.TABLE_FAVORITE, "(EVENT_ID = {id_event})",
                        "id_event" to idEvent)
            }
            swipeRefresh.snackbar("Removed to favorite").show()
        } catch (e: SQLiteConstraintException) {
            swipeRefresh.snackbar(e.localizedMessage).show()
        }
    }

    private fun addToFavorite() {
        try {
            database.use {
                insert(Favorite.TABLE_FAVORITE,
                        Favorite.EVENT_ID to idEvent,
                        Favorite.AWAY_ID to idAway,
                        Favorite.HOME_ID to idHome,
                        Favorite.TEAM_HOME to homeTeam,
                        Favorite.TEAM_AWAY to awayTeam,
                        Favorite.TEAM_HOME_SCORE to homeScore,
                        Favorite.TEAM_AWAY_SCORE to awayScore,
                        Favorite.DATE to dateEvent)
//                println(teams?.idEvent)
            }
            swipeRefresh.snackbar("Added to favorite").show()
        } catch (e: SQLiteConstraintException) {
            swipeRefresh.snackbar(e.localizedMessage).show()
        }
    }

    private fun favoriteState() {
        database.use {
            val result = select(Favorite.TABLE_FAVORITE)
                    .whereArgs("(EVENT_ID = {event_id})",
                            "event_id" to idEvent)
            val favorite = result.parseList(classParser<Favorite>())
            if (!favorite.isEmpty()) isFavorite = true
        }
    }

    @SuppressLint("SimpleDateFormat")
    fun parseEventData(event: DetailEvent) {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd")
        val date = dateFormat.parse(event.dateEvent)
        home_team2.text = event.strHomeTeam
        homeTeam = event.strHomeTeam
        away_team2.text = event.strAwayTeam
        awayTeam = event.strAwayTeam
        tanggal2.text = toSimpleString(date)
        dateEvent = toSimpleString(date)!!
        goal_away.text = event.strAwayGoalDetails
        goal_home.text = event.strHomeGoalDetails
        shot_away.text = event.intAwayShots
        shot_home.text = event.intHomeShots
        keeper_away.text = event.strAwayLineupGoalkeeper
        keeper_home.text = event.strHomeLineupGoalkeeper
        def_away.text = event.strAwayLineupDefense
        def_home.text = event.strHomeLineupDefense
        mid_away.text = event.strAwayLineupMidfiel
        mid_home.text = event.strHomeLineupMidfiel
        for_away.text = event.strAwayLineupForward
        for_home.text = event.strHomeLineupForward
        sub_away.text = event.strAwayLineupSubstitutes
        sub_home.text = event.strHomeLineupSubstitutes
        away_score2.text = event.intAwayScore
        awayScore = event.intAwayScore
        home_score2.text = event.intHomeScore
        homeScore = event.intHomeScore
        idAway = event.idAwayTeam
        idHome = event.idHomeTeam

    }

}

@SuppressLint("SimpleDateFormat")
fun toSimpleString(date: Date?): String? = with(date ?: Date()) {
    SimpleDateFormat("EEE, dd MMM yyy").format(this)
}

