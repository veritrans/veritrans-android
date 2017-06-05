# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in /Users/rakawm/Library/Android/sdk/tools/proguard/proguard-android.txt
# You can edit the include path and order by changing the proguardFiles
# directive in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# Add any project specific keep options here:

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

-keepattributes Exceptions, InnerClasses, Signature

# Midtrans Core SDK

-keep class com.midtrans.sdk.core.models.** { *; }
-keep class com.midtrans.sdk.core.utils.Logger { *; }
-keep class com.midtrans.sdk.core.utils.Utilities { *; }
-keep class com.midtrans.sdk.core.utils.CardUtilities { *; }
-keep class com.midtrans.sdk.core.MidtransCore { *; }
-keep class com.midtrans.sdk.core.MidtransCore$Builder { *; }
-keep interface com.midtrans.sdk.core.MidtransCoreCallback { *; }
-keep class com.midtrans.sdk.core.Environment { *; }

# Retrofit 2

## Platform calls Class.forName on types which do not exist on Android to determine platform.
-dontnote retrofit2.Platform
## Platform used when running on RoboVM on iOS. Will not be used at runtime.
-dontnote retrofit2.Platform$IOS$MainThreadExecutor
## Platform used when running on Java 8 VMs. Will not be used at runtime.
-dontwarn retrofit2.Platform$Java8

# ok http
-dontwarn com.squareup.okhttp.**
-dontwarn okio.**