package org.sensors2.pd.sensors;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import org.puredata.android.io.AudioParameters;
import org.puredata.android.service.PdService;
import org.puredata.android.utils.PdUiDispatcher;
import org.puredata.core.PdBase;
import org.sensors2.common.sensors.DataDispatcher;
import org.sensors2.common.sensors.Measurement;
import org.sensors2.pd.R;

import java.io.File;
import java.io.IOException;

/**
 * Created by thomas on 12.11.14.
 */
public class PdDispatcher implements DataDispatcher {
	private PdUiDispatcher dispatcher;
	private File pdFile;
	private static final String TAG = "Sensors2PD";
	private final Activity activity;

	public PdDispatcher(Activity activity) {
		this.activity = activity;
	}

	@Override
	public void dispatch(Measurement sensorData) {
		float[] values = sensorData.getValues();
		for (int i = 0; i < values.length; i++) {
			PdBase.sendFloat("sensor" + sensorData.getSensorType() + "v" + i, values[i]);
		}
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

	private void finish() {
	}

	private void initPd() throws IOException {
		// Configure the audio glue
		int sampleRate = AudioParameters.suggestSampleRate();
		pdService.initAudio(sampleRate, 1, 2, 10.0f);
		pdService.startAudio();
		start();
// Create and install the dispatcher
		dispatcher = new PdUiDispatcher();
		PdBase.setReceiver(dispatcher);
	}

	private void start() {
		if (!pdService.isRunning()) {
			Intent intent = new Intent(this.activity, this.activity.getClass());
			pdService.startAudio(intent, R.drawable.sensors2pd, "Sensors2PD", "Return to Sensors2PD.");
		}
	}

	private void loadPatch(File pdFile) throws IOException {
// Hear the sound
		if (pdFile != null) {
			try {
				PdBase.openPatch(pdFile.getAbsolutePath());
				Log.e(TAG, "File " + pdFile.getAbsolutePath() + " " + pdFile.getAbsolutePath());
				this.activity.bindService(new Intent(this.activity, PdService.class), pdConnection, this.activity.BIND_AUTO_CREATE);
			} catch (IOException e) {
				Toast.makeText(activity, "The zip file needs a file with the same name inside: patch.zip -> patch.pd", Toast.LENGTH_SHORT).show();
			}
		}
	}

	public boolean setPdFile(File pdFile) {
		this.pdFile = pdFile;
		try {
			loadPatch(this.pdFile);
			return true;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
	}

	public void onResume() {
		this.activity.bindService(new Intent(this.activity, PdService.class), pdConnection, this.activity.BIND_AUTO_CREATE);
	}

	public void onPause() {
		this.activity.unbindService(pdConnection);
	}
}
