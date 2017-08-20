package com.inhenyero;

public class ReportManager implements JSONKeys {
    private static ReportManager instance = null;

    private ReportManager(){}
    public static ReportManager getInstance(){
        if(instance == null){
            instance = new ReportManager();
        }

        return instance;
    }
}
