package com.student.student.repository.responce;


import java.util.UUID;

public interface StudentProjectionAndId {
UUID getUuid();
String getFirstName();
String getLastName();

public default String getProjectionAndId(){
    return  getFirstName() + " " + getLastName();
}
}
