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
import org.puredata.core.utils.IoUtils;
import org.sensors2.common.sensors.DataDispatcher;
import org.sensors2.common.sensors.Measurement;
import org.sensors2.pd.R;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

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

	}

	private PdService pdService;
	private final ServiceConnection pdConnection = new ServiceConnection() {
		@Override
		public void onServiceConnected(ComponentName name, IBinder service) {
			pdService = ((PdService.PdBinder)service).getService();
			try {
				initPd();
				loadPatch();
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
			pdService.startAudio(intent, R.drawable.ic_launcher, "S2PD", "Return to S2PD.");
		}
	}

	private void loadPatch() throws IOException {
// Hear the sound
		if (pdFile != null) {
			Log.i(TAG, pdFile.getParentFile()+" <"+pdFile.getName().replace(".zip", ".pd")+">");
			if(pdFile.getAbsolutePath().endsWith(".pd")) {
				PdBase.openPatch(pdFile.getAbsolutePath());
			} else if(pdFile.getAbsolutePath().endsWith(".zip")) {
				InputStream in = null;
				in = new BufferedInputStream(new FileInputStream(pdFile));
								IoUtils.extractZipResource(in, pdFile.getParentFile(), true);
				File patchFile = new File(pdFile.getParentFile()+"/"+pdFile.getName().replace(".zip", ""), pdFile.getName().replace(".zip", ".pd"));
				try {
					PdBase.openPatch(patchFile.getAbsolutePath());
					Log.e(TAG, "File "+pdFile.getAbsolutePath()+" "+patchFile.getAbsolutePath());
				} catch (IOException e) {
					Toast.makeText(activity, "The zip file needs a file with the same name inside: patch.zip -> patch.pd", Toast.LENGTH_SHORT).show();
				}
			} else {
				Toast.makeText(activity, "Invalid file format. Please try .pd or .zip", Toast.LENGTH_SHORT).show();
			}
		}
	}
}
