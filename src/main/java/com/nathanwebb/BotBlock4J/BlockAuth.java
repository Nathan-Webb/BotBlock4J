package com.nathanwebb.BotBlock4J;


import java.util.HashMap;



public class BlockAuth {
    private HashMap<BotList, String> authHashMap = new HashMap<>();

    public BlockAuth(){}


    /**
     * Sets the authorization token given a Bot List and the token associated with the
     * @param list Specified Bot list. If the value has already been set in the preceding code, the value will be overwritten.
     * @param authToken The token that you would normally put in your {@code Authorization} header if you were doing this manually.
     */
    public void setListAuthToken(BotList list, String authToken){
        authHashMap.put(list, authToken);
    }

    public HashMap<BotList, String> getAuthHashMap() {
        return authHashMap;
    }
}

