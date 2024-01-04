package com.webview.myapplication;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DownloadManager;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.webkit.CookieManager;
import android.webkit.URLUtil;
import android.webkit.WebChromeClient;
import android.webkit.WebChromeClient.CustomViewCallback;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.Toast;
import android.content.pm.PackageManager;
import androidx.core.content.ContextCompat;

import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import android.webkit.SslErrorHandler;
import android.net.http.SslError;
import android.webkit.WebSettings.PluginState;
import java.util.Objects;

public class MainActivity extends Activity {
    private final int STORAGE_PERMISSION_CODE = 1;
    private static final String ALLOWED_DOMAIN = BuildConfig.ALLOWED_DOMAIN;
    private static final String SECOND_ALLOWED_DOMAIN = BuildConfig.SECOND_ALLOWED_DOMAIN;
    private static final boolean BLOCK_MEDIA = BuildConfig.BLOCK_MEDIA;
    private static final String VIEW_MODE = BuildConfig.VIEW_MODE;
    private WebView mWebView;
    private View mCustomView;
    private CustomViewCallback mCustomViewCallback;
    private ProgressBar mProgressBar;

    private void requestStoragePermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
            new AlertDialog.Builder(this)
                    .setTitle("Permission needed")
                    .setMessage("This permission is needed to download files")
                    .setPositiveButton("ok", (dialog, which) -> ActivityCompat.requestPermissions(MainActivity.this,
                            new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, STORAGE_PERMISSION_CODE))
                    .setNegativeButton("cancel", (dialog, which) -> dialog.dismiss())
                    .create().show();
        } else {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, STORAGE_PERMISSION_CODE);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.Q)
    @Override
    @SuppressLint("SetJavaScriptEnabled")
    protected void onCreate(Bundle savedInstanceState) {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            requestStoragePermission();
        }
        super.onCreate(savedInstanceState);

        if (VIEW_MODE == "PORTRAIT") {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        } else if (VIEW_MODE == "LANDSCAPE") {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        } else {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);
        }

        setContentView(R.layout.activity_main);
        mWebView = findViewById(R.id.activity_main_webview);
        mProgressBar = findViewById(R.id.progressBar);
        WebSettings webSettings = mWebView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setDomStorageEnabled(true);
        webSettings.setLoadWithOverviewMode(true);
        webSettings.setUseWideViewPort(true);
        webSettings.setBuiltInZoomControls(true);
        webSettings.setDisplayZoomControls(false);
        webSettings.setSupportZoom(true);
        webSettings.setDefaultTextEncodingName("utf-8");
        webSettings.setPluginState(PluginState.ON);
        if (BLOCK_MEDIA) {
            webSettings.setLoadsImagesAutomatically(false);
        }


        mWebView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onShowCustomView(View view, CustomViewCallback callback) {
                if (mCustomView != null) {
                    callback.onCustomViewHidden();
                    return;
                }
                mCustomView = view;
                mWebView.setVisibility(View.GONE);
                mCustomViewCallback = callback;
                ((FrameLayout) getWindow().getDecorView()).addView(mCustomView);
                getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN);
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);
            }

            @Override
            public void onHideCustomView() {
                ((FrameLayout) getWindow().getDecorView()).removeView(mCustomView);
                mCustomView = null;
                mWebView.setVisibility(View.VISIBLE);
                getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_VISIBLE);
                mCustomViewCallback.onCustomViewHidden();
                mCustomViewCallback = null;
                if (VIEW_MODE == "PORTRAIT") {
                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                } else if (VIEW_MODE == "LANDSCAPE") {
                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
                } else {
                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);
                }
            }
        });

        mWebView.setWebViewClient(new HelloWebViewClient());
        mWebView.setDownloadListener((url, userAgent, contentDisposition, mimeType, contentLength) -> {
            Uri source = Uri.parse(url);
            DownloadManager.Request request = new DownloadManager.Request(source);
            String cookies = CookieManager.getInstance().getCookie(url);
            request.addRequestHeader("cookie", cookies);
            request.addRequestHeader("User-Agent", userAgent);
            request.setDescription("Downloading File...");
            request.setTitle(URLUtil.guessFileName(url, contentDisposition, mimeType));
            request.allowScanningByMediaScanner();
            request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
            request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, URLUtil.guessFileName(url, contentDisposition, mimeType));
            DownloadManager dm = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
            dm.enqueue(request);
            Toast.makeText(getApplicationContext(), "Downloading File", Toast.LENGTH_LONG).show();
        });
        mWebView.loadUrl("https://" + ALLOWED_DOMAIN);
    }

    private class HelloWebViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(final WebView view, final String url) {
            String host = Uri.parse(url).getHost();
            if (host.equals(ALLOWED_DOMAIN) || host.equals(SECOND_ALLOWED_DOMAIN)) {
                mProgressBar.setVisibility(View.VISIBLE);
                view.loadUrl(url);
                return true;
            } else {
                Toast.makeText(view.getContext(), "This URL is not allowed (" + host + ")", Toast.LENGTH_LONG).show();
                return true;
            }
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            mProgressBar.setVisibility(View.GONE);
            if (BLOCK_MEDIA) {
                view.loadUrl("javascript: (() => { function handle(node) { if (node.tagName === 'IMG' && node.style.visibility !== 'hidden' && node.width > 32 && node.height > 32) { const blankImageUrl = 'data:image/gif;base64,R0lGODlhAQABAIAAAP///////yH5BAEKAAEALAAAAAABAAEAAAICTAEAOw=='; const { width, height } = window.getComputedStyle(node); node.src = blankImageUrl; node.style.visibility = 'hidden'; node.style.background = 'none'; node.style.backgroundImage = `url(${blankImageUrl})`; node.style.width = width; node.style.height = height; } else if (node.tagName === 'VIDEO' || node.tagName === 'IFRAME' || ((!node.type || node.type.includes('video')) && node.tagName === 'SOURCE') || node.tagName === 'OBJECT') { node.remove(); } } document.querySelectorAll('img,video,source,object,embed,iframe,[type^=video]').forEach(handle); const observer = new MutationObserver((mutations) => mutations.forEach((mutation) => mutation.addedNodes.forEach(handle))); observer.observe(document.body, { childList: true, subtree: true }); })();");
            }
        }

        @Override
        public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
            handler.cancel();
            view.loadData("<html><body><h1 style='color: grey'>SSL Error</h1></body></html>", "text/html; charset=utf-8", "UTF-8");
            if (Objects.equals(error.getCertificate().getIssuedBy().getOName(), "NetFree")) {
                Toast.makeText(view.getContext(), "טיפ: נראה שלא מותקנת תעודת אבטחה של נטפרי", Toast.LENGTH_LONG).show();
            } else if (Objects.equals(error.getCertificate().getIssuedBy().getOName(), "Netspark")) {
                Toast.makeText(view.getContext(), "טיפ: נראה שלא מותקנת תעודת אבטחה של אתרוג/רימון", Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    public void onBackPressed() {
        if (mCustomView != null) {
            mWebView.getWebChromeClient().onHideCustomView();
        } else if (mWebView.canGoBack()) {
            mWebView.goBack();
        } else {
            super.onBackPressed();
        }
    }
}
