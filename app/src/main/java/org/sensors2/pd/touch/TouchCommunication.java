package org.sensors2.pd.touch;

import android.view.MotionEvent;
import android.view.View;

import org.sensors2.common.dispatch.DataDispatcher;
import org.sensors2.common.dispatch.Measurement;
import org.sensors2.common.touch.TouchActivity;

/**
 * Created by thomas on 04.04.15.
 */
public class TouchCommunication implements View.OnTouchListener {
	private final DataDispatcher dispatcher;

	public TouchCommunication(TouchActivity activity) {
		this.dispatcher = activity.getDispatcher();
		activity.getTouchView().setOnTouchListener(this);
	}

	private void sendResult(MotionEvent motionEvent) {
		this.dispatcher.dispatch(new Measurement(motionEvent));
	}

	@Override
	public boolean onTouch(View view, MotionEvent motionEvent) {
		if (motionEvent.getAction() !=  MotionEvent.ACTION_DOWN && motionEvent.getAction() != MotionEvent.ACTION_POINTER_DOWN){
			return false;
		}
		this.sendResult(motionEvent);
		return false;
	}
}
