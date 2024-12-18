package com.schibsted.nde.testUtils

import androidx.room.DatabaseConfiguration
import androidx.room.InvalidationTracker
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteOpenHelper

class FakeRoomDatabase() : RoomDatabase() {

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