package com.mqa.android.kade.modules.fragments.Match

import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import com.google.gson.GsonBuilder
import com.mqa.android.kade.adapter.NextAdapter
import com.mqa.android.kade.api.TheSportDBApi
import com.mqa.android.kade.model.TeamFeed
import com.mqa.android.kade.modules.activities.DetailActivity
import com.mqa.android.kade.R
import kotlinx.android.synthetic.main.fragment_last.*
import kotlinx.android.synthetic.main.fragment_next.*
import kotlinx.android.synthetic.main.fragment_next.view.*
import okhttp3.OkHttpClient
import okhttp3.Request
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.support.v4.runOnUiThread
import org.jetbrains.anko.uiThread
import java.io.IOException
import javax.security.auth.callback.Callback

class NextFragment : Fragment() {

    private lateinit var leagueName: String

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        val spinnerItems = resources.getStringArray(R.array.league_event)
        val spinnerAdapter = ArrayAdapter(context,
                android.R.layout.simple_spinner_dropdown_item,
                spinnerItems)
        nextSpinner?.adapter = spinnerAdapter
        data()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val rootView = inflater.inflate(R.layout.fragment_next, container, false)
        rootView.recyclerView_next.layoutManager = LinearLayoutManager(activity)

        return rootView
    }

    fun data() {
        nextSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {

            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                leagueName = nextSpinner.selectedItem.toString()
                when (leagueName) {
                    "English Premier League" -> leagueName = "4328"
                    "English League Championship" -> leagueName = "4329"
                    "German Bundesliga" -> leagueName = "4331"
                    "Italian Serie A" -> leagueName = "4332"
                    "French Ligue 1" -> leagueName = "4334"
                    "Spanish La Liga" -> leagueName = "4335"
                }

                pBar?.visibility = View.VISIBLE
                doAsync {
                    val url = TheSportDBApi.getMatchesNext(leagueName)
                    val request = Request.Builder().url(url).build()
                    val client = OkHttpClient()
                    uiThread {
                        client.newCall(request).enqueue(object : Callback, okhttp3.Callback {
                            override fun onResponse(call: okhttp3.Call?, response: okhttp3.Response?) {
                                val body = response?.body()?.string()
                                println(body)

                                runOnUiThread {
                                    val gson = GsonBuilder().create()
                                    val teamFeed = gson.fromJson(body, TeamFeed::class.java)
                                    recyclerView_next?.adapter = NextAdapter(requireContext(), teamFeed) {
                                        val intent = Intent(requireContext(), DetailActivity::class.java)
                                        intent.putExtra("id_event", it.idEvent)
                                        intent.putExtra("id_home", it.idHomeTeam)
                                        intent.putExtra("id_away", it.idAwayTeam)
                                        startActivity(intent)
                                    }
                                    pBar?.visibility = View.GONE
                                }
                            }

                            override fun onFailure(call: okhttp3.Call?, e: IOException) {
                                println("failed")
                            }
                        })


                    }
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }

    }

    companion object {
        fun nextInstance(): NextFragment = NextFragment()
    }
}