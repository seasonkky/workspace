package com.nexgo.xtms.net.helper;

import android.content.Context;

import com.nexgo.xtms.constant.SPConstant;
import com.nexgo.xtms.util.LogUtil;
import com.nexgo.xtms.util.PreferenceUtil;

import java.io.InputStream;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;

/**
 * Created by xiaox on 2017/1/19.
 */

public class SSLHelper {
    public static final String TAG = SSLHelper.class.getSimpleName();

    private static final String KEY_STORE_TYPE_BKS = "bks";
    private static final String KEY_STORE_TYPE_P12 = "PKCS12";
    private static final String KEY_STORE_CLIENT_PATH = "client.p12";
    private static final String KEY_STORE_TRUST_PATH = "client.truststore";
    private static final String KEY_STORE_PASSWORD = "123456";
    private static final String KEY_STORE_TRUST_PASSWORD = "123456";

    public static SSLSocketFactory getSocketFactory(Context context) {
        String otaName = NEXGODriverHelper.getInstance(context).getOTAName();
        if (otaName.equals("pab")|| "1".equals(PreferenceUtil.getString(SPConstant.SSL_AUTH,"0"))){
            // 平安需要的单向验证
            LogUtil.d(TAG," set SSL single auth");
            TrustManager[] trustAllCerts = new TrustManager[] { new X509TrustManager() {
                public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                    return new java.security.cert.X509Certificate[] {};
                }
                public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
                   LogUtil.d("skyapp", "checkClientTrusted");
                }
                public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
                    LogUtil.d("skyapp", "checkServerTrusted");
                }
            } };

            SSLContext sslContext = null;
            try {
                sslContext = SSLContext.getInstance("TLS");
                sslContext.init(null, trustAllCerts, new java.security.SecureRandom());
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            } catch (KeyManagementException e) {
                e.printStackTrace();
            }
            return sslContext.getSocketFactory();
        }else {
            try {
                LogUtil.d(TAG," set SSL double auth");
                // 双向验证
                KeyStore keyStore = KeyStore.getInstance(KEY_STORE_TYPE_P12);
                KeyStore trustStore = KeyStore.getInstance(KEY_STORE_TYPE_BKS);

                InputStream ksIn = context.getResources().getAssets().open(KEY_STORE_CLIENT_PATH);
                InputStream tsIn = context.getResources().getAssets().open(KEY_STORE_TRUST_PATH);
                try {
                    keyStore.load(ksIn, KEY_STORE_PASSWORD.toCharArray());
                    trustStore.load(tsIn, KEY_STORE_TRUST_PASSWORD.toCharArray());
                } catch (Exception e) {
                    e.printStackTrace();
                    LogUtil.d(TAG,"Exception->"+e.toString());
                } finally {
                    ksIn.close();
                    tsIn.close();
                    LogUtil.d(TAG,"load ok->");
                }
//            SSLContext sslContext = SSLContext.getInstance("TLSv1.2");
                SSLContext sslContext = SSLContext.getInstance("TLS", "AndroidOpenSSL");
                TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
                trustManagerFactory.init(trustStore);
                KeyManagerFactory keyManagerFactory = KeyManagerFactory.getInstance("X509");
                keyManagerFactory.init(keyStore, KEY_STORE_PASSWORD.toCharArray());
                sslContext.init(keyManagerFactory.getKeyManagers(), trustManagerFactory.getTrustManagers(), null);
                return sslContext.getSocketFactory();

            } catch (Exception e) {
                e.printStackTrace();
                LogUtil.d(TAG,"Exception->"+e.toString());
            }
        }
        return null;
    }
}
