package com.example.login_application;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.webkit.CookieManager;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;

import cz.msebera.android.httpclient.HttpResponse;
import cz.msebera.android.httpclient.NameValuePair;
import cz.msebera.android.httpclient.client.entity.UrlEncodedFormEntity;
import cz.msebera.android.httpclient.client.methods.HttpPost;
import cz.msebera.android.httpclient.cookie.Cookie;
import cz.msebera.android.httpclient.impl.client.DefaultHttpClient;
import cz.msebera.android.httpclient.message.BasicNameValuePair;
import cz.msebera.android.httpclient.protocol.HTTP;


public class Parser {

    static String id;
    static String pwd;
    static Context context;
    static int login_ok=2;

    String TotalCookie = "x";

    public Parser(String _id, String _pw, Context _context){
        context = _context;
        id = _id;
        pwd = _pw;
    }

    public void toParsing() { new ReHttpPost().execute();}

    public class ReHttpPost extends AsyncTask<Void, String, Void>{
        @Override
        protected Void doInBackground(Void... voids) {
            try{
                HttpPostData();
            } catch(Exception e){
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
        }

        public void HttpPostData() throws Exception{
            try{
                login_ok = 1;
                String l_token = "";

                if(login_ok==1){
                    trustAllHttpsCertificates();
                    String Address1 = "https://sso.duksung.ac.kr/svc/tk/Auth.do?ac=Y&ifa=N&id=portal&";
                    URL url= new URL(Address1);

                    HttpURLConnection con1 = (HttpURLConnection) url.openConnection();

                    con1.setDoInput(true);
                    con1.setUseCaches(false);

                    BufferedReader br = new BufferedReader(new InputStreamReader(con1.getInputStream(), "EUC-KR"));

                    while ((l_token = br.readLine()) != null) {
                        if (l_token.lastIndexOf("l_token") > 0) {
                            System.out.println(l_token);
                            l_token = l_token.substring(l_token.indexOf("value"), l_token.lastIndexOf("\""));
                            System.out.println(l_token);
                            l_token = l_token.substring(l_token.indexOf("\"") + 1);
                            System.out.println(l_token);
                            login_ok = 2;
                            break;
                        }
                    }
                }

                if (login_ok == 2) {

                    trustAllHttpsCertificates();
                    DefaultHttpClient httpclient_2 = new DefaultHttpClient();

                    HttpPost httppost_2 = new HttpPost("https://sso.duksung.ac.kr/Login.do");

                    List<Cookie> cookies_2;
                    ArrayList<NameValuePair> nameValuePairs_2 = new ArrayList<>();
                    nameValuePairs_2.add(new BasicNameValuePair("user_id", id));
                    nameValuePairs_2.add(new BasicNameValuePair("user_password", pwd));
//                    nameValuePairs_2.add(new BasicNameValuePair("pwdPolicy", "N"));
//                    nameValuePairs_2.add(new BasicNameValuePair("user_timezone_offset", "-540"));
                    nameValuePairs_2.add(new BasicNameValuePair("l_token", l_token));
                    httppost_2.setEntity(new UrlEncodedFormEntity(nameValuePairs_2, HTTP.UTF_8));

                    HttpResponse response_2 = httpclient_2.execute(httppost_2);
                    cookies_2 = httpclient_2.getCookieStore().getCookies();

                    int cookiecount = 0;

                    Cookie sessionInfo1;
                    String cookieTemp1 = "";
                    CookieManager cookieManager1 = CookieManager.getInstance();
                    for (Cookie cookie1 : cookies_2) {
                        sessionInfo1 = cookie1;
                        String cookieString = sessionInfo1.getName() + "=" + sessionInfo1.getValue() + "; path=" + sessionInfo1.getPath();
                        cookieTemp1 = cookieTemp1 + " " + cookieString;
                        cookiecounnt++;
                        cookieManager1.setCookie(sessionInfo1.getDomain(), cookieString);
                    }

                    System.out.println("Cookie1 : " + cookies_2);

                    cookieManager1.setAcceptCookie(true);

                    if ((response_2 != null && cookieTemp1.lastIndexOf("eXSignOnSessionId") > 0)) {

//                        for (int i = 0; i < cookiecounnt; i++) {
//                            if (cookies_2.get(i).toString().lastIndexOf("__smVisitorID") > 0)
//                                __smVisitorID = "__smVisitorID=" + cookies_2.get(i).toString().substring(cookies_2.get(i).toString().indexOf("[value: ") + 8, cookies_2.get(i).toString().lastIndexOf("][domain: ptl.duksung.ac.kr]"));
//                            if (cookies_2.get(i).toString().lastIndexOf("JSESSIONID") > 0)
//                                JSESSIONID = "JSESSIONID=" + cookies_2.get(i).toString().substring(cookies_2.get(i).toString().indexOf("[value: ") + 8, cookies_2.get(i).toString().lastIndexOf("][domain: ptl.duksung.ac.kr]"));
//                            if (cookies_2.get(i).toString().lastIndexOf("sso_duksung") > 0)
//                                ssotoken = "sso_duksung=" + cookies_2.get(i).toString().substring(cookies_2.get(i).toString().indexOf("[value: ") + 8, cookies_2.get(i).toString().lastIndexOf("][domain: .duksung.ac.kr]"));
//                        }

                        TotalCookie = cookies_2 + "";
                        Thread.sleep(100);


                        //TODO : 로그인 성공 후 실행할 로직 혹은 엑티비티를 넣으세요.
                        // 로그인이 잘 되는지 확인하고자, 로그인 성공시 포탈로 들어가도록 했어요.
                        String WebView_URL = "https://portal.duksung.ac.kr/";
                        Intent intent = new Intent(MainActivity.mContext, Web_View.class);
                        intent.putExtra("URL", WebView_URL);
                        intent.putExtra("HTML", WebView_URL);
                        MainActivity.mContext.startActivity(intent);

                        // 기존 덕성여대IN 어플에서는 성적 불러오는 로직이 있었어요. 지금은 정상작동하지 않아서 주석처리합니다.
//                        viewGrade();

                    }
                }
            } catch (IOException e){

            }
        }

        private void trustAllHttpsCertificates() throws Exception {
            javax.net.ssl.TrustManager[] trustAllCerts = new javax.net.ssl.TrustManager[1];
            javax.net.ssl.TrustManager tm = new miTM();
            trustAllCerts[0] = tm;
            javax.net.ssl.SSLContext sc = javax.net.ssl.SSLContext.getInstance("SSL");
            sc.init(null, trustAllCerts, null);
            HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
        }
    }

    public class miTM implements javax.net.ssl.TrustManager, javax.net.ssl.X509TrustManager {
        public java.security.cert.X509Certificate[] getAcceptedIssuers() {
            return null;
        }

        public void checkServerTrusted(
                java.security.cert.X509Certificate[] certs, String authType) {
            return;
        }

        public void checkClientTrusted(
                java.security.cert.X509Certificate[] certs, String authType) {
            return;
        }
    }
}
