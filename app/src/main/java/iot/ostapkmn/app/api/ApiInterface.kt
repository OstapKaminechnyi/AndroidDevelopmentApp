package iot.ostapkmn.app.api

import iot.ostapkmn.app.models.Panel
import retrofit2.Call
import retrofit2.http.GET


interface ApiInterface {
    @GET("panels/")
    fun getPanels(): Call<List<Panel>>
}