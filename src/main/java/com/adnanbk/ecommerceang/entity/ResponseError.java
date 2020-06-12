package com.adnanbk.ecommerceang.entity;

import lombok.Data;

@Data
public class ResponseError {

    private String fieldName;
    private String name;
    private String message;

    public ResponseError(String fieldName, String message) {
        this.name=fieldName;
        if(!fieldName.equals(fieldName.toLowerCase()))
        {
            String name="";
            for (Character c :fieldName.toCharArray()) {
                if(Character.isUpperCase(c))
                {
                    name+=" "+c.toString();
                }
                else
                name+=c.toString();
            }
            this.name=name.toLowerCase();
        }

        this.fieldName = fieldName;
        this.message = message;
    }
}