package com.melon.rest;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpParams;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;

import android.util.Log;





public class RestClientBase {

    static final String TAG = "RestClientBase";

    protected ObjectMapper mapper;
    protected HttpClient client;
    protected HttpHost host;

    {
        mapper = new ObjectMapper();
        client = new DefaultHttpClient();
        ClientConnectionManager mgr = client.getConnectionManager();
        HttpParams params = client.getParams();
        client = new DefaultHttpClient(new ThreadSafeClientConnManager(params,
                mgr.getSchemeRegistry()), params);
    }

 
    public RestClientBase(String host, int port) {
        this.host = new HttpHost(host, port);
    }

    public <T> T get(String uri, Class<?> T) {
        try {
            HttpGet getRequest = new HttpGet(uri);
            HttpResponse httpResp = client.execute(host, getRequest);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            httpResp.getEntity().writeTo(baos);
            String resp = new String(baos.toByteArray());
            Log.d(TAG, "Response: " + resp);
            @SuppressWarnings("unchecked")
            T res = (T) mapper.readValue(resp, T);
            return res;
        } catch (Exception e) {
            Log.e(TAG, "Failed to execute GET to server " + host.getHostName() + ":" + host.getPort(), e);
            return null;
        } finally {
            client.getConnectionManager().closeExpiredConnections();
        }
    }

    public byte[] get(String uri, Map<String, String> params) {
        try {
            if (params != null) {
                StringBuilder sb = new StringBuilder();
                sb.append("?");
                for (String key : params.keySet()) {
                    sb.append(key).append("=").append(params.get(key)).append(
                            "&");
                }
                uri += sb.toString();
            }
            HttpGet getRequest = new HttpGet(uri);
            HttpResponse httpResp = client.execute(host, getRequest);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            httpResp.getEntity().writeTo(baos);
            baos.flush();
            baos.close();
            return baos.toByteArray();
        } catch (Exception e) {
            Log.e(TAG, "Failed to execute GET to server " + host.getHostName() + ":" + host.getPort(), e);
            return null;
        } finally {
            client.getConnectionManager().closeExpiredConnections();
        }
    }

    @SuppressWarnings("unused")
    private String getString(String uri, Map<String, Object> params) {
        try {
            HttpGet getRequest = new HttpGet(uri);
            if (params != null) {
                HttpParams httpParams = new BasicHttpParams();
                for (String key : params.keySet()) {
                    httpParams.setParameter(key, params.get(key));
                }
                getRequest.setParams(httpParams);
            }
            HttpResponse httpResp = client.execute(host, getRequest);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            httpResp.getEntity().writeTo(baos);
            String resp = new String(baos.toByteArray());
            return resp;
        } catch (Exception e) {
            Log.e(TAG, "Failed to execute GET String to server " + host.getHostName() + ":" + host.getPort(), e);
            return null;
        } finally {
            client.getConnectionManager().closeExpiredConnections();
        }
    }

    public <T> T post(String uri, TypeReference<T> tref)
            throws Exception {
        return post(uri, tref, null);
    }

    public <T> T post(String uri, TypeReference<T> tref,
            Map<String, String> params) throws Exception {
        try {
            HttpPost postRequest = new HttpPost(uri);
            /*
             * Set HTTP Form parameters if any
             */
            if (params != null) {
                List<NameValuePair> formparams = new ArrayList<NameValuePair>();
                for (String key : params.keySet()) {
                    formparams.add(new BasicNameValuePair(key, params.get(key)));
                }
                UrlEncodedFormEntity entity = new UrlEncodedFormEntity(
                        formparams, "UTF-8");
                postRequest.setEntity(entity);
            }
            
            /*
             * Execute POST request
             */
            Log.i(TAG, "postRequest=" + postRequest.getURI().getPath());
            HttpResponse httpResp = client.execute(host, postRequest);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            httpResp.getEntity().writeTo(baos);
            String resp = new String(baos.toByteArray());
            try {
            	Log.i(TAG, "Response: " + resp);
                T res = mapper.readValue(resp, tref);
                return res;
            } catch (JsonMappingException e) {
            	e.printStackTrace();
            	Exception se = mapper.readValue(resp,
                        new TypeReference<Exception>() {
                        });
                throw se;
            }
        } catch (UnsupportedEncodingException e) {
            Log.e(TAG, "Failed to execute POST String to server " + host.getHostName() + ":" + host.getPort(), e);
            throw new Exception("Failed to execute POST to server " + host.getHostName() + ":" + host.getPort(), e);
        } catch (ClientProtocolException e) {
            Log.e(TAG, "Failed to execute POST String to server " + host.getHostName() + ":" + host.getPort(), e);
            throw new Exception("Failed to execute POST to server " + host.getHostName() + ":" + host.getPort(), e);
        } catch (IOException e) {
            Log.e(TAG, "Failed to execute POST String to server " + host.getHostName() + ":" + host.getPort(), e);
            throw new Exception("Failed to execute POST to server " + host.getHostName() + ":" + host.getPort(), e);
        } finally {
            client.getConnectionManager().closeExpiredConnections();
        }
    }

    public String post(String uri, Map<String, String> params)
            throws Exception {
        try {
            HttpPost postRequest = new HttpPost(uri);
            /*
             * Set HTTP Form parameters if any
             */
            if (params != null) {
                List<NameValuePair> formparams = new ArrayList<NameValuePair>();
                for (String key : params.keySet()) {
                    formparams.add(new BasicNameValuePair(key, params.get(key)));
                }
                UrlEncodedFormEntity entity = new UrlEncodedFormEntity(
                        formparams, "UTF-8");
                postRequest.setEntity(entity);
            }
            /*
             * Execute POST request
             */
            HttpResponse httpResp = client.execute(host, postRequest);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            httpResp.getEntity().writeTo(baos);
            String resp = new String(baos.toByteArray());
            return resp;
        } catch (UnsupportedEncodingException e) {
            Log.e(TAG, "Failed to execute POST String to server " + host.getHostName() + ":" + host.getPort(), e);
            throw new Exception("Failed to execute POST to server " + host.getHostName() + ":" + host.getPort(), e);
        } catch (ClientProtocolException e) {
            Log.e(TAG, "Failed to execute POST String to server " + host.getHostName() + ":" + host.getPort(), e);
            throw new Exception("Failed to execute POST to server " + host.getHostName() + ":" + host.getPort(), e);
        } catch (IOException e) {
            Log.e(TAG, "Failed to execute POST String to server " + host.getHostName() + ":" + host.getPort(), e);
            throw new Exception("Failed to execute POST to server " + host.getHostName() + ":" + host.getPort(), e);
        } finally {
            client.getConnectionManager().closeExpiredConnections();
        }
    }

}