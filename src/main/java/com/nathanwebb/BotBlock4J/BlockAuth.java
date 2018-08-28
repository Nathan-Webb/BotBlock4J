package com.nathanwebb.BotBlock4J;


import java.util.HashMap;


/**
 * Main class that handles Bot List authorization.
 */
public class BlockAuth {
    private HashMap<String, String> authHashMap = new HashMap<>();

    public BlockAuth(){}


    /**
     * Sets the authorization token given a Bot List URL and the authorization token that the site gave you.
     * If the value has already been set in the preceding code, the value will be overwritten.
     * @param listURL Bot List URL. It _must_ be the base url. eg. {@code google.com} and not {@code https://google.com/search/other}
     * @param authToken The token that you would normally put in your {@code Authorization} header if you were doing this manually.
     */
    public void setListAuthToken(String listURL, String authToken){
        authHashMap.put(listURL.replaceAll("^https?://", ""), authToken);
    }

    public HashMap<String, String> getAuthHashMap() {
        return authHashMap;
    }
}

