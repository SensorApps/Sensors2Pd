package org.sensors2.pd.dispatch;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import org.jetbrains.annotations.Contract;
import org.puredata.android.io.AudioParameters;
import org.puredata.android.service.PdService;
import org.puredata.android.utils.PdUiDispatcher;
import org.puredata.core.PdBase;
import org.sensors2.common.dispatch.DataDispatcher;
import org.sensors2.common.dispatch.Measurement;
import org.sensors2.pd.R;

import java.io.File;
import java.io.IOException;

/**
 * Created by thomas on 12.11.14.
 */
public class PdDispatcher implements DataDispatcher {
	private int patchHandle;
	private static final String TAG = "Sensors2PD";
	private final Activity activity;

	public PdDispatcher(Activity activity) {
		this.activity = activity;
	}

	@Override
	public void dispatch(Measurement sensorData) {
		float[] values = sensorData.getValues();
		for (int i = 0; i < values.length; i++) {
			String sendSymbol = getSendSymbol(sensorData, i);
			PdBase.sendFloat(sendSymbol, values[i]);
		}
	}

	private String getSendSymbol(Measurement sensorData, int i) {
		switch (sensorData.getType()) {
			case Sensor:
				return "sensor_" + sensorData.getSensorType() + "_" + i;
			case Wifi:
				switch (i) {
					case 0:
						return "wifi_" + sensorData.getName() + "_level";
					case 1:
						return "wifi_" + sensorData.getName() + "_frequency";
				}
				return "wifi_" + sensorData.getName() + "_" + i;
			case Touch:
				switch (i){
					case 0:
						return "touch_x";
					case 1:
						return "touch_y";
				}
		}
		return "";
	}

	private PdService pdService;
	private final ServiceConnection pdConnection = new ServiceConnection() {
		@Override
		public void onServiceConnected(ComponentName name, IBinder service) {
			pdService = ((PdService.PdBinder) service).getService();
			try {
				initPd();
			} catch (IOException e) {
				Log.e(TAG, e.toString());
				finish();
			}
		}

		@Override
		public void onServiceDisconnected(ComponentName name) {
// this method will never be called?!
		}
	};

	@Contract(pure = true)
	private void finish() {
	}

	private void initPd() throws IOException {
		// Configure the audio glue
		int sampleRate = AudioParameters.suggestSampleRate();
		pdService.initAudio(sampleRate, 1, 2, 10.0f);
		pdService.startAudio();
		start();
// Create and install the dispatcher
		PdUiDispatcher dispatcher = new PdUiDispatcher();
		PdBase.setReceiver(dispatcher);
	}

	private void start() {
		if (!pdService.isRunning()) {
			Intent intent = new Intent(this.activity, this.activity.getClass());
			pdService.startAudio(intent, R.drawable.sensors2pd, "Sensors2PD", "Return to Sensors2PD.");
		}
	}

	private void loadPatch(File pdFile) {
		PdBase.closePatch(patchHandle);
		patchHandle = 0;
		if (pdFile != null) {
			try {
				patchHandle = PdBase.openPatch(pdFile.getAbsolutePath());
				Log.e(TAG, "File " + pdFile.getAbsolutePath() + " " + pdFile.getAbsolutePath());
				this.activity.bindService(new Intent(this.activity, PdService.class), pdConnection, Context.BIND_AUTO_CREATE);
			} catch (IOException e) {
				Toast.makeText(activity, "The zip file needs a file with the same name inside: patch.zip -> patch.pd", Toast.LENGTH_SHORT).show();
			}
		}
	}

	public boolean setPdFile(File pdFile) {
		loadPatch(pdFile);
		return true;
	}

	public void onResume() {
		this.activity.bindService(new Intent(this.activity, PdService.class), pdConnection, Context.BIND_AUTO_CREATE);
	}

	public void onPause() {
		this.activity.unbindService(pdConnection);
	}
}
