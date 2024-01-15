package com.webview.myapplication;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.net.http.SslError;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.webkit.CookieManager;
import android.webkit.SslErrorHandler;
import android.webkit.URLUtil;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebChromeClient.CustomViewCallback;
import android.webkit.WebResourceResponse;
import android.webkit.WebSettings;
import android.webkit.WebSettings.PluginState;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Objects;

class AdBlockerUtil {
    private final HashSet<String> adHosts = new HashSet<>();

    private static AdBlockerUtil mInstance;

    public static AdBlockerUtil getInstance() {
        if (mInstance == null) {
            mInstance = new AdBlockerUtil();
        }
        return mInstance;
    }

    public void initialize(Context context) {
        try {
            InputStream inputStream = context.getAssets().open("adblock.txt");
            loadHostsFromInputStream(inputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean isAd(String url) {
        if (url == null) {
            return false;
        }

        String requestHost = Uri.parse(url).getHost();
        
        if (requestHost == null) {
            return false;
        }

        if (adHosts.contains(requestHost)) {
            return true;
        }
        
        String[] subDomains = requestHost.split("\\.");

        for (int i = subDomains.length - 1; i >= 0; i--) {
            String domain = String.join(".", Arrays.copyOfRange(subDomains, i, subDomains.length));

            if (adHosts.contains(domain)) {
                return true;
            }
        }

        return false;
    }

    private void loadHostsFromInputStream(InputStream inputStream) {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
            String line;
            while ((line = reader.readLine()) != null) {
                adHosts.add(line);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

public class MainActivity extends Activity {
    private final int STORAGE_PERMISSION_CODE = 1;
    private static final String[] ALLOWED_DOMAINS = BuildConfig.ALLOWED_DOMAINS.split(",");
    private static final String STARTUP_URL = BuildConfig.STARTUP_URL;
    private static final String VIEW_MODE = BuildConfig.VIEW_MODE;
    private static final boolean BLOCK_MEDIA = BuildConfig.BLOCK_MEDIA;
    private static final boolean BLOCK_ADS = BuildConfig.BLOCK_ADS;
    private static final boolean NO_SSL = BuildConfig.NO_SSL;
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        if (requestCode == FILECHOOSER_RESULTCODE) {
            if (null == mUploadMessage && null == mFilePathCallback) return;
            Uri result = intent == null || resultCode != RESULT_OK ? null : intent.getData();
            if (mFilePathCallback != null) {
                mFilePathCallback.onReceiveValue(WebChromeClient.FileChooserParams.parseResult(resultCode, intent));
                mFilePathCallback = null;
            } else if (mUploadMessage != null) {
                mUploadMessage.onReceiveValue(result);
                mUploadMessage = null;
            }
        }
    }

    private ValueCallback<Uri> mUploadMessage;
    private ValueCallback<Uri[]> mFilePathCallback;
    private final static int FILECHOOSER_RESULTCODE = 1;

    @RequiresApi(api = Build.VERSION_CODES.Q)
    @Override
    @SuppressLint("SetJavaScriptEnabled")
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AdBlockerUtil adBlockerUtil = AdBlockerUtil.getInstance();
        adBlockerUtil.initialize(this);
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
        webSettings.setAllowFileAccess(false);
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

            @Override
            public boolean onShowFileChooser(
                WebView webView, ValueCallback<Uri[]> filePathCallback, FileChooserParams fileChooserParams) {
                mFilePathCallback = filePathCallback;

                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.addCategory(Intent.CATEGORY_OPENABLE);

                if (fileChooserParams.getAcceptTypes().length > 0 && !Objects.equals(fileChooserParams.getAcceptTypes()[0], "")) {
                    intent.setType(fileChooserParams.getAcceptTypes()[0]);
                } else {
                    intent.setType("*/*");
                }

                startActivityForResult(intent, FILECHOOSER_RESULTCODE);
                return true;
            }

            // support android < 5.0
            public void openFileChooser(ValueCallback<Uri> uploadMsg, String acceptType) {
                mUploadMessage = uploadMsg;
                Intent i = new Intent(Intent.ACTION_GET_CONTENT);
                i.addCategory(Intent.CATEGORY_OPENABLE);
                
                if (acceptType != null && !acceptType.isEmpty()) {
                    i.setType(acceptType);
                } else {
                    i.setType("*/*");
                }
                startActivityForResult(Intent.createChooser(i, "Choice File"), FILECHOOSER_RESULTCODE);
            }
        });

        mWebView.setWebViewClient(new HelloWebViewClient());
        mWebView.setDownloadListener((url, userAgent, contentDisposition, mimeType, contentLength) -> {
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q && ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                requestStoragePermission();
            }
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
            Toast.makeText(getApplicationContext(), "Downloading File...", Toast.LENGTH_LONG).show();
        });
        mWebView.loadUrl(STARTUP_URL.isEmpty() ? "https://" + ALLOWED_DOMAINS[0] : STARTUP_URL);
    }
    
    private class HelloWebViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(final WebView view, final String url) {
            String host = Uri.parse(url).getHost();
            boolean isAllowed = false;

            if (url.startsWith("tel:")) {
                Intent tel = new Intent(Intent.ACTION_DIAL, Uri.parse(url));
                startActivity(tel);
                return true;
            }

            if (url.contains("mailto:")){
                   view.getContext().startActivity(
                    new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
            }

            if (host == null) {
                isAllowed = true; // mailto: links
            }
            if (!STARTUP_URL.isEmpty() && url.equals(STARTUP_URL)) {
                isAllowed = true;
            }
            for (String domain : ALLOWED_DOMAINS) {
                if (host == null || host.equals(domain) || host.equals("www." + domain) || host.equals("m." + domain)) {
                    isAllowed = true;
                    break;
                }
            }

            if (isAllowed) {
                mProgressBar.setVisibility(View.VISIBLE);
                view.loadUrl(url);
                return true;
            } else {
                Toast.makeText(view.getContext(), "This URL is not allowed (" + host + ")", Toast.LENGTH_SHORT).show();
                return true;
            }
        }

        @Override
        public WebResourceResponse shouldInterceptRequest(WebView view, String url) {
            if (BLOCK_ADS && AdBlockerUtil.getInstance().isAd(url)) {
                WebResourceResponse emptyResponse = new WebResourceResponse("text/plain", "utf-8", new ByteArrayInputStream("".getBytes()));
                return emptyResponse;
            } else {
                return super.shouldInterceptRequest(view, url);
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
            String issuerName = error.getCertificate().getIssuedBy().getOName();
            if (NO_SSL && (Objects.equals(issuerName, "NetFree") || Objects.equals(issuerName, "Netspark"))) {
                handler.proceed();
            } else {
                handler.cancel();
                view.loadData("<html><body><h1 style='color: grey'>SSL Error</h1></body></html>", "text/html; charset=utf-8", "UTF-8");
                if (Objects.equals(issuerName, "NetFree")) {
                    Toast.makeText(view.getContext(), "טיפ: נראה שלא מותקנת תעודת אבטחה של נטפרי", Toast.LENGTH_LONG).show();
                } else if (Objects.equals(issuerName, "Netspark")) {
                    Toast.makeText(view.getContext(), "טיפ: נראה שלא מותקנת תעודת אבטחה של אתרוג/רימון", Toast.LENGTH_LONG).show();
                }
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
