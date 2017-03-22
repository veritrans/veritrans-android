function downloadKeystore {
    # use curl to download a keystore from $KEYSTORE_URI, if set,
    # to the path/filename set in $KEYSTORE_PATH.
    if [[ $KEYSTORE_PATH && ${KEYSTORE_PATH} && $KEYSTORE_URI && ${KEYSTORE_URI} ]]
    then
        echo "Keystore detected - downloading..."
        # we're using curl instead of wget because it will not
        # expose the sensitive uri in the build logs:
        curl -L -o ${KEYSTORE_PATH} ${KEYSTORE_URI}
    else
        echo "Keystore uri not set.  .APK artifact will not be signed."
    fi
}

function downloadFabricSecret {
        # use curl to download a keystore from $FABRIC_URI, if set,
        # to the path/filename set in $FABRIC_PATH
        if [[ FABRIC_URI && ${FABRIC_URI} && FABRIC_PATH && ${FABRIC_PATH} ]]
        then
            echo "Keystore detected - downloading..."
            # we're using curl instead of wget because it will not
            # expose the sensitive uri in the build logs:
            curl -L -o ${FABRIC_PATH} ${FABRIC_URI}
        else
            echo "Fabric uri not set. Cannot build APK"
        fi
}