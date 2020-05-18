package com.prajitdas.sunshine;

import android.app.Activity;
import android.app.Fragment;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ProviderInfo;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class MainActivity extends Activity {
	private Bundle dataBundle;
	private PackageManager pm;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		dataBundle = new Bundle();
		pm = getPackageManager();
		getApplicationsList();
		getProviderList();
		if (savedInstanceState == null) {
			PlaceholderFragment placeholderFragment = new PlaceholderFragment();

            // In case this activity was started with special instructions from an
            // Intent, pass the Intent's extras to the fragment as arguments
			placeholderFragment.setArguments(dataBundle);
        	getFragmentManager().beginTransaction()
					.add(R.id.container, placeholderFragment).commit();
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
			TextView textView = (TextView) rootView.findViewById(R.id.textView);
			textView.setText(getArguments().getString("apps")+getArguments().getString("providers"));
			return rootView;
		}
		public void setText() {
		}
	}

	/**
	 * Finds all the applications on the phone and stores them in a database accessible to the whole app 
	 */
	private void getApplicationsList() {
		StringBuffer temp = new StringBuffer();
		// Flags: See below
		int flags = PackageManager.GET_META_DATA | 
		            PackageManager.GET_SHARED_LIBRARY_FILES |     
		            PackageManager.GET_UNINSTALLED_PACKAGES | 
		            PackageManager.GET_PERMISSIONS;
		for(PackageInfo pack : getPackageManager().getInstalledPackages(flags)) {
		    if ((pack.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) != 1)
				if(pack.requestedPermissions!=null && pack.packageName.equals("com.twitter.android"))
					temp.append("Name: "+pack.packageName+"\n"+
							"Application Label: "+pm.getApplicationLabel(pack.applicationInfo)+"\n"+
							"--------------------------------------------------------------------------------------\n"+
							"Requested Permissions: "+ArrayToString(pack)+
							"--------------------------------------------------------------------------------------\n"+
							"Shared User Id: "+pack.sharedUserId+"\n"+
							"Version Name: "+pack.versionName+"\n"+
							"End of application info!\n"+
							"--------------------------------------------------------------------------------------\n");
		}
		dataBundle.putString("apps", temp.toString());
	}

	private String ArrayToString(PackageInfo pack) {
		StringBuffer temp = new StringBuffer();
		for(String perm : pack.requestedPermissions) 
			temp.append(perm+"\n");
		return temp.toString();
	}

	/**
	 * Finds all the content providers on the phone and stores them in a database accessible to the whole app 
	 */
	private void getProviderList() {
		StringBuffer temp = new StringBuffer();
		ProviderInfo[] providerArray;
		for(PackageInfo pack : pm.getInstalledPackages(PackageManager.GET_PROVIDERS)) {
			providerArray = pack.providers;
			if (providerArray != null)
				for (ProviderInfo provider : providerArray)
					if(provider.name.equals("com.android.providers.contacts.ContactsProvider2"))
						temp.append("Name: "+provider.name+"\n"+
								"Authority: "+provider.authority+"\n"+
								"Read Permission: "+provider.readPermission+"\n"+
								"Write Permission: "+provider.writePermission+"\n"+
								"Package Name: "+provider.packageName+"\n"+
								"Process Name: "+provider.processName+"\n"+
								"Application Label: "+pm.getApplicationLabel(provider.applicationInfo)+"\n"+
								"End of provider info!\n");
		}
		dataBundle.putString("providers", temp.toString());
	}
}