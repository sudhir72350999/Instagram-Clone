package com.sudhirtheindian.instagramclone.core.database

import app.cash.sqldelight.db.SqlDriver

actual class DriverFactory {
    actual fun createDriver(): SqlDriver {
        throw UnsupportedOperationException("JS driver not yet implemented")
    }
}
