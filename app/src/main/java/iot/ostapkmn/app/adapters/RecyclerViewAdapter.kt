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

class RecyclerViewAdapter(
        private var panelList: List<Panel>,
        private val itemClickListener: (Panel) -> Unit
) :
        RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
                LayoutInflater.from(parent.context).inflate(R.layout.fragment_items, parent,
                        false)
        )
    }

    override fun getItemCount(): Int {
        return panelList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(panelList[position], itemClickListener)

    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private var nameTextView: TextView = itemView.findViewById(R.id.name)
        private var sectionTextView: TextView = itemView.findViewById(R.id.section)
        private var manufacturerTextView: TextView = itemView.findViewById(R.id.manufacturer)
        private var technicalCharacteristicsTextView: TextView = itemView.findViewById(R.id.technicalCharacteristics)
        private var amountTextView: TextView = itemView.findViewById(R.id.amount)
        private var addressTextView: TextView = itemView.findViewById(R.id.address)
        private var photoImageView: ImageView = itemView.findViewById(R.id.image)

        fun bind(panelModel: Panel, clickListener: (Panel) -> Unit) {
            nameTextView.text = panelModel.name
            sectionTextView.text = panelModel.section
            manufacturerTextView.text = panelModel.manufacturer
            technicalCharacteristicsTextView.text = panelModel.technicalCharacteristic
            amountTextView.text = panelModel.amount.toString()
            addressTextView.text = panelModel.address
            if (panelModel.image.isEmpty()){
                photoImageView.setImageResource(R.drawable.placeholder)
            } else{
            Picasso
                    .get()
                    .load(panelModel.image)
                    .into(photoImageView)}
            itemView.setOnClickListener { clickListener(panelModel) }
        }
    }

}