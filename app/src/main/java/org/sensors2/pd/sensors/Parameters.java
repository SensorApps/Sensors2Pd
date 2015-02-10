package org.sensors2.pd.sensors;

import android.hardware.Sensor;

/**
 * Created by thomas on 14.11.14.
 */
public class Parameters extends org.sensors2.common.sensors.Parameters {

	private final Sensor sensor;

	public Sensor getSensor() {
		return sensor;
	}

	public Parameters(Sensor sensor) {
		super(sensor.getType());
		this.sensor = sensor;
	}
}