## Veritrans Android SDK


[ ![Download](https://api.bintray.com/packages/pt-midtrans/maven/corekit/images/download.svg) ](https://bintray.com/pt-midtrans/maven/corekit/_latestVersion)
[![CircleCI](https://circleci.com/gh/veritrans/veritrans-android/tree/master.svg?style=svg)](https://circleci.com/gh/veritrans/veritrans-android/tree/master)


### Overview of Payments and Mobile SDK
 To see a overview of Veritrans SDK read [here](https://mobile-docs.midtrans.com/#getting-started)
 
### Setting up Android SDK

### SDK Installation

To see how to install this SDK please read [this wiki](https://mobile-docs.midtrans.com/#installation).


## Implementation

There are two implementation modes on this SDK.
- **UI Kit** To see the implementation guide please read [this wiki](https://mobile-docs.midtrans.com/#prepare-transaction-details).

- **Core Kit** To see the implementation guide please read [this wiki](https://github.com/veritrans/veritrans-android/wiki/Core-Flow).

## Sample/ Demo App
- To build the sdk repository, need to create file `fabric.properties` inside the `demo` module folder and then define  `apiKey=fabric_api_key` and `apiSecret=fabric_api_secret`. or if you doesn't have fabric credentials just remove  `apply plugin: 'io.fabric'` in file `build.gradle` of `demo` folder.
- To see the sample app (contains UI flow), you can download from from  [Release Section](https://github.com/veritrans/veritrans-android/releases) or see [Release Section](https://github.com/veritrans/veritrans-android/releases)`sdk-example` sub-module to see the code. Or simply download it from Google Play Store by clicking the banner below.

<a href='https://play.google.com/store/apps/details?id=com.midtrans.sdk.demo.development'><img alt='Get it on Google Play' src='https://play.google.com/intl/en_us/badges/images/generic/en_badge_web_generic.png' height='80'/></a>

## Minimum Support OS
**Android 4.0 Ice Cream Sandwitch API Level 14**

# Supports
***Sorry, we no longer answer questions from repository issues. If you need any assistance, please contact***  support@midtrans.com

# License
```
Copyright 2019 PT Midtrans
Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), 
to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense,
and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER 
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS
IN THE SOFTWARE.
```