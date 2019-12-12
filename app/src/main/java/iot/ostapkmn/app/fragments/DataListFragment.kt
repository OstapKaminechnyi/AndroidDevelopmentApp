package iot.ostapkmn.app.fragments


import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.ConnectivityManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import iot.ostapkmn.app.R
import iot.ostapkmn.app.activities.ObjectDetailActivity
import iot.ostapkmn.app.adapters.RecyclerViewAdapter
import iot.ostapkmn.app.api.Api
import iot.ostapkmn.app.models.Panel
import kotlinx.android.synthetic.main.activity_list.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

private var dataList = ArrayList<Panel>()

class DataListFragment : Fragment() {

    private var broadcastReceiver: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            val isDisconnected = intent.getBooleanExtra(ConnectivityManager.EXTRA_NO_CONNECTIVITY,
                    false)
            if (isDisconnected) {
                recyclerView.visibility = View.INVISIBLE
                progressBar.visibility = View.VISIBLE
                Toast.makeText(recyclerView.context,
                        getString(R.string.offline), Toast.LENGTH_SHORT)
                        .show()
            } else {
                recyclerView.visibility = View.VISIBLE
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        retainInstance = true
    }

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? =
            inflater.inflate(R.layout.activity_list, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setProgressBar()
        recyclerView.apply {
            layoutManager = LinearLayoutManager(activity)
            adapter = RecyclerViewAdapter(
                    dataList
            ) { item: Panel -> partItemClicked(item) }
        }
        loadData()
        swipeRefreshLayout.setOnRefreshListener {
            refreshData()
        }
    }

    override fun onStart() {
        super.onStart()
        activity?.registerReceiver(
                broadcastReceiver,
                IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION)
        )
    }

    override fun onStop() {
        super.onStop()
        activity?.unregisterReceiver(broadcastReceiver)
    }

    private fun partItemClicked(partItem: Panel) {
        Toast.makeText(this.activity, "Clicked: ${partItem.name}", Toast.LENGTH_LONG).show()
        val showDetailActivityIntent = Intent(this.activity, ObjectDetailActivity::class.java)
        showDetailActivityIntent.putExtra("id", partItem.id)
        showDetailActivityIntent.putExtra("name", partItem.name)
        showDetailActivityIntent.putExtra("description", partItem.address)
        showDetailActivityIntent.putExtra("photo", partItem.manufacturer)
        showDetailActivityIntent.putExtra("size", partItem.amount)
        startActivity(showDetailActivityIntent)
    }

    private fun loadData() {
        val call = Api.getClient.getPanels()
        call.enqueue(object : Callback<List<Panel>> {

            override fun onResponse(
                    call: Call<List<Panel>>?,
                    response: Response<List<Panel>>?
            ) = changeDataSet(response)

            override fun onFailure(call: Call<List<Panel>>?, t: Throwable?) {
                progressBar.visibility = View.INVISIBLE
            }
        })
    }

    private fun changeDataSet(response: Response<List<Panel>>?) {
        progressBar.visibility = View.INVISIBLE
        dataList.addAll(response!!.body()!!)
        recyclerView.adapter?.notifyDataSetChanged()
    }

    private fun setProgressBar() {
        progressBar.visibility = View.VISIBLE
    }

    private fun refreshData() {
        dataList.clear()
        setProgressBar()
        loadData()
        swipeRefreshLayout.isRefreshing = false
    }


}