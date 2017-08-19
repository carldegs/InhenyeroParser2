package com.inhenyero;

public class Subscriber {
    String studentNum;
    String[] name;
    String[] org1;
    String[] org2;
    String[] org3;
    String email;
    String mobile;
    String birthday;
    String telephone;
    String address;
    String writeup;
    String packageType;
    String course;

    public Subscriber(){
        name = new String[3];
        org1 = new String[2];
        org2 = new String[2];
        org3 = new String[2];
    }
    
    public void print(){
        System.out.println("(studentNum) " + studentNum);
        System.out.println("(name) " + name[0] + ", " + name[1] + " " + name[2]);
        System.out.println("(course) " + course);
        System.out.println("(org1) " + org1[0] + " - " + org1[1]);
        System.out.println("(org2) " + org2[0] + " - " + org2[1]);
        System.out.println("(org3) " + org3[0] + " - " + org3[1]);
        System.out.println("(email) " + email);
        System.out.println("(mobile) " + mobile);
        System.out.println("(birthday) " + birthday);
        System.out.println("(telephone) " + telephone);
        System.out.println("(address) " + address);
        System.out.println("(package) " + packageType);
        System.out.println("(writeup) " + writeup);
        System.out.println();
    }
    
    public Subscriber(Subscriber sub){
        this.name = new String[3];
        this.org1 = new String[2];
        this.org2 = new String[2];
        this.org3 = new String[2];

        this.studentNum = sub.studentNum;
        this.name[0] = sub.name[0];
        this.name[1] = sub.name[1];
        this.name[2] = sub.name[2];
        this.course = sub.course;
        this.org1[0] = sub.org1[0];
        this.org1[1] = sub.org1[1];
        this.org2[0] = sub.org2[0];
        this.org2[1] = sub.org2[1];
        this.org3[0] = sub.org3[0];
        this.org3[1] = sub.org3[1];
        this.email = sub.email;
        this.mobile = sub.mobile;
        this.birthday = sub.birthday;
        this.telephone = sub.telephone;
        this.address = sub.address;
        this.packageType = sub.packageType;
        this.writeup = sub.writeup;
    }

    public String getCsv(){
        if(name[0] != null){
            return  name[0].toUpperCase() + ";" +
                    name[1].toUpperCase() + ";" +
                    name[2].toUpperCase() + ";" +
                    org1[0] + ";" +
                    org1[1] + ";" +
                    org2[0] + ";" +
                    org2[1] + ";" +
                    org3[0] + ";" +
                    org3[1] + ";" +
                    writeup + ";" +
                    getPhotoPath("SABLAY") + ";" +
                    getPhotoPath(packageType.equals("A") ? "FORMAL" : "CREATIVE") + ";" +
                    getPhotoPath("TOGA");
        } else {
            return ";;;;;;;;;;;;";
        }
    }

    private String getPhotoPath(String name){
        return "/images/" + course + "/" + studentNum + "/" + name + ".jpg";
    }
}
