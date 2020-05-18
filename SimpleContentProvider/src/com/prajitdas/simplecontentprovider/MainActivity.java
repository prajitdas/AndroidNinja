package com.prajitdas.simplecontentprovider;

import java.io.FileNotFoundException;
import java.io.IOException;
import android.app.Activity;
import android.app.Fragment;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore.Images.ImageColumns;
import android.provider.MediaStore.Images.Media;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		if (savedInstanceState == null) {
			getFragmentManager().beginTransaction()
					.add(R.id.container, new PlaceholderFragment()).commit();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	/**
	 * A placeholder fragment containing a simple view.
	 */
	public static class PlaceholderFragment extends Fragment {

		public PlaceholderFragment() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.fragment_main, container,
					false);
			return rootView;
		}
	}
	
	public void showImage(View view) {
		String URL = "content://com.prajitdas.provider.ImageData/images";
		Uri images = Uri.parse(URL);
		Cursor queryCursor = getContentResolver().query(images, null, null, null, null);
		ImageView mImageView = (ImageView)findViewById(R.id.imageViewForPicture);
		queryCursor.moveToFirst();
//		int idx = queryCursor.getColumnIndex(ImageColumns._ID);
//		byte[] image = queryCursor.getBlob(queryCursor.getColumnIndexOrThrow("image"));
//		Bitmap bmpImg = BitmapFactory.decodeByteArray(image, 0, image.length);
//		mImageView.setImageBitmap(bmpImg);
//		ByteArrayOutputStream bytes = new ByteArrayOutputStream();
//		bmpImg.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
//		String path = Media.insertImage(this.getContentResolver(), bmpImg, "Title", null);
//		Toast.makeText(this, Uri.parse(path).toString(), Toast.LENGTH_SHORT).show();
		try {
			int idx = queryCursor.getColumnIndex(ImageColumns._ID);
			Log.v("verbose", queryCursor.getString(idx));
			mImageView.setImageBitmap(Media.getBitmap(this.getContentResolver(), 
					Uri.withAppendedPath(Media.EXTERNAL_CONTENT_URI, queryCursor.getString(idx))));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		queryCursor.close();
	}
	
    public void deleteAllBirthdays (View view) {
		// delete all the records and the table of the database provider
		String URL = "content://com.prajitdas.provider.Birthday/friends";
		Uri friends = Uri.parse(URL);
		int count = getContentResolver().delete(
		friends, null, null);
		String countNum = "Javacodegeeks: "+ count +" records are deleted.";
		Toast.makeText(getBaseContext(), 
				countNum, Toast.LENGTH_LONG).show();
	}

	public void addBirthday(View view) {
		// Add a new birthday record
		ContentValues values = new ContentValues();

		values.put(BirthProvider.NAME, 
		((EditText)findViewById(R.id.name)).getText().toString());

		values.put(BirthProvider.BIRTHDAY, 
		((EditText)findViewById(R.id.birthday)).getText().toString());

		Uri uri = getContentResolver().insert(
		BirthProvider.CONTENT_URI, values);

		Toast.makeText(getBaseContext(), 
				"Javacodegeeks: " + uri.toString() + " inserted!", Toast.LENGTH_LONG).show();
	}

	public void showAllBirthdays(View view) {
		// Show all the birthdays sorted by friend's name
		String URL = "content://com.prajitdas.provider.Birthday/friends";
		Uri friends = Uri.parse(URL);
		Cursor c = getContentResolver().query(friends, null, null, null, "name");
		String result = "Javacodegeeks Results:";

		if (!c.moveToFirst()) {
			Toast.makeText(this, result+" no content yet!", Toast.LENGTH_LONG).show();
		}
		else {
			do {
				result = result + "\n" + c.getString(c.getColumnIndex(BirthProvider.NAME)) + 
						" with id " +  c.getString(c.getColumnIndex(BirthProvider.ID)) + 
						" has birthday: " + c.getString(c.getColumnIndex(BirthProvider.BIRTHDAY));
			} while (c.moveToNext());
			Toast.makeText(this, result, Toast.LENGTH_LONG).show();
		}
	}
}