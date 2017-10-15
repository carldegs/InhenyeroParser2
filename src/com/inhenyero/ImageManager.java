package com.inhenyero;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;

class ImageManager implements JSONKeys {
    private static ImageManager instance = null;

    private ImageManager(){}
    static ImageManager getInstance(){
        if(instance == null){
            instance = new ImageManager();
        }

        return instance;
    }

    public boolean start(){
        ReportManager mReport = ReportManager.getInstance();
        MessageManager mMsg = MessageManager.getInstance();
        JSONArray departments = DataManager.getInstance().getDepartments();
        for(int k = 0; k < departments.length(); k++){
            JSONObject cDept = (JSONObject) departments.get(k);
            File deptDir = new File(Constants.IMAGES_DIRECTORY_PATH + File.separator + cDept.getString(DEPT_NAME));

            mMsg.printSubheader("Renaming images of department " + cDept.getString(DEPT_NAME));
            if(!deptDir.isDirectory()){
                mReport.add(mReport.NO_DEPARTMENT_FOLDER, cDept.getString(DEPT_NAME));
                mMsg.printResult("MISSING");
            } else {
                JSONArray subscribers = cDept.getJSONArray(DEPT_SUBS);
                for(int j = 0; j < subscribers.length(); j++){
                    File[] subDirList = deptDir.listFiles();
                    ArrayList<File> sameSurname = new ArrayList<>();
                    File actualSubDir= null;
                    String dirLastname, dirFirstname;
                    boolean alreadyRenamed = false;

                    // Skip if department directory is empty.
                    if (subDirList != null) {
                        JSONObject subscriber = subscribers.getJSONObject(j);
                        // Find subscriber's directory
                        for(File cSubDir : subDirList){
                            // Check if already renamed, and if the renamed file is for subscriber.
                            if(cSubDir.getName().toUpperCase().charAt(0) == '2'
                                    && cSubDir.getName().toUpperCase().equals(subscriber.getString(STUDENT_NUM))){
                                mReport.add(mReport.FOLDER_ALREADY_RENAMED, subscriber.getJSONObject(NAME), subscriber.getString(STUDENT_NUM));
                                alreadyRenamed = true;
                                break;
                            } else if(cSubDir.getName().toUpperCase().charAt(0) <
                                    subscriber.getJSONObject(NAME).getString(LAST_NAME).replace("ñ","n").toUpperCase().charAt(0)) {
                                continue;
                            } else if(cSubDir.getName().toUpperCase().charAt(0) >
                                    subscriber.getJSONObject(NAME).getString(LAST_NAME).replace("ñ","n").toUpperCase().charAt(0)) {
                                break;
                            } else if(cSubDir.getName().toUpperCase().contains("BARKADA")){
                                rmdir(cSubDir);
                                mReport.add(mReport.FOLDER_DELETED, cSubDir.getName());
                            }

                            if(cSubDir.getName().split(",").length > 1){
                                dirLastname = cSubDir.getName().split(",")[0].replace(" ", "").toUpperCase().replace("Ñ", "N");

                                if(subscriber.getJSONObject(NAME).getString(LAST_NAME).replace(" ", "").replace("ñ","n").toUpperCase().compareTo(dirLastname) == 0){
                                    sameSurname.add(cSubDir);
                                }
                            }
                        }

                        // Finalize subscriber's directory
                        if(!alreadyRenamed) {
                            if(sameSurname.size() == 1) {
                                actualSubDir = sameSurname.get(0);
                            } else {
                                for(File dir : sameSurname){
                                    dirFirstname = dir.getName().split(",")[1].replace(" ", "").toUpperCase().replace("Ñ", "N");
                                    if(subscriber.getJSONObject(NAME).getString(FIRST_NAME).replace(" ","").replace("ñ","n").toUpperCase().compareTo(dirFirstname) == 0
                                            && dir.isDirectory()){
                                        actualSubDir = dir;
                                    }
                                }
                            }

                            // Rename subscriber's directory and images]
                            if(actualSubDir != null && actualSubDir.isDirectory()){
                                File[] images = actualSubDir.listFiles();
                                if(images != null){
                                    for(File cImage : images){
                                        if (cImage.getName().substring(cImage.getName().lastIndexOf('.') + 1).equals("jpg")) {
                                            String newName = cImage.getName().replace('.', ' ').split(" ")[0] + ".jpg";
                                            cImage.renameTo(new File(actualSubDir + File.separator + newName));
                                        }
                                    }
                                }

                                if(actualSubDir.renameTo(new File(deptDir + File.separator + subscriber.getString(STUDENT_NUM)))){
                                    mReport.add(mReport.FOLDER_RENAMED, subscriber.getJSONObject(NAME), subscriber.getString(STUDENT_NUM));
                                }
                            } else {
                                mReport.add(mReport.NO_SUBSCRIBER_FOLDER, subscriber.getJSONObject(NAME), subscriber.getString(COURSE));
                            }
                        }


                    } else {
                        mReport.add(mReport.NO_DEPARTMENT_FOLDER, cDept.getString(DEPT_NAME));
                        mMsg.printResult("MISSING");
                    }
                }

                mMsg.printResult("DONE");
            }
        }
        return true;
    }

    private void rmdir(File dir){
        String[] files = dir.list();
        if(files != null){
            for(String f : files){
                File currFile = new File(dir.getPath(), f);
                currFile.delete();
            }
        }
        dir.delete();
    }
}
