package com.loginapplication;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

/**
 * Created by cipher1729 on 10/14/2015.
 */
public class Util {

        public  static  final  String sand_box_id="APP-80W284485P519543T";
        public  static  final  String paypal_liv_id="APP-0C165959RL117014F";
    public static StringBuilder sb;
    static boolean polled= false;

    public static void insertIntoLogin(final String url, final String userName, final String passWord,final String phNo,final String emailId,final String licenseTag)
    {   sb=null;
        polled =false;
        //POST code here
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                URL myUrl = null;

                try {
                    myUrl = new URL(url);

                    HttpURLConnection httpURLConnection = (HttpURLConnection) myUrl.openConnection();
                    // httpURLConnection.connect();
                    httpURLConnection.setDoOutput(true);
                    httpURLConnection.setRequestMethod("POST");
                    httpURLConnection.setRequestProperty("Content-Type", "application/json");
                    OutputStream os = httpURLConnection.getOutputStream();
                    BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
                    JSONObject recordObject = new JSONObject();
                    recordObject.put("name", userName);
                    recordObject.put("password", passWord);
                    recordObject.put("phone", phNo);
                    recordObject.put("email", emailId);
                    recordObject.put("licensePlate", licenseTag);

                    bufferedWriter.write(recordObject.toString());
                    bufferedWriter.flush();
                    bufferedWriter.close();
                    httpURLConnection.connect();

                    InputStream is = httpURLConnection.getInputStream();
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
                    String line;
                    sb = new StringBuilder();
                    line = bufferedReader.readLine();
                    while (line != null) {
                        sb.append(line);
                        line = bufferedReader.readLine();
                    }
                    is.close();
                    os.close();
                    polled= true;
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                } catch (ProtocolException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });
        t.start();
        while(polled==false);
    }

    public static void validateCredentials(final String url)
    {
        sb=null;
        polled =false;
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                URL myUrl = null;

                try {
                    myUrl = new URL(url);

                    HttpURLConnection httpURLConnection = (HttpURLConnection) myUrl.openConnection();
                    // httpURLConnection.connect();
                    httpURLConnection.setRequestMethod("GET");
                    httpURLConnection.setRequestProperty("Content-Type", "application/json");
/*
                    OutputStream os = httpURLConnection.getOutputStream();
                    BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
                    JSONObject recordObject = new JSONObject();
                    recordObject.put("username", userName);
                    recordObject.put("password", passWord);

                    bufferedWriter.write(recordObject.toString());
                    bufferedWriter.flush();
                    bufferedWriter.close();
                    httpURLConnection.connect();

*/

                    InputStream is = httpURLConnection.getInputStream();
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
                    String line;
                    sb = new StringBuilder();
                    line = bufferedReader.readLine();
                    while (line != null) {
                        sb.append(line);
                        line = bufferedReader.readLine();
                    }
                    is.close();
                    polled= true;
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                } catch (ProtocolException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        });
        t.start();
        while(polled==false);
    }

}