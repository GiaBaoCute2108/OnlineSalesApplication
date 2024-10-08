buildscript {
    repositories {
        mavenCentral()
        jcenter() // Sử dụng cái này chỉ khi cần thiết, vì JCenter đã ngừng hoạt động
    }
    dependencies {
        classpath("com.google.gms:google-services:4.4.2")
        classpath("com.android.tools.build:gradle:8.3.2")
    }
}

// Tệp build cấp cao nhất nơi bạn có thể thêm các tùy chọn cấu hình chung cho tất cả các sub-project/module.
@Suppress("DSL_SCOPE_VIOLATION") // TODO: Xóa khi KTIJ-19369 được khắc phục
plugins {
    alias(libs.plugins.androidApplication) apply false
}
true // Cần thiết để làm cho annotation Suppress hoạt động cho khối plugins
