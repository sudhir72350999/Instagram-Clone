package com.sudhirtheindian.instagramclone.core.database

import android.content.Context
import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.android.AndroidSqliteDriver
import com.sudhirtheindian.instagramclone.db.InstagramDatabase

actual class DriverFactory(private val context: Context) {
    actual fun createDriver(): SqlDriver {
        return AndroidSqliteDriver(InstagramDatabase.Schema, context, "instagram.db")
    }
}
