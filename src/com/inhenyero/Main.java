package com.inhenyero;

import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        // 1. Place masterdata.csv file in the /public folder
        System.out.print("Checking if masterdata.csv file is in /public... ");

        try {
            String filePath = new File("public/masterdata.csv").getAbsolutePath();
            FileReader file = new FileReader(filePath);
            Scanner scanner = new Scanner(file);
            scanner.useDelimiter(";");
            System.out.println(" PASS!");


            System.out.print("Creating subscriber objects... ");
            ArrayList<Subscriber> subscribers = new ArrayList<>();
            boolean receivedStudentNum = false;
            String tempStudentNum = "";

            while(scanner.hasNext()){
                Subscriber sub = new Subscriber();
                // 2. Format to follow:
                // studentNum | lastname | firstname | middlename | course | org1Name | org1Pos |
                // org2Name | org2Pos | org3Name | org3Pos | email | mobile | birthday | telephone |
                // address | writeup | packageType = 18 columns
                sub.studentNum = scanner.next();
                if(sub.studentNum.charAt(0) == '/'){
                    sub.studentNum = sub.studentNum.substring(3);
                }

                if(!scanner.hasNext()){
                    break;
                }

                sub.name[0] = scanner.next();
                sub.name[1] = scanner.next();
                sub.name[2] = scanner.next();
                sub.course = scanner.next();
                sub.org1[0] = scanner.next();
                sub.org1[1] = scanner.next();
                sub.org2[0] = scanner.next();
                sub.org2[1] = scanner.next();
                sub.org3[0] = scanner.next();
                sub.org3[1] = scanner.next();
                sub.email = scanner.next();
                sub.mobile = scanner.next();
                sub.birthday = scanner.next();
                sub.telephone = scanner.next();
                sub.address = scanner.next();
                sub.writeup = scanner.next();
                String tempStr = scanner.next();
                while(tempStr.length() > 1){
                    sub.writeup = sub.writeup + ";" + tempStr;
                    tempStr = scanner.next();
                }

                sub.packageType = tempStr;
                subscribers.add(sub);
            }

            System.out.println(" " + subscribers.size() + " subscribers created!");



            System.out.println("Splitting into departments... ");
            String currDeptName = "";
            ArrayList<Subscriber> mDeptSubs = new ArrayList<>();
            Department mDept = new Department(currDeptName);
            ArrayList<Department> departments = new ArrayList<>();
            boolean firstTime = true;
            for(int k = 0; k < subscribers.size(); k++){
                Subscriber mSub = subscribers.get(k);
                if(!mSub.course.equals(currDeptName)){
                    if(!firstTime){
                        mDept.subscribers = mDeptSubs;
                        departments.add(mDept);
                        System.out.println("\t Added " + mDept.name + " with " + mDept.subscribers.size() + " subscribers.");
                    }
                    currDeptName = mSub.course;
                    mDeptSubs = new ArrayList<>();
                    mDept = new Department(currDeptName);
                    firstTime = false;
                }

                mDeptSubs.add(new Subscriber(mSub));
            }

            mDept.subscribers = mDeptSubs;
            departments.add(mDept);
            System.out.println("\t Added " + mDept.name + " with " + mDept.subscribers.size() + " subscribers.");


            System.out.println("Generating CSV Files... ");
            for(int k = 0; k < departments.size(); k++){
                Department cDept = departments.get(k);
                ArrayList<Spread> spreads = new ArrayList<>();
                boolean isLeft = true;
                int deptSize = cDept.subscribers.size();
                boolean hasSubheader = false;

                Spread spread = new Spread();
                if( cDept.name.equals("BSCoE") ||
                        cDept.name.equals("BSECE") ||
                        cDept.name.equals("BSEE")  ||
                        cDept.name.equals("BSMatE") ||
                        cDept.name.equals("BSEM") ||
                        cDept.name.equals("BSMetE")){
                    isLeft = false;
                    hasSubheader = true;
                    spread.left = new Subscriber();
                    deptSize++;
                }

                for(Subscriber cSub : cDept.subscribers){
                    if(isLeft){
                        spread = new Spread();
                        spread.left = new Subscriber(cSub);
                    } else {
                        spread.right = new Subscriber(cSub);
                        spreads.add(spread);
                    }

                    isLeft = !isLeft;
                }

                if(deptSize % 2 == 1){
                    spread.right = new Subscriber();
                    spreads.add(spread);
                }

                departments.get(k).spreads = spreads;
                System.out.println("\t Added " + departments.get(k).spreads.size() + " spreads to " + departments.get(k).name + ".");
            }

            System.out.println("Export to csv files...");
            String expFilePath = new File("public/output").getAbsolutePath();
            for(int j = 0; j < departments.size(); j++){
                Department cDept = departments.get(j);
                String csvPath = expFilePath + "\\" + cDept.name + ".csv";

                System.out.print("\t" + "Creating file " + cDept.name + ".csv... ");
                try {
                    File csvFile = new File(csvPath);
                    csvFile.createNewFile();

                    FileWriter writer = new FileWriter(csvFile);

                    // Header row
                    writer.write("lastNameLeft;firstNameLeft;middleinitialLeft;" +
                            "org1Left;pos1Left;org2Left;pos2Left;org3Left;pos3Left;" +
                            "writeupLeft;@pic1Left;@pic2Left;@pic3Left;" +
                            "lastNameRight;firstNameRight;middleinitialRight;" +
                            "org1Right;pos1Right;org2Right;pos2Right;org3Right;pos3Right;" +
                            "writeupRight;@pic1Right;@pic2Right;@pic3Right;\n");

                    for(int m = 0; m < cDept.spreads.size(); m++){
                        Spread cSpread = cDept.spreads.get(m);
                        writer.write(cSpread.getCsv());
                        writer.flush();
                    }
                    writer.close();
                    System.out.println("DONE!");
                } catch (IOException e){
                    System.out.println("FAIL!");
                }
            }

            System.out.print("Renaming image files...");
            ArrayList<String> report = new ArrayList<>();
            for(Department cDept : departments){
                String deptFolderPath = new File("public/images").getAbsolutePath() + File.separatorChar + cDept.name;
                File deptFolder  = new File(deptFolderPath);

                if(deptFolder.isDirectory()){
                    File[] subFolders = deptFolder.listFiles();
                    for(Subscriber cSub : cDept.subscribers){
                        ArrayList<File> sameSurname = new ArrayList<>();
                        File cSubFolder = null;
                        System.out.println("dept directory: " + deptFolder.getName());

                        // Compare surname of subscriber and available folders.
                        String fSurname, fFirstname;
                        for(File subFolder : subFolders) {
                            if(subFolder.getName().toUpperCase().charAt(0) < cSub.name[0].toUpperCase().charAt(0)){
                                continue;
                            } else if(subFolder.getName().toUpperCase().charAt(0) > cSub.name[0].toUpperCase().charAt(0)){
                                break;
                            }

                            if (subFolder.getName().charAt(0) != '2') {
                                if(subFolder.getName().split(",").length > 1 &&
                                        !subFolder.getName().toUpperCase().contains("BARKADA")) {
                                    fSurname = subFolder.getName().split(",")[0].replace(" ", "").toUpperCase();

                                    if (cSub.name[0].replace(" ", "").toUpperCase().compareTo(fSurname) == 0) {
                                        sameSurname.add(subFolder);
                                    }
                                } else {
                                    // Delete folder and contents
                                    String[] deleteFiles = subFolder.list();
                                    if(deleteFiles != null) {
                                        for (String str : deleteFiles) {
                                            File currFile = new File(subFolder.getPath(), str);
                                            currFile.delete();
                                        }
                                    }
                                    subFolder.delete();
                                }
                            }
                        }

                        if(sameSurname.size() == 1){
                            if(sameSurname.get(0).isDirectory()) {
                                cSubFolder = sameSurname.get(0);
                            }
                        } else {
                            for(File fDir : sameSurname){
                                fFirstname = fDir.getName().split(",")[1].replace(" ", "").toUpperCase();

                                if(cSub.name[1].replace(" ","").toUpperCase().compareTo(fFirstname) == 0 &&
                                        fDir.isDirectory()) {
                                    cSubFolder = fDir;
                                }
                            }
                        }

                        // rename subscribers's folder and images
                        if(cSubFolder != null && cSubFolder.isDirectory()){
                            System.out.println("sub directory: " + cSubFolder.getName() + "(" + cSub.name[0] + ", " + cSub.name[1] + ")");

                            File[] images = cSubFolder.listFiles();

                            if(images != null){
                                for(File img : images){
                                    if(img.getName().substring(img.getName().lastIndexOf('.') + 1).equals("jpg")){
                                        String newName = img.getName().replace('.', ' ').split(" ")[0] + ".jpg";
                                        img.renameTo(new File(cSubFolder + File.separator + newName));
                                    }
                                }
                            } else {
//                                System.out.println("NOIMGS: " + cSub.name[0] + ", " + cSub.name[1]);
                                report.add("NOIMGS: " + cSub.name[0] + ", " + cSub.name[1]);
                            }

                            cSubFolder.renameTo(new File(deptFolder + File.separator + cSub.studentNum));
                        } else {
//                            System.out.println("NOSUBDIR: " + cSub.name[0] + ", " + cSub.name[1]);
                            report.add("NOSUBDIR: " + cSub.name[0] + ", " + cSub.name[1] + "(" + cSub.course + ")");
                        }
                    }
                } else {
//                    System.out.println("NO DIR: " + cDept.name);
                    report.add("NO DIR: " + cDept.name);
                }
            }

            if(report.size() > 0){
                System.out.println(" DONE!");
                System.out.print("Saving errors...");

                try {
                    String errPath = new File("public/output").getAbsolutePath() + "\\errors.txt";
                    File errFile = new File(errPath);
                    errFile.createNewFile();

                    FileWriter writer = new FileWriter(errFile);
                    for(String rep : report){
                        writer.write(rep + "\n");
                        writer.flush();
                    }
                    writer.close();
                    System.out.println(" DONE!");
                } catch(IOException e){
                    System.out.println(" FAIL!");
                }
            } else {
                System.out.println(" DONE!");
            }

            System.out.println("PARSING DONE" + (report.size() > 0 ? (" WITH " + report.size() + " ERRORS!") : "!"));
        } catch (java.io.IOException e) {
            System.out.println(" FAIL!");
        }
    }
}
