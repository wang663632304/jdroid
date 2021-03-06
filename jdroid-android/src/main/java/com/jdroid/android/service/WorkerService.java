package com.jdroid.android.service;

import roboguice.service.RoboIntentService;
import android.content.Context;
import android.content.Intent;
import com.jdroid.android.utils.WakeLockManager;

/**
 * 
 * @author Maxi Rosson
 */
public abstract class WorkerService extends RoboIntentService {
	
	private static final String ENABLE_PARTIAL_WAKE_LOCK = "enablePartialWakeLock";
	
	public WorkerService() {
		super(WorkerService.class.getSimpleName());
	}
	
	/**
	 * @see android.app.IntentService#onHandleIntent(android.content.Intent)
	 */
	@Override
	protected final void onHandleIntent(Intent intent) {
		try {
			doExecute(intent);
		} finally {
			if (intent.hasExtra(ENABLE_PARTIAL_WAKE_LOCK)) {
				WakeLockManager.releasePartialWakeLock();
			}
		}
	}
	
	protected abstract void doExecute(Intent intent);
	
	protected static void runIntentInService(Context context, Intent intent,
			Class<? extends WorkerService> serviceClass, Boolean enablePartialWakeLock) {
		if (enablePartialWakeLock) {
			WakeLockManager.acquirePartialWakeLock(context);
			intent.putExtra(ENABLE_PARTIAL_WAKE_LOCK, true);
		}
		intent.setClass(context, serviceClass);
		context.startService(intent);
	}
	
	protected static void runIntentInService(Context context, Intent intent, Class<? extends WorkerService> serviceClass) {
		WorkerService.runIntentInService(context, intent, serviceClass, true);
	}
	
}
