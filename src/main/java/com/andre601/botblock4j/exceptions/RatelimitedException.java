/*
 * Copyright 2018 Nathan Webb (nathanwgithub@gmail.com), Andre601
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
package com.andre601.botblock4j.exceptions;

import org.json.JSONObject;

public class RatelimitedException extends Throwable{

    private int delay;
    private String ip;
    private String route;
    private String id;

    public RatelimitedException(String response){
        JSONObject json = new JSONObject(response);

        this.delay = json.getInt("retry_after");
        this.ip = json.getString("ratelimit_ip");
        this.route = json.getString("ratelimit_route");
        this.id = json.getString("ratelimit_bot_id");
    }

    @Override
    public String getMessage(){
        return String.format(
                "Bot ID: %s (IP: %s) got ratelimited on route %s! You can try again in %d seconds!",
                id,
                ip,
                route,
                delay
        );
    }

}
