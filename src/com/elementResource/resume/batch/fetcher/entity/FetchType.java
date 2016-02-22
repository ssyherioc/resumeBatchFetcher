package com.elementResource.resume.batch.fetcher.entity;

public enum FetchType {

    liepin("liepin");     // liepin
    //job("51job"),         // 51job
    //zhaopin("zhaopin");   // zhaopin
    
    FetchType(String name) {
        this.name = name;
    }
    
    private String name;
    
    public static FetchType getEnumByName(String name) {
        for (FetchType each : values()) {
            if (each.getName().equals(name)) {
                return each;
            }
        }
        
        return null;
    }
   
    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    @Override
    public String toString()
    {
        return this.name;
    }
}
