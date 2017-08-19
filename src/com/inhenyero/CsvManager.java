package com.inhenyero;

import org.json.JSONArray;

/**
 * Created by Carl on 20 Aug 2017.
 */
public class CsvManager {
    private static CsvManager csvManager = null;

    private CsvManager(){}
    public static CsvManager getInstance(){
        if(csvManager == null){
            csvManager = new CsvManager();
        }

        return csvManager;
    }

    public static boolean start(){
        MessageManager mMsg = MessageManager.getInstance();
        JSONArray departments = DataManager.getInstance().getDepartments();
        return true;
    }

}
