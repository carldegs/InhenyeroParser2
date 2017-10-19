package com.inhenyero;

import com.sun.xml.internal.bind.v2.runtime.reflect.opt.Const;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

class CsvManager implements JSONKeys{
    private static CsvManager instance = null;

    private CsvManager(){}
    static CsvManager getInstance(){
        if(instance == null){
            instance = new CsvManager();
        }

        return instance;
    }

    public boolean start(){
        MessageManager mMsg = MessageManager.getInstance();
        JSONArray departments = DataManager.getInstance().getDepartments();
        JSONArray book = new JSONArray();

        mMsg.printHeader("Generating Spreads", false);
        for(int k = 0; k < departments.length(); k++){
            JSONObject cDept = (JSONObject) departments.get(k);
            JSONArray cSubs = cDept.getJSONArray(DEPT_SUBS);
            JSONArray cSpreads = new JSONArray();
            JSONObject cBook = (new JSONObject()).put(SPREAD_NAME, cDept.getString(DEPT_NAME));
            boolean isLeft = true;
            int deptSize = cSubs.length();

            JSONObject cSpread = new JSONObject();
            for(String courseHasSubheader : Constants.COURSE_WITH_SUBHEADERS){
                if(courseHasSubheader.equals(cDept.getString(DEPT_NAME))){
                    isLeft = false;
                    cSpread.put(SPREAD_LEFT, new JSONObject());
                    deptSize++;
                    break;
                }
            }

            for(int j = 0; j < cSubs.length(); j++){
                JSONObject cSub = (JSONObject) cSubs.get(j);
                if(isLeft){
                    cSpread = new JSONObject();
                    cSpread.put(SPREAD_LEFT, cSub);
                } else {
                    cSpread.put(SPREAD_RIGHT, cSub);
                    cSpreads.put(cSpread);
                }

                isLeft = !isLeft;
            }

            if(deptSize % 2 == 1){
                cSpread.put(SPREAD_RIGHT, new JSONObject());
                cSpreads.put(cSpread);
            }

            cBook.put(SPREADS, cSpreads);
            book.put(cBook);

            mMsg.printSubheader("Added " + cBook.getJSONArray(SPREADS).length() + " spreads to " + cBook.getString(SPREAD_NAME), false, k == departments.length() - 1);
        }

        mMsg.printHeader("Exporting to CSV files", false);
        for(int k = 0; k < departments.length(); k++){
            String cDeptName = book.getJSONObject(k).getString(DEPT_NAME);
            JSONArray cSpreads = book.getJSONObject(k).getJSONArray(SPREADS);

            try {
                if(k != departments.length() - 1) {
                    mMsg.printSubheader("Creating file " + cDeptName + ".csv");
                } else {
                    mMsg.printSubheader("Creating file " + cDeptName + ".csv", true, true);
                }

                File publicDir = new File(Constants.PUBLIC_DIRECTORY_PATH);
                if(!publicDir.exists()){
                    publicDir.mkdir();
                }

                File outputDir = new File(Constants.OUTPUT_DIRECTORY_PATH);
                if(!outputDir.exists()){
                    outputDir.mkdir();
                }

                File csvFile = new File(Constants.OUTPUT_DIRECTORY_PATH + File.separatorChar + cDeptName + ".csv");
                csvFile.createNewFile();

                FileWriter writer = new FileWriter(csvFile);

                // Header row
                writer.write(Constants.SPREAD_FILE_HEADER);

                for(int m = 0; m < cSpreads.length(); m++){
                    JSONObject cSpread = (JSONObject) cSpreads.get(m);
                    writer.write(getSpreadCsv(cSpread));
                    writer.flush();
                }
                writer.close();
                mMsg.printResult("PASS");
            } catch (IOException e) {
                mMsg.printResult("FAIL");
                e.printStackTrace();
                return false;
            }
        }

        return true;
    }
    static String csvSeparator = ",";
    private static String getSpreadCsv(JSONObject spread) {
        String res = "";
        res += getPageCsv(spread.getJSONObject(SPREAD_LEFT)) + csvSeparator;
        res += getPageCsv(spread.getJSONObject(SPREAD_RIGHT)) + "\n";
        return res;
    }

    private static String getPageCsv(JSONObject page){
        String res = "";
        if(page.has(NAME) && !page.getJSONObject(NAME).getString(LAST_NAME).isEmpty()){
            JSONObject name = page.getJSONObject(NAME);
            JSONArray orgs = page.getJSONArray(ORGS);

            res += csvcell(name.getString(LAST_NAME).toUpperCase()) + csvSeparator +
                    csvcell(name.getString(FIRST_NAME).toUpperCase()) + csvSeparator +
                    csvcell(name.getString(MIDDLE_NAME).toUpperCase()) + csvSeparator;

            for(int k = 0; k < 3; k++){
                JSONObject org = (JSONObject) orgs.get(k);
                res += csvcell(org.getString(ORG_NAME)) + csvSeparator +
                        csvcell(org.getString(ORG_POSITION)) + csvSeparator;
            }

            res += csvcell(page.getString(WRITEUP)) + csvSeparator;
            res += getPhotoPath(page, "SABLAY") + csvSeparator +
                    getPhotoPath(page, page.getString(PACKAGE_TYPE).equals("A") ? "FORMAL" : "CREATIVE") + csvSeparator +
                    getPhotoPath(page, "TOGA");
        } else {
            for(int k = 0; k < 12; k++){
                res += csvSeparator;
            }
        }

        return res;
    }

    private static String csvcell(String str){
        if(str.isEmpty()){
            return str;
        }

        if(str.charAt(0) != '"' || str.charAt(str.length() - 1) != '"'){
            // Not enclosed by double quotes
            // Replace all double quotes inside with double double quotes
            str.replaceAll("\"", "\"\"");
            str = "\"" + str + "\"";
        }
        return str;
    }

    private static String getPhotoPath(JSONObject page, String name) {
        return "/images/" + page.getString(COURSE) + "/" + page.getString(STUDENT_NUM) + "/" + name + ".jpg";
    }

}
