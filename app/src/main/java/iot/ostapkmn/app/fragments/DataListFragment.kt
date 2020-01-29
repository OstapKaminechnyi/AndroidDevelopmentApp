package iot.ostapkmn.app.fragments

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.ConnectivityManager
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import iot.ostapkmn.app.R
import iot.ostapkmn.app.activities.PostPanelActivity
import iot.ostapkmn.app.activities.CardDetailActivity
import iot.ostapkmn.app.adapters.RecyclerViewAdapter
import iot.ostapkmn.app.api.Api
import iot.ostapkmn.app.models.Panel
import kotlinx.android.synthetic.main.data_fragment_list.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

private var dataList = ArrayList<Panel>()

class DataListFragment : Fragment() {

    private var broadcastReceiver: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            val isDisconnected = intent.getBooleanExtra(
                    ConnectivityManager
                            .EXTRA_NO_CONNECTIVITY, false
            )
            when (isDisconnected) {
                true -> launchDisconnectedState()
                false -> launchConnectedState()
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
            inflater.inflate(R.layout.data_fragment_list, container, false)


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setProgressBar()
        list_recycler_view.apply {
            layoutManager = LinearLayoutManager(activity)
            adapter = RecyclerViewAdapter(
                    dataList
            ) { item: Panel -> partItemClicked(item) }
        }
        fab.setOnClickListener {
            val intent = Intent(activity, PostPanelActivity::class.java)
            activity!!.startActivity(intent)
        }

        loadData()
        swipeContainer.setOnRefreshListener {
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
        val showDetailActivityIntent = Intent(this.activity, CardDetailActivity::class.java)
        showDetailActivityIntent.putExtra("id", partItem.id)
        showDetailActivityIntent.putExtra("name", partItem.name)
        showDetailActivityIntent.putExtra("section", partItem.section)
        showDetailActivityIntent.putExtra("manufacturer", partItem.manufacturer)
        showDetailActivityIntent.putExtra("amount", partItem.amount.toString())
        showDetailActivityIntent.putExtra("technicalCharacteristics",
                partItem.technicalCharacteristic)
        showDetailActivityIntent.putExtra("address", partItem.address)
        showDetailActivityIntent.putExtra("imageUrls", partItem.image)

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
        list_recycler_view.adapter?.notifyDataSetChanged()
    }

    private fun setProgressBar() {
        progressBar.visibility = View.VISIBLE
    }

    private fun refreshData() {
        dataList.clear()
        setProgressBar()
        loadData()
        swipeContainer.isRefreshing = false
    }

    private fun launchDisconnectedState() {
        list_recycler_view.visibility = View.INVISIBLE
        progressBar.visibility = View.VISIBLE
        Toast.makeText(
                list_recycler_view.context,
                getString(R.string.offline),
                Toast.LENGTH_LONG
        ).show()
    }

    private fun launchConnectedState() {
        list_recycler_view.visibility = View.VISIBLE
    }
}