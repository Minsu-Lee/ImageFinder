package com.jackson.imagefinder

import com.jackson.imagefinder.base.ParamsInfo
import com.jackson.imagefinder.di.apiModule
import com.jackson.imagefinder.di.networkModule
import com.jackson.imagefinder.di.viewModelModule
import com.jackson.imagefinder.network.KakaoAPIService
import com.jackson.imagefinder.viewModel.ImageViewModel
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.koin.core.context.stopKoin
import org.koin.test.KoinTest
import org.koin.test.KoinTestRule
import org.koin.test.inject
import java.util.concurrent.TimeUnit

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest: KoinTest {

    val service: KakaoAPIService by inject()
    val imageViewModel: ImageViewModel by inject()

    @get:Rule
    val koinTestRule = KoinTestRule.create {
        printLogger()
        modules(networkModule, apiModule, viewModelModule)
    }

    @Before
    fun before() {
        // service = get<Retrofit>().create(KakaoAPIService::class.java)
        // imageViewModel = get() // lifeOnwer가 필요
        // imageViewModel = getViewModel(owner) // lifeOnwer가 필요
    }

    @Test
    fun api_test() {

        var query = "mvvm"
        var sort = "accuracy"
        var page = 1
        var pageSize = 10

        runBlocking {

            launch {

                // imageViewModel.searchRepositories(query= query, sort= sort, page= page, pageSize = pageSize)

                // 단위테스트에서 값을 확인 못하는 이유, 참조: https://youngest-programming.tistory.com/360
//                imageViewModel.items.observe(it, Observer<List<ImageData>> {
//                    println("testssss: ${it.size}")
//                })
//                imageViewModel.items.observeForTesting {
//
//                }


                hashMapOf<String, Any?>().apply {
                    put(ParamsInfo.KEY_SEARCH_QUERY, query)
                    put(ParamsInfo.KEY_SEARCH_SORT, sort)
                    put(ParamsInfo.KEY_SEARCH_PAGE, "$page")
                    put(ParamsInfo.KEY_SEARCH_PAGE_SIZE, "$pageSize")
                }.let { params ->

                    // https://babosamo.tistory.com/211
                    service.searchImage(params)
                        .doOnError { println(it) }
                        .doOnNext { println(it) }
                        .test()
                        .awaitDone(3, TimeUnit.SECONDS)
                        .assertValue { it ->
                            it != null && it.documents.size > 0
                        }
                        .assertComplete()


                        //.subscribeOn(AndroidSchedulers.mainThread())
//                        .subscribeOn(Schedulers.io())
//                        .subscribe({
//                            println("result : ${it.documents.size}")
//                        }, {
//                            println("Throwable: ${it.message}")
//                        })

                }
            }
        }

    }

    @After
    fun after() {
        stopKoin()
    }

}