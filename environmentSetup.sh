function copyEnvVarsToGradleProperties {
    GRADLE_PROPERTIES=$HOME"/.gradle/gradle.properties"
    export GRADLE_PROPERTIES
    echo "Gradle Properties should exist at $GRADLE_PROPERTIES"

    if [ ! -f "$GRADLE_PROPERTIES" ]; then
        echo "Gradle Properties does not exist"

        echo "Creating Gradle Properties file..."
        touch $GRADLE_PROPERTIES

        echo "Writing MIXPANEL_TOKEN to gradle.properties..."
        echo "MIXPANEL_TOKEN_SANDBOX=$MIXPANEL_TOKEN_SANDBOX" >> $GRADLE_PROPERTIES
        echo "MIXPANEL_TOKEN_PRODUCTION=$MIXPANEL_TOKEN_PRODUCTION" >> $GRADLE_PROPERTIES
        echo "MERCHANT_BASE_URL_SANDBOX=$MERCHANT_BASE_URL_SANDBOX" >> $GRADLE_PROPERTIES
        echo "MERCHANT_CLIENT_KEY_SANDBOX=$MERCHANT_CLIENT_KEY_SANDBOX" >> $GRADLE_PROPERTIES
        echo "MERCHANT_BASE_URL_PRODUCTION=$MERCHANT_BASE_URL_PRODUCTION" >> $GRADLE_PROPERTIES
        echo "MERCHANT_CLIENT_KEY_PRODUCTION=$MERCHANT_CLIENT_KEY_PRODUCTION" >> $GRADLE_PROPERTIES
        echo "MERCHANT_BASE_URL_STAGING=$MERCHANT_BASE_URL_STAGING" >> $GRADLE_PROPERTIES
        echo "MERCHANT_CLIENT_KEY_STAGING=$MERCHANT_CLIENT_KEY_STAGING" >> $GRADLE_PROPERTIES
        echo "KEYSTORE=$KEYSTORE" >> $GRADLE_PROPERTIES
        echo "KEYSTORE_PASSWORD=$KEYSTORE_PASSWORD" >> $GRADLE_PROPERTIES
        echo "KEY_ALIAS=$KEY_ALIAS" >> $GRADLE_PROPERTIES
        echo "KEY_PASSWORD=$KEY_PASSWORD" >> $GRADLE_PROPERTIES
        echo "ISSUE_TRACKER_API_KEY=$ISSUE_TRACKER_API_KEY" >> $GRADLE_PROPERTIES
        echo "PREFERENCE_PASSWORD=$PREFERENCE_PASSWORD" >> $GRADLE_PROPERTIES

    else
        > $GRADLE_PROPERTIES
        echo "File exist. Clear previous data."
        echo "Writing MIXPANEL_TOKEN to gradle.properties..."
                echo "MIXPANEL_TOKEN_SANDBOX=$MIXPANEL_TOKEN_SANDBOX" >> $GRADLE_PROPERTIES
                echo "MIXPANEL_TOKEN_PRODUCTION=$MIXPANEL_TOKEN_PRODUCTION" >> $GRADLE_PROPERTIES
                echo "MERCHANT_BASE_URL_SANDBOX=$MERCHANT_BASE_URL_SANDBOX" >> $GRADLE_PROPERTIES
                echo "MERCHANT_CLIENT_KEY_SANDBOX=$MERCHANT_CLIENT_KEY_SANDBOX" >> $GRADLE_PROPERTIES
                echo "MERCHANT_BASE_URL_PRODUCTION=$MERCHANT_BASE_URL_PRODUCTION" >> $GRADLE_PROPERTIES
                echo "MERCHANT_CLIENT_KEY_PRODUCTION=$MERCHANT_CLIENT_KEY_PRODUCTION" >> $GRADLE_PROPERTIES
                echo "MERCHANT_BASE_URL_STAGING=$MERCHANT_BASE_URL_STAGING" >> $GRADLE_PROPERTIES
                echo "MERCHANT_CLIENT_KEY_STAGING=$MERCHANT_CLIENT_KEY_STAGING" >> $GRADLE_PROPERTIES
                echo "KEYSTORE=$KEYSTORE" >> $GRADLE_PROPERTIES
                echo "KEYSTORE_PASSWORD=$KEYSTORE_PASSWORD" >> $GRADLE_PROPERTIES
                echo "KEY_ALIAS=$KEY_ALIAS" >> $GRADLE_PROPERTIES
                echo "KEY_PASSWORD=$KEY_PASSWORD" >> $GRADLE_PROPERTIES
                echo "ISSUE_TRACKER_API_KEY=$ISSUE_TRACKER_API_KEY" >> $GRADLE_PROPERTIES
                echo "PREFERENCE_PASSWORD=$PREFERENCE_PASSWORD" >> $GRADLE_PROPERTIES
    fi
}

function setGit {
    git config --global user.name $GITHUB_NAME
    git config --global user.email $GITHUB_EMAIL
}