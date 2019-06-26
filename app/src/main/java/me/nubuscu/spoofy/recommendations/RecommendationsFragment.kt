package me.nubuscu.spoofy.recommendations

import android.os.Bundle
import android.support.design.widget.TabLayout
import android.support.v4.app.Fragment
import android.support.v4.view.ViewPager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import me.nubuscu.spoofy.R
import me.nubuscu.spoofy.utils.DataManager
import me.nubuscu.spoofy.utils.TitledFragmentPagerAdapter

class RecommendationsFragment : Fragment() {
    private val spotify = DataManager.instance.spotify

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_recommendations, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val viewPager: ViewPager = view.findViewById(R.id.rec_types_view_pager)
        val tabLayout: TabLayout = view.findViewById(R.id.rec_types_tab_layout)
        viewPager.adapter = TitledFragmentPagerAdapter(
            requireActivity().supportFragmentManager,
            listOf(ArtistRecFragment(), GenreRecFragment(), PlaylistRecFragment())
        )
        tabLayout.setupWithViewPager(viewPager)
    }
}