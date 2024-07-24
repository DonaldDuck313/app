package io.github.donaldduck313.myapplication

import android.annotation.SuppressLint
import android.os.Bundle
import android.webkit.CookieManager
import android.webkit.WebView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val webView: WebView = findViewById(R.id.webView)
        WebView.setWebContentsDebuggingEnabled(true)

        //Enable Javascript and third-party cookies
        webView.settings.javaScriptEnabled = true
        val cookieManager = CookieManager.getInstance()
        cookieManager.setAcceptThirdPartyCookies(webView, true)
        webView.loadUrl("about:blank")

        //This shows "good" in a toast, so the webview correctly registered that third-party cookies are enabled
        Toast.makeText(this, if(cookieManager.acceptThirdPartyCookies(webView)) "good" else "bad", Toast.LENGTH_LONG).show()

        //But when actually trying to use third-party cookies it doesn't work.
        //https://mapcollector.eu5.org/ajax/cookietest.php contains the code from https://stackoverflow.com/a/59843869/4284627 to check if third-party cookies are enabled.
        //When you run this, it will print "bad" in the web view body, indicating that third-party cookies are actually not enabled.
        webView.evaluateJavascript(
            "function myCallback(is_enabled) {document.body.textContent = is_enabled ? 'good' : 'bad'}\n"
                    +"script = document.createElement('script');\n"
                    +"script.src = 'https://mapcollector.eu5.org/ajax/cookietest.php?callback=myCallback';\n"
                    +"document.body.appendChild(script);"
            , null)
    }
}