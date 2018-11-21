### Core Flow Implementation Guide

#### Installation

Add Midtrans bintray repository in your `build.gradle` and put Midtrans SDK dependencies.

*Midtrans Bintray Repository*
```Groovy
repositories {
    jcenter()
    // Add the midtrans repository into the list of repositories
    maven { url "http://dl.bintray.com/pt-midtrans/maven" }
    maven { url "https://jitpack.io" }
}
```
And add Midtrans Corekit SDK
*Sandbox Dependencies*

```Groovy
implementation 'com.midtrans.corekit:$VERSION-SANDBOX'
```

*Production Dependencies*

```Groovy
implementation 'com.midtrans.corekit:$VERSION'
```

Note : 
- `SANDBOX` is testing environment that use for experimentation from the production environment.


#### Initialize SDK

This can be implemented in application or your main activity class or application class.

```Java
MidtransSDK
            .init(this,
            	  BuildConfig.CLIENT_KEY,
                  BuildConfig.BASE_URL)
            .setLogEnabled(true)
            .setEnvironment(Environment.SANDBOX)
            .buildSDK();
```

Note:

- `CONTEXT` is application or activity context and this is mandatory for Android SDK implementation.
- `CLIENT_KEY` is Midtrans Client Key (you can get from MAP) and this is mandatory.
- `BASE_URL` is merchant server base URL and this is mandatory.
- `Environment` is environment you want to use, if you not set it will be `SANDBOX` as default.
- `Logger` can set by putting value true or false for showing SDK log, this is optional and default value is false.
- For merchant server implementation please see [this page](https://github.com/veritrans/veritrans-android/wiki/Implementation-for-Merchant-Server).

To get SDK instance you must init the SDK first and you can use this:

```Java
MidtransSDK midtransSDK = MidtransSDK.getInstance();
```
SDK instance is a simple way to access and implement all public method from Midtrans Core SDK that already initialize before, so you only need once to initialize the SDK then you can only access it from instance, if you not initialize the SDK first instance will return null and runtime error.