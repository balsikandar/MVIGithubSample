package com.balsikandar.github.ui.trending

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.view.menu.MenuBuilder
import androidx.appcompat.view.menu.MenuPopupHelper
import androidx.appcompat.widget.PopupMenu
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.balsikandar.common.base.BaseScreen
import com.balsikandar.common.base.UserIntent
import com.balsikandar.github.R
import com.balsikandar.github.ui.trending.adapter.TrendingListAdapter
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject
import kotlinx.android.synthetic.main.error_layout.*
import kotlinx.android.synthetic.main.shimmer_layout.*
import kotlinx.android.synthetic.main.trending_screen.*
import timber.log.Timber
import java.util.concurrent.TimeUnit

class TrendingScreen : BaseScreen<TrendingContract.State>() {

    private val sortingOrderPublishSubject: PublishSubject<Int> = PublishSubject.create()
    private var refreshPublishSubject = PublishSubject.create<Unit>()
    private var retryPublishSubject = PublishSubject.create<Unit>()

    override fun userIntents(): Observable<UserIntent> {
        return Observable.mergeArray(

            Observable.just(TrendingContract.Intent.Load).doOnNext {
                Timber.e("Load: RepoScreen")
            },

            refreshPublishSubject
                .throttleFirst(300, TimeUnit.MILLISECONDS)
                .map {
                    TrendingContract.Intent.Refresh
                },

            sortingOrderPublishSubject
                .throttleFirst(300, TimeUnit.MILLISECONDS)
                .map {
                    TrendingContract.Intent.SortingOrder(it)
                },

            retryPublishSubject
                .throttleFirst(300, TimeUnit.MILLISECONDS)
                .map {
                    TrendingContract.Intent.Load
                }
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.trending_screen, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setRecyclerView()

        swipeToRefresh.setOnRefreshListener {

            swipeToRefresh.isRefreshing = false
            refreshPublishSubject.onNext(Unit)
        }

        ic_overflow_menu.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                setPopupMenu()
            }

        })

        retry_loading.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                retryPublishSubject.onNext(Unit)
            }

        })
    }

    private fun setRecyclerView() {
        recycler_view.layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        recycler_view.adapter = TrendingListAdapter(context)
    }

    override fun render(state: TrendingContract.State) {

        if (state.isLoading) {
            slRoot.visibility = View.VISIBLE
            swipeToRefresh.visibility = View.GONE
            erLayout.visibility = View.GONE
            return
        }

        if (state.isError) {
            slRoot.visibility = View.GONE
            swipeToRefresh.visibility = View.GONE
            erLayout.visibility = View.VISIBLE
            return
        }

        if (state.trendingRepos.isNullOrEmpty().not()) {
            slRoot.visibility = View.GONE
            erLayout.visibility = View.GONE
            swipeToRefresh.visibility = View.VISIBLE

            (recycler_view.adapter as TrendingListAdapter).updateList(state.trendingRepos)
        }
    }

    @SuppressLint("RestrictedApi")
    fun setPopupMenu() {
        val menu = PopupMenu(context!!, ic_overflow_menu)
        menu.inflate(R.menu.menus)
        menu.setOnMenuItemClickListener {
            if (it.itemId == R.id.sort_by_stars) {
                sortingOrderPublishSubject.onNext(1) //1 by author
            } else if (it.itemId == R.id.sort_by_name) {
                sortingOrderPublishSubject.onNext(2) //2 by author
            }
            return@setOnMenuItemClickListener false
        }

        val menuHelper = MenuPopupHelper(context!!, menu.menu as MenuBuilder, ic_overflow_menu)
        menuHelper.setForceShowIcon(true)
        menuHelper.show()
    }

}