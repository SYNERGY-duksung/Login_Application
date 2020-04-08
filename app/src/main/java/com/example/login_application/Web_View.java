package com.example.login_application;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.net.http.SslError;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.CookieSyncManager;
import android.webkit.DownloadListener;
import android.webkit.SslErrorHandler;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;
import android.widget.Toast;


public class Web_View extends Activity {
    WebView webview;
    String URL = "";
    String HTML = "";
    View m_progress;
    public Context mContext;

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.web_view);

        mContext = this;

        Intent intent = getIntent();
        URL = intent.getStringExtra("URL");
        HTML = intent.getStringExtra("HTML");

        m_progress = findViewById(R.id.web_progress);

        webview = findViewById(R.id.webview);

        WebSettings set = webview.getSettings();
        set.setJavaScriptEnabled(true);
        set.setSupportZoom(true);
        set.setBuiltInZoomControls(true);
        set.setJavaScriptCanOpenWindowsAutomatically(true);
        set.setUseWideViewPort(true);
        set.setLoadWithOverviewMode(true);
//        set.setDefaultZoom(WebSettings.ZoomDensity.FAR);
        webview.setHorizontalScrollbarOverlay(true);
        webview.setVerticalScrollbarOverlay(true);
        webview.setHorizontalScrollBarEnabled(true);
        webview.setVerticalScrollBarEnabled(true);
        webview.setInitialScale(100);
        //webview.setScrollBarStyle(WebView.SCROLLBARS_OUTSIDE_OVERLAY);

        webview.loadUrl(URL);

        webview.setWebViewClient(new SslWebViewConnect() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                if (url.lastIndexOf("?") > 0) {

                } else
                    url = url + "?chk_password=Ejreosu1158^";

                return super.shouldOverrideUrlLoading(view, url);
            }

            @Override
            public void onReceivedError(WebView view, int errorcode, String description, String fallingUrl) {
                Toast.makeText(Web_View.this, "오류 : " + description, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                m_progress.setVisibility(View.VISIBLE);
            }

            public void onPageFinished(WebView view, String Url) {
                CookieSyncManager.getInstance().sync();
                m_progress.setVisibility(View.GONE);
            }
        });

        webview.setDownloadListener(new DownloadListener() {
            public void onDownloadStart(String url, String userAgent,
                                        String contentDisposition, String mimetype,
                                        long contentLength) {
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(i);
            }
        });

        TextView tvClose = findViewById(R.id.tv_toolbar_close);
        tvClose.setOnClickListener(tabClickListener);
        TextView tvLinkUrl = findViewById(R.id.tv_toolbar_web);
        tvLinkUrl.setOnClickListener(tabClickListener);
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK) && webview.canGoBack()) {
            webview.goBack();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    public class SslWebViewConnect extends WebViewClient {
        @Override
        public void onReceivedSslError(WebView view, final SslErrorHandler handler, SslError error) {
            StringBuilder sb = new StringBuilder();
            switch (error.getPrimaryError()) {
                case SslError.SSL_EXPIRED:
                    sb.append("이 사이트의 보안 인증서는 신뢰할 수 없습니다.\n");
                    break;
                case SslError.SSL_IDMISMATCH:
                    sb.append("이 사이트의 보안 인증서는 신뢰할 수 없습니다.\n");
                    break;
                case SslError.SSL_NOTYETVALID:
                    sb.append("이 사이트의 보안 인증서는 신뢰할 수 없습니다.\n");
                    break;
                case SslError.SSL_UNTRUSTED:
                    sb.append("이 사이트의 보안 인증서는 신뢰할 수 없습니다.\n");
                    break;
                default:
                    sb.append("보안 인증서에 오류가 있습니다.\n");
                    break;
            }

            AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
            builder.setTitle("SSL 오류")
                    .setCancelable(false)
                    .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // TODO Auto-generated method stub
                            handler.proceed();
                        }
                    }).setNegativeButton("취소", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    // TODO Auto-generated method stub
                    handler.cancel();
                }
            });

            final AlertDialog dialog = builder.create();
            dialog.show();
        }

        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        CookieSyncManager.createInstance(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        CookieSyncManager.getInstance().startSync();
    }

    @Override
    public void onPause() {
        super.onPause();
        CookieSyncManager.getInstance().stopSync();
    }

    OnClickListener tabClickListener = new OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.tv_toolbar_close:
                    finish();
                    HProgressDialog.stopProgressDialog();
                    break;
                case R.id.tv_toolbar_web:
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(URL));
                    startActivity(intent);
                    break;
            }
        }
    };
}
