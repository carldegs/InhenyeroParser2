package com.inhenyero;

class MessageManager {
    private static MessageManager messageManager = null;
    private final int maxTitleLength = 25;

    private MessageManager(){}
    static MessageManager getInstance(){
        if(messageManager == null){
            messageManager = new MessageManager();
        }

        return messageManager;
    }

    void printTitle(String title){
        System.out.println();
        for(int k = 0; k < maxTitleLength * 2; k++){
            System.out.print("-");
        }
        System.out.println();
        System.out.println(title);
        for(int k = 0; k < maxTitleLength * 2; k++){
            System.out.print("-");
        }
        System.out.println();
    }

    void printHeader(String header){
        printHeader(header, true);
    }

    void printHeader(String header, boolean hasResult){
        System.out.print("> " + header + (hasResult ? "... " : "\n"));
    }

    void printSubheader(String subheader){
        printSubheader(subheader, true, false);
    }

    void printSubheader(String subheader, boolean hasResult){
        printSubheader(subheader, hasResult, false);
    }

    void printSubheader(String subheader, boolean hasResult, boolean isFinal){
        System.out.print("\t" + (isFinal ? "└ " : "├ ") + subheader + (hasResult ? "... " : "\n"));
    }
    
    void printResult(String result){
        System.out.println(result);
    }

    void printStart(){
        System.out.println(Constants.START_TEXT);
        System.out.println();
    }

    void printError(String error){
        System.out.println("ERROR: " + error);
    }
}
