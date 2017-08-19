package com.inhenyero;

import org.json.JSONArray;

public class InhenyeroParser {
    private static MessageManager mMsg;
    private static DataManager mData;

    static void initialize(){
        mMsg = new MessageManager();
        mData = new DataManager();
    }

    public static void main(String[] args){
        initialize();

        mMsg.printStart();

        mMsg.printTitle("Starting DataManager");
        if(!mData.start()){
            mMsg.printError("Error in DataManager");
            return;
        }

        System.out.println(mData.getDepartments());
    }

}
