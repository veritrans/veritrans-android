# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in /Users/ziahaqi/Library/Android/sdk/tools/proguard/proguard-android.txt
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

# Midtrans UI SDK

-keep class com.midtrans.sdk.ui.themes.** { *; }
-keep class com.midtrans.sdk.ui.thirdparty.** { *; }
-keep class com.midtrans.sdk.ui.models.** { *; }
-keep class com.midtrans.sdk.ui.utils.** { *; }
-keep class com.midtrans.sdk.ui.CustomSetting { *; }
-keep class com.midtrans.sdk.ui.MidtransUi { *; }
-keep class com.midtrans.sdk.ui.MidtransUi$Builder { *; }
-keep class com.midtrans.sdk.ui.MidtransUiCallback { *; }
-keep class com.midtrans.sdk.ui.constants.PaymentStatus { *; }
