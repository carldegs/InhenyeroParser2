package com.inhenyero;

/**
 * Created by Carl on 22 Jul 2017.
 */
public class Spread {
    Subscriber left;
    Subscriber right;

    public Spread(){

    }

    public Spread(Subscriber left, Subscriber right){
        this.left = left;
        this.right = right;
    }

    public void print(){
        System.out.println("(left) " + left.name[0] + ", " + left.name[1] + " " + left.name[2]);
        System.out.println("(right) " + right.name[0] + ", " + right.name[1] + " " + right.name[2]);
    }

    public String getCsv(){
        return (left.getCsv() + ";" + right.getCsv() + "\n");
    }
}
