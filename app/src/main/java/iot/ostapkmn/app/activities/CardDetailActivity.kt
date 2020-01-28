package iot.ostapkmn.app.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.squareup.picasso.Picasso
import iot.ostapkmn.app.R
import kotlinx.android.synthetic.main.app_bar.*
import kotlinx.android.synthetic.main.list_item_detailed.*

class CardDetailActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.list_item_detailed)
        setToolbar()
        setCard()
    }

    private fun setToolbar() {
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        toolbar.setNavigationOnClickListener {
            finish()
        }
    }

    private fun setCard() {
        if (intent.hasExtra("name")) {
            detailed_name.text = intent.getStringExtra("name")
            detailed_section.text = intent.getStringExtra("section")
            detailed_manufacturer.text = intent.getStringExtra("manufacturer")
            detailed_amount.text = intent.getStringExtra("amount")
            detailed_technicalCharacteristics.text =
                    intent.getStringExtra("technicalCharacteristics")
            detailed_address.text = intent.getStringExtra("address")
            val photo = intent.getStringExtra("imageUrls")
            if(photo.isEmpty()){
                detailed_photo.setImageResource(R.drawable.placeholder)
            } else {
                Picasso
                        .get()
                        .load(photo)
                        .into(detailed_photo)
            }
        }
    }
}