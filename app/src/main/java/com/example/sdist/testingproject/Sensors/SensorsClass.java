package com.example.sdist.testingproject.Sensors;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

/**
 * Created by ezeki on 09/09/2017.
 */

public class SensorsClass implements SensorEventListener{
    SensorManager mSensorManager;
    Sensor tmpSensor, accSensor, lightSensor;
    String SENSOR_TYPE;

    Context context;
    public SensorsClass (Context c){
        context = c;
    }


    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }
    public void resumeAcc(){

    }
    public void resumeTmp(){

    }
    public void resumeLight(){

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
