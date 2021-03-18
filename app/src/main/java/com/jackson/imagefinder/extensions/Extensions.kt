package com.jackson.imagefinder.extensions

import android.view.View
import android.view.ViewTreeObserver
import androidx.lifecycle.MutableLiveData
import com.jackson.imagefinder.utils.SSLHelper
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import okhttp3.OkHttpClient.Builder
import retrofit2.Retrofit
import java.text.SimpleDateFormat
import java.util.*

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
// 참조 : https://blog.codejun.space/86
fun <T> MutableLiveData<T>.setPost(value: T) : MutableLiveData<T> {
    this.setPost(value)
    return this
}

/**
 * 단일 클릭 on / off
 * API 응답이 오기전 여러차례 요청을 보내는 이슈 대안
 */
fun View.safeViewLock(lock: Boolean) {
    isEnabled = !lock
    isClickable = !lock
}

/**
 * 참고 : https://meetup.toast.com/posts/130
 * formatStr : datetime의 format
 * timeZone : 변경하려는 TimeZone
 */
fun String.toDate(formatStr: String = "yyyy-MM-dd'T'HH:mm:ss'Z'", timeZone: TimeZone = TimeZone.getTimeZone("Asia/seoul")): Date {
    return with(SimpleDateFormat(formatStr).also { it.timeZone = timeZone }) {
        parse(this@toDate)
    }
}
fun Date.dateFormat(toFormatStr: String, locale: Locale = Locale.KOREAN) = with(SimpleDateFormat(toFormatStr, locale)) {
    this.format(this@dateFormat)
}