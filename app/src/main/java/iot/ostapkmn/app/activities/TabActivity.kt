package iot.ostapkmn.app.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabLayout
import iot.ostapkmn.app.R
import iot.ostapkmn.app.adapters.TabsAdapter
import iot.ostapkmn.app.fragments.DataListFragment
import iot.ostapkmn.app.fragments.ProfileFragment
import iot.ostapkmn.app.fragments.Tab2Fragment
import kotlinx.android.synthetic.main.activity_tabs.*
import kotlinx.android.synthetic.main.app_bar.*

class TabActivity : AppCompatActivity() {

    private lateinit var viewPager: ViewPager
    private lateinit var tabLayout: TabLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tabs)
        initViews()
        setStatePageAdapter()
        initSupportActionBar()
        tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                setTabLayout(tab)
            }
            override fun onTabUnselected(tab: TabLayout.Tab) = Unit
            override fun onTabReselected(tab: TabLayout.Tab) = Unit
        })
    }

    private fun initViews() {
        viewPager = findViewById(R.id.viewpager_main)
        tabLayout = findViewById(R.id.tabs_main)
    }

    private fun setStatePageAdapter() {
        val fragmentAdapter = TabsAdapter(supportFragmentManager)
        fragmentAdapter.addFragment(DataListFragment(), getString(R.string.list))
        fragmentAdapter.addFragment(Tab2Fragment(), getString(R.string.tab2))
        fragmentAdapter.addFragment(ProfileFragment(), getString(R.string.profile))
        viewpager_main.adapter = fragmentAdapter
        tabs_main.setupWithViewPager(viewpager_main)
    }

    private fun initSupportActionBar() {
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
    }

    private fun setTabLayout(tab: TabLayout.Tab) {
        viewPager.currentItem = tab.position
        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        val count = fragmentManager.backStackEntryCount
        if (count >= 1) {
            supportFragmentManager.popBackStack()
        }
        fragmentTransaction.commit()
    }

}