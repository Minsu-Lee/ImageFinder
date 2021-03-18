package com.jackson.imagefinder.extensions

import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.ViewManager
import android.widget.EditText
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.jackson.imagefinder.utils.DLog
import com.jackson.imagefinder.utils.DeviceUtils
import com.jackson.imagefinder.view.component.SquareImageView
import org.jetbrains.anko.custom.ankoView


//inline fun ViewManager.flowLayout() = flowLayout {}
//inline fun ViewManager.flowLayout(init: FlowLayout.() -> Unit): FlowLayout {
//    return ankoView({ FlowLayout(it) }, theme = 0, init = init)
//}

fun View.string(res: Int): String = context.resources.getString(res)
fun View.integer(res: Int): Int = context.resources.getInteger(res)
fun View.deviceWidth(): Int = resources.displayMetrics.widthPixels

/** width와 height이 같은 Layout 추가 */
inline fun ViewManager.squareImageView() = squareImageView {}
inline fun ViewManager.squareImageView(init: SquareImageView.() -> Unit): SquareImageView {
    return ankoView({ SquareImageView(it) }, theme= 0, init= init)
}

/**
 * 양방향 데이터바인딩을 위해 아래와 같이 LiveData를 사용
 */
inline fun EditText.setLiveData(lifecycleOwner: LifecycleOwner?, liveData: MutableLiveData<String>?, crossinline changed: (String)->Unit? = {}) {
    // 변동된 값을 LiveData에도 반영
    addTextChangedListener(object: TextWatcher {
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) { }
        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) { }
        override fun afterTextChanged(s: Editable?) {
            changed(this@setLiveData.text.toString())
            liveData?.let { data ->
                val currentText = this@setLiveData.text.toString()
                if (data.value != currentText) {
                    data.value = currentText
                    DLog.e("editText", "change: ${this@setLiveData.text}")
                }
            }
        }
    })
    // 외부에서 liveData 값을 변경할 수 있으므로, 구독하여 데이터 변동 체크
    lifecycleOwner?.let { owner ->
        liveData?.observe(owner, Observer {
            val currentText = this@setLiveData.text.toString()
            if (it != currentText) {
                setText(it)
                DLog.e("observe", "change: ${this@setLiveData.text}")
            }
        })
    } ?: setText(liveData?.getDefault(""))
}

/**
 * liveData로 키보드 노출여부를 제어하기 위해 사용
 */
inline fun EditText.initKeyboardControl(lifecycleOwner: LifecycleOwner?, liveData: MutableLiveData<Boolean>?) {
    lifecycleOwner?.let { owner ->
        liveData?.observe(owner, Observer {
            if (it) DeviceUtils.showKeyboard(context, this) else DeviceUtils.hideKeyboard(context, this)
        })
    }
}