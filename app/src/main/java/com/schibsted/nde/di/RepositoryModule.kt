package com.schibsted.nde.di

import com.schibsted.nde.data.MealsRepository
import com.schibsted.nde.data.MealsRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    abstract fun mealsRepository(impl: MealsRepositoryImpl): MealsRepository
}