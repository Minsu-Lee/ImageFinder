package com.jackson.imagefinder.viewModel

import androidx.lifecycle.MutableLiveData
import com.jackson.imagefinder.base.BaseViewModel
import com.jackson.imagefinder.base.ParamsInfo
import com.jackson.imagefinder.extensions.getDefault
import com.jackson.imagefinder.extensions.set
import com.jackson.imagefinder.model.ImageData
import com.jackson.imagefinder.model.MetaData
import com.jackson.imagefinder.network.KakaoAPIService
import com.jackson.imagefinder.utils.DLog
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class ImageViewModel(private val service: KakaoAPIService): BaseViewModel() {

    companion object {
        val TAG = javaClass.simpleName
    }

    private val _meta: MutableLiveData<MetaData> = MutableLiveData()
    val meta: MutableLiveData<MetaData>
        get() = _meta

    private val _items: MutableLiveData<ArrayList<ImageData>> = MutableLiveData(arrayListOf())
    val items: MutableLiveData<ArrayList<ImageData>>
        get() = _items

    private val _query: MutableLiveData<String> = MutableLiveData("")
    val pQuery: MutableLiveData<String>
        get() = _query
    private val _pSort: MutableLiveData<String> = MutableLiveData("")
    val pSort: MutableLiveData<String>
        get() = _pSort
    private val _pPage: MutableLiveData<Int> = MutableLiveData(1)
    val pPage: MutableLiveData<Int>
        get() = _pPage
    private val _pPageSize: MutableLiveData<Int> = MutableLiveData(30)
    val pPageSize: MutableLiveData<Int>
        get() = _pPageSize

    fun searchRepositories(query: String = pQuery.getDefault(""), sort: String = pSort.getDefault("accuracy"), page: Int = pPage.getDefault(1), pageSize: Int = pPageSize.getDefault(30), isFirst: Boolean = false) {
        hashMapOf<String, Any?>().apply {
            put(ParamsInfo.KEY_SEARCH_QUERY, query)
            put(ParamsInfo.KEY_SEARCH_SORT, sort)
            put(ParamsInfo.KEY_SEARCH_PAGE, "$page")
            put(ParamsInfo.KEY_SEARCH_PAGE_SIZE, "$pageSize")
        }.let { params ->

            progressStatus(isFirst, true)

            addDisposable(service.searchImage(params)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .doOnError { progressStatus(isFirst, false) }
                .doOnComplete { progressStatus(isFirst, false) }
                .subscribe({
                    meta.set(it.meta ?: MetaData())
                    items.set(it.documents)
                    DLog.e(TAG, "result: ${it.documents.size}")
                    println("result: ${it.documents.size}")
                    progressStatus(isFirst, false)
                }, {
                    DLog.e(TAG, "Throwable: ${it.message}")
                    println("Throwable: ${it.message}")
                    progressStatus(isFirst, false)
                }))
        }
    }
}