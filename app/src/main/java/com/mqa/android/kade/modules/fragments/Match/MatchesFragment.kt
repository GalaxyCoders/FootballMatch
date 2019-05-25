package com.mqa.android.kade.modules.fragments.Match

import android.os.Bundle
import android.support.design.widget.TabLayout
import android.support.v4.app.Fragment
import android.support.v4.view.ViewPager
import android.support.v7.widget.Toolbar
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.mqa.android.kade.adapter.ViewPagerAdapter
import com.mqa.android.kade.modules.activities.SearchActivity
import com.mqa.android.kade.R
import kotlinx.android.synthetic.main.fragment_matches.view.*
import org.jetbrains.anko.find
import org.jetbrains.anko.sdk27.coroutines.onClick
import org.jetbrains.anko.support.v4.intentFor

class MatchesFragment : Fragment() {

    private lateinit var mToolbar: Toolbar
    private lateinit var mTabLayout: TabLayout
    private lateinit var mViewPager: ViewPager

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val v = inflater.inflate(R.layout.fragment_matches, container, false)

        mTabLayout = v.findViewById(R.id.tabs)
        mToolbar = v.findViewById(R.id.toolbar)
        mViewPager = v.find(R.id.container_match)

        setupViewPager(mViewPager)
        mTabLayout.setupWithViewPager(mViewPager)

        v.searchBtn?.onClick {
            val intent = intentFor<SearchActivity>()
            startActivity(intent)
        }

        return v
    }

    companion object {
        fun matchesInstance(): MatchesFragment = MatchesFragment()
    }

        private fun setupViewPager(pager: ViewPager) {
            val adapter = fragmentManager?.let { ViewPagerAdapter(it) }

            val past = LastFragment.lastInstance()
            adapter?.addFragment(past, getString(R.string.title_past_fragment))

            val next = NextFragment.nextInstance()
            adapter?.addFragment(next, getString(R.string.title_next_fragment))

            pager.adapter = adapter
        }
}
