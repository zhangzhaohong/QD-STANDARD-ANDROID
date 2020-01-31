package com.autumn.framework.update.utils;

/**
 * Created by zhang on 2018/6/29.
 */

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
/*
作者：zval
*/

public class FileDownload {

    private URL url;
    private File tempFile;
    private int count = 0;


    final static HostnameVerifier DO_NOT_VERIFY = new HostnameVerifier() {

        public boolean verify(String hostname, SSLSession session) {
            return true;
        }
    };

    /**
     * Trust every server - dont check for any certificate
     */
    private static void trustAllHosts() {
        TrustManager[] trustAllCerts = new TrustManager[] { new X509TrustManager() {

            public X509Certificate[] getAcceptedIssuers() {
                return new X509Certificate[] {};
            }

            public void checkClientTrusted(X509Certificate[] chain,
                                           String authType) throws CertificateException {
                // Log.i(TAG, "checkClientTrusted");
            }

            public void checkServerTrusted(X509Certificate[] chain,
                                           String authType) throws CertificateException {
                // Log.i(TAG, "checkServerTrusted");
            }
        } };

        // Install the all-trusting trust manager
        try {
            SSLContext sc = SSLContext.getInstance("TLS");
            sc.init(null, trustAllCerts, new java.security.SecureRandom());
            HttpsURLConnection
                    .setDefaultSSLSocketFactory(sc.getSocketFactory());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static long getRemoteFileSizes(String urls) {
        long size = 0;
        try {
            HttpURLConnection conn = null;
            URL urla = new URL(urls);

            if (urla.getProtocol().toLowerCase().equals("https")) {
                trustAllHosts();
                HttpsURLConnection https = (HttpsURLConnection) urla
                        .openConnection();
                https.setHostnameVerifier(DO_NOT_VERIFY);
                conn = https;
            } else {
                conn = (HttpURLConnection) urla.openConnection();
            }
            conn.setRequestProperty("Accept-Encoding", "identity");
            conn.connect();
            size = conn.getContentLength();
            conn.disconnect();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return size;
    }

    public static void main(String[] args) {



        while(true) {
            // FileDownload down = new FileDownload(args[0],args[1]);
            File file = new File(args[1]);
            //
            long remoteFileSize = getRemoteFileSizes(args[0]);
            System.out.println("remote file size="+remoteFileSize);

            FileDownload down = new FileDownload(args[0],args[1]);
            if(file.exists()) {
                long localFileSize = file.length();
                if(localFileSize < remoteFileSize) {
                    down.execute(file.length());
                }else {
                    System.out.println("File download completed\n");
                    return;

                }


            }else {
                down.execute(0);

            }

        }





    }

    public FileDownload(String url,String savePath)  {
        try {
            this.url = new URL(url);
        } catch (MalformedURLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        tempFile = new File(savePath);
    }

    public void execute(long fileLength) {
        long currentSize = fileLength;
        HttpURLConnection httpConnection = null;
        InputStream input = null;
        RandomAccessFile targetFile = null;
        try {
            //  httpConnection = (HttpURLConnection) url.openConnection();

            if (url.getProtocol().toLowerCase().equals("https")) {
                trustAllHosts();
                HttpsURLConnection https = (HttpsURLConnection) url
                        .openConnection();
                https.setHostnameVerifier(DO_NOT_VERIFY);
                httpConnection = https;
            } else {
                httpConnection = (HttpURLConnection) url.openConnection();
            }
            //
            //httpConnection.setContentLength(();
            httpConnection.setRequestProperty("Accept-Encoding", "identity");
            httpConnection.setConnectTimeout(60*1000);
            //
            httpConnection.setReadTimeout(60*1000);
            //
            httpConnection.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:34.0) Gecko/20100101 Firefox/34.0");

            //  httpConnection.setRequestProperty("User-Agent", "NetFox");
            httpConnection.setRequestProperty("RANGE", "bytes=" + fileLength + "-");
            input = httpConnection.getInputStream();
            targetFile = new RandomAccessFile(tempFile, "rw");
            targetFile.seek(fileLength);
            byte[] b = new byte[1024];
            int nRead;
            httpConnection.connect();
            if(count == 0) {
                System.out.print("start download...\n");
                System.out.print("Remote file size: "+httpConnection.getContentLength()+"\n");


            }else {
                System.out.println("Retry Downloads: "+count);
                System.out.println("Downloaded file size: "+currentSize);
                System.out.print("start download...\n");
            }
            while ((nRead = input.read(b, 0, 1024)) > 0) {
                (targetFile).write(b, 0, nRead);
                currentSize += nRead;
            }
        } catch (Exception e) {
            //  logger.error(e.getMessage(), e);
            //e.printStackTrace();
            System.out.println("Error:"+e.getMessage());
            System.out.println("");

            System.out.println("Wait 20 seconds before downloading again");
            count++;
            try {
                Thread.sleep(20*1000);
            } catch (Exception e1) {
            }

            if (currentSize < getRemoteFileSize()) {
                if (count < 20) {
                    execute(currentSize);
                }else {
                    System.out.println("Download file error");
                    System.exit(0);
                }
            }
        } finally {
            if (targetFile != null) {
                try {
                    targetFile.close();
                } catch (Exception e) {
                }
                targetFile = null;
            }

            if (input != null) {
                try {
                    input.close();
                } catch (IOException e) {
                }
                input = null;
            }
            if(httpConnection != null){
                httpConnection.disconnect();
            }
        }
    }

    public File getFile() {
        return tempFile;
    }

    public long getRemoteFileSize() {
        long size = 0;
        try {
            HttpURLConnection conn = null;


            if (url.getProtocol().toLowerCase().equals("https")) {
                trustAllHosts();
                HttpsURLConnection https = (HttpsURLConnection) url
                        .openConnection();
                https.setHostnameVerifier(DO_NOT_VERIFY);
                conn = https;
            } else {
                conn = (HttpURLConnection) url.openConnection();
            }

            conn.setRequestProperty("Accept-Encoding", "identity");
            conn.connect();
            size = conn.getContentLength();
            conn.disconnect();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return size;
    }
}
