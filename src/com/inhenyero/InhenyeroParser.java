package com.inhenyero;

class InhenyeroParser {
    private static MessageManager mMsg;
    private static DataManager mData;
    private static CsvManager mCsv;
    private static ImageManager mImg;
    private static ReportManager mReport;
    private static boolean renameImages = false;
    private static boolean generateCsv = false;
    private static boolean printReport = true;
    private static boolean isDebug = false;
    private static String separator = ";";

    private static void initialize(){
        mMsg = MessageManager.getInstance();
        mData = DataManager.getInstance();
        mCsv = generateCsv ? CsvManager.getInstance() : null;
        mImg = renameImages ? ImageManager.getInstance() : null;
        mReport = renameImages && printReport ? ReportManager.getInstance() : null;
    }

    public static void main(String[] args){
        for(String arg : args){
            switch(arg){
                case "-rename":
                    renameImages = true;
                    break;
                case "-csv":
                    generateCsv = true;
                    break;
                case "-noreport":
                    printReport = false;
                    break;
                case "-debug":
                    isDebug = true;
                    break;
                case "-comma":
                    separator = ",";
            }
        }

        if(args.length == 0){
            generateCsv = true;
            printReport = false;
        }

        initialize();

        mMsg.printStart();

        mMsg.printTitle("Starting DataManager");
        if(!mData.start(isDebug)){
            mMsg.printError("Error in DataManager");
            return;
        }

        if(generateCsv) {
            mMsg.printTitle("Starting CsvManager");
            if (!mCsv.start()) {
                mMsg.printError("Error in CsvManager");
                return;
            }
        }

        if(renameImages) {
            mMsg.printTitle("Starting ImageManager");
            if(!mImg.start()){
                mMsg.printError("Error in ImageManager");
                return;
            }

            if(printReport){
                mMsg.printTitle("Starting ReportManager");
                if(!mReport.compile()){
                    mMsg.printError("Error in ReportManager");
                    return;
                }
            }
        }
    }

}
