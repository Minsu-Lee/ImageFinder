package com.jackson.imagefinder

import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.jackson.imagefinder.base.BackPressCloseHandler
import com.jackson.imagefinder.base.BaseView
import com.jackson.imagefinder.base.BaseViewModel
import com.jackson.imagefinder.view.component.NetworkProgressDialog
import java.util.*

abstract class BaseActivity<VIEW: BaseView<VIEWMODEL>, VIEWMODEL: BaseViewModel>: AppCompatActivity() {

    companion object {
        const val DEFAULT_PROGRESS_TIME: Long = 1000L
    }

    lateinit var mProgressView: AlertDialog
    lateinit var backPressHandler: BackPressCloseHandler

    abstract var view: VIEW
    lateinit var viewModel: VIEWMODEL
    abstract fun onCreateViewModel(): VIEWMODEL

    override fun onCreate(savedInstanceState: Bundle?) {
        overridePendingTransition(R.anim.act_slide_right_in, R.anim.stay)
        super.onCreate(savedInstanceState)
        // TimeZone Asia/Seoul로 지정
        TimeZone.setDefault(TimeZone.getTimeZone("Asia/Seoul"))
        viewModel = onCreateViewModel().apply {
            lifecycleOwner = this@BaseActivity
            // 프로그래스 동작 status 구독
            progressStatus.observe(this@BaseActivity, Observer {
                if (it) onVisibleProgress() else onInvisibleProgress()
            })
        }
        with(view) {
            vm = viewModel
            setContentView(createView(org.jetbrains.anko.AnkoContext.create(this@BaseActivity, this@BaseActivity)))
        }

        mProgressView = NetworkProgressDialog.getInstance(this@BaseActivity)
        backPressHandler = BackPressCloseHandler(this@BaseActivity)
    }

    fun onVisibleProgress() {
        mProgressView.show()
    }

    fun onInvisibleProgress() {
        mProgressView.let {
            if (it.isShowing) Handler().postDelayed({
                it.dismiss()
            }, DEFAULT_PROGRESS_TIME)
        }
    }

}