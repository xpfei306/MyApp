package xpfei.myapp.manager;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.URL;
import java.util.Map;

import xpfei.myapp.model.FormFile;
import xpfei.mylibrary.utils.AppLog;


/**
 * 上传文件到服务器
 */
public class SocketHttpRequester {
    /**
     * @param path   上传路径
     * @param params 请求参数 key为参数名,value为参数值
     * @param files  上传文件
     */
    public static String post(String path, Map<String, String> params, FormFile[] files, Handler handler) throws Exception {
        try {
            final String BOUNDARY = "---------------------------7da2137580612"; // 数据分隔线
            final String endline = "--" + BOUNDARY + "--\r\n";// 数据结束标志
            int fileDataLength = 0; // 文件长度
            for (FormFile uploadFile : files) {// 得到文件类型数据的总长度
                StringBuilder fileExplain = new StringBuilder();
                fileExplain.append("--");
                fileExplain.append(BOUNDARY);
                fileExplain.append("\r\n");
                fileExplain.append("Content-Disposition: form-data;name=\""
                        + uploadFile.getParameterName() + "\";filename=\""
                        + uploadFile.getFilname() + "\"\r\n");
                fileExplain.append("Content-Type: " + uploadFile.getContentType() + "\r\n\r\n");
                fileExplain.append("\r\n");
                fileDataLength += fileExplain.length();
                if (uploadFile.getInStream() != null) {
                    if (uploadFile.getFile() != null) {
                        fileDataLength += uploadFile.getFile().length();
                    } else {
                        fileDataLength += uploadFile.getFileSize();
                    }
                } else {
                    fileDataLength += uploadFile.getData().length;
                }
            }
            StringBuilder textEntity = new StringBuilder();
            for (Map.Entry<String, String> entry : params.entrySet()) {// 构造文本类型参数的实体数据
                textEntity.append("--");
                textEntity.append(BOUNDARY);
                textEntity.append("\r\n");
                textEntity.append("Content-Disposition: form-data; name=\"" + entry.getKey() + "\"\r\n\r\n");
                textEntity.append(entry.getValue());
                textEntity.append("\r\n");
            }
            // 计算传输给服务器的实体数据总长度
            int dataLength = textEntity.toString().getBytes().length + fileDataLength + endline.getBytes().length;
            URL url = new URL(path);
            int port = url.getPort() == -1 ? 80 : url.getPort();
            Socket socket = new Socket(InetAddress.getByName(url.getHost()), port);
            OutputStream outStream = socket.getOutputStream();
            // 下面完成HTTP请求头的发送
            String requestmethod = "POST " + url.getPath() + " HTTP/1.1\r\n";
            outStream.write(requestmethod.getBytes());
            String accept = "Accept:image/png, image/gif, image/jpeg, image/pjpeg, image/pjpeg, " +
                    "application/x-shockwave-flash, application/xaml+xml, application/vnd.ms-xpsdocument, " +
                    "application/x-ms-xbap, application/x-ms-application, application/vnd.ms-excel, application/vnd.ms-powerpoint, application/msword, */*\r\n";
            outStream.write(accept.getBytes());
            String language = "Accept-Language: zh-CN\r\n";
            outStream.write(language.getBytes());
            String contenttype = "Content-Type: multipart/form-data; boundary=" + BOUNDARY + "\r\n";
            outStream.write(contenttype.getBytes());
            String contentlength = "Content-Length: " + dataLength + "\r\n";
            outStream.write(contentlength.getBytes());
            String alive = "Connection: Keep-Alive\r\n";
            outStream.write(alive.getBytes());
            String host = "Host: " + url.getHost() + ":" + port + "\r\n";
            outStream.write(host.getBytes());
            // 写完HTTP请求头后根据HTTP协议再写一个回车换行
            outStream.write("\r\n".getBytes());
            // 把所有文本类型的实体数据发送出来
            outStream.write(textEntity.toString().getBytes());
            int lenTotal = 0;
            // 把所有文件类型的实体数据发送出来
            for (FormFile uploadFile : files) {
                StringBuilder fileEntity = new StringBuilder();
                fileEntity.append("--");
                fileEntity.append(BOUNDARY);
                fileEntity.append("\r\n");
                fileEntity.append("Content-Disposition: form-data;name=\""
                        + uploadFile.getParameterName() + "\";filename=\"" + uploadFile.getFilname() + "\"\r\n");
                fileEntity.append("Content-Type: " + uploadFile.getContentType() + "\r\n\r\n");
                outStream.write(fileEntity.toString().getBytes());
                InputStream is = uploadFile.getInStream();
                if (is != null) {
                    byte[] buffer = new byte[1024];
                    int len;
                    while ((len = is.read(buffer)) > 0) {
                        outStream.write(buffer, 0, len);
                        lenTotal += len; // 每次上传的长度
                        Message message = new Message();
                        Bundle bundle = new Bundle();
                        bundle.putInt("lenTotal", lenTotal);
                        AppLog.Logd(lenTotal+"");
                        message.arg1 = 11;
                        message.setData(bundle);
                        handler.sendMessage(message);
                    }
                    is.close();
                } else {
                    outStream.write(uploadFile.getData(), 0, uploadFile.getData().length);
                }
                outStream.write("\r\n".getBytes());
            }
            // 下面发送数据结束标志，表示数据已经结束
            outStream.write(endline.getBytes());
            BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            String str;
            boolean requestCodeSuccess = false;
            boolean uploadSuccess = false;
            int indexResult = 1;
            while ((str = reader.readLine()) != null) {
                if (indexResult == 1) {
                    if (str.indexOf("200") > 0) {
                        requestCodeSuccess = true;
                    }
                }
                if (indexResult == 6) {
                    if ("true".equals(str.trim())) {
                        uploadSuccess = true;
                    }
                }

                if (requestCodeSuccess && uploadSuccess) {
                    outStream.flush();
                    if (null != socket && socket.isConnected()) {
                        socket.shutdownInput();
                        socket.shutdownOutput();
                    }
                    outStream.close();
                    reader.close();
                    socket.close();
                    return "000";
                } else if (indexResult == 6) {
                    outStream.flush();
                    if (null != socket && socket.isConnected()) {
                        socket.shutdownInput();
                        socket.shutdownOutput();
                    }
                    outStream.close();
                    reader.close();
                    socket.close();
                    return "000";
                }
                ++indexResult;
            }
            outStream.flush();
            if (null != socket && socket.isConnected()) {
                socket.shutdownInput();
                socket.shutdownOutput();
            }
            outStream.close();
            reader.close();
            socket.close();
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            AppLog.Logd(e.getMessage());
            return null;
        }
    }

    /**
     * 提交数据到服务器
     *
     * @param path   上传路径(注：避免使用localhost或127.0.0.1这样的路径测试，因为它会指向手机模拟器，你可以使用http://
     *               www.itcast.cn或http://192.168.1.10:8080这样的路径测试)
     * @param params 请求参数 key为参数名,value为参数值
     * @param file   上传文件
     */
    public static String post(String path, Map<String, String> params,
                              FormFile file, Handler handler) throws Exception {
        return post(path, params, new FormFile[]{file}, handler);
    }

}