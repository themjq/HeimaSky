package com.example.skydelivery.utils;

import com.alibaba.druid.support.json.JSONUtils;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.WriteListener;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.springframework.boot.autoconfigure.web.ServerProperties;

import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class MyResponse extends HttpClientBuilder {
    private int status;
    private HashMap<String, String> headers;
    private byte[] responseBody;
    private ServletOutputStream outputStream;

    public MyResponse(){
        this.status=200;
        this.headers=new HashMap<>();
        this.responseBody=null;
        outputStream=null;
    }

}
