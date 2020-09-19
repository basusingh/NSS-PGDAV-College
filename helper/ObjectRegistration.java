package com.basusingh.nsspgdav.helper;

import java.io.Serializable;

/**
 * Created by Basu Singh on 10/10/2016.
 */
public class ObjectRegistration implements Serializable{

    String id, heading, deadline, imageurl;
    boolean name;
    boolean course;
    boolean mobile;
    boolean email;
    boolean address;
    boolean year;
    boolean dob;

    public String getImageurl() {
        return imageurl;
    }

    public void setImageurl(String imageurl) {
        this.imageurl = imageurl;
    }

    public boolean isEmail() {
        return email;
    }

    public void setEmail(boolean email) {
        this.email = email;
    }

    public boolean isAddress() {
        return address;
    }

    public void setAddress(boolean address) {
        this.address = address;
    }

    public boolean isMobile() {
        return mobile;
    }

    public void setMobile(boolean mobile) {
        this.mobile = mobile;
    }

    boolean past_exp;
    boolean new_ideas;
    boolean blood_group;
    boolean organic_medicine;
    boolean medical_issue;
    boolean inter_college_name;

    public String getId() {
        return id;
    }

    public String getDeadline() {
        return deadline;
    }

    public void setDeadline(String deadline) {
        this.deadline = deadline;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getHeading() {
        return heading;
    }

    public void setHeading(String heading) {
        this.heading = heading;
    }

    public boolean isName() {
        return name;
    }

    public void setName(boolean name) {
        this.name = name;
    }

    public boolean isCourse() {
        return course;
    }

    public void setCourse(boolean course) {
        this.course = course;
    }

    public boolean isYear() {
        return year;
    }

    public void setYear(boolean year) {
        this.year = year;
    }

    public boolean isDob() {
        return dob;
    }

    public void setDob(boolean dob) {
        this.dob = dob;
    }

    public boolean isPast_exp() {
        return past_exp;
    }

    public void setPast_exp(boolean past_exp) {
        this.past_exp = past_exp;
    }

    public boolean isNew_ideas() {
        return new_ideas;
    }

    public void setNew_ideas(boolean new_ideas) {
        this.new_ideas = new_ideas;
    }

    public boolean isBlood_group() {
        return blood_group;
    }

    public void setBlood_group(boolean blood_group) {
        this.blood_group = blood_group;
    }

    public boolean isOrganic_medicine() {
        return organic_medicine;
    }

    public void setOrganic_medicine(boolean organic_medicine) {
        this.organic_medicine = organic_medicine;
    }

    public boolean isMedical_issue() {
        return medical_issue;
    }

    public void setMedical_issue(boolean medical_issue) {
        this.medical_issue = medical_issue;
    }

    public boolean isInter_college_name() {
        return inter_college_name;
    }

    public void setInter_college_name(boolean inter_college_name) {
        this.inter_college_name = inter_college_name;
    }
}
