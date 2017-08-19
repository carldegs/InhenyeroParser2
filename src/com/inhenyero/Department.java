package com.inhenyero;

import java.util.ArrayList;

/**
 * Created by Carl on 22 Jul 2017.
 */
public class Department {
    ArrayList<Subscriber> subscribers;
    ArrayList<Spread> spreads;
    String name;

    public Department(String deptName){
        subscribers = new ArrayList<>();
        spreads = new ArrayList<>();
        name = deptName;
    }

    public void print(){
        System.out.println("(name) " + name);
        System.out.println("(subscribers) " + subscribers.size());
        if(spreads.size() > 0){
            System.out.println("(spreads) " + spreads.size());
        }
    }

    public void print(String type){
        System.out.println("============================");
        print();
        System.out.println("============================");
        if(type.equals("sub")){
            for (Subscriber subscriber : subscribers) {
                subscriber.print();
            }
        } else if(type.equals("spread")){
            for (Spread spread : spreads) {
                spread.print();
            }
        }
        System.out.println();
    }
}
