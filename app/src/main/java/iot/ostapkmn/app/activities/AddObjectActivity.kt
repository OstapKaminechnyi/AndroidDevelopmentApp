package iot.ostapkmn.app.activities

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import iot.ostapkmn.app.R
import iot.ostapkmn.app.api.Api
import iot.ostapkmn.app.models.Panel
import kotlinx.android.synthetic.main.activity_add_object.*
import kotlinx.android.synthetic.main.app_bar.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.IOException


class AddObjectActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_object)
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
            launchTabActivity()
        }
    }


    private fun postData() {
        val name =
                addName.text.toString()
        val section =
                addDescription.text.toString()
        val manufacturer =
                addSize.text.toString()
        val panel = Panel(0, name, 0, section, manufacturer, "",
                "", "")
        val call = Api.getClient.addPanel(panel)
        call.enqueue(object : Callback<Panel> {
            override fun onFailure(call: Call<Panel>, t: Throwable) {
                t.printStackTrace()
            }

            override fun onResponse(
                    call: Call<Panel>,
                    response: Response<Panel>
            ) {
                setProgressBar()
                Toast.makeText(
                        this@AddObjectActivity,
                        getString(R.string.sucessfully_posted),
                        Toast.LENGTH_LONG
                )
                launchTabActivity()
            }
        })
        setProgressBar()
    }

    private fun setProgressBar() = when {
        progressBar.visibility == View.VISIBLE -> progressBar.visibility = View.INVISIBLE
        else -> progressBar.visibility = View.VISIBLE
    }

    private fun launchTabActivity() {
        startActivity(Intent(applicationContext, TabActivity::class.java))
        finish()
    }
}