package com.example.sdist.testingproject.Sensors;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.widget.TextView;

/**
 * Created by ezeki on 09/09/2017.
 */

public class SensorsClass implements SensorEventListener{
    SensorManager mSensorManager;
    Sensor tmpSensor, accSensor, lightSensor;
    public TextView temperature;
    public boolean isDark;

    Context context;
    public SensorsClass (Context c, TextView txtTmp){
        temperature = txtTmp;
        context = c;
        mSensorManager = (SensorManager)context.getSystemService(Context.SENSOR_SERVICE);
    }


    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        Sensor sensor = sensorEvent.sensor;
        if(sensor.getType()== sensor.TYPE_ACCELEROMETER){

        }
        if(sensor.getType()== sensor.TYPE_AMBIENT_TEMPERATURE){
            temperature.setText(String.valueOf(sensorEvent.values[0]));
        }
        if(sensor.getType()== sensor.TYPE_LIGHT){
            if(sensorEvent.values[0] < 1000){
                isDark = true;
            }
            else
                isDark = false;
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }
    public void resumeAcc(){
        accSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        mSensorManager.registerListener(this, accSensor, SensorManager.SENSOR_DELAY_GAME);
    }
    public void resumeTmp(){
        tmpSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_AMBIENT_TEMPERATURE);
        mSensorManager.registerListener(this, tmpSensor, SensorManager.SENSOR_DELAY_GAME);
    }
    public void resumeLight(){
        lightSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);
        mSensorManager.registerListener(this, lightSensor, SensorManager.SENSOR_DELAY_GAME);
    }

    public void pauseAcc(){
        mSensorManager.unregisterListener(this, accSensor);
    }
    public void pauseTmp(){
        mSensorManager.unregisterListener(this, tmpSensor);
    }
    public void pauseLight(){
        mSensorManager.unregisterListener(this, lightSensor);
    }
}
