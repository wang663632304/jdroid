package com.jdroid.android.utils;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.FragmentActivity;
import com.jdroid.android.AbstractApplication;
import com.jdroid.android.R;
import com.jdroid.android.dialog.AlertDialogFragment;

/**
 * 
 * @author Maxi Rosson
 */
public class GooglePlayUtils {
	
	public static class UpdateAppDialogFragment extends AlertDialogFragment {
		
		@Override
		protected void onPostivieClick() {
			FragmentActivity fragmentActivity = (FragmentActivity)AbstractApplication.get().getCurrentActivity();
			launchAppDetails(fragmentActivity, AndroidUtils.getPackageName());
		};
	}
	
	public static class DownloadAppDialogFragment extends AlertDialogFragment {
		
		private static final String PACKAGE_NAME = "PACKAGE_NAME";
		
		@Override
		protected void onPostivieClick() {
			FragmentActivity fragmentActivity = (FragmentActivity)AbstractApplication.get().getCurrentActivity();
			launchAppDetails(fragmentActivity, getArguments().getString(PACKAGE_NAME));
		};
		
		public void setPackageName(String packageName) {
			addParameter(PACKAGE_NAME, packageName);
		}
	}
	
	public static void showUpdateDialog() {
		FragmentActivity fragmentActivity = (FragmentActivity)AbstractApplication.get().getCurrentActivity();
		String appName = AndroidUtils.getApplicationName();
		String title = fragmentActivity.getString(R.string.updateAppTitle, appName);
		String message = fragmentActivity.getString(R.string.updateAppMessage, appName);
		AlertDialogFragment.show(fragmentActivity, new UpdateAppDialogFragment(), title, message,
			fragmentActivity.getString(R.string.ok), null, true);
	}
	
	public static void showDownloadDialog(int appNameResId, String packageName) {
		FragmentActivity fragmentActivity = (FragmentActivity)AbstractApplication.get().getCurrentActivity();
		String appName = fragmentActivity.getString(appNameResId);
		String title = fragmentActivity.getString(R.string.installAppTitle, appName);
		String message = fragmentActivity.getString(R.string.installAppMessage, appName);
		DownloadAppDialogFragment fragment = new DownloadAppDialogFragment();
		fragment.setPackageName(packageName);
		AlertDialogFragment.show(fragmentActivity, fragment, title, message, fragmentActivity.getString(R.string.yes),
			fragmentActivity.getString(R.string.no), true);
	}
	
	public static void launchAppDetails(Context context, String packageName) {
		Uri uri = Uri.parse("market://details?id=" + packageName);
		Intent intent = new Intent(Intent.ACTION_VIEW, uri);
		context.startActivity(intent);
	}
}
