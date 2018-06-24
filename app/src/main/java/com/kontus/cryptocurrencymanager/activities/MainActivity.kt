package com.kontus.cryptocurrencymanager.activities

import android.net.Uri
import android.os.Bundle
import android.support.design.widget.NavigationView
import android.support.v4.app.Fragment
import android.support.v4.view.GravityCompat
import android.support.v4.widget.DrawerLayout
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.view.Menu
import android.view.MenuItem
import com.kontus.cryptocurrencymanager.R
import com.kontus.cryptocurrencymanager.fragments.DashboardFragment
import com.kontus.cryptocurrencymanager.fragments.PurchaseFragment
import com.kontus.cryptocurrencymanager.fragments.StatisticsFragment
import com.kontus.cryptocurrencymanager.models.Purchase
import com.kontus.cryptocurrencymanager.interfaces.OnFragmentInteractionListener
import com.kontus.cryptocurrencymanager.interfaces.OnListFragmentInteractionListener
import android.content.Intent
import com.google.firebase.iid.FirebaseInstanceId
import android.content.pm.PackageManager
import android.support.design.widget.AppBarLayout
import android.support.design.widget.CollapsingToolbarLayout
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.util.Log
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.kontus.cryptocurrencymanager.helpers.Config
import com.kontus.cryptocurrencymanager.helpers.General
import com.kontus.cryptocurrencymanager.helpers.SharedPreferencesHelper

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener, OnFragmentInteractionListener, OnListFragmentInteractionListener {
    private var drawer: DrawerLayout? = null
    private var toggle: ActionBarDrawerToggle? = null
    private var toolbar: Toolbar? = null
    private var mShared: SharedPreferencesHelper? = null

    private var collapsingToolbar: CollapsingToolbarLayout? = null
    private var appBarLayout: AppBarLayout? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        collapsingToolbar = findViewById(R.id.collapsing_toolbar)
        appBarLayout = findViewById(R.id.appbar)
        appBarLayout?.setExpanded(false, false)

        drawer = findViewById(R.id.drawer_layout)
        toggle = ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        drawer?.addDrawerListener(toggle!!)
        toggle?.syncState()

        val navigationView = findViewById<NavigationView>(R.id.nav_view)
        navigationView.setNavigationItemSelectedListener(this)

        mShared = SharedPreferencesHelper(this)
        println(mShared?.selectedBittrexColumnsCSV)

        General.getKeyHashForApp(packageManager)

        // Get token
        val token = FirebaseInstanceId.getInstance().token
        if (token != null) {
            Log.d("token", token)
        }

        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.WRITE_EXTERNAL_STORAGE), Config.REQUEST_WRITE_EXTERNAL_STORAGE)
        } else {
            println("PERMISSION_GRANTED")
        }

        var fragment: Fragment = DashboardFragment.newInstance("param1", "param2")
        switchFragment(fragment)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        when (requestCode) {
            Config.REQUEST_WRITE_EXTERNAL_STORAGE -> if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                println("PERMISSION_GRANTED")
            }
        }
    }

    override fun onStop() {
        super.onStop()
        toggle?.let { drawer?.removeDrawerListener(it) }
    }

    override fun onBackPressed() {
        if (drawer!!.isDrawerOpen(GravityCompat.START)) {
            drawer?.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        val id = item.itemId

        val intent: Intent
        return if (id == R.id.action_settings) {
            intent = Intent(this, SettingsActivity::class.java)
            startActivity(intent)
            true
        } else super.onOptionsItemSelected(item)

    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        val id = item.itemId

        var fragment: Fragment
        when (id) {
            R.id.nav_dashboard -> {
                fragment = DashboardFragment.newInstance("param1", "param2")
                switchFragment(fragment)

                collapsingToolbar?.title = getString(R.string.app_name)
                appBarLayout?.setExpanded(false,false)
            }
            R.id.nav_purchases -> {
                println(mShared?.selectedExchange)
                fragment = PurchaseFragment.newInstance(1)
                switchFragment(fragment)

                collapsingToolbar?.title = getString(R.string.latest_purchases)
                appBarLayout?.setExpanded(true,true)
                loadBackdrop()
            }
            R.id.nav_gain_loss_ratio -> {
                fragment = StatisticsFragment.newInstance("param1", "param2")
                switchFragment(fragment)

                collapsingToolbar?.title = getString(R.string.app_name)
                appBarLayout?.setExpanded(false,false)
            }
            R.id.nav_manage -> {

            }
            R.id.nav_share -> {

            }
            R.id.nav_send -> {

            }
            R.id.nav_sign_in -> {
                val intent = Intent(this, SignInActivity::class.java)
                startActivity(intent)
            }
            R.id.nav_write_post -> {
                val intent = Intent(this, FCMRealtimeActivity::class.java)
                startActivity(intent)
            }
        }

        val drawer = findViewById<DrawerLayout>(R.id.drawer_layout)
        drawer.closeDrawer(GravityCompat.START)
        return true
    }

    private fun switchFragment(fragment: Fragment) {
        try {
            val ft = supportFragmentManager.beginTransaction()
            ft.setCustomAnimations(android.R.anim.slide_in_left, android.R.anim.slide_out_right)
            if (supportFragmentManager.findFragmentById(R.id.fragment_container) == null) {
                ft.add(R.id.fragment_container, fragment)
            } else {
                ft.replace(R.id.fragment_container, fragment)
            }
            // ft.addToBackStack(null)
            ft.commit()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun onFragmentInteraction(uri: Uri) {
        print("onFragmentInteraction(uri: Uri) " + uri)
    }

    override fun onListFragmentInteraction(item: Purchase.PurchaseItem) {
        print("onListFragmentInteraction(item: Purchase.PurchaseItem) " + item.coinName + " " + item.coinSymbol + " " + item.quantity + " ")
    }

    private fun loadBackdrop() {
        val imageView = findViewById<ImageView>(R.id.backdrop)
        Glide.with(this).load(R.drawable.logo_2).apply(RequestOptions.centerCropTransform()).into(imageView)
    }
}