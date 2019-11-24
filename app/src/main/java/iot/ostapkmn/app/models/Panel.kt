package iot.ostapkmn.app.models

import com.google.gson.annotations.SerializedName

data class Panel(
        @SerializedName("id")
        var id: Int,
        @SerializedName("name")
        val name: String,
        @SerializedName("amount")
        val amount: Int,
        @SerializedName("section")
        val section: String,
        @SerializedName("manufacturer")
        val manufacturer: String,
        @SerializedName("technicalCharacteristics")
        val technicalCharacteristic: String,
        @SerializedName("address")
        val address: String,
        @SerializedName("imageUrls")
        val image: String
)