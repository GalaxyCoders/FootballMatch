package com.mqa.android.kade.modules.fragments.Team

import android.app.Fragment
import android.content.Context
import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.support.design.widget.AppBarLayout
import android.support.v4.app.Fragment
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.mqa.android.kade.api.ApiRepository
import com.mqa.android.kade.model.Team
import com.mqa.android.kade.utils.invisible
import com.mqa.android.kade.utils.visible
import com.google.gson.Gson
import com.mqa.android.kade.adapter.TeamsAdapter
import com.mqa.android.kade.modules.activities.SearchTeamActivity
import com.mqa.android.kade.modules.activities.TeamDetailActivity
import com.mqa.android.kade.R
import com.mqa.android.kade.R.array.league
import com.mqa.android.kade.R.color.colorAccent
import org.jetbrains.anko.*
import org.jetbrains.anko.appcompat.v7.toolbar
import org.jetbrains.anko.design.themedAppBarLayout
import org.jetbrains.anko.recyclerview.v7.recyclerView
import org.jetbrains.anko.sdk27.coroutines.onClick
import org.jetbrains.anko.support.v4.onRefresh
import org.jetbrains.anko.support.v4.startActivity
import org.jetbrains.anko.support.v4.swipeRefreshLayout


@Suppress("DEPRECATION")
class TeamsFragment : Fragment(), AnkoComponent<Context>, TeamsView {

    private var teams: MutableList<Team> = mutableListOf()
    private lateinit var presenter: TeamsPresenter
    private lateinit var adapter: TeamsAdapter
    private lateinit var spinner: Spinner
    private lateinit var listTeam: RecyclerView
    private lateinit var progressBar: ProgressBar
    private lateinit var swipeRefresh: SwipeRefreshLayout
    private lateinit var imageSearch: ImageView
    private lateinit var leagueName: String

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        val spinnerItems = resources.getStringArray(league)
        val spinnerAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_dropdown_item, spinnerItems)
        spinner.adapter = spinnerAdapter

        adapter = TeamsAdapter(teams) {
            context?.startActivity<TeamDetailActivity>("id" to "${it.teamId}")
        }
        listTeam.adapter = adapter

        val request = ApiRepository()
        val gson = Gson()
        presenter = TeamsPresenter(this, request, gson)
        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                leagueName = spinner.selectedItem.toString()
                print(leagueName)
                presenter.getTeamList(leagueName)
            }

            override fun onNothingSelected(parent: AdapterView<*>) {}
        }

        swipeRefresh.onRefresh {
            presenter.getTeamList(leagueName)
        }

        imageSearch.onClick {
            startActivity<SearchTeamActivity>()
        }
    }

    companion object {
        fun teamsInstance(): TeamsFragment = TeamsFragment()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return createView(AnkoContext.create(requireContext()))
    }

    override fun createView(ui: AnkoContext<Context>): View = with(ui) {
        linearLayout {
            themedAppBarLayout(R.style.AppTheme_AppBarOverlay) {
                id = R.id.appbar
                topPadding = dip(8)
                toolbar {
                    id = R.id.toolbar
                    backgroundColor = resources.getColor(R.color.colorPrimary)
                    popupTheme = R.style.AppTheme_PopupOverlay
                    linearLayout {
                        orientation = LinearLayout.HORIZONTAL
                        textView {
                            text = "Teams"
                            typeface = Typeface.DEFAULT_BOLD
                            textSize = sp(6).toFloat()
                            textColor = Color.WHITE
                        }.lparams {
                            width = matchParent
                            weight = 1f
                            horizontalGravity = Gravity.START
                        }
                        imageSearch = imageView {
                            setImageResource(R.drawable.ic_search)
                        }.lparams {
                            width = matchParent
                            weight = 3f
                            horizontalGravity = Gravity.END
                        }
                    }.lparams(width = matchParent, height = wrapContent)
                }.lparams(width = matchParent, height = dimenAttr(R.attr.actionBarSize)) {
                    weight = 1f
                    scrollFlags = AppBarLayout.LayoutParams.SCROLL_FLAG_SCROLL or AppBarLayout.LayoutParams.SCROLL_FLAG_ENTER_ALWAYS
                }
            }.lparams(width = matchParent)

            lparams(width = matchParent, height = wrapContent)
            orientation = LinearLayout.VERTICAL

            spinner = spinner {
                id = R.id.spinner
            }
            swipeRefresh = swipeRefreshLayout {
                setColorSchemeResources(colorAccent,
                        android.R.color.holo_green_light,
                        android.R.color.holo_orange_light,
                        android.R.color.holo_red_light)

                relativeLayout {
                    lparams(width = matchParent, height = wrapContent)

                    listTeam = recyclerView {
                        id = R.id.list_team
                        lparams(width = matchParent, height = wrapContent)
                        layoutManager = LinearLayoutManager(ctx)
                    }

                    progressBar = progressBar {
                    }.lparams {
                        centerHorizontally()
                    }
                }
            }
        }
    }

    override fun showLoading() {
        progressBar.visible()
    }

    override fun hideLoading() {
        progressBar.invisible()
    }

    override fun showTeamList(data: List<Team>) {
        swipeRefresh.isRefreshing = false
        teams.clear()
        teams.addAll(data)
        adapter.notifyDataSetChanged()
    }

}
