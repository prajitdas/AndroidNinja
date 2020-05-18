package com.prajitdas.httpPostTest;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningTaskInfo;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

/**
 * @author Prajit
 */

@TargetApi(Build.VERSION_CODES.KITKAT)
public class MainActivity extends Activity {
	private EditText txtMessage;
	private Button sendBtn;
	private TextView textViewMessage;

	private String originalMessage;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		init();
		setOnClickListeners();
	}

	private void init() {
		txtMessage = (EditText) findViewById(R.id.editTextMessage);
		sendBtn = (Button) findViewById(R.id.send_btn);
		textViewMessage = (TextView) findViewById(R.id.textViewMessage);
		setOriginalMessage(textViewMessage.getText().toString());
		isOnline();
	}

	private void setOnClickListeners() {
		sendBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(isOnline())
					new SendDataToServerAsyncTask().execute(HTTPPOSTTest.getConstWebserviceUri(), getTxtMessage().getText().toString());
			}
		});
	}
	
	private class SendDataToServerAsyncTask extends AsyncTask<String, Void, String> {
	    // Do the long-running work in here
		@Override
	    protected String doInBackground(String... urls) {
			return HTTPPOST(urls[0], urls[1]);
	    }

	    // This is called when doInBackground() is finished
	    @Override
	    protected void onPostExecute(String result) {
	    	Toast.makeText(getApplicationContext(), "This data was received: "+result, Toast.LENGTH_LONG).show();
	    }
	}

	private String HTTPPOST(String webserviceURIStr, String input) {
		String result = new String("Empty response!");

		URL url;		
		HttpURLConnection httpURLConnection = null;
		try {
			//Create connection
			url = new URL(webserviceURIStr);
			httpURLConnection = (HttpURLConnection)url.openConnection();
			httpURLConnection.setRequestMethod("POST");
			httpURLConnection.setChunkedStreamingMode(0);

//			httpURLConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
//	    	httpURLConnection.setRequestProperty("User-Agent", "");

			httpURLConnection.setUseCaches (false);
			httpURLConnection.setDoInput(true);
			httpURLConnection.setDoOutput(true);
			httpURLConnection.connect();

			//Send request
			writeDataToStream(new BufferedOutputStream(httpURLConnection.getOutputStream()));

			//Get Response	
			InputStream in = new BufferedInputStream(httpURLConnection.getInputStream());
			result = convertInputStreamToString(in);
			Log.i(HTTPPOSTTest.getHttpPostTestDebugTag(), "Read from server: "+result);
		} catch (IOException e) {
			// writing exception to log
			Log.e(HTTPPOSTTest.getHttpPostTestDebugTag(), "IOException");
		} catch (JSONException e) {
			// writing exception to log
			Log.e(HTTPPOSTTest.getHttpPostTestDebugTag(), "JSONException");
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		} finally {
			if(httpURLConnection != null) {
				httpURLConnection.disconnect(); 
			}
		}
		
		return result;
	}

	private void writeDataToStream(OutputStream out) throws JSONException, IOException {
		// Add your data
		//Create JSONObject here 
		JSONObject jsonParam = new JSONObject();
		jsonParam.put("identity", "prajitdas@gmail.com");
		jsonParam.put("location", "UMBC");
		jsonParam.put("activity", "Research");
		jsonParam.put("time", "Afternoon");
		jsonParam.put("purpose", "Personal");

		JSONArray jsonArray = new JSONArray();

		ActivityManager activityManager = (ActivityManager) getApplicationContext().getSystemService(Activity.ACTIVITY_SERVICE);
		List<RunningTaskInfo> listOfTasks = activityManager.getRunningTasks(Integer.MAX_VALUE);
//			        StringBuilder output = new StringBuilder(); 
		for(RunningTaskInfo taskInfo:listOfTasks)
		jsonArray.put(taskInfo.baseActivity.getPackageName());
//		        	jsonArray.put("Facebook");
//		        	jsonArray.put("Twitter");
//		        	jsonArray.put("G+");

		jsonParam.put("appsInstalled",jsonArray);
		
		byte[] outputStreamByteArray = URLEncoder.encode(jsonParam.toString(), HTTPPOSTTest.getConstCharset().toString()).getBytes(HTTPPOSTTest.getConstCharset());
		Log.d(HTTPPOSTTest.getHttpPostTestDebugTag(), "HTTPPOSTEXAMPLE"+outputStreamByteArray.toString());
		
		out.write(outputStreamByteArray);
		out.flush();
		out.close();
	}

	private static String convertInputStreamToString(InputStream inputStream) throws IOException{
        BufferedReader bufferedReader = new BufferedReader( new InputStreamReader(inputStream));
        String line = "";
        String result = "";
        while((line = bufferedReader.readLine()) != null)
            result += line;
        inputStream.close();
        return result;
    }

    public boolean isOnline() {
        ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Activity.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        boolean result = networkInfo != null && networkInfo.isConnected();
		// check if you are connected or not
        if(result) {
        	textViewMessage.setBackgroundColor(Color.GREEN);
        	textViewMessage.setText(getOriginalMessage()+"\n ... And you are conncted");
        }
        else{
        	textViewMessage.setBackgroundColor(Color.RED);
            textViewMessage.setText(getOriginalMessage()+"\n ... But you are NOT conncted");
        }
        return result;
    }

	public EditText getTxtMessage() {
		return txtMessage;
	}

	public void setTxtMessage(EditText txtMessage) {
		this.txtMessage = txtMessage;
	}

	public Button getSendBtn() {
		return sendBtn;
	}

	public void setSendBtn(Button sendBtn) {
		this.sendBtn = sendBtn;
	}

	public TextView getTextViewMessage() {
		return textViewMessage;
	}

	public void setTextViewMessage(TextView textViewMessage) {
		this.textViewMessage = textViewMessage;
	}

	/**
	 * @return the originalMessage
	 */
	public String getOriginalMessage() {
		return originalMessage;
	}

	/**
	 * @param originalMessage the originalMessage to set
	 */
	public void setOriginalMessage(String originalMessage) {
		this.originalMessage = originalMessage;
	}
}