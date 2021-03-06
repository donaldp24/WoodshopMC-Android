package com.tim.WoodshopMC;

import android.content.Intent;
import com.tim.WoodshopMC.Database.DataManager;
import com.tim.WoodshopMC.Database.FSReading;
import com.tim.WoodshopMC.Global.CommonDefs;
import com.tim.WoodshopMC.Global.CommonMethods;
import com.tim.WoodshopMC.Global.GlobalData;
import com.tim.WoodshopMC.Scan.ReadingParser;
import com.tim.WoodshopMC.Scan.ScanManager;
import com.tim.WoodshopMC.Scan.ScanManagerListener;

import java.util.HashMap;

/**
 * Created by donald on 6/19/14.
 */
public class ScanManagerListenerInstance implements ScanManagerListener {
    public static ScanManagerListenerInstance _instance = null;

    private ScanManagerListenerInstance()
    {
        //
    }

    public static ScanManagerListenerInstance sharedInstance() {
        if (_instance == null)
            _instance = new ScanManagerListenerInstance();
        return _instance;
    }

    @Override
    public void didFindSensor(ScanManager scanManager, HashMap<String, Object> sensorData)
    {
        GlobalData globalData = GlobalData.sharedData();
        if (globalData.isSaved == false)
            return;


        // save new data

        FSReading reading = new FSReading();
        reading.readID = 0;
        reading.readLocProductID = globalData.selectedLocProductID;
        reading.readTimestamp = CommonMethods.str2date((String)sensorData.get(ReadingParser.kSensorDataReadingTimestampKey), CommonDefs.DATETIME_FORMAT);
        reading.readUuid = (String)sensorData.get(ReadingParser.kSensorDataUuidKey);
        reading.readRH = (Integer)(sensorData.get(ReadingParser.kSensorDataRHKey));
        reading.readConvRH = (Float)(sensorData.get(ReadingParser.kSensorDataConvRHKey));
        reading.readTemp = (Integer)(sensorData.get(ReadingParser.kSensorDataTemperatureKey));
        reading.readConvTemp = (Float)(sensorData.get(ReadingParser.kSensorDataConvTempKey));
        reading.readBattery = (Integer)(sensorData.get(ReadingParser.kSensorDataBatteryKey));
        reading.readDepth = (Integer)(sensorData.get(ReadingParser.kSensorDataDepthKey));
        reading.readGravity = (Integer)(sensorData.get(ReadingParser.kSensorDataGravityKey));
        reading.readMaterial = (Integer)(sensorData.get(ReadingParser.kSensorDataMaterialKey));
        reading.readMC = (Integer)(sensorData.get(ReadingParser.kSensorDataMCKey));
        DataManager.sharedInstance().addReadingToDatabase(reading);

        if (GlobalData._currentActivity != null)
        {
            GlobalData._currentActivity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    /*if (GlobalData._readingActivity != null)
                    {
                        GlobalData._readingActivity.initDateTable();
                        GlobalData._readingActivity.scrolltoEndList();
                        GlobalData._readingActivity.showWarning();
                    }
                    else*/
                    {
                        Intent readingIntent = new Intent(GlobalData._mainContext, ReadingActivity.class);
                        readingIntent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                        readingIntent.putExtra(CommonDefs.ACTIVITY_TAG_LOC_PRODUCT_ID, GlobalData.sharedData().selectedLocProductID);
                        readingIntent.putExtra(CommonDefs.ACTIVITY_TAG_FRRECORD, true);
                        GlobalData._currentActivity.startActivity(readingIntent);
                        GlobalData.bFromRecord = true;
                    }
                }
            });
        }
        //
    }

    @Override
    public void didFindThirdPackage(ScanManager scanManager)
    {
        GlobalData globalData = GlobalData.sharedData();
        if (globalData.isSaved == false)
            return;
    }

    @Override
    public void scanManagerDidStartScanning(ScanManager scanManager)
    {
        GlobalData globalData = GlobalData.sharedData();
        if (globalData.isSaved == false)
            return;
    }

    @Override
    public boolean isSame(HashMap<String, Object> beforeData, HashMap<String, Object> currData)
    {
        if (beforeData == null)
        {
            if (currData == null)
                return true;
            else
                return false;
        }
        FSReading beforeReading = new FSReading();


        beforeReading.readUuid = (String)beforeData.get(ReadingParser.kSensorDataUuidKey);
        beforeReading.readRH = (Integer)(beforeData.get(ReadingParser.kSensorDataRHKey));
        beforeReading.readConvRH = (Float)(beforeData.get(ReadingParser.kSensorDataConvRHKey));
        beforeReading.readTemp = (Integer)(beforeData.get(ReadingParser.kSensorDataTemperatureKey));
        beforeReading.readConvTemp = (Float)(beforeData.get(ReadingParser.kSensorDataConvTempKey));
        beforeReading.readBattery = (Integer)(beforeData.get(ReadingParser.kSensorDataBatteryKey));
        beforeReading.readDepth = (Integer)(beforeData.get(ReadingParser.kSensorDataDepthKey));
        beforeReading.readGravity = (Integer)(beforeData.get(ReadingParser.kSensorDataGravityKey));
        beforeReading.readMaterial = (Integer)(beforeData.get(ReadingParser.kSensorDataMaterialKey));
        beforeReading.readMC = (Integer)(beforeData.get(ReadingParser.kSensorDataMCKey));

        FSReading reading = new FSReading();
        reading.readUuid = (String)currData.get(ReadingParser.kSensorDataUuidKey);
        reading.readRH = (Integer)(currData.get(ReadingParser.kSensorDataRHKey));
        reading.readConvRH = (Float)(currData.get(ReadingParser.kSensorDataConvRHKey));
        reading.readTemp = (Integer)(currData.get(ReadingParser.kSensorDataTemperatureKey));
        reading.readConvTemp = (Float)(currData.get(ReadingParser.kSensorDataConvTempKey));
        reading.readBattery = (Integer)(currData.get(ReadingParser.kSensorDataBatteryKey));
        reading.readDepth = (Integer)(currData.get(ReadingParser.kSensorDataDepthKey));
        reading.readGravity = (Integer)(currData.get(ReadingParser.kSensorDataGravityKey));
        reading.readMaterial = (Integer)(currData.get(ReadingParser.kSensorDataMaterialKey));
        reading.readMC = (Integer)(currData.get(ReadingParser.kSensorDataMCKey));

        if (reading.readMC == beforeReading.readMC &&
                reading.readMaterial == beforeReading.readMaterial &&
                reading.readGravity == beforeReading.readGravity &&
                reading.readDepth == beforeReading.readDepth &&
                reading.readBattery == beforeReading.readBattery &&
                reading.readTemp == beforeReading.readTemp &&
                reading.readRH == beforeReading.readRH)
            return true;
        return false;

    }
}
