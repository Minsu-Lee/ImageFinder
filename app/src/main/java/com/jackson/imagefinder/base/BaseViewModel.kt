package com.jackson.imagefinder.base

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.jackson.imagefinder.extensions.set
import com.jackson.imagefinder.extensions.setPost
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import kotlinx.coroutines.CoroutineScope

enum class ListItemStatus(var value: String, var id: Int) {
    FIRST("FIRST", 0),              // 검색 전 ) 처음 상태
    FULL("FULL", 100),              // 검색 후 ) 데이터가 존재하는 경우
    NOT_FOUND("NOT_FOUND", 200),    // 검색 후 ) 검색 데이터가 없는 경우
    EMPTY("EMPTY", 300),            // 예외사항
}

open class BaseViewModel : ViewModel() {

    var lifecycleOwner: LifecycleOwner? = null

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

    private val _keyboardStatus: MutableLiveData<Boolean> = MutableLiveData(false)
    val keyboardStatus: MutableLiveData<Boolean>
        get() = _keyboardStatus

    fun progressStatus(state: Boolean) {
        progressStatus.set(state)
    }

    fun keyboardStatus(isShow: Boolean) {
        keyboardStatus.value = isShow
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