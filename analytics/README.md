# Midtrans Analytics Library

This library will be used as analytics tracker library for Midtrans UI SDK.

## How to Install

Add this dependency.

```groovy
compile 'com.midtrans.sdk:analytics:2.0.0'
```

## Initialise Analytics SDK

```java
new MidtransAnalytics.Builder()
    .setLogEnabled(true|false)
    .setEnvironment(AnalyticsEnvironment.SANDBOX|PRODUCTION)
    .setSdkVersion(SDK_VERSION)
    .setMerchantName(MERCHANT_NAME)
    .setDeviceId(DEVICE_SPECIFIC_ID)
    .setDeviceType(MidtransAnalytics.DEVICE_TYPE_TABLET|DEVICE_TYPE_PHONE)
    .build();
```

## Track Event 

### Track General Event

```java
MidtransAnalytics.getInstance().trackEvent(DISTINCT_ID, EVENT_NAME);
```

### Track General Event with Response Time

```java
MidtransAnalytics.getInstance().trackEvent(DISTINCT_ID, EVENT_NAME, RESPONSE_TIME);
```

### Track Credit Card Event with Card Mode

```java
MidtransAnalytics.getInstance().trackEvent(DISTINCT_ID, EVENT_NAME, CARD_PAYMENT_MODE);
```