/*
 * Copyright 2018 Nathan Webb (nathanwgithub@gmail.com)
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated
 * documentation files (the "Software"), to deal in the Software without restriction, including without limitation
 * the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software,
 * and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial
 * portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED,
 * INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT.
 * IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY,
 * WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE
 * OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 *
 */
package com.nathanwebb.botblock4j;

import java.util.HashMap;
import java.util.Map;


/**
 * Main class that handles Bot List authorization.
 */
public class BlockAuth {
    private Map<String, String> authHashMap = new HashMap<>();

    public BlockAuth(){}

    /**
     * Directly sets a instance of BlockAuth with the provided Map.
     *
     * @param authHashMap
     *        The Map to set.
     */
    public BlockAuth(Map<String, String> authHashMap){
        this.authHashMap = authHashMap;
    }


    /**
     * Sets the authorization token for the given site. You may receive a API-token from the botlist-site.
     * <br><b>If the value has already been set in the preceding code, the value will be overwritten.</b>
     *
     * @param listURL
     *        The URL of the botlist. This must be without any subdomain nor subpages.
     *        <br>Example: {@code google.com} instead of {@code https://google.com/subsite}.
     * @param authToken
     *        The token that you would normally put in your {@code Authorization} header if you were doing this manually.
     */
    public void setListAuthToken(String listURL, String authToken){
        isNotNull(listURL, "url");
        isNotNull(authToken, "authToken");

        authHashMap.put(listURL.replaceAll("^https?://", ""), authToken);
    }

    /**
     * Gives the HashMap containing the sites and their corresponding token.
     *
     * @return The HashMap with the sites and their tokens.
     */
    public Map<String, String> getAuthHashMap() {
        return authHashMap;
    }

    private static void isNotNull(Object object, String name){
        if(object == null)
            throw new IllegalStateException(name + " may not be null.");
    }

    public static class Builder{
        private Map<String, String> authTokens = new HashMap<>();

        /**
         * Adds the provided url and auth-token to the list.
         * <br><b>If the value has already been set in the preceding code, the value will be overwritten.</b>
         *
         * @param  url
         *         The URL of the botlist. This must be without any subdomain nor subpages.
         *         <br>Example: {@code google.com} instead of {@code https://google.com/subsite}.
         * @param  authToken
         *         The token that you would normally put in your {@code Authorization} header if you were doing this manually.
         *
         * @return The Builder after the url and token have been added.
         */
        public Builder addListAuthToken(String url, String authToken){
            isNotNull(url, "url");
            isNotNull(authToken, "authToken");

            authTokens.put(url.replaceAll("^https?://", ""), authToken);

            return this;
        }

        /**
         * Creates a new {@link com.nathanwebb.botblock4j.BlockAuth BlockAuth} and sets the created HashMap.
         *
         * @return new {@link com.nathanwebb.botblock4j.BlockAuth BlockAuth} instance.
         */
        public BlockAuth build(){
            if(authTokens.isEmpty())
                throw new IllegalStateException("authTokens may not be empty!");

            return new BlockAuth(authTokens);
        }

    }
}

