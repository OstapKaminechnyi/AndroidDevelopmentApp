package iot.ostapkmn.app.activities

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import iot.ostapkmn.app.R
import iot.ostapkmn.app.api.Api
import iot.ostapkmn.app.models.Panel
import kotlinx.android.synthetic.main.activity_post_panel.*
import kotlinx.android.synthetic.main.app_bar.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class PostPanelActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_post_panel)
        setToolbar()
        addBtn.setOnClickListener { postData() }
        progressBar.visibility = View.INVISIBLE
    }

    private fun setToolbar() {
        setSupportActionBar(toolbar)
        supportActionBar?.run {
            setDisplayHomeAsUpEnabled(true)
            setDisplayShowHomeEnabled(true)
        }
        toolbar.setNavigationOnClickListener {
            finish()
        }
    }

    private fun postData() {
        val name =
                addName.text.toString()
        val amount = addAmount.text.toString().toInt()
        val section =
                addSection.text.toString()
        val manufacturer =
                addManufacturer.text.toString()
        val technicalCharacteristics = addTechnicalCharacteristic.text.toString()
        val address = addAddress.text.toString()
        val panel = Panel(0, name, amount, section, manufacturer, technicalCharacteristics,
                address, "")
        val call = Api.getClient.addPanel(panel)
        call.enqueue(object : Callback<Panel> {
            override fun onFailure(call: Call<Panel>, t: Throwable) {
                t.printStackTrace()
            }

            override fun onResponse(
                    call: Call<Panel>,
                    response: Response<Panel>
            ) {
                handleResponse()
            }
        })
        setProgressBar()
    }

    private fun handleResponse() {
        setProgressBar()
        Toast.makeText(
                this@PostPanelActivity,
                getString(R.string.sucessfully_posted),
                Toast.LENGTH_LONG
        ).show()
        finish()
    }

    private fun setProgressBar() = when {
        progressBar.visibility == View.VISIBLE -> progressBar.visibility = View.INVISIBLE
        else -> progressBar.visibility = View.VISIBLE
    }

}