package miq0717.downloadwithprogress

import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Streaming
import retrofit2.http.Url

/*Created by MiQ0717 on 15-Jun-2020.*/
interface RetrofitInterface {
    @GET()
    @Streaming
    fun downloadFile(@Url downloadUrl: String): Call<ResponseBody?>?
}