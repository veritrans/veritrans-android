package com.midtrans.raygun.network.http;

import com.midtrans.raygun.network.RaygunNetworkLogger;
import com.midtrans.raygun.network.RaygunNetworkUtils;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLConnection;
import java.security.Permission;
import java.util.List;
import java.util.Map;

public final class RaygunHttpUrlConnection extends HttpURLConnection {
    private URLConnection connectionInstance;

    public RaygunHttpUrlConnection(URLConnection connection) {
        super(connection.getURL());
        connectionInstance = connection;
        RaygunNetworkLogger.startNetworkCall(connectionInstance.getURL().toExternalForm(), System.currentTimeMillis());
    }

    public void connect() throws IOException {
        try {
            connectionInstance.connect();
        } catch (IOException e) {
            RaygunNetworkLogger.cancelNetworkCall(url.toExternalForm(), getRequestMethod(), System.currentTimeMillis(), e.getMessage());
            throw e;
        }
    }

    public void disconnect() {
        int statusCode = RaygunNetworkUtils.getStatusCode(connectionInstance);
        RaygunNetworkLogger.endNetworkCall(url.toExternalForm(), getRequestMethod(), System.currentTimeMillis(), statusCode);

        if ((connectionInstance instanceof HttpURLConnection)) {
            ((HttpURLConnection) connectionInstance).disconnect();
        }
    }

    public InputStream getInputStream() throws IOException {
        try {
            return connectionInstance.getInputStream();
        } catch (IOException e) {
            RaygunNetworkLogger.cancelNetworkCall(url.toExternalForm(), getRequestMethod(), System.currentTimeMillis(), e.getMessage());
            throw e;
        }
    }

    public OutputStream getOutputStream() throws IOException {
        try {
            return connectionInstance.getOutputStream();
        } catch (IOException e) {
            RaygunNetworkLogger.cancelNetworkCall(url.toExternalForm(), getRequestMethod(), System.currentTimeMillis(), e.getMessage());
            throw e;
        }
    }

    public boolean getAllowUserInteraction() {
        return connectionInstance.getAllowUserInteraction();
    }

    public void addRequestProperty(String field, String newValue) {
        connectionInstance.addRequestProperty(field, newValue);
    }

    public int getConnectTimeout() {
        return connectionInstance.getConnectTimeout();
    }

    public Object getContent() throws IOException {
        try {
            return connectionInstance.getContent();
        } catch (IOException e) {
            RaygunNetworkLogger.cancelNetworkCall(url.toExternalForm(), getRequestMethod(), System.currentTimeMillis(), e.getMessage());
            throw e;
        }
    }

    public Object getContent(Class[] types) throws IOException {
        try {
            return connectionInstance.getContent(types);
        } catch (IOException e) {
            RaygunNetworkLogger.cancelNetworkCall(url.toExternalForm(), getRequestMethod(), System.currentTimeMillis(), e.getMessage());
            throw e;
        }
    }

    public String getContentEncoding() {
        return connectionInstance.getContentEncoding();
    }

    public int getContentLength() {
        return connectionInstance.getContentLength();
    }

    public String getContentType() {
        return connectionInstance.getContentType();
    }

    public long getDate() {
        return connectionInstance.getDate();
    }

    public boolean getDefaultUseCaches() {
        return connectionInstance.getDefaultUseCaches();
    }

    public boolean getDoInput() {
        return connectionInstance.getDoInput();
    }

    public boolean getDoOutput() {
        return connectionInstance.getDoOutput();
    }

    public long getExpiration() {
        return connectionInstance.getExpiration();
    }

    public String getHeaderField(int pos) {
        return connectionInstance.getHeaderField(pos);
    }

    public String getHeaderField(String key) {
        return connectionInstance.getHeaderField(key);
    }

    public long getHeaderFieldDate(String field, long defaultValue) {
        return connectionInstance.getHeaderFieldDate(field, defaultValue);
    }

    public int getHeaderFieldInt(String field, int defaultValue) {
        return connectionInstance.getHeaderFieldInt(field, defaultValue);
    }

    public String getHeaderFieldKey(int posn) {
        return connectionInstance.getHeaderFieldKey(posn);
    }

    public Map<String, List<String>> getHeaderFields() {
        return connectionInstance.getHeaderFields();
    }

    public long getIfModifiedSince() {
        return connectionInstance.getIfModifiedSince();
    }

    public long getLastModified() {
        return connectionInstance.getLastModified();
    }

    public Permission getPermission() throws IOException {
        try {
            return connectionInstance.getPermission();
        } catch (IOException e) {
            RaygunNetworkLogger.cancelNetworkCall(url.toExternalForm(), getRequestMethod(), System.currentTimeMillis(), e.getMessage());
            throw e;
        }
    }

    public int getReadTimeout() {
        return connectionInstance.getReadTimeout();
    }

    public Map<String, List<String>> getRequestProperties() {
        return connectionInstance.getRequestProperties();
    }

    public String getRequestProperty(String field) {
        return connectionInstance.getRequestProperty(field);
    }

    public URL getURL() {
        return connectionInstance.getURL();
    }

    public boolean getUseCaches() {
        return connectionInstance.getUseCaches();
    }

    public void setAllowUserInteraction(boolean newValue) {
        connectionInstance.setAllowUserInteraction(newValue);
    }

    public void setConnectTimeout(int timeoutMillis) {
        connectionInstance.setConnectTimeout(timeoutMillis);
    }

    public void setDefaultUseCaches(boolean newValue) {
        connectionInstance.setDefaultUseCaches(newValue);
    }

    public void setDoInput(boolean newValue) {
        connectionInstance.setDoInput(newValue);
    }

    public void setDoOutput(boolean newValue) {
        connectionInstance.setDoOutput(newValue);
    }

    public void setIfModifiedSince(long newValue) {
        connectionInstance.setIfModifiedSince(newValue);
    }

    public void setReadTimeout(int timeoutMillis) {
        connectionInstance.setReadTimeout(timeoutMillis);
    }

    public void setRequestProperty(String field, String newValue) {
        connectionInstance.setRequestProperty(field, newValue);
    }

    public void setUseCaches(boolean newValue) {
        connectionInstance.setUseCaches(newValue);
    }

    public boolean usingProxy() {
        if ((connectionInstance instanceof HttpURLConnection)) {
            return ((HttpURLConnection) connectionInstance).usingProxy();
        }
        return false;
    }

    public InputStream getErrorStream() {
        if ((connectionInstance instanceof HttpURLConnection)) {
            return ((HttpURLConnection) connectionInstance).getErrorStream();
        }
        return null;
    }

    public boolean getInstanceFollowRedirects() {
        if ((connectionInstance instanceof HttpURLConnection)) {
            return ((HttpURLConnection) connectionInstance).getInstanceFollowRedirects();
        }
        return true;
    }

    public String getRequestMethod() {
        if ((connectionInstance instanceof HttpURLConnection)) {
            return ((HttpURLConnection) connectionInstance).getRequestMethod();
        }
        return "GET";
    }

    public int getResponseCode()
            throws IOException {
        if ((connectionInstance instanceof HttpURLConnection)) {
            return ((HttpURLConnection) connectionInstance).getResponseCode();
        }
        return -1;
    }

    public String getResponseMessage()
            throws IOException {
        if ((connectionInstance instanceof HttpURLConnection)) {
            return ((HttpURLConnection) connectionInstance).getResponseMessage();
        }
        return "";
    }

    public void setChunkedStreamingMode(int chunkLength) {
        if ((connectionInstance instanceof HttpURLConnection)) {
            ((HttpURLConnection) connectionInstance).setChunkedStreamingMode(chunkLength);
        }
    }

    public void setFixedLengthStreamingMode(int contentLength) {
        if ((connectionInstance instanceof HttpURLConnection)) {
            ((HttpURLConnection) connectionInstance).setFixedLengthStreamingMode(contentLength);
        }
    }

    public void setInstanceFollowRedirects(boolean followRedirects) {
        if ((connectionInstance instanceof HttpURLConnection)) {
            ((HttpURLConnection) connectionInstance).setInstanceFollowRedirects(followRedirects);
        }
    }

    public void setRequestMethod(String method)
            throws ProtocolException {
        if ((connectionInstance instanceof HttpURLConnection)) {
            ((HttpURLConnection) connectionInstance).setRequestMethod(method);
        }
    }
}
