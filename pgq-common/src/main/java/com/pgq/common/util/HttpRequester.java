package com.pgq.common.util;


import java.io.*;
import java.net.*;
import java.util.HashMap;
import java.util.Map;

public class HttpRequester
{
    private static final String CHARSET = "utf-8";
    private static final Integer CONNECT_TIME_OUT = 5000;
    private static final Integer READ_TIME_OUT = 15000;
    private static final String BOUNDARY = "SadEyesAppBoundary";
    private static final String PREFIX = "--";
    private static final String LINEND = "\r\n";
    private static final String MULTIPART_FROM_DATA = "multipart/form-data";

    /**
     * 生成http连接
     *
     * @param method http请求方式
     * @return httpURLConnection
     * @throws IOException 连接生成失败
     */
    private static HttpURLConnection createConnection(String urlPath, String method) throws IOException
    {
        URL url = new URL(urlPath);
        HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
        httpURLConnection.setRequestMethod(method);
        httpURLConnection.setRequestProperty("Charsert", CHARSET);
        httpURLConnection.setRequestProperty("connection", "Keep-Alive");
        httpURLConnection.setRequestProperty("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
        httpURLConnection.setConnectTimeout(CONNECT_TIME_OUT);
        httpURLConnection.setReadTimeout(READ_TIME_OUT);
        return httpURLConnection;
    }

    /**
     * DO GET request
     *
     * @param url 请求地址
     * @return
     * @throws Exception
     */
    public static String doGet(String url) throws Exception
    {
        return doGet(url, null, null);
    }

    /**
     * DO GET request
     *
     * @param url   请求地址
     * @param param 参数map
     * @return
     * @throws Exception
     */
    public static String doGet(String url, Map<String, Object> param) throws Exception
    {
        return doGet(url, param, null);
    }

    /**
     * DO GET request
     *
     * @param url
     * @param param
     * @param header
     * @return
     * @throws Exception
     */
    public static String doGet(String url, Map<String, Object> param, Map<String, String> header) throws Exception
    {
        url = initURLParam(url, param);
        HttpURLConnection httpURLConnection = createConnection(url, "GET");
        httpURLConnection = setHeader(httpURLConnection, header);
        // 响应失败
        if (httpURLConnection.getResponseCode() >= 300)
        {
            throw new Exception("HTTP Request is not success, Response code is " + httpURLConnection.getResponseCode());
        }
        return getResponse(httpURLConnection);
    }

    /**
     * 设置请求头
     *
     * @param httpURLConnection
     * @param header
     * @return soap
     */
    private static HttpURLConnection setHeader(HttpURLConnection httpURLConnection, Map<String, String> header)
    {
        if (header != null)
        {
            for (Map.Entry<String, String> entry : header.entrySet())
            {
                httpURLConnection.setRequestProperty(entry.getKey(), entry.getValue());
            }
        }
        return httpURLConnection;
    }

    /**
     * 拼接普通参数
     *
     * @param url
     * @param param
     * @return
     * @throws UnsupportedEncodingException
     */
    private static String initURLParam(String url, Map<String, Object> param) throws UnsupportedEncodingException
    {
        if (param != null)
        {
            String queryString = "";
            for (Map.Entry<String, Object> entry : param.entrySet())
            {
                queryString += entry.getKey() + "=" + URLEncoder.encode(entry.getValue().toString(), "UTF-8") + "&";
            }
            if (queryString.length() > 0)
            {
                queryString = queryString.substring(0, queryString.length() - 1);
                if (url.contains("?"))
                {
                    if (url.endsWith("?"))
                    {
                        url = url + queryString;
                    } else
                    {
                        url = url + "&" + queryString;
                    }
                } else
                {
                    url = url + "?" + queryString;
                }
            }
        }
        return url;
    }

    /**
     * 获取返回结果
     *
     * @param httpURLConnection
     * @return
     * @throws Exception
     */
    public static String getResponse(HttpURLConnection httpURLConnection) throws Exception
    {
        InputStream inputStream = null;
        InputStreamReader inputStreamReader = null;
        BufferedReader reader = null;
        String tempLine;
        StringBuffer resultBuffer = new StringBuffer();
        try
        {
            inputStream = httpURLConnection.getInputStream();
            inputStreamReader = new InputStreamReader(inputStream, CHARSET);
            reader = new BufferedReader(inputStreamReader);
            while ((tempLine = reader.readLine()) != null)
            {
                resultBuffer.append(tempLine);
            }
        } finally
        {
            if (reader != null)
            {
                reader.close();
            }
            if (inputStreamReader != null)
            {
                inputStreamReader.close();
            }
            if (inputStream != null)
            {
                inputStream.close();
            }
            httpURLConnection.disconnect();
        }
        return resultBuffer.toString();
    }


    /**
     * DO POST request
     *
     * @param url    请求地址
     * @param param  普通参数
     * @param body   body报文
     * @param header 请求头
     * @return
     * @throws Exception
     */
    public static String doPost(String url, Map<String, Object> param, String body, Map<String, String> header) throws Exception
    {
        OutputStream outputStream = null;
        OutputStreamWriter outputStreamWriter = null;

        url = initURLParam(url, param);
        HttpURLConnection httpURLConnection = createConnection(url, "POST");
        httpURLConnection = setHeader(httpURLConnection, header);

        httpURLConnection.setDoOutput(true);
        httpURLConnection.setDoInput(true);

        try
        {

            outputStream = httpURLConnection.getOutputStream();
            outputStreamWriter = new OutputStreamWriter(outputStream, CHARSET);
            outputStreamWriter.write(body);
            outputStreamWriter.flush();
            // 响应失败
            if (httpURLConnection.getResponseCode() >= 300)
            {
                throw new Exception("HTTP Request is not success, Response code is " + httpURLConnection.getResponseCode());
            }
        } finally
        {
            if (outputStream != null)
            {
                outputStream.close();
            }
            if (outputStreamWriter != null)
            {
                outputStreamWriter.close();
            }
        }
        return getResponse(httpURLConnection);

    }

    /**
     * DO POST request
     *
     * @param url  请求地址
     * @param body body报文
     * @return
     * @throws Exception
     */
    public static String doPost(String url, String body) throws Exception
    {
        Map<String, String> header = new HashMap<>();
        header.put("Content-Type", "application/xml,application/json");
        return doPost(url, null, body, header);
    }

    /**
     * DO POST request
     *
     * @param url   请求地址
     * @param param 普通参数
     * @return
     * @throws Exception
     */
    public static String doPost(String url, Map<String, Object> param) throws Exception
    {
        return doPost(url, param, null, null);
    }

    /**
     * DO POST request
     *
     * @param url    请求地址
     * @param body   body报文
     * @param header 请求头
     * @return
     * @throws Exception
     */
    public static String doPost(String url, String body, Map<String, String> header) throws Exception
    {
        if (!header.containsKey("Content-Type"))
        {
            header.put("Content-Type", "application/xml,application/json");
        }
        return doPost(url, null, body, header);
    }

    /**
     * DO POST request
     *
     * @param url    请求地址
     * @param param  普通参数
     * @param header 请求头
     * @return
     * @throws Exception
     */
    public static String doPost(String url, Map<String, Object> param, Map<String, String> header) throws Exception
    {
        return doPost(url, param, null, header);
    }

    /**
     * DO POST request
     *
     * @param url   请求地址
     * @param param 普通参数
     * @param body  body报文
     * @return
     * @throws Exception
     */
    public static String doPost(String url, Map<String, Object> param, String body) throws Exception
    {
        Map<String, String> header = new HashMap<>();
        header.put("Content-Type", "application/xml,application/json");
        return doPost(url, param, body, null);
    }

    /**
     * @param actionUrl 访问的服务器URL
     * @param file      单文件
     * @return
     * @throws IOException
     */
    public static String postFile(String actionUrl, File file) throws Exception
    {
        HttpURLConnection conn = createConnection(actionUrl, "POST");
        conn.setDoInput(true);// 允许输入
        conn.setDoOutput(true);// 允许输出
        conn.setUseCaches(false); // 不允许使用缓存
        conn.setRequestProperty("Content-Type", "application/octet-stream");
        DataOutputStream outStream = new DataOutputStream(conn.getOutputStream());
        writeFile(file, outStream);
        return getResponse(conn);
    }

    private static void writeFile(File file, DataOutputStream outStream) throws IOException
    {
        InputStream is = new FileInputStream(file);
        byte[] buffer = new byte[1024];
        int len;
        while ((len = is.read(buffer)) != -1)
        {
            outStream.write(buffer, 0, len);
        }
        is.close();
    }

    /**
     * 通过application/octet-stream方式上传
     *
     * @param actionUrl 访问的服务器URL
     * @param files     文件参数
     * @return
     * @throws IOException
     */
    public static String postFile(String actionUrl, File[] files) throws Exception
    {

        return postFile(actionUrl, null, files);
    }

    /**
     * 通过拼接的方式构造请求内容，实现参数传输以及文件传输
     *
     * @param actionUrl 访问的服务器URL
     * @param params    普通参数
     * @param files     文件参数
     * @return
     * @throws IOException
     */
    public static String postFile(String actionUrl, Map<String, Object> params, File[] files) throws Exception
    {
        return postFile(actionUrl, params, files, null);
    }

    /**
     * 通过拼接的方式构造请求内容，实现参数传输以及文件传输
     *
     * @param actionUrl 访问的服务器URL
     * @param params    普通参数
     * @param files     文件参数
     * @param header    请求头
     * @return
     * @throws IOException
     */
    public static String postFile(String actionUrl, Map<String, Object> params, File[] files, Map<String, String> header)
            throws Exception
    {
        DataOutputStream outStream = null;
        HttpURLConnection conn = createConnection(actionUrl, "POST");
        conn = setHeader(conn, header);
        conn.setDoInput(true);// 允许输入
        conn.setDoOutput(true);// 允许输出
        conn.setUseCaches(false); // 不允许使用缓存
        conn.setRequestProperty("Content-Type", MULTIPART_FROM_DATA + ";boundary=" + BOUNDARY);
        try
        {
            outStream = new DataOutputStream(conn.getOutputStream());
            if (params != null)
            {
                // 组拼文本类型的参数
                StringBuilder sb = new StringBuilder();
                for (Map.Entry<String, Object> entry : params.entrySet())
                {
                    sb.append(PREFIX).append(BOUNDARY).append(LINEND);
                    sb.append("Content-Disposition: form-data; name=\"").append(entry.getKey()).append("\"").append(LINEND);
                    sb.append("Content-Type: text/plain; charset=").append(CHARSET).append(LINEND);
                    sb.append("Content-Transfer-Encoding: 8bit").append(LINEND);
                    sb.append(LINEND);
                    sb.append(entry.getValue()).append(LINEND);
                }
                outStream.write(sb.toString().getBytes());
            }

            // 发送文件数据
            if (files != null)
            {
                for (File file : files)
                {
                    StringBuilder sb1 = new StringBuilder();
                    sb1.append(PREFIX).append(BOUNDARY).append(LINEND);
                    // name是post中传参的键 filename是文件的名称
                    sb1.append("Content-Disposition: form-data; name=\"file\"; filename=\"").append(file.getName()).append("\"").append(LINEND);
                    sb1.append("Content-Type: application/octet-stream; charset=").append(CHARSET).append(LINEND);
                    sb1.append(LINEND);
                    outStream.write(sb1.toString().getBytes());
                    //写入附件流
                    writeFile(file, outStream);
                    outStream.write(LINEND.getBytes());//最后换行
                }
            }
            // 请求结束标志
            byte[] end_data = (PREFIX + BOUNDARY + PREFIX + LINEND).getBytes();
            outStream.write(end_data);
            outStream.flush();
            // 响应失败
            if (conn.getResponseCode() >= 300)
            {
                throw new Exception("HTTP Request is not success, Response code is " + conn.getResponseCode());
            }
        } finally
        {
            if (outStream != null)
            {
                outStream.close();
            }
        }
        return getResponse(conn);
    }

}