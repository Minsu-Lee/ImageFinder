package com.jackson.imagefinder.base

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.jackson.imagefinder.extensions.set
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable

enum class ListItemStatus(var value: String) {
    FIRST("FIRST"),         // 검색 전 ) 처음 상태
    FULL("FULL"),           // 검색 후 ) 데이터가 존재하는 경우
    EMPTY("EMPTY"),         // 검색 후 ) 검색데이터가 없는 경우
}

open class BaseViewModel : ViewModel() {

    /**
     * 데이터 상태, ListItemStatus
     *
     * 검색 후 ) 데이터가 존재하는 경우      --  ListItemStatus.FULL
     * 검색 전 ) 데이터가 없는 경우         --  ListItemStatus.EMPTY
     * 검색 후 ) 데이터가 없는 경우         --  ListItemStatus.NOT_FOUND
     */
    private val _itemStatus: MutableLiveData<ListItemStatus> = MutableLiveData(ListItemStatus.FIRST)
    val itemStatus: MutableLiveData<ListItemStatus>
        get() = _itemStatus

    private val _progressStatus: MutableLiveData<Boolean> = MutableLiveData(false)
    val progressStatus: MutableLiveData<Boolean>
        get() = _progressStatus

    fun progressStatus(isFirst: Boolean, state: Boolean) {
        if (!isFirst) progressStatus.set(state)
    }

    private val disposables: CompositeDisposable = CompositeDisposable()

    fun addDisposable(disposable: Disposable) {
        disposables.add(disposable)
    }

    override fun onCleared() {
        disposables.clear()
        super.onCleared()
    }

}