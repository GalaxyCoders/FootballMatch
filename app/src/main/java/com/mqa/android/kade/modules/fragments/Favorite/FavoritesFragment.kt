package com.mqa.android.kade.modules.fragments.Favorite

import android.os.Bundle
import android.support.design.widget.TabLayout
import android.support.v4.app.Fragment
import android.support.v4.view.ViewPager
import android.support.v7.widget.Toolbar
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.mqa.android.kade.adapter.ViewPagerAdapter
import com.mqa.android.kade.R
import org.jetbrains.anko.find

class FavoritesFragment : Fragment() {

    private lateinit var mToolbar: Toolbar
    private lateinit var mTabLayout: TabLayout
    private lateinit var mViewPager: ViewPager

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val v = inflater.inflate(R.layout.fragment_favories, container, false)

        mTabLayout = v.findViewById(R.id.tabs2)
        mToolbar = v.findViewById(R.id.toolbar2)
        mViewPager = v.find(R.id.container_fav)

        setupViewPager(mViewPager)
        mTabLayout.setupWithViewPager(mViewPager)

        return v
    }


    companion object {
        fun favoritesInstance(): FavoritesFragment = FavoritesFragment()
    }

    private fun setupViewPager(pager: ViewPager) {
        val adapter = fragmentManager?.let { ViewPagerAdapter(it) }

        val past = MatchesFav.matchInstance()
        adapter?.addFragment(past, getString(R.string.title_match_fragment))

        val next = TeamsFav.teamInstance()
        adapter?.addFragment(next, getString(R.string.title_team_fragment))

        pager.adapter = adapter
    }
}