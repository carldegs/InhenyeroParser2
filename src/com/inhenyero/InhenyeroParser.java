package com.inhenyero;

import org.json.JSONArray;

public class InhenyeroParser {
    private static MessageManager mMsg;
    private static DataManager mData;
    private static CsvManager mCsv;

    static void initialize(){
        mMsg = MessageManager.getInstance();
        mData = DataManager.getInstance();
        mCsv = CsvManager.getInstance();
    }

    public static void main(String[] args){
        initialize();

        mMsg.printStart();

        mMsg.printTitle("Starting DataManager");
        if(!mData.start()){
            mMsg.printError("Error in DataManager");
            return;
        }

        mMsg.printTitle("Starting CsvManager");
        if(!mCsv.start()){

        }

    }

}
