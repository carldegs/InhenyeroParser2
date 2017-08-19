package com.inhenyero;

/**
 * Created by Carl on 19 Aug 2017.
 */
public class MessageManager {

    public void printTitle(String title){

    }

    public void printHeader(String header){
        printHeader(header, true);
    }

    public void printHeader(String header, boolean hasResult){
        System.out.print(header + (hasResult ? "... " : "\n"));
    }

    public void printSubheader(String subheader){
        printSubheader(subheader, true);
    }

    public void printSubheader(String subheader, boolean hasResult){
        System.out.print("\t" + subheader + (hasResult ? "..." : "\n"));
    }
    
    public void printResult(String result){
        System.out.println(result);
    }

    public void printStart(){
        System.out.println(Constants.START_TEXT);
        System.out.println();
    }

    public void printError(String error){
        System.out.println("ERROR: " + error);
    }
}
