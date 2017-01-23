package com.windfindtech.icommon.http;

import android.content.Context;

import com.windfindtech.icommon.R;

import java.io.IOException;
import java.io.InputStream;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;

/**
 * Created by cplu on 2015/10/26.
 */
public class HttpsTrustManager {
//	protected static final String TAG = "NukeSSLCerts";
//	private static SSLSocketFactory s_defaultSSLSocketFactory = null;
//	private static HostnameVerifier s_defaultHostnameVerifier = null;
	private static SSLSocketFactory s_sslSocketFactory;
	private static HostnameVerifier s_hostnameVerifier;

	/**
	 * Nuke ssl socket factory and host name verifier to accept all https certification
	 */
//	public static void nuke() {
//		Logger.debug("HttpsTrustManager.nuke");
//		try {
//			TrustManager[] trustAllCerts = new TrustManager[] {
//				new X509TrustManager() {
//					public X509Certificate[] getAcceptedIssuers() {
//						X509Certificate[] myTrustedAnchors = new X509Certificate[0];
//						return myTrustedAnchors;
//					}
//
//					@Override
//					public void checkClientTrusted(X509Certificate[] certs, String authType) {}
//
//					@Override
//					public void checkServerTrusted(X509Certificate[] certs, String authType) {}
//				}
//			};
//
//			SSLContext sc = SSLContext.getInstance("TLS");
//			sc.init(null, trustAllCerts, new SecureRandom());
//			s_defaultSSLSocketFactory = HttpsURLConnection.getDefaultSSLSocketFactory();
//			HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
//			s_defaultHostnameVerifier = HttpsURLConnection.getDefaultHostnameVerifier();
//			HttpsURLConnection.setDefaultHostnameVerifier(new HostnameVerifier() {
//				@Override
//				public boolean verify(String arg0, SSLSession arg1) {
//					return true;
//				}
//			});
//		} catch (Exception e) {
//			Logger.error(e);
//		}
//	}
//
//	/**
//	 * Reset ssl socket factory and host name verifier to defaults
//	 * This will disallow untrusted https certifications
//	 */
//	public static void unnuke(){
//		Logger.debug("HttpsTrustManager.unnuke");
//		if(s_defaultSSLSocketFactory != null) {
//			HttpsURLConnection.setDefaultSSLSocketFactory(s_defaultSSLSocketFactory);
//		}
//		if(s_defaultHostnameVerifier != null) {
//			HttpsURLConnection.setDefaultHostnameVerifier(s_defaultHostnameVerifier);
//		}
//	}

	public static SSLSocketFactory getTestEnvSSLSocketFactory(Context ctx)
			throws CertificateException, KeyStoreException, IOException, NoSuchAlgorithmException, KeyManagementException {
		if(s_sslSocketFactory == null) {
			CertificateFactory cf = CertificateFactory.getInstance("X.509");
			InputStream caInput = ctx.getResources().openRawResource(R.raw.internal);

			Certificate ca = cf.generateCertificate(caInput);
			caInput.close();

			KeyStore keyStore = KeyStore.getInstance("BKS");
			keyStore.load(null, null);
			keyStore.setCertificateEntry("ca", ca);

			String tmfAlgorithm = TrustManagerFactory.getDefaultAlgorithm();
			TrustManagerFactory tmf = TrustManagerFactory.getInstance(tmfAlgorithm);
			tmf.init(keyStore);

			TrustManager[] wrappedTrustManagers = tmf.getTrustManagers();

			SSLContext sslContext = SSLContext.getInstance("TLS");
			sslContext.init(null, wrappedTrustManagers, null);

			s_sslSocketFactory = sslContext.getSocketFactory();
		}
		return s_sslSocketFactory;
	}

	private static final String TEST_ENV_HOST_NAME = "10.0.4.2";

	public static HostnameVerifier getTestEnvHostnameVerifier(){
		if(s_hostnameVerifier == null) {
			s_hostnameVerifier = new HostnameVerifier() {
				@Override
				public boolean verify(String hostname, SSLSession session) {
					return hostname != null && hostname.contains(TEST_ENV_HOST_NAME);
				}
			};
		}
		return s_hostnameVerifier;
	}
}