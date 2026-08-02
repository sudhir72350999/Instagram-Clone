package com.sudhirtheindian.instagramclone.core.database

import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.native.NativeSqliteDriver
import com.sudhirtheindian.instagramclone.db.InstagramDatabase

actual class DriverFactory {
    actual fun createDriver(): SqlDriver {
        return NativeSqliteDriver(InstagramDatabase.Schema, "instagram.db")
    }
}
