package com.balsikandar.github.usecases

import com.balsikandar.common.base.UseCase
import com.balsikandar.common.base.UseCase_
import com.balsikandar.github.data.TrendingRepo
import dagger.Binds

object UseCases {

    @dagger.Module
    abstract class Module {
        @Binds
        @UseCase_("trending_list")
        abstract fun getTrendingList(getTrendingList: GetTrendingList): UseCase<Unit, MutableList<TrendingRepo>>

        @Binds
        @UseCase_("trending_list")
        abstract fun getTrendingListFromServer(getTrendingList: GetTrendingListFromServer): UseCase<Unit, MutableList<TrendingRepo>>
    }
}