package iot.ostapkmn.app.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import iot.ostapkmn.app.R
import iot.ostapkmn.app.models.Panel

class RecyclerViewAdapter(private var panelList: List<Panel>) :
        RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =
                LayoutInflater.from(parent.context).inflate(R.layout.fragment_items, parent,
                        false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return panelList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        with(panelList[position]) {
            holder.nameTextView.text = name
            holder.sectionTextView.text = section
            holder.manufacturerTextView.text = manufacturer
            holder.amountTextView.text = amount.toString()
            holder.technicalCharacteristicsTextView.text = technicalCharacteristic
            holder.addressTextView.text = address
            Picasso
                    .get()
                    .load(image)
                    .into(holder.photoImageView)
        }
    }

    class ViewHolder(itemLayoutView: View) : RecyclerView.ViewHolder(itemLayoutView) {
        var nameTextView: TextView = itemLayoutView.findViewById(R.id.name)
        var sectionTextView: TextView = itemLayoutView.findViewById(R.id.section)
        var manufacturerTextView: TextView = itemLayoutView.findViewById(R.id.manufacturer)
        var technicalCharacteristicsTextView: TextView = itemLayoutView.
                findViewById(R.id.technicalCharacteristics)
        var amountTextView: TextView = itemLayoutView.findViewById(R.id.amount)
        var addressTextView: TextView = itemLayoutView.findViewById(R.id.address)
        var photoImageView: ImageView = itemLayoutView.findViewById(R.id.image)
    }
}