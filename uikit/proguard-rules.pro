# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in /Users/chetan/Documents/android_sdk/sdk/tools/proguard/proguard-android.txt
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
-dontpreverify
#-repackageclasses ''
-allowaccessmodification
-optimizations !code/simplification/arithmetic
-keepattributes *Annotation*

## GreenRobot EventBus specific rules ##
# https://github.com/greenrobot/EventBus/blob/master/HOWTO.md#proguard-configuration

-keepclassmembers class ** {
    public void onEvent*(***);
    public void on*(***);
}

-keepclassmembers class ** {
    @org.greenrobot.eventbus.Subscribe <methods>;
}
-keep enum org.greenrobot.eventbus.ThreadMode { *; }

# Only required if you use AsyncExecutor
-keepclassmembers class * extends org.greenrobot.eventbus.util.ThrowableFailureEvent {
    public <init>(java.lang.Throwable);
}

# Don't warn for missing support classes
-dontwarn org.greenrobot.eventbus.util.*$Support
-dontwarn org.greenrobot.eventbus.util.*$SupportManagerFragment

-keepnames class * implements android.os.Parcelable {
    public static final ** CREATOR;
    public static final android.os.Parcelable$Creator *;
}
-keep interface org.parceler.Parcel
-keep @org.parceler.Parcel class * { *; }
-keep class **$$Parcelable { *; }

# Keep the BuildConfig
-keep class com.midtrans.sdk.corekit.BuildConfig { *; }
-keep class com.midtrans.sdk.uikit.BuildConfig { *; }

# Keep the support library
-keep class android.support.v4.** { *; }
-keep interface android.support.v4.** { *; }

# http://stackoverflow.com/questions/29679177/cardview-shadow-not-appearing-in-lollipop-after-obfuscate-with-proguard/29698051
-keep class android.support.v7.widget.RoundRectDrawable { *; }

# keep public class android.support.v7.widget.** { *; }
-keep public class android.support.v7.internal.widget.** { *; }
-keep public class android.support.v7.internal.view.menu.** { *; }

-keep public class * extends android.support.v4.view.ActionProvider {
    public <init>(android.content.Context);
}

-keepclassmembers class * extends android.content.Context {
   public void *(android.view.View);
   public void *(android.view.MenuItem);
}

-dontwarn android.support.design.**
-keep class android.support.design.** { *; }
-keep interface android.support.design.** { *; }
-keep public class android.support.design.R$* { *; }

# Retrofit 1.X

-keep class com.squareup.okhttp.** { *; }
-keep class retrofit.** { *; }
-keep interface com.squareup.okhttp.** { *; }

-dontwarn com.squareup.okhttp.**
-dontwarn okio.**
-dontwarn retrofit.**
-dontwarn rx.**

-keepclasseswithmembers class * {
    @retrofit.http.* <methods>;
}

# If in your rest service interface you use methods with Callback argument.
-keepattributes Exceptions

# If your rest service methods throw custom exceptions, because you've defined an ErrorHandler.
-keepattributes Signature

# Also you must note that if you are using GSON for conversion from JSON to POJO representation, you must ignore those POJO classes from being obfuscated.
# Here include the POJO's that have you have created for mapping JSON response to POJO for example.
#-keep class com.midtrans.sdk.activities.**{ *;}
-keep class com.midtrans.sdk.uikit.abstracts.**{*;}
-keep class com.midtrans.sdk.uikit.views.**{*;}
-keep class com.midtrans.sdk.uikit.fragments.**{*;}
-keep class com.midtrans.sdk.corekit.models.** { *; }
-keep class com.midtrans.sdk.uikit.models.** { *; }
-keep class com.midtrans.sdk.uikit.scancard.** { *; }
-keep class com.midtrans.sdk.corekit.eventbus.**{*;}
-keep class com.midtrans.sdk.corekit.analytics.MixpanelEvent{*;}
-keep class com.midtrans.sdk.corekit.analytics.MixpanelProperties{*;}
-keep class com.midtrans.sdk.corekit.core.MidtransSDK {*;}
-keep class com.midtrans.sdk.corekit.core.TransactionRequest {*;}
-keep class com.midtrans.sdk.corekit.core.LocalDataHandler {*;}
-keep class com.midtrans.sdk.corekit.core.Logger {*;}
-keep class com.midtrans.sdk.corekit.core.UIKitCustomSetting {*;}
-keep class com.midtrans.sdk.corekit.core.Constants {*;}
-keep class com.midtrans.sdk.corekit.core.SdkUtil {*;}
-keep class com.midtrans.sdk.corekit.core.PaymentType {*;}
-keep class com.midtrans.sdk.corekit.core.SdkCoreFlowBuilder {*;}
-keep class com.midtrans.sdk.corekit.core.BaseSdkBuilder {*;}
-keep class com.midtrans.sdk.corekit.core.ISdkFlow {*;}
-keep class com.midtrans.sdk.corekit.core.IScanner {*;}
-keep class com.midtrans.sdk.corekit.utilities.** {*;}
-keep class com.midtrans.sdk.corekit.core.themes.BaseColorTheme {*;}
-keep class com.midtrans.sdk.corekit.core.themes.ColorTheme {*;}
-keep class com.midtrans.sdk.corekit.core.themes.CustomColorTheme {*;}
-keep class com.midtrans.sdk.uikit.utilities.SdkUiFlowUtil {*;}
-keep class com.midtrans.sdk.uikit.SdkUIFlowBuilder {*;}
-keep class com.midtrans.sdk.uikit.uikit {*;}
-keep class com.midtrans.sdk.uikit.PaymentMethods {*;}
-keep class com.midtrans.sdk.uikit.scancard.ExternalScanner{*;}
-keep class com.midtrans.sdk.corekit.core.PaymentMethods{*;}
-keep class com.midtrans.sdk.corekit.callback.** { *; }


# RxJava 0.21

-keep class rx.schedulers.Schedulers {
    public static <methods>;
}
-keep class rx.schedulers.ImmediateScheduler {
    public <methods>;
}
-keep class rx.schedulers.TestScheduler {
    public <methods>;
}
-keep class rx.schedulers.Schedulers {
    public static ** test();
}

## GSON 2.2.4 specific rules ##

# For using GSON @Expose annotation
-keepattributes *Annotation*

-keepattributes EnclosingMethod

# Gson specific classes
-keep class sun.misc.Unsafe { *; }
-keep class com.google.gson.stream.** { *; }

# Android
-keep public class * extends android.app.Activity
-keep public class * extends android.support.v7.app.AppCompatActivity
-keep public class * extends android.app.Application
-keep public class * extends android.app.Service
-keep public class * extends android.content.BroadcastReceiver
-keep public class * extends android.content.ContentProvider

-keep public class * extends android.view.View {
    public <init>(android.content.Context);
    public <init>(android.content.Context, android.util.AttributeSet);
    public <init>(android.content.Context, android.util.AttributeSet, int);
    public void set*(...);
}

# Also keep - Enumerations. Keep the special static
# methods that are required in enumeration classes.
-keepclassmembers enum  * {
    public static **[] values();
    public static ** valueOf(java.lang.String);
}

# Flurry
-keep class com.flurry.** { *; }
-dontwarn com.flurry.**
-keepattributes *Annotation*,EnclosingMethod
-keepclasseswithmembers class * {
    public <init>(android.content.Context, android.util.AttributeSet);
}

-keepclasseswithmembers class * {
    public <init>(android.content.Context, android.util.AttributeSet, int);
}

# Google Play Services library
-keep class * extends java.util.ListResourceBundle {
protected Object[][] getContents(); }

-keep public class com.google.android.gms.common.internal.safeparcel.SafeParcelable {
public static final *** NULL; }

-keepnames @com.google.android.gms.common.annotation.KeepName class *
-keepclassmembernames class * {
@com.google.android.gms.common.annotation.KeepName *; }

-keepattributes InnerClasses

-keep class **.R
-keep class **.R$* {
   <fields>;
}

-keepnames class * implements java.io.Serializable
-keepclassmembers class * implements java.io.Serializable {
    static final long serialVersionUID;
    private static final java.io.ObjectStreamField[] serialPersistentFields;
    !static !transient <fields>;
    private void writeObject(java.io.ObjectOutputStream);
    private void readObject(java.io.ObjectInputStream);
    java.lang.Object writeReplace();
    java.lang.Object readResolve();
}
-keepclasseswithmembernames class * {
    native <methods>;
}
-keepclassmembers public class * extends android.view.View {
   void set*(***);
   *** get*();
}

-keepclassmembers class * extends android.app.Activity {
   public void *(android.view.View);
}
-keepclassmembers class * extends android.support.v7.app.AppCompatActivity{
    public void *(android.view.View);
}

-keepclassmembers public class * extends android.support.v4.app.Fragment{
     public *** on*(...);
     public *** *Dialog();
     public void *(android.view.View);
}

-keepclassmembers class fqcn.of.javascript.interface.for.webview {
   public *;
}

-keepclassmembers class * {
    @android.webkit.JavascriptInterface <methods>;
}

-keep class com.viewpagerindicator.** {*;}

-keep class com.midtrans.raygun.** { *; }
-keepattributes Exceptions, Signature, InnerClasses, SourceFile, LineNumberTable
-renamesourcefileattribute SourceFile

-keep class com.tozny.crypto.android.AesCbcWithIntegrity$PrngFixes$* { *; }

-dontwarn com.koushikdutta.ion.conscrypt.**

# Platform calls Class.forName on types which do not exist on Android to determine platform.
-dontnote retrofit2.Platform
# Platform used when running on Java 8 VMs. Will not be used at runtime.
-dontwarn retrofit2.Platform$Java8
# Retain generic type information for use by reflection by converters and adapters.
-keepattributes Signature
# Retain declared checked exceptions for use by a Proxy instance.
-keepattributes Exceptions
-dontwarn javax.annotation.**