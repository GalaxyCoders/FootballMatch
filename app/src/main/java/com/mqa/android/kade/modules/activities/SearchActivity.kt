package com.mqa.android.kade.modules.activities

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import com.google.gson.GsonBuilder
import com.mqa.android.kade.adapter.SearchAdapter
import com.mqa.android.kade.api.TheSportDBApi
import com.mqa.android.kade.model.SearchFeed
import com.mqa.android.kade.R
import kotlinx.android.synthetic.main.activity_search.*
import okhttp3.OkHttpClient
import okhttp3.Request
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.sdk27.coroutines.onClick
import org.jetbrains.anko.toast
import org.jetbrains.anko.uiThread
import java.io.IOException
import javax.security.auth.callback.Callback

class SearchActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)
        recyclerView_search.layoutManager = LinearLayoutManager(this)

        searchBtn2.onClick {
            fetchJson()
        }

    }

    private fun fetchJson() {
        progS?.visibility = View.VISIBLE
        doAsync {
            val url = TheSportDBApi.getSearch(searchET.text.toString())
            val request = Request.Builder().url(url).build()
            val client = OkHttpClient()

            uiThread {
                client.newCall(request).enqueue(object : Callback, okhttp3.Callback {
                    override fun onResponse(call: okhttp3.Call?, response: okhttp3.Response?) {
                        val body = response?.body()?.string()
                        println(url)
                        println(body)
                        uiThread {
                            val gson = GsonBuilder().create()
                            val teamFeed = gson.fromJson(body, SearchFeed::class.java)
                            if (teamFeed.event.isEmpty()) {
                                toast("Data tidak ditemukan")
                            } else
                                recyclerView_search?.adapter = SearchAdapter(this@SearchActivity, teamFeed) {
                                    val intent = Intent(this@SearchActivity, DetailActivity::class.java)
                                    intent.putExtra("id_event", it.idEvent)
                                    startActivity(intent)
                                }

                            progS?.visibility = View.GONE
                        }
                    }

                    override fun onFailure(call: okhttp3.Call?, e: IOException) {
                        println("failed")
                    }

                })
            }
        }
    }
}
