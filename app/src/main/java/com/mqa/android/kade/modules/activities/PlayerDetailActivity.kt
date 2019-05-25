package com.mqa.android.kade.modules.activities

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.google.gson.GsonBuilder
import com.mqa.android.kade.api.TheSportDBApi
import com.mqa.android.kade.model.DetailPlayer
import com.mqa.android.kade.model.DetailPlayerFeed
import com.mqa.android.kade.R
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_player_detail.*
import okhttp3.OkHttpClient
import okhttp3.Request
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread
import java.io.IOException
import javax.security.auth.callback.Callback

class PlayerDetailActivity : AppCompatActivity() {

    var idPlayer = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_player_detail)

        val intent = intent
        idPlayer = intent.getStringExtra("id")

        fetchJson()
    }

    private fun fetchJson() {
        doAsync {
            val url = TheSportDBApi.getPlayer(idPlayer)
            val request = Request.Builder().url(url).build()
            val client = OkHttpClient()

            uiThread {
                client.newCall(request).enqueue(object : Callback, okhttp3.Callback {
                    override fun onResponse(call: okhttp3.Call?, response: okhttp3.Response?) {
                        val body = response?.body()?.string()
                        println(body)
                        uiThread {
                            val gson = GsonBuilder().create()
                            val playerFeed = gson.fromJson(body, DetailPlayerFeed::class.java)

                            uiThread {
                                parseDetailData(playerFeed.players.single())
                            }
                        }
                    }

                    override fun onFailure(call: okhttp3.Call?, e: IOException) {
                        println("failed")
                    }

                })
            }
        }
    }

    fun parseDetailData(event: DetailPlayer) {
        Picasso.get().load(event.strFanart1).into(fanartIV)
        weightTV.text = event.strWeight
        heightTV.text = event.strHeight
        positionTV.text = event.strPosition
        descTV.text = event.strDescriptionEN
        supportActionBar?.title = event.strPlayer

    }


}
