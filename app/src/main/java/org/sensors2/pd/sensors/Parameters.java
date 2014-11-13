package org.sensors2.pd.sensors;

/**
 * Created by thomas on 14.11.14.
 */
public class Parameters implements org.sensors2.common.sensors.Parameters {
	private final int sensorType;
	private final int dimensions;

	public Parameters(int sensorType){
		this.sensorType = sensorType;
		switch (sensorType){
			// 1 int TYPE_ACCELEROMETER A constant describing an accelerometer sensor type.
			case 1:
				this.dimensions = 3;
				break;
			// 2 int TYPE_MAGNETIC_FIELD A constant describing a magnetic field sensor type.
			case 2:
				this.dimensions = 3;
				break;
			// 3 int TYPE_ORIENTATION This constant was deprecated in API level 8. use SensorManager.getOrientation() instead.
			case 3:
				this.dimensions = 3;
				break;
			// 4 int TYPE_GYROSCOPE A constant describing a gyroscope sensor type
			case 4:
				this.dimensions = 3;
				break;
			// 5 int TYPE_LIGHT A constant describing a light sensor type.
			case 5:
				this.dimensions = 1;
				break;
			// 6 int TYPE_PRESSURE A constant describing a pressure sensor type
			case 6:
				this.dimensions = 1;
				break;
			// 7 int TYPE_TEMPERATURE This constant was deprecated in API level 14. use Sensor.TYPE_AMBIENT_TEMPERATURE instead.
			case 7:
				this.dimensions = 1;
				break;
			// 8 int TYPE_PROXIMITY A constant describing a proximity sensor type.
			case 8:
				this.dimensions = 1;
				break;
			// 9 int TYPE_GRAVITY A constant describing a gravity sensor type.
			case 9:
				this.dimensions = 3;
				break;
			// 10 int TYPE_LINEAR_ACCELERATION A constant describing a linear acceleration sensor type.
			case 10:
				this.dimensions = 3;
				break;
			// 11 int TYPE_ROTATION_VECTOR A constant describing a rotation vector sensor type.
			case 11:
				this.dimensions = 4;
				break;
			// 12 int TYPE_RELATIVE_HUMIDITY A constant describing a relative humidity sensor type.
			case 12:
				this.dimensions = 1;
				break;
			// 13 int TYPE_AMBIENT_TEMPERATURE A constant describing an ambient temperature sensor type
			case 13:
				this.dimensions = 1;
				break;
			// 14 int TYPE_MAGNETIC_FIELD_UNCALIBRATED A constant describing an uncalibrated magnetic field sensor type.
			case 14:
				this.dimensions = 6;
				break;
			// 15 int TYPE_GAME_ROTATION_VECTOR A constant describing an uncalibrated rotation vector sensor type.
			case 15:
				this.dimensions = 3;
				break;
			// 16 int TYPE_GYROSCOPE_UNCALIBRATED A constant describing an uncalibrated gyroscope sensor type.
			case 16:
				this.dimensions = 6;
				break;
			default:
				throw new IllegalArgumentException();
		}
	}


	@Override
	public int getSensorType() {
		return this.sensorType;
	}

	@Override
	public int getDimensions() {
		return this.dimensions;
	}
}