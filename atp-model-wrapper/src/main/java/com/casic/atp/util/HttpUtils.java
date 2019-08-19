package com.casic.atp.util;

import com.casic.atp.model.ATPEnvironment;
import org.apache.http.HttpEntity;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;

import javax.net.ssl.*;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Map;


/**
 * HttpUtils帮助类
 * User: PunkLin林克澎
 * Email: lentr@sina.cn
 * Date: 2017-04-04
 */
public class HttpUtils {
    private static Charset charset=Charset.defaultCharset();
    private static final String TAG = "HttpUtils";
    //链接超时
    private static final int mReadTimeOut = 1000 * 10; // 10秒
    //链接超时
    private static final int mConnectTimeOut = 1000 * 5; // 5秒
    private static final String CHAR_SET = charset.displayName();


    private static final int mRetry = 2; // 默认尝试访问次数

    /**
     * 处理访问字符串处理
     * @param params
     * @return
     * @throws UnsupportedEncodingException
     */
    private static String buildParams(Map<String,? extends Object> params) throws UnsupportedEncodingException{
        if(params ==null || params.isEmpty()){
            return null;
        }
        StringBuilder builder = new StringBuilder();
        for (Map.Entry<String, ? extends Object> entry : params.entrySet()) {
            if (entry.getKey() != null && entry.getValue() != null)
                builder.append(entry.getKey().trim()).append("=")
                        .append(URLEncoder.encode(entry.getValue().toString(), CHAR_SET)).append("&");
        }
        if(builder.charAt(builder.length()-1)=='&'){
            builder.deleteCharAt(builder.length()-1);
        }
        return builder.toString();
    }


    /**
     * 无参数的Get访问
     * @param url
     * @return
     * @throws Exception
     */
    public static String get(String url) throws Exception {
        return get(url, null);
    }

    /**
     * 有参数的Get 访问
     * @param url
     * @param params
     * @return
     * @throws Exception
     */
    public static String get(String url, Map<String, ? extends Object> params) throws Exception {
        return get(url, params, null);
    }

    /**
     * 含有报文头的Get请求
     * @param url
     * @param params
     * @param headers
     * @return
     * @throws Exception
     */
    public static String get(String url, Map<String, ? extends Object> params, Map<String, String> headers) throws Exception {
        if (url == null || url.trim().length() == 0) {
            throw new Exception(TAG + ": url is null or empty!");
        }

        if (params != null && params.size() > 0) {
            if (!url.contains("?")) {
                url += "?";
            }
            if (url.charAt(url.length() - 1) != '?') {
                url += "&";
            }
            url += buildParams(params);
        }

        return tryToGet(url, headers);
    }

    private static String tryToGet(String url,Map<String,String> headers) throws Exception{
        int tryTime = 0;
        Exception ex = null;
        while (tryTime < mRetry) {
            try {
                return doGet(url, headers);
            } catch (Exception e) {
                if (e != null)
                    ex = e;
                tryTime++;
            }
        }
        if (ex != null)
            throw ex;
        else
            throw new Exception("未知网络错误 ");
    }

    /**
     * Get 请求实现方法 核心
     * @param strUrl
     * @param headers
     * @return
     * @throws Exception
     */
    private static String doGet(String strUrl, Map<String, String> headers) throws Exception {
        HttpURLConnection connection = null;
        InputStream stream = null;
        try {

            connection = getConnection(strUrl);
            configConnection(connection);
            if (headers != null && headers.size() > 0) {
                for (Map.Entry<String, String> entry : headers.entrySet()) {
                    connection.setRequestProperty(entry.getKey(), entry.getValue());
                }
            }

            connection.setInstanceFollowRedirects(true);
            connection.connect();

            stream = connection.getInputStream();
            ByteArrayOutputStream obs = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024];
            for (int len = 0; (len = stream.read(buffer)) > 0;) {
                obs.write(buffer, 0, len);
            }
            obs.flush();
            obs.close();
            stream.close();

            return new String(obs.toByteArray());
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
            if (stream != null) {
                stream.close();
            }
        }
    }

    /**
     * Post 请求，无参数的请求
     * @param url
     * @return
     * @throws Exception
     */
    public static String post(String url) throws Exception {
        return post(url, null);
    }

    /**
     * Post 请求，带参数的请求。
     * @param url
     * @param params
     * @return
     * @throws Exception
     */
    public static String post(String url, Map<String, ? extends Object> params) throws Exception {
        return post(url, params, null);
    }

    /**
     * Post 请求，带参数的请求，请求报文的
     * @param url
     * @param params
     * @return
     * @throws Exception
     */
    public static String post(String url, Map<String, ? extends Object> params, Map<String, String> headers)
            throws Exception {
        if (url == null || url.trim().length() == 0) {
            throw new Exception(TAG + ":url is null or empty!");
        }

        if (params != null && params.size() > 0) {
            return tryToPost(url, buildParams(params), headers);
        } else {
            return tryToPost(url, null, headers);
        }
    }

    public static String post(String url, String content, Map<String, String> headers) throws Exception {
        return tryToPost(url, content, headers);
    }


    private static String tryToPost(String url, String postContent, Map<String, String> headers) throws Exception {
        int tryTime = 0;
        Exception ex = null;
        while (tryTime < mRetry) {
            try {
                return doPost(url, postContent, headers);
            } catch (Exception e) {
                if (e != null)
                    ex = e;
                tryTime++;
            }
        }
        if (ex != null)
            throw ex;
        else
            throw new Exception("未知网络错误 ");
    }


    private static String doPost(String strUrl, String postContent, Map<String, String> headers) throws Exception {
        HttpURLConnection connection = null;
        InputStream stream = null;
        try {
            connection = getConnection(strUrl);
            configConnection(connection);
            if (headers != null && headers.size() > 0) {
                for (Map.Entry<String, String> entry : headers.entrySet()) {
                    connection.setRequestProperty(entry.getKey(), entry.getValue());
                }
            }
            connection.setRequestMethod("POST");
            connection.setDoOutput(true);

            if (null != postContent && !"".equals(postContent)) {
                DataOutputStream dos = new DataOutputStream(connection.getOutputStream());
                dos.write(postContent.getBytes(CHAR_SET));
                dos.flush();
                dos.close();
            }
            stream = connection.getInputStream();
            ByteArrayOutputStream obs = new ByteArrayOutputStream();

            byte[] buffer = new byte[1024];
            for (int len = 0; (len = stream.read(buffer)) > 0;) {
                obs.write(buffer, 0, len);
            }
            obs.flush();
            obs.close();
            return new String(obs.toByteArray());
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
            if (stream != null) {
                stream.close();
            }
        }
    }



    private static void configConnection(HttpURLConnection connection){
        if(connection==null){
            return;
        }

        connection.setReadTimeout(mReadTimeOut);
        connection.setConnectTimeout(mConnectTimeOut);
        connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
        connection.setRequestProperty("User-Agent",
                "Mozilla/5.0 (Windows NT 6.3; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/33.0.1750.146 Safari/537.36");
    }

    /**
     * 根据输入的请求地址判断使用https 还是使用http 获取不同的HttpURLConnection.
     * @param strUrl
     * @return
     * @throws Exception
     */
    private static HttpURLConnection getConnection(String strUrl) throws Exception {
        if(strUrl == null){
            return null;
        }
        if(strUrl.toLowerCase().startsWith("https")){
            return getHttpsConnection(strUrl);
        }else{
            return getHttpConnection(strUrl);
        }
    }


    //获取HTTP
    private static HttpURLConnection getHttpConnection(String urlStr) throws Exception {
        URL url =new URL(urlStr);
        HttpURLConnection conn=(HttpURLConnection) url.openConnection();
        return conn;
    }


    private static HttpURLConnection getHttpsConnection(String urlStr) throws Exception {
        URL url =new URL(urlStr);
        HttpsURLConnection conn=(HttpsURLConnection) url.openConnection();
        conn.setHostnameVerifier(hnv);
        SSLContext sslContext = SSLContext.getInstance("SSL", "SunJSSE");
        if(sslContext != null){
            TrustManager[] tm={xtm};
            sslContext.init(null, tm, null);
            SSLSocketFactory ssf =sslContext.getSocketFactory();
            conn.setSSLSocketFactory(ssf);
        }

        return conn;
    }

    private static X509TrustManager xtm = new X509TrustManager() {
        public void checkClientTrusted(X509Certificate[] chain, String authType)
                throws CertificateException {
        }

        public void checkServerTrusted(X509Certificate[] chain, String authType)
                throws CertificateException {
        }

        public X509Certificate[] getAcceptedIssuers() {
            return null;
        }
    };

    /**
     * 用于主机名验证的基接口
     */
    private static HostnameVerifier hnv = new HostnameVerifier() {
        //验证主机名和服务器验证方案的匹配是可接受的。 可以接受返回true
        public boolean verify(String hostname, SSLSession session) {
            return true;
        }
    };

    /**
     * 上传文件
     * @param url
     * @param filePath
     * @return
     * @throws IOException
     */
    public static int upload(String url,String filePath) throws IOException {
        CloseableHttpClient httpClient = HttpClientBuilder.create().build();
        CloseableHttpResponse httpResponse = null;
        RequestConfig requestConfig = RequestConfig.custom().setConnectTimeout(200000).setSocketTimeout(200000000).build();
        HttpPost httpPost = new HttpPost(url);
        httpPost.setConfig(requestConfig);
        MultipartEntityBuilder multipartEntityBuilder = MultipartEntityBuilder.create();
        //multipartEntityBuilder.setCharset(Charset.forName("UTF-8"));
        File file = new File(filePath);
        multipartEntityBuilder.addBinaryBody("file",file);
        HttpEntity httpEntity = multipartEntityBuilder.build();
        httpPost.setEntity(httpEntity);

        httpResponse = httpClient.execute(httpPost);
        HttpEntity responseEntity = httpResponse.getEntity();
        int statusCode= httpResponse.getStatusLine().getStatusCode();

        BufferedReader reader = new BufferedReader(new InputStreamReader(responseEntity.getContent()));
        String str = "";
        while((str = reader.readLine()) != null) {
            System.out.println(str);
        }
        httpClient.close();
        if(httpResponse!=null){
            httpResponse.close();
        }
        return statusCode;
    };

    public static void main(String[] args){
        try {
            HttpUtils.upload("http://192.168.56.102:5000/upload_app",
                    ATPEnvironment.getRoot() + ATPEnvironment.APPRoot + "/iris_server.py");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}