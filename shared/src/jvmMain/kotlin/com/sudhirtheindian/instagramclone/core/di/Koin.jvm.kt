package com.sudhirtheindian.instagramclone.core.di

import app.cash.sqldelight.db.SqlDriver
import com.sudhirtheindian.instagramclone.core.database.DriverFactory
import org.koin.core.module.Module
import org.koin.dsl.module

actual val platformModule: Module = module {
    single<SqlDriver> { DriverFactory().createDriver() }
}
