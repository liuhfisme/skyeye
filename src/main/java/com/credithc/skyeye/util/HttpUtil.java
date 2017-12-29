package com.credithc.skyeye.util;


import com.google.common.collect.Lists;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by dongbin on 2016/11/30.
 * HTTP工具箱
 */
public class HttpUtil {
    private static final Logger logger = Logger.getLogger(HttpUtil.class);
    public static final String CONTENT_TYPE_JSON="application/json";
    private static final String DEF_CHARSET="UTF-8";
    private static final int TIME_OUT=20000;//单位ms

    /**
     * 执行GET请求
     *
     * @param url 请求的url。参数直接拼接在url后面
     * @return 返回请求响应的HTML文本
     * @throws Exception
     */
    public static String doGetReturnText(String url) throws Exception {
        String resultText = "";
        CloseableHttpClient httpClient = HttpClients.createDefault();
        CloseableHttpResponse response = null;
        try {
            response = httpClient.execute(new HttpGet(url));
            HttpEntity entity = response.getEntity();
            if (entity != null) {
                resultText = EntityUtils.toString(entity, DEF_CHARSET);
            }
        } catch (Exception e) {
            logger.error("doGetReturnText error:"+e.getMessage());
        } finally {
            if (response != null) {
                response.close();
            }
            httpClient.close();
        }
        return resultText;
    }

    /**
     * 执行GET请求
     *
     * @param url 请求的url。参数直接拼接在url后面
     * @return 返回请求响应的状态码，请求成功返回200
     * @throws Exception
     */
    public static int doGetReturnStatus(String url) throws Exception {
        int status = HttpStatus.SC_NOT_FOUND;
        CloseableHttpClient httpClient = HttpClients.createDefault();
        CloseableHttpResponse response = null;
        try {
            response = httpClient.execute(new HttpGet(url));
            status = response.getStatusLine().getStatusCode();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (response != null) {
                response.close();
            }
            httpClient.close();
        }
        return status;
    }

    /**
     * 执行一个HTTP POST请求，返回请求响应的HTML
     *
     * @param url    请求的URL地址
     * @param params 请求的查询参数,可以为null
     * @return  请求响应的HTML
     */
    public static String doPostReturnText(String url, Map<String, String> params) throws Exception {
        String resultText = "";
        CloseableHttpClient httpClient = HttpClients.createDefault();
        CloseableHttpResponse response = null;
        HttpPost httpPost;
        try {
            httpPost = new HttpPost(url);
            if (MapUtils.isNotEmpty(params)) {
                UrlEncodedFormEntity urlEncodedFormEntity= new UrlEncodedFormEntity(buildPostParams(params),DEF_CHARSET);
                httpPost.setEntity(urlEncodedFormEntity);
            }
            //设置超时时间
            RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(TIME_OUT).setConnectTimeout(TIME_OUT).build();
            httpPost.setConfig(requestConfig);

            response = httpClient.execute(httpPost);
            HttpEntity entity = response.getEntity();
            if (entity != null) {
                resultText = EntityUtils.toString(entity, DEF_CHARSET);
            }
        } catch (Exception e) {
            logger.error("doPostReturnText params error:",e);
        } finally {
            if (response != null) {
                response.close();
            }
            httpClient.close();
        }
        return resultText;
    }

    /**
     * 执行一个HTTP POST请求，返回请求响应的状态码
     *
     * @param url    请求的url地址
     * @param params 请求的查询参数,可以为null
     * @return  请求响应的状态码
     */
    public static int doPostReturnStatus(String url, Map<String, String> params) throws Exception {
        int status = HttpStatus.SC_NOT_FOUND;
        CloseableHttpClient httpClient = HttpClients.createDefault();
        CloseableHttpResponse response = null;
        try {
            HttpPost httpPost = new HttpPost(url);
            if (MapUtils.isNotEmpty(params)) {
                UrlEncodedFormEntity urlEncodedFormEntity=new UrlEncodedFormEntity(buildPostParams(params));
                urlEncodedFormEntity.setContentEncoding(DEF_CHARSET);
                httpPost.setEntity(urlEncodedFormEntity);
            }
            response = httpClient.execute(httpPost);
            status = response.getStatusLine().getStatusCode();
        } catch (Exception e) {
            logger.error("doPostReturnStatus params error:"+e.getMessage());
        } finally {
            if (response != null) {
                response.close();
            }
            httpClient.close();
        }
        return status;
    }

    /**
     * 组装post请求的参数
     * @param params map参数
     * @return list参数
     */
    private static List buildPostParams(Map params) {
        List<NameValuePair> nvps = Lists.newArrayList();
        Set<Map.Entry<String, String>> set = params.entrySet();
        for (Map.Entry<String, String> entry : set) {
            nvps.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
        }
        return nvps;
    }

    /**
     * 执行一个HTTP POST请求，返回请求响应的HTML
     *
     * @param url    请求的url
     * @param body 请求的body体
     * @return 请求响应的HTML
     */
    public static String doPostReturnText(String url, String body) throws Exception {
        return doPostReturnText(url,"application/x-www-form-urlencoded",body);
    }
    /**
     *执行一个HTTP POST请求，返回请求响应的HTML
     * @param url 请求的url
     * @param contentType 请求的内容类型
     * @param body 请求的body体
     * @return
     * @throws Exception
     */
    public static String doPostReturnText(String url, String contentType, Map headMap, String body) throws Exception {
        String resultText = null;
        CloseableHttpClient httpClient = HttpClients.createDefault();
        CloseableHttpResponse response = null;
        try {
            HttpPost httpPost = new HttpPost(url);
            if(headMap!=null){
                Iterator<Map.Entry<String,String>> iterator=headMap.entrySet().iterator();
                Map.Entry<String,String> entry;
                while(iterator.hasNext()){
                    entry =iterator.next();
                    httpPost.setHeader(entry.getKey(),entry.getValue());
                }
            }
            StringEntity reqEntity = new StringEntity(body, DEF_CHARSET);
            if(StringUtils.isNotBlank(contentType)){
                reqEntity.setContentType(contentType);
            }
            httpPost.setEntity(reqEntity);
            //设置超时时间
            RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(TIME_OUT).setConnectTimeout(TIME_OUT).build();
            httpPost.setConfig(requestConfig);
            
            response = httpClient.execute(httpPost);
            HttpEntity entity = response.getEntity();
            if (entity != null) {
                resultText = EntityUtils.toString(entity, DEF_CHARSET);
            }
        } catch (Exception e) {
            logger.error("doPostReturnText body error:"+e);
        } finally {
            if (response != null) {
                response.close();
            }
            httpClient.close();
        }
        return resultText;
    }
    /**
     *执行一个HTTP POST请求，返回请求响应的HTML
     * @param url 请求的url
     * @param contentType 请求的内容类型
     * @param body 请求的body体
     * @return
     * @throws Exception
     */
    public static String doPostReturnText(String url, String contentType, String body) throws Exception {
        return doPostReturnText(url,contentType,null,body);
    }
    /**
     * 执行一个HTTP POST请求，返回请求响应的状态码
     *
     * @param url    请求的url
     * @param body 请求的body体
     * @return 请求响应的状态码
     */
    public static int doPostReturnStatus(String url, String body) throws Exception {
        int status = HttpStatus.SC_NOT_FOUND;
        CloseableHttpClient httpClient = HttpClients.createDefault();
        CloseableHttpResponse response = null;
        try {
            HttpPost httpPost = new HttpPost(url);
            StringEntity reqEntity = new StringEntity(body, DEF_CHARSET);
            httpPost.setEntity(reqEntity);
            response = httpClient.execute(httpPost);
            status = response.getStatusLine().getStatusCode();
        } catch (Exception e) {
            logger.error("doPostReturnStatus body error:"+e.getMessage());
        } finally {
            if (response != null) {
                response.close();
            }
            httpClient.close();
        }
        return status;
    }
}
