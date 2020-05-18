package com.prajitdas.httpPostTest;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import android.annotation.TargetApi;
import android.app.Application;
import android.os.Build;

@TargetApi(Build.VERSION_CODES.KITKAT)
public class HTTPPOSTTest extends Application {	
	private static final String HTTP_POST_TEST_DEBUG_TAG = "HTTP_POST_TEST_DEBUG_TAG";
	private static final String CONST_WEBSERVICE_URI = "http://www.csee.umbc.edu/~prajit1/CurrentApps/index.php";
	private static final Charset CONST_CHARSET = StandardCharsets.UTF_8;

	/**
	 * @return the httpPostTestDebugTag
	 */
	public static String getHttpPostTestDebugTag() {
		return HTTP_POST_TEST_DEBUG_TAG;
	}


	/**
	 * @return the constWebserviceUri
	 */
	public static String getConstWebserviceUri() {
		return CONST_WEBSERVICE_URI;
	}


	/**
	 * @return the constCharset
	 */
	public static Charset getConstCharset() {
		return CONST_CHARSET;
	}
}