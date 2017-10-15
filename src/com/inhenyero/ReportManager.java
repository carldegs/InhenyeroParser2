package com.inhenyero;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

class ReportManager implements JSONKeys {
    private static ReportManager instance = null;
    private JSONArray reports = new JSONArray();

    private File reportFile;

    public final int NO_DEPARTMENT_FOLDER = 0;
    public final int NO_SUBSCRIBER_FOLDER = 1;
    public final int FOLDER_RENAMED = 2;
    public final int FOLDER_ALREADY_RENAMED = 3;
    public final int FOLDER_DELETED = 4;
    private int error = 0;

    private ReportManager(){}
    static ReportManager getInstance(){
        if(instance == null){
            instance = new ReportManager();
        }

        return instance;
    }

    public void add(int type, String name){ add(type, (new JSONObject()).put(DEPT_NAME, name), null); }
    public void add(int type, JSONObject name){ add(type, name, null); }
    public void add(int type, JSONObject name, String otherInfo){
        JSONObject report = new JSONObject();
        report.put(TYPE, type);
        report.put(NAME, name);
        report.put(DETAIL, otherInfo);
        reports.put(report);
    }

    public boolean compile(){
        try {
            MessageManager.getInstance().printHeader("Compiling report");
            reportFile = new File(Constants.REPORT_FILE_PATH);
            reportFile.delete();
            if(reportFile.createNewFile()){
                FileWriter writer = new FileWriter(reportFile);

                DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
                Date date = new Date();
                writer.write("Report created at " + dateFormat.format(date) + "\n\n");
                writer.flush();

                String[] titles = {"NO DEPARTMENT FOLDER",
                        "NO SUBSCRIBER FOLDER",
                        "RENAMED FOLDERS",
                        "FOLDERS ALREADY RENAMED",
                        "DELETED FOLDERS"
                };

                int[] types = {
                        NO_DEPARTMENT_FOLDER,
                        NO_SUBSCRIBER_FOLDER,
                        FOLDER_RENAMED,
                        FOLDER_ALREADY_RENAMED,
                        FOLDER_DELETED
                };

                for(int k = 0; k < titles.length; k++){
                    writer.write(writeReport(titles[k], types[k]));
                    writer.flush();
                }

                writer.write("\n" + (error == 0 ? "no" : error) +" error"+ (error != 1 ? "s" : ""));
                MessageManager.getInstance().printResult("DONE");
                return true;
            } else {
                return false;
            }
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    private String writeReport(String title, int type){
        String compiledReport = "";
        int numReport = 0;
        JSONObject name;

        for(int k = 0; k < reports.length(); k++){
            JSONObject report = reports.getJSONObject(k);
            if(report.has(TYPE) && type == report.getInt(TYPE)){
                numReport++;
                switch (report.getInt(TYPE)){
                    case NO_DEPARTMENT_FOLDER:
                        compiledReport += report.getJSONObject(NAME).getString(DEPT_NAME);
                        error++;
                        break;
                    case NO_SUBSCRIBER_FOLDER:
                        name = report.getJSONObject(NAME);
                        compiledReport += name.getString(LAST_NAME) + ", " +
                                name.getString(FIRST_NAME) + " (" +
                                report.getString(DETAIL) + ")";
                        error++;
                        break;
                    case FOLDER_RENAMED:
                    case FOLDER_ALREADY_RENAMED:
                        name = report.getJSONObject(NAME);
                        compiledReport += name.getString(LAST_NAME) + ", " +
                                name.getString(FIRST_NAME) + " (" +
                                report.getString(DETAIL) + ")";
                        break;
                    case FOLDER_DELETED:
                        compiledReport += report.getJSONObject(NAME).getString(DEPT_NAME);
                }
                compiledReport += "\n";
            }
        }

        if(numReport > 0){
            compiledReport = printTitle(title) + compiledReport;
        }

        return compiledReport;
    }

    String printTitle(String title){
        String str = "\n";
        str += title + "\n";
        for(int k = 0; k < 25; k++){
            str += "-";
        }
        str += "\n";
        return str;
    }
}
