package edu.gatech.seclass;

import java.util.Arrays;
import java.util.ArrayList;
import java.util.*;

public class MyString implements MyStringInterface {
    private String str;

    public String getString() {
        return str;
    }

    public void setString(String string) throws IllegalArgumentException {
        if (string == easterEgg) {
            throw new IllegalArgumentException("IllegalArgumentException");
        }
        str = string;

    }

    public int countNumbers() throws NullPointerException {
        if (str == null) {
            throw new NullPointerException("NullPointerException");
        }
        int count = 0;
        //use this to mark if previous one is number
        boolean temp = false;
        for (int i = 0; i < str.length(); i++) {
            if (Character.isDigit(str.charAt(i))) {
                if (!temp) {
                    count += 1;
                    temp = true;
                }
            } else {
                temp = false;
            }
        }
        return count;
    }

    public String addNumber(int n, boolean invert) throws NullPointerException, IllegalArgumentException {
        if (str == null) {
            throw new NullPointerException();
        } else {
            if (n < 0) {
                throw new IllegalArgumentException();
            }
        }
        /*ArrayList<String> replace = new ArrayList<>();
        String tempstr = "";
        for (int i = 0; i < str.length(); i++) {
            if (Character.isDigit(str.charAt(i))) {
                tempstr += str.charAt(i);
                if (i == str.length() - 1) {
                    replace.add(tempstr);
                }
            } else {
                if (tempstr != "") {
                    replace.add(tempstr);
                    tempstr = "";
                }
            }
        }
        for (String s : replace) {
            String newstr = String.valueOf(Integer.parseInt(s) + n);
            if (invert) {
                StringBuffer sbr = new StringBuffer(newstr);
                sbr.reverse();
                newstr = sbr.toString();
            }
            str = str.replaceAll(s, newstr);
        }*/

        char[] ch=str.toCharArray();
        ArrayList<String> clist=new ArrayList<String>();
        for(char c:ch){
            clist.add(String.valueOf(c));
        }
        int i=0;
        boolean s = false;
        String num="";
        int start =0;
        while (i<clist.size()){
            if(Character.isDigit(clist.get(i).charAt(0))){
                num+=clist.get(i);
                //int temp=Integer.parseInt(clist.get(i));
                //temp+=n;
                //System.out.println(temp);
                if (!s){
                     start=i;
                }
                s=true;
                if (i==clist.size()-1){
                    //System.out.println(i);
                    String numf = String.valueOf(Integer.parseInt(num)+n);
                    if (invert){
                        StringBuffer sbr=new StringBuffer(numf);
                        sbr.reverse();
                        numf=sbr.toString();
                    }
                    clist.add(start,numf);
                    clist.subList(start+1,i+2).clear();
                    //System.out.println(i);
                }
            }else{
                if (s){
                    String numf = String.valueOf(Integer.parseInt(num)+n);
                    if (invert){
                        StringBuffer sbr=new StringBuffer(numf);
                        sbr.reverse();
                        numf=sbr.toString();
                    }
                    clist.add(start,numf);
                    clist.subList(start+1,i+1).clear();
                    i=start;
                }
                num="";
                s=false;
            }

            i++;
        }
        return String.join("",clist);
        //return str;
    }

    public void convertDigitsToNamesInSubstring(int initialPosition, int finalPosition) throws NullPointerException,
            IllegalArgumentException, MyIndexOutOfBoundsException {
        if (str == null) {
            throw new NullPointerException();
        } else {
            if (initialPosition < 1 || initialPosition > finalPosition) {
                throw new IllegalArgumentException();
            }
            if (finalPosition > str.length()) {
                throw new MyIndexOutOfBoundsException();
            }
        }

        String tempstr = str.substring(initialPosition - 1, finalPosition);
        tempstr = tempstr.replaceAll("0", "zero");
        tempstr = tempstr.replaceAll("1", "one");
        tempstr = tempstr.replaceAll("2", "two");
        tempstr = tempstr.replaceAll("3", "three");
        tempstr = tempstr.replaceAll("4", "four");
        tempstr = tempstr.replaceAll("5", "five");
        tempstr = tempstr.replaceAll("6", "six");
        tempstr = tempstr.replaceAll("7", "seven");
        tempstr = tempstr.replaceAll("8", "eight");
        tempstr = tempstr.replaceAll("9", "nine");
        str = str.substring(0, initialPosition - 1) + tempstr + str.substring(finalPosition, str.length());

    }
    /*public static void main(String[] args) {
        MyString gg = new MyString();
        gg.setString("fe32ffe67");
        //System.out.println(gg.countNumbers());
        //gg.addNumber(3,true);
        //System.out.println(gg.countNumbers());
        System.out.println(gg.addNumber(10, true));
        //gg.convertDigitsToNamesInSubstring(17,23);
        //System.out.println(gg.str);

    }*/
}
