package com.sudhirtheindian.instagramclone

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform