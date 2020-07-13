package com.example.uploadimageviarest.remote;

public class APIUtils {
    private APIUtils(){

    }
    // This must be your UTR for the required Rest call and should be tested first-hand via
    // a Rest call tool like Postman for working properly
    public static final String API_URL = "https://j5ruf9tphj.execute-api.eu-west-2.amazonaws.com/v1/";
    //public static final String API_URL = " https://0l2eyelvk3.execute-api.eu-west-2.amazonaws.com/";


    // Package-private step to be taken here
    public static FileService getFileService(){
         return RetrofitClient.getClient(API_URL).create(FileService.class);
    }
}

