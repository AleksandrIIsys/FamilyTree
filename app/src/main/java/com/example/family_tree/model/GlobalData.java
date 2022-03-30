package com.example.family_tree.model;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;

public class GlobalData {
    public static ArrayList<Person> family = new ArrayList<>();
    public static ArrayList<String> getFamilyFullName(){
        ArrayList<String> FullName = new ArrayList<>();
        for (Person person:
             family) {
            FullName.add(person.getName()+ " " + person.getSurname());
        }
        return FullName;
    }
    public static void DeleteMember(Person person){
        family.remove(FindMember(person));
        try {
            Files.delete(Paths.get(person.getImage()));
        } catch (IOException e) {
            e.printStackTrace();
        }
        for (Person p:
             getChildrens(person)) {
            if(person.getSex().equals("Male")){
                p.setFather(null);
            }else {
                p.setMother(null);
            }
        }
        for(Person p:getChildrens(person)){
            if(getChildrens(p).size() != 0){
                editChildrens(p);
            }
        }
    }
    public static void editChildrens(Person person){
        for (Person p:
                getChildrens(person)) {
            if(person.getSex().equals("Male")){
                p.setFather(person);
            }else {
                p.setMother(person);
            }
        }
        for(Person p:getChildrens(person)){
            if(getChildrens(p).size() != 0){
                editChildrens(p);
            }
        }
    }
    public static int FindMember(Person person){
        for (Person p:
             family) {
            if(p.getName().equals(person.getName())
                    && p.getSurname().equals(person.getSurname())
                    && p.getSex().equals(person.getSex())
                    && (p.getDate() == null
                    || p.getDate().equals(person.getDate()))
                    ){
                return family.indexOf(p);
            }
        }
        return -1;
    }
    public static ArrayList<String> getMale(){
        ArrayList<String> FullName = new ArrayList<>();
        for (Person person:
                family) {
            if(person.getSex().equals("Male"))
            FullName.add(person.getName()+ " " + person.getSurname());
        }
        return FullName;
    }
    public static ArrayList<String> getFemale(){
        ArrayList<String> FullName = new ArrayList<>();
        for (Person person:
                family) {
            if(person.getSex().equals("Female"))
                FullName.add(person.getName()+ " " + person.getSurname());
        }
        return FullName;
    }
    public static Person findMember(String fullName){
        String name = fullName.split(" ")[0];
        String surname = fullName.split(" ")[1];
        for (Person person:
                family
             ) {
            if(person.getName().equals(name) && person.getSurname().equals(surname)){
                return person;
            }
        }
        return null;
    }
    public static ArrayList<Person> getChildrens(Person person){
        ArrayList<Person> childrens = new ArrayList<>();
        for (Person p:
             family) {
            if((p.getFather() != null && p.getFather().getName().equals(person.getName())&& p.getFather().getSurname().equals(person.getSurname()))
                    || (p.getMother() != null && p.getMother().getName().equals(person.getName())&& p.getMother().getSurname().equals(person.getSurname())))
                childrens.add(p);
        }

        return childrens;
    }
}
