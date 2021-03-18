package com.jackson.imagefinder.extensions

import androidx.annotation.Nullable
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.jackson.imagefinder.utils.SSLHelper
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import okhttp3.OkHttpClient.Builder
import retrofit2.Retrofit
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit
import kotlin.jvm.Throws

//inline fun ViewManager.flowLayout() = flowLayout {}
//inline fun ViewManager.flowLayout(init: FlowLayout.() -> Unit): FlowLayout {
//    return ankoView({ FlowLayout(it) }, theme = 0, init = init)
//}

inline fun Builder.applySSL(): Builder = SSLHelper.configureClient(this)

fun <T> Single<T>.with(): Single<T> = subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())

inline fun <T> Retrofit.init(service: Class<T>) = create(service)

fun <T> MutableLiveData<T>.get(): T? {
    return this.value
}
fun <T> MutableLiveData<T>.getDefault(default: T): T = when {
    (value != null) || (value != null && value == "" && default == "") -> value!!
    else -> default
}
fun <T> MutableLiveData<T>.set(value: T) : MutableLiveData<T> {
    this.value = value
    return this
}