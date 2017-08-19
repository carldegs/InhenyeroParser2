package com.inhenyero;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Scanner;

public class DataManager implements JSONKeys{
    private JSONArray departments;
    private MessageManager mMsg;

    public boolean start(){
        FileReader masterdata;
        Scanner scanner;
        mMsg = new MessageManager();
        departments = new JSONArray();

        mMsg.printHeader("Checking if masterdata.csv file is in /public");
        try {
            masterdata = new FileReader(Constants.MASTERDATA_PATH);
            mMsg.printResult("PASS");
        } catch (FileNotFoundException e) {
            mMsg.printResult("FAIL");
            e.printStackTrace();
            return false;
        }

        mMsg.printHeader("Creating subscriber objects");
        scanner = new Scanner(masterdata);
        scanner.useDelimiter(";");

        JSONArray subscribers = new JSONArray();
        while (scanner.hasNext()){
            JSONObject cSub = new JSONObject();

            String studentNum = scanner.next();
            if(studentNum.charAt(0) == '/'){
                studentNum = studentNum.substring(3);
            }
            cSub.put(STUDENT_NUM, studentNum);

            if(!scanner.hasNext()){
                break;
            }

            JSONObject name = new JSONObject();
            name.put(LAST_NAME, scanner.next());
            name.put(FIRST_NAME, scanner.next());
            name.put(MIDDLE_NAME, scanner.next());
            cSub.put(NAME, name);

            cSub.put(COURSE, scanner.next());

            JSONArray orgs = new JSONArray();
            for(int k = 0; k < 3; k++){
                JSONObject org = new JSONObject();
                String orgName = scanner.next();
                String orgPos = scanner.next();

                org.put(ORG_NAME, orgName);
                org.put(ORG_POSITION, orgPos);
                orgs.put(org);
            }
            cSub.put(ORGS, orgs);

            cSub.put(EMAIL, scanner.next());
            cSub.put(MOBILE, scanner.next());
            cSub.put(BIRTHDAY, scanner.next());
            cSub.put(TELEPHONE, scanner.next());
            cSub.put(ADDRESS, scanner.next());

            String writeup = scanner.next();
            String tempStr = scanner.next();
            while(tempStr.length() > 1){
                writeup = writeup + ";" + tempStr;
                tempStr = scanner.next();
            }
            cSub.put(WRITEUP, writeup);
            cSub.put(PACKAGE_TYPE, tempStr);
            subscribers.put(cSub);
        }
        mMsg.printResult(subscribers.length() + " CREATED");

        mMsg.printHeader("Splitting into departments", false);
        String cDeptName = "";
        JSONObject cDept = new JSONObject();
        JSONArray cDeptSubs = new JSONArray();
        boolean firstTime = true;

        for(int k = 0; k < subscribers.length(); k++){
            JSONObject cSub = (JSONObject) subscribers.get(k);
            if(!cSub.getString(COURSE).equals(cDeptName)){
                if(!firstTime){
                    cDept.put(DEPT_SUBS, cDeptSubs);
                    departments.put(cDept);
                    mMsg.printSubheader("Added " + cDeptSubs.length() + " subscribers to " + cDeptName, false);
                }
                cDeptName = cSub.getString(COURSE);
                cDeptSubs = new JSONArray();
                cDept = (new JSONObject()).put(DEPT_NAME, cDeptName);
                firstTime = false;
            }

            cDeptSubs.put(cSub);
        }

        cDept.put(DEPT_SUBS, cDeptSubs);
        departments.put(cDept);
        mMsg.printSubheader("Added " + cDeptSubs.length() + " subscribers to " + cDeptName, false);

        return true;
    }

    public JSONArray getDepartments() {
        return departments;
    }
}
