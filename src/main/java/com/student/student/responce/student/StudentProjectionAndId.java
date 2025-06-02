package com.student.student.responce.student;


import java.util.UUID;

public interface StudentProjectionAndId {
UUID getUuid();
String getFirstName();
String getLastName();

public default String getProjectionAndId(){
    return  getFirstName() + " " + getLastName();
}
}
