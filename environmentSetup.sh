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

    fi
}