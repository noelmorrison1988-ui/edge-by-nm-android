package za.nm.edge;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceError;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.view.Gravity;
import android.view.KeyEvent;
import android.content.Intent;
import android.net.Uri;

public class MainActivity extends Activity {
    private static final String HOME = "https://whimsical-smakager-361549.netlify.app/";
    private WebView web;
    private FrameLayout root;
    private View videoFullScreen;
    private WebChromeClient.CustomViewCallback videoCallback;

    @Override public void onCreate(Bundle state) {
        super.onCreate(state);
        getWindow().setStatusBarColor(Color.rgb(9,10,11));
        getWindow().setNavigationBarColor(Color.rgb(9,10,11));
        root = new FrameLayout(this); root.setBackgroundColor(Color.rgb(9,10,11));
        web = new WebView(this); web.setBackgroundColor(Color.rgb(9,10,11));
        WebSettings cfg = web.getSettings();
        cfg.setJavaScriptEnabled(true);
        cfg.setDomStorageEnabled(true);
        cfg.setMediaPlaybackRequiresUserGesture(false);
        cfg.setSupportMultipleWindows(false);
        cfg.setAllowFileAccess(false);
        cfg.setAllowContentAccess(false);
        cfg.setMixedContentMode(WebSettings.MIXED_CONTENT_NEVER_ALLOW);
        cfg.setUseWideViewPort(true);
        cfg.setLoadWithOverviewMode(true);
        cfg.setJavaScriptCanOpenWindowsAutomatically(false);
        web.setWebViewClient(new WebViewClient() {
            @Override public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest req) {
                Uri uri=req.getUrl();
                String scheme=uri.getScheme();
                if ("https".equals(scheme) || "http".equals(scheme)) {
                    if ("whimsical-smakager-361549.netlify.app".equals(uri.getHost())) return false;
                    // Video embeds and linked content are handled normally by their own frame;
                    // top-level external links are opened in an ordinary browser.
                    if (!req.isForMainFrame()) return false;
                    try { startActivity(new Intent(Intent.ACTION_VIEW, uri)); } catch(Exception ignored) {}
                    return true;
                }
                if (req.isForMainFrame()) { try { startActivity(new Intent(Intent.ACTION_VIEW, uri)); } catch(Exception ignored) {} }
                return true;
            }
        });
        web.setWebChromeClient(new WebChromeClient() {
            @Override public void onShowCustomView(View view, CustomViewCallback callback) {
                if (videoFullScreen!=null) { callback.onCustomViewHidden(); return; }
                videoFullScreen=view; videoCallback=callback;
                root.addView(view,new FrameLayout.LayoutParams(-1,-1));
                web.setVisibility(View.GONE);
                getWindow().getDecorView().setSystemUiVisibility(5894|1024|512);
            }
            @Override public void onHideCustomView() { hideFullScreen(); }
        });
        root.addView(web,new FrameLayout.LayoutParams(-1,-1));
        setContentView(root);
        web.loadUrl(HOME);
    }
    private void hideFullScreen() {
        if (videoFullScreen==null) return;
        root.removeView(videoFullScreen); videoFullScreen=null;
        web.setVisibility(View.VISIBLE);
        getWindow().getDecorView().setSystemUiVisibility(0);
        if(videoCallback!=null) {videoCallback.onCustomViewHidden();videoCallback=null;}
    }
    @Override public void onBackPressed() {
        if(videoFullScreen!=null){hideFullScreen();return;}
        if(web.canGoBack()){web.goBack();return;}
        super.onBackPressed();
    }
    @Override protected void onPause(){ super.onPause(); if(web!=null)web.onPause(); }
    @Override protected void onResume(){ super.onResume(); if(web!=null)web.onResume(); }
    @Override protected void onDestroy(){if(web!=null){web.destroy();web=null;}super.onDestroy();}
}
