package com.jackson.imagefinder.viewModel

import androidx.lifecycle.MutableLiveData
import com.jackson.imagefinder.base.AppConst
import com.jackson.imagefinder.base.BaseViewModel
import com.jackson.imagefinder.base.ListItemStatus
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
    private val _pSort: MutableLiveData<String> = MutableLiveData(AppConst.SORT_ACCURACY)
    val pSort: MutableLiveData<String>
        get() = _pSort
    private val _pPage: MutableLiveData<Int> = MutableLiveData(AppConst.PAGE_FIRST_VALUE)
    val pPage: MutableLiveData<Int>
        get() = _pPage
    private val _pPageSize: MutableLiveData<Int> = MutableLiveData(AppConst.PER_PAGE_DEFAULT)
    val pPageSize: MutableLiveData<Int>
        get() = _pPageSize

    fun itemClear() {
        items.value = arrayListOf()
    }

    fun searchRepositories(query: String = pQuery.getDefault(""),
                           sort: String = pSort.getDefault(AppConst.SORT_ACCURACY),
                           page: Int = pPage.getDefault(AppConst.PAGE_FIRST_VALUE),
                           pageSize: Int = pPageSize.getDefault(AppConst.PER_PAGE_DEFAULT)) {
        if (query.isNotEmpty()) hashMapOf<String, Any?>().apply {
            put(ParamsInfo.KEY_SEARCH_QUERY, query)
            put(ParamsInfo.KEY_SEARCH_SORT, sort)
            put(ParamsInfo.KEY_SEARCH_PAGE, "$page")
            put(ParamsInfo.KEY_SEARCH_PAGE_SIZE, "$pageSize")
        }.let { params ->

            progressStatus(page == 0)
            if (page == 0) itemClear()

            addDisposable(service.searchImage(params)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe({
                    meta.set(it.meta ?: MetaData())
                    with(items) {
                        value?.addAll(it.documents)
                        // addAll 호출만으로 구독에 반영이 없어 재초기화
                        value = value
                    }
                    progressStatus(false)
                    DLog.e(TAG, "result: ${it.documents.size}")

                    itemStatus.set(
                        if (it.documents.size > 0) ListItemStatus.FULL
                        else ListItemStatus.NOT_FOUND
                    )

                    keyboardStatus(false)

                }, {
                    progressStatus(false)
                    DLog.e(TAG, "Throwable: ${it.message}")
                }))

        }
    }
}