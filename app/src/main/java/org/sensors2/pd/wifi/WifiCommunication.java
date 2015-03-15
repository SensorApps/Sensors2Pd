package org.sensors2.pd.wifi;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;

import org.sensors2.common.dispatch.Measurement;
import org.sensors2.common.sensors.DataDispatcher;
import org.sensors2.common.wifi.WifiActivity;

import java.util.List;

/**
 * Created by thomas on 15.03.15.
 */
public class WifiCommunication {

	private final DataDispatcher dispatcher;
	private final WifiReceiver receiver;
	private final WifiManager wifiManager;

	public WifiCommunication(WifiActivity activity) {
		this.dispatcher = activity.getDispatcher();
		this.wifiManager = activity.getWifiManager();
		this.receiver = new WifiReceiver();
		activity.registerReceiver(receiver, new IntentFilter(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION));
		wifiManager.startScan();
	}

	public void sendResult(ScanResult scanResult) {
		this.dispatcher.dispatch(new Measurement(scanResult));
	}

	private class WifiReceiver extends BroadcastReceiver {
		@Override
		public void onReceive(Context context, Intent intent) {
			List<ScanResult> results = wifiManager.getScanResults();
			wifiManager.startScan();
			for (ScanResult scanResult : results) {
				sendResult(scanResult);
			}
		}
	}
}
