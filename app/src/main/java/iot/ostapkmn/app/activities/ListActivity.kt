package iot.ostapkmn.app.activities

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.ConnectivityManager
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import iot.ostapkmn.app.R
import iot.ostapkmn.app.adapters.RecyclerViewAdapter
import iot.ostapkmn.app.api.Api
import iot.ostapkmn.app.models.Panel
import kotlinx.android.synthetic.main.activity_list.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ListActivity : AppCompatActivity() {

    private var auth = FirebaseAuth.getInstance()
    lateinit var recyclerView: RecyclerView
    private lateinit var progressBar: ProgressBar
    private lateinit var recycleViewAdapter: RecyclerViewAdapter
    private var panelList = ArrayList<Panel>()

    private var broadcastReceiver: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            val isDisconnected = intent.getBooleanExtra(ConnectivityManager.EXTRA_NO_CONNECTIVITY,
                    false)
            if (isDisconnected) {
                recyclerView.visibility = View.INVISIBLE
                progressBar.visibility = View.VISIBLE
                Toast.makeText(applicationContext, getString(R.string.offline), Toast.LENGTH_SHORT)
                        .show()
            } else {
                recyclerView.visibility = View.VISIBLE
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list)
        setRecyclerViewAdapter()
        setLinearLayoutManager()
        setProgressBar()
        swipeRefreshLayout.setOnRefreshListener {
            updateList()
        }
        loadList()
    }

    override fun onStart() {
        super.onStart()
        registerReceiver(broadcastReceiver, IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION))
    }

    override fun onStop() {
        super.onStop()
        unregisterReceiver(broadcastReceiver)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.sign_out_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        signOut()
        return super.onOptionsItemSelected(item)

    }

    private fun setRecyclerViewAdapter() {
        recyclerView = findViewById(R.id.recyclerView)
        recycleViewAdapter = RecyclerViewAdapter(panelList)
        recycleViewAdapter.notifyDataSetChanged()
        recyclerView.adapter = recycleViewAdapter
    }

    private fun setLinearLayoutManager() {
        val linearLayoutManager = LinearLayoutManager(recyclerView.context)
        linearLayoutManager.reverseLayout = false
        linearLayoutManager.stackFromEnd = true
        recyclerView.layoutManager = linearLayoutManager
    }

    private fun setProgressBar() {
        progressBar = findViewById(R.id.progressBar)
        progressBar.visibility = View.VISIBLE
    }

    private fun updateList() {
        panelList.clear()
        setProgressBar()
        loadList()
        swipeRefreshLayout.isRefreshing = false
    }


    private fun loadList() {
        val call = Api.getClient.getPanels()
        call.enqueue(object : Callback<List<Panel>> {

            override fun onResponse(
                    call: Call<List<Panel>>?,
                    response: Response<List<Panel>>?
            ) = handleResponse(response)

            override fun onFailure(call: Call<List<Panel>>?, t: Throwable?) {
                progressBar.visibility = View.INVISIBLE
            }
        })
    }

    private fun handleResponse(response: Response<List<Panel>>?) {
        progressBar.visibility = View.INVISIBLE
        panelList.addAll(response!!.body()!!)
        recyclerView.adapter?.notifyDataSetChanged()
    }

    private fun signOut() {
        auth.signOut()
        startActivity(Intent(this, SignInActivity::class.java))
        finish()
    }
}