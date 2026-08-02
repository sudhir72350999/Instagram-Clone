package com.sudhirtheindian.instagramclone.core.database

import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.jdbc.sqlite.JdbcSqliteDriver
import com.sudhirtheindian.instagramclone.db.InstagramDatabase

actual class DriverFactory {
    actual fun createDriver(): SqlDriver {
        val driver = JdbcSqliteDriver(JdbcSqliteDriver.IN_MEMORY)
        InstagramDatabase.Schema.create(driver)
        return driver
    }
}
