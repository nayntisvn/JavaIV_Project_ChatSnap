package com.example.sdist.testingproject.Sensors;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.widget.TextView;

/**
 * Created by ezeki on 20/08/2017.
 */

public class SensorsClass implements SensorEventListener {
    SensorManager sensorManager;
    Sensor tmp;
    Sensor acc;
    String sensorType;

    TextView txtTmp, txtSpd;
    Context context;

    public SensorsClass(Context c,TextView temp, TextView speed){
        context = c;
        txtTmp = temp;
        txtSpd = speed;
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        if(sensorEvent.sensor.getType() == Sensor.TYPE_AMBIENT_TEMPERATURE){
            float[] tmpValues = sensorEvent.values.clone();
            txtTmp.setText("AMBIENT TEMPERATURE: " + String.valueOf(tmpValues[0]));
        }
        else if(sensorEvent.sensor.getType() == Sensor.TYPE_ACCELEROMETER){
            float[] accelValues = sensorEvent.values.clone();

            txtSpd.setText("SPEED: "  );
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }

    public void resumeTmp(){
        sensorManager = (SensorManager)context.getSystemService(Context.SENSOR_SERVICE);
        acc = sensorManager.getDefaultSensor(Sensor.TYPE_AMBIENT_TEMPERATURE);
        sensorManager.registerListener(this, acc, SensorManager.SENSOR_DELAY_NORMAL);
        sensorType = "SENSOR_TEMPERATURE";
    }

    public void resumeAcc(){
        sensorManager = (SensorManager)context.getSystemService(Context.SENSOR_SERVICE);
        acc = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        sensorManager.registerListener(this, acc, SensorManager.SENSOR_DELAY_NORMAL);
        sensorType = "SENSOR_ACCELEROMETER";
    }

    public void pause(){
        if (sensorManager != null) {
            if(sensorType.equals("SENSOR_ACCELEROMETER")){
                sensorManager.unregisterListener(this, acc);
            }
            else if (sensorType.equals("SENSOR_TEMPERATURE")){
                sensorManager.unregisterListener(this, tmp);
            }
            sensorManager = null;
        }
    }
}
