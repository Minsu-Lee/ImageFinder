package com.jackson.imagefinder

import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.jackson.imagefinder.base.BackPressCloseHandler
import com.jackson.imagefinder.base.BaseViewModel
import com.jackson.imagefinder.base.BaseView
import com.jackson.imagefinder.view.component.NetworkProgressDialog

abstract class BaseActivity<VIEW: BaseView, VIEWMODEL: BaseViewModel>: AppCompatActivity() {

    companion object {
        const val DEFAULT_PROGRESS_TIME: Long = 1000L
    }

    lateinit var mProgressView: AlertDialog
    lateinit var backPressHandler: BackPressCloseHandler

    protected var view: VIEW? = null
    abstract fun onCreatedView(): VIEW?
    protected var viewModel: VIEWMODEL? = null
    abstract fun onCreatedViewModel(): VIEWMODEL?

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        overridePendingTransition(R.anim.act_slide_right_in, R.anim.stay)
        onCreatedView()?.let { view = it }
        onCreatedViewModel()?.let { viewModel = it }
        view?.apply {
            vm = viewModel
            setContentView(createView(org.jetbrains.anko.AnkoContext.create(this@BaseActivity, this@BaseActivity)))
        }
        mProgressView = NetworkProgressDialog.getInstance(this@BaseActivity)
        backPressHandler = BackPressCloseHandler(this@BaseActivity)

        // 프로그래스 동작 status 구독
        viewModel?.progressStatus?.observe(this, Observer {
            if (it) onVisibleProgress() else onInvisibleProgress()
        })
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