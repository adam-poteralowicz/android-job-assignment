package com.schibsted.nde.testUtils

import androidx.room.DatabaseConfiguration
import androidx.room.InvalidationTracker
import androidx.sqlite.db.SupportSQLiteOpenHelper
import com.schibsted.nde.database.AppDatabase
import com.schibsted.nde.database.MealEntityDao
import com.schibsted.nde.database.MealEntityDao_Impl

class FakeAppDatabase : AppDatabase() {

    private val fakeRoomDatabase = FakeRoomDatabase()

    override fun mealDao(): MealEntityDao {
        return MealEntityDao_Impl(fakeRoomDatabase)
    }

    override fun clearAllTables() {
        return
    }

    override fun createInvalidationTracker(): InvalidationTracker {
        return InvalidationTracker(this)
    }

    override fun createOpenHelper(config: DatabaseConfiguration): SupportSQLiteOpenHelper {
        val configuration = SupportSQLiteOpenHelper.Configuration.builder(config.context).build()
        return config.sqliteOpenHelperFactory.create(configuration)
    }

}