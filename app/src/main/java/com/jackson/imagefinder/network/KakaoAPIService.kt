package com.jackson.imagefinder.network

import com.jackson.imagefinder.base.ApiUrl
import com.jackson.imagefinder.model.ImageSearchResponse
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.QueryMap

interface KakaoAPIService {

    @GET(ApiUrl.SEARCH_IMAGE)
    // @Headers("Authorization: KakaoAK 2dd8b775a924f0deb5bb1e26b8b0b40b")
    fun searchImage(@QueryMap params: HashMap<String, Any?>): Observable<ImageSearchResponse>

}