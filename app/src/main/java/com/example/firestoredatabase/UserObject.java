package com.example.firestoredatabase;

import android.net.Uri;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UserObject {

    String idValue,nameValue,classValue,subjectValue;
    public List<Uri> images = new ArrayList<>();

    public UserObject(String idValue, String nameValue, String classValue, String subjectValue, List<Uri> images) {
        this.idValue = idValue;
        this.nameValue = nameValue;
        this.classValue = classValue;
        this.subjectValue = subjectValue;
        this.images = images;
    }

    public String getIdValue() {
        return idValue;
    }

    public void setIdValue(String idValue) {
        this.idValue = idValue;
    }

    public String getNameValue() {
        return nameValue;
    }

    public void setNameValue(String nameValue) {
        this.nameValue = nameValue;
    }

    public String getClassValue() {
        return classValue;
    }

    public void setClassValue(String classValue) {
        this.classValue = classValue;
    }

    public String getSubjectValue() {
        return subjectValue;
    }

    public void setSubjectValue(String subjectValue) {
        this.subjectValue = subjectValue;
    }

    public List<Uri> getImages() {
        return images;
    }

    public void setImages(List<Uri> images) {
        this.images = images;
    }




}
