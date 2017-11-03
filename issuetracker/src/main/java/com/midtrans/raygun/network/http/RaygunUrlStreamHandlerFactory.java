package com.midtrans.raygun.network.http;

import android.os.Build;

import com.midtrans.raygun.RaygunLogger;

import java.net.URLStreamHandler;
import java.net.URLStreamHandlerFactory;
import java.util.HashMap;


public class RaygunUrlStreamHandlerFactory implements URLStreamHandlerFactory {
    private HashMap<String, URLStreamHandler> handlers;

    public RaygunUrlStreamHandlerFactory() {
        createStreamHandlers();
    }

    private void createStreamHandlers() {
        handlers = new HashMap<String, URLStreamHandler>();

        URLStreamHandler httpHandler = findHandler("http");
        if (httpHandler != null) {
            RaygunHttpUrlStreamHandler raygunHttpHandler = new RaygunHttpUrlStreamHandler(httpHandler);
            handlers.put(raygunHttpHandler.getProtocol(), raygunHttpHandler);
        }

        URLStreamHandler httpsHandler = findHandler("https");
        if (httpsHandler != null) {
            RaygunHttpsUrlStreamHandler raygunHttpsHandler = new RaygunHttpsUrlStreamHandler(httpsHandler);
            handlers.put(raygunHttpsHandler.getProtocol(), raygunHttpsHandler);
        }
    }

    private URLStreamHandler findHandler(String protocol) {
        URLStreamHandler streamHandler = null;
        String packageList = System.getProperty("java.protocol.handler.pkgs");
        ClassLoader contextClassLoader = Thread.currentThread().getContextClassLoader();

        if (packageList != null && contextClassLoader != null) {
            for (String packageName : packageList.split("\\|")) {
                String className = packageName + "." + protocol + ".Handler";
                try {
                    Class<?> c = contextClassLoader.loadClass(className);
                    streamHandler = (URLStreamHandler) c.newInstance();

                    if (streamHandler != null) {
                        return streamHandler;
                    }
                } catch (IllegalAccessException ignore) {
                } catch (InstantiationException ignore) {
                } catch (ClassNotFoundException ignore) {
                }
            }
        }

        if (Build.VERSION.SDK_INT >= 19) {
            if (protocol.equals("http")) {
                streamHandler = createStreamHandler("com.android.okhttp.HttpHandler");
            } else if (protocol.equals("https")) {
                streamHandler = createStreamHandler("com.android.okhttp.HttpsHandler");
            }
        } else {
            if (protocol.equals("http")) {
                streamHandler = createStreamHandler("libcore.net.http.HttpHandler");
            } else if (protocol.equals("https")) {
                streamHandler = createStreamHandler("libcore.net.http.HttpsHandler");
            }
        }

        return streamHandler;
    }

    private URLStreamHandler createStreamHandler(String className) {
        try {
            return (URLStreamHandler) Class.forName(className).newInstance();
        } catch (Exception e) {
            RaygunLogger.e("Exception occurred in createStreamHandler: " + e.getMessage());
        }
        return null;
    }

    public URLStreamHandler createURLStreamHandler(String protocol) {
        return handlers.get(protocol);
    }
}