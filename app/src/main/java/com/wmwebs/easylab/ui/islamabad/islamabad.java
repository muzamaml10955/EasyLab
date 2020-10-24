package com.wmwebs.easylab.ui.islamabad;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.Environment;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.CookieManager;
import android.webkit.DownloadListener;
import android.webkit.URLUtil;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;
import android.widget.Toast;

import com.wmwebs.easylab.MainActivity;
import com.wmwebs.easylab.R;
import com.wmwebs.easylab.ui.aboutus.AboutusFragment;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static android.content.Context.DOWNLOAD_SERVICE;
import static androidx.core.content.ContextCompat.getSystemService;

public class islamabad extends Fragment {

    private static final String TAG = islamabad.class.getSimpleName();

    public static final int REQUEST_CODE_LOLIPOP = 1;
    private final static int RESULT_CODE_ICE_CREAM = 2;


    private ValueCallback<Uri[]> mFilePathCallback;
    private String mCameraPhotoPath;

    private ValueCallback<Uri> mUploadMessage;


    String url = "https://easylab.pk/islamabad-diagnostic/";
    public static final int RESULT_OK = -1;
    SwipeRefreshLayout swipeRefreshLayout;
    private final static int FCR = 1;
    WebView webView;
    private String mCM;
    private ValueCallback<Uri> mUM;
    private ValueCallback<Uri[]> mUMA;
    // private ValueCallback<Uri> mUploadMessage;
    private Uri mCapturedImageURI = null;
    //  private ValueCallback<Uri[]> mFilePathCallback;
    // private String mCameraPhotoPath;
    private static final int INPUT_FILE_REQUEST_CODE = 1;
    private static final int FILECHOOSER_RESULTCODE = 1;
    // private static final String TAG = MainActivity.class.getSimpleName();

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case RESULT_CODE_ICE_CREAM:
                Uri uri = null;
                if (data != null) {
                    uri = data.getData();
                }
                mUploadMessage.onReceiveValue(uri);
                mUploadMessage = null;
                break;
            case REQUEST_CODE_LOLIPOP:
                Uri[] results = null;
                // Check that the response is a good one
                if (resultCode == Activity.RESULT_OK) {
                    if (data == null) {
                        // If there is not data, then we may have taken a photo
                        if (mCameraPhotoPath != null) {
                            Log.d("AppChooserFragment", mCameraPhotoPath);

                            results = new Uri[]{Uri.parse(mCameraPhotoPath)};
                        }
                    } else {
                        String dataString = data.getDataString();
                        if (dataString != null) {
                            results = new Uri[]{Uri.parse(dataString)};
                        }
                    }
                }

                mFilePathCallback.onReceiveValue(results);
                mFilePathCallback = null;
                break;
        }
    }


    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        File imageFile = File.createTempFile(imageFileName, ".jpg", storageDir);
        return imageFile;
    }


    @SuppressLint({"SetJavaScriptEnabled", "WrongViewCast"})
    @Override

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_islamabad, container, false);


        webView = root.findViewById(R.id.islamabadwebview);
        swipeRefreshLayout = root.findViewById(R.id.islamabadswiperefreshayout);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                webView.clearFormData();
                webView.clearHistory();
                webView.clearCache(true);
                loadwebview(url);

            }
        });
        loadwebview(url);

        return root;
    }


    public void loadwebview(String url) {
        if (Build.VERSION.SDK_INT >= 23 && (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(getContext(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED)) {
            ActivityCompat.requestPermissions((Activity) getContext(), new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA}, 1);
        }
        assert webView != null;

        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setAllowFileAccess(true);


        //  webView.getSettings().setSupportZoom(true);
        //    webView.getSettings().setBuiltInZoomControls(true);

        if (Build.VERSION.SDK_INT >= 21) {
            webSettings.setMixedContentMode(0);
            webView.setLayerType(View.LAYER_TYPE_HARDWARE, null);
        } else if (Build.VERSION.SDK_INT >= 19) {
            webView.setLayerType(View.LAYER_TYPE_HARDWARE, null);
        } else if (Build.VERSION.SDK_INT < 19) {
            webView.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        }


        //webView.loadUrl("https://infeeds.com/");

        webView.loadUrl(url);
        //  webView.setWebViewClient(new Callback());
        //  webView.setWebViewClient(new MyBrowser());
        //faster to load
        webView.getSettings().setRenderPriority(WebSettings.RenderPriority.HIGH);
        webView.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
        webView.getSettings().setAppCacheEnabled(true);
        webView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        webSettings.setDomStorageEnabled(true);
        webSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NARROW_COLUMNS);
        webSettings.setUseWideViewPort(true);
        webSettings.setSavePassword(true);
        webSettings.setSaveFormData(true);
        swipeRefreshLayout.setRefreshing(true);




/*
        //handle downloading
        webView.setDownloadListener(new DownloadListener()
        {
            @Override
            public void onDownloadStart(String url, String userAgent,
                                        String contentDisposition, String mimeType,
                                        long contentLength) {
                DownloadManager.Request request = new DownloadManager.Request(
                        Uri.parse(url));
                request.setMimeType(mimeType);
                String cookies = CookieManager.getInstance().getCookie(url);
                request.addRequestHeader("cookie", cookies);
                request.addRequestHeader("User-Agent", userAgent);
                request.setDescription("Downloading File...");
                request.setTitle(URLUtil.guessFileName(url, contentDisposition, mimeType));
                request.allowScanningByMediaScanner();
                request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
                request.setDestinationInExternalPublicDir(
                        Environment.DIRECTORY_DOWNLOADS, URLUtil.guessFileName(
                                url, contentDisposition, mimeType));
                DownloadManager dm =(DownloadManager) this.getSystemService(Context.DOWNLOAD_SERVICE);
              //  DownloadManager dm = (DownloadManager)this.getSystemService(Context.DOWNLOAD_SERVICE);
                dm.enqueue(request);
                Toast.makeText(getContext(), "Downloading File", Toast.LENGTH_LONG).show();
            }});


        webView.setDownloadListener(new DownloadListener() {
            public void onDownloadStart(String url, String userAgent,
                                        String contentDisposition, String mimetype,
                                        long contentLength) {
                DownloadManager.Request request = new DownloadManager.Request(
                        Uri.parse(url));
                request.allowScanningByMediaScanner();
                request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
                request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, "download");
                DownloadManager dm = (DownloadManager) getActivity().getSystemService(Context.DOWNLOAD_SERVICE);
                dm.enqueue(request);

            }
        });





        wv.setDownloadListener(new DownloadListener() {
    @Override
    public void onDownloadStart(String url, String userAgent, String
    contentDisposition, String mimeType, long contentLength) {
        DownloadManager.Request request = new
    DownloadManager.Request(Uri.parse(url));

        request.setMimeType(mimeType);
        //------------------------COOKIE!!------------------------
        String cookies = CookieManager.getInstance().getCookie(url);
        request.addRequestHeader("cookie", cookies);
        //------------------------COOKIE!!------------------------
        request.addRequestHeader("User-Agent", userAgent);
        request.setDescription("Downloading file...");
        request.setTitle(URLUtil.guessFileName(url, contentDisposition, mimeType));
        request.allowScanningByMediaScanner();
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, URLUtil.guessFileName(url, contentDisposition, mimeType));
        DownloadManager dm = (DownloadManager) getSystemService(DOWNLOAD_SERVICE);
        dm.enqueue(request);
        Toast.makeText(getApplicationContext(), "Downloading File", Toast.LENGTH_LONG).show();
    }
});


*/


        webView.setDownloadListener(new DownloadListener() {
            public void onDownloadStart(String url, String userAgent,
                                        String contentDisposition, String mimetype,
                                        long contentLength) {
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(i);
            }
        });


        //back button implimetation
        webView.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if ((keyCode == KeyEvent.KEYCODE_BACK) && webView.canGoBack()) {
                    webView.goBack();
                    return true;
                }
                return false;
            }
        });
        // Force links and redirects to open in the WebView instead of in a browser
        webView.setWebViewClient(new WebViewClient());
        webView.setWebViewClient(new WebViewClient() {

            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {

                Toast.makeText(getContext(), "Failed loading app!, No Internet Connection found.", Toast.LENGTH_SHORT).show();
            }

            public void onPageFinished(WebView view, String url) {

                //Hide the SwipeReefreshLayout

                swipeRefreshLayout.setRefreshing(false);

            }


            /* @Override
             public boolean shouldOverrideUrlLoading(WebView view, String url) {
                 if (url.startsWith("https://easylab.pk/"))
                 {
                     view.loadUrl(url);
                     return true;
                 }
                 else{ if(url.startsWith("https://m.facebook.com"))


                 {
                     Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse("fb://messaging/117510536301396"));
                     startActivity(i);
                     return  true;}
                 else {
                     Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                     startActivity(intent);
                     return true;
                 }

                 }

             }
 */
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                Log.d("url", url);
                if (url.contains("https://easylab.pk/")) {
                    try {
                        view.loadUrl(url);
                    } catch (Exception e) {
                        Toast.makeText(getContext(), "Oups!Can't open Facebook messenger right now. Please install and try again later.", Toast.LENGTH_SHORT)
                                .show();
                        view.loadUrl(url);
                    }
                } else if (url.contains("intent://user/117510536301396/?intent_trigger=mme&nav=discover&source=customer_cha")) {

                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("fb-messenger://user/117510536301396"));
                    Log.d("muzammal1", url);
                    try {
                        startActivity(intent);

                    } catch (Exception e) {
                        Toast.makeText(getContext(), "Oups!Can't open Facebook messenger right now. Please install and try again later.", Toast.LENGTH_SHORT)
                                .show();
                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=com.facebook.orca&hl=en")));

                    }
                } else {

                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                    Log.d("muzammal3", url);
                    try {
                        startActivity(intent);
                    } catch (Exception e) {
                        Toast.makeText(getContext(), "Oups!Can't open Facebook messenger right now. Please install and try again later." + e, Toast.LENGTH_LONG)
                                .show();
                        view.loadUrl(url);
                    }
                }


                return true;

            }

        });


        webView.setWebChromeClient(new

                                           WebChromeClient() {


                                               private void openFileChooser(String type) {
                                                   Intent i = new Intent(Intent.ACTION_GET_CONTENT);
                                                   i.addCategory(Intent.CATEGORY_OPENABLE);
                                                   i.setType(type);
                                                   startActivityForResult(Intent.createChooser(i, getString(R.string.file_chooser)),
                                                           RESULT_CODE_ICE_CREAM);
                                               }

                                               private void onShowFileChooser(Intent cameraIntent) {
                                                   //整个弹出框为:相机、相册、文件管理
                                                   //如果安装了其他的相机、文件管理程序，也有可能会弹出
                                                   //selectionIntent(相册、文件管理)
                                                   //Intent selectionIntent = new Intent(Intent.ACTION_GET_CONTENT);
                                                   //selectionIntent.addCategory(Intent.CATEGORY_OPENABLE);
                                                   //selectionIntent.setType("image/*");

                                                   //------------------------------------
                                                   //如果通过下面的方式，则弹出的选择框有:相机、相册(Android9.0,Android8.0)
                                                   //如果是小米Android6.0系统上，依然是：相机、相册、文件管理
                                                   //如果安装了其他的相机(百度魔拍)、文件管理程序(ES文件管理器)，也有可能会弹出
                                                   Intent selectionIntent = new Intent(Intent.ACTION_PICK, null);
                                                   selectionIntent.setType("image/*");
                                                   //------------------------------------


                                                   Intent[] intentArray;
                                                   if (cameraIntent != null) {
                                                       intentArray = new Intent[]{cameraIntent};
                                                   } else {
                                                       intentArray = new Intent[0];
                                                   }

                                                   Intent chooserIntent = new Intent(Intent.ACTION_CHOOSER);
                                                   chooserIntent.putExtra(Intent.EXTRA_TITLE, getString(R.string.file_chooser));
                                                   chooserIntent.putExtra(Intent.EXTRA_INTENT, selectionIntent);
                                                   chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, intentArray);

                                                   startActivityForResult(chooserIntent, REQUEST_CODE_LOLIPOP);

                                               }


                                               //The undocumented magic method override
                                               //Eclipse will swear at you if you try to put @Override here
                                               // For Android 3.0+
                                               public void openFileChooser(ValueCallback<Uri> uploadMsg) {
                                                   mUploadMessage = uploadMsg;
                                                   openFileChooser("image/*");
                                               }

                                               // For Android 3.0+
                                               public void openFileChooser(ValueCallback uploadMsg, String acceptType) {
                                                   mUploadMessage = uploadMsg;
                                                   openFileChooser("*/*");
                                               }

                                               //For Android 4.1
                                               public void openFileChooser(ValueCallback<Uri> uploadMsg, String acceptType, String
                                                       capture) {
                                                   mUploadMessage = uploadMsg;
                                                   openFileChooser("image/*");
                                               }

                                               //For Android5.0+
                                               public boolean onShowFileChooser(WebView webView, ValueCallback<Uri[]>
                                                       filePathCallback, FileChooserParams fileChooserParams) {
                                                   if (mFilePathCallback != null) {
                                                       mFilePathCallback.onReceiveValue(null);
                                                   }
                                                   mFilePathCallback = filePathCallback;

                                                   Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                                                   if (takePictureIntent.resolveActivity(getActivity().getPackageManager()) != null) {
                                                       // Create the File where the photo should go
                                                       File photoFile = null;
                                                       try {
                                                           photoFile = createImageFile();
                                                           takePictureIntent.putExtra("PhotoPath", mCameraPhotoPath);
                                                       } catch (IOException ex) {
                                                           ex.printStackTrace();
                                                       }

                                                       // Continue only if the File was successfully created
                                                       if (photoFile != null) {
                                                           mCameraPhotoPath = "file:" + photoFile.getAbsolutePath();
                                                           takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(photoFile));
                                                       } else {
                                                           takePictureIntent = null;
                                                       }
                                                   }
                                                   onShowFileChooser(takePictureIntent);
                                                   return true;
                                               }
                                           });


    }

    private void GoForward() {
        if (webView.canGoForward()) {
            webView.goForward();
        } else {
            Toast.makeText(getContext(), "Can't go further!", Toast.LENGTH_SHORT).show();
        }
    }


}





























 /* public class Callback extends WebViewClient {
        public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
            Toast.makeText(getContext(), "Failed loading app!", Toast.LENGTH_SHORT).show();
        }

        public void onPageFinished(WebView view, String url) {

            //Hide the SwipeReefreshLayout


            swipeRefreshLayout.setRefreshing(false);
        }


    }


/*    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater myMenuInflater = getMenuInflater();
        myMenuInflater.inflate(R.menu.super_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }*/

 /*   @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case R.id.myMenuOne:
                onBackPressed();
                break;

            case R.id.myMenuTwo:
                GoForward();
                break;

        }
        return true;
    }*/



 /*   @Override
    public void onBackPressed() {
        if (webView.canGoBack()) {
            webView.goBack();
        }
    }*/


//}

    /*
    WebView webView;
    SwipeRefreshLayout swipeRefreshLayout;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_islamabad, container, false);
        webView = root.findViewById(R.id.islamabadwebview);
        swipeRefreshLayout=root.findViewById(R.id.islamabadswiperefreshayout);

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                webView.clearFormData();
                webView.clearHistory();
                webView.clearCache(true);
                loadwebview();

            }
        });

loadwebview();

        return root;
    }
    public void loadwebview() {
        // fruitewebview.clearCache(true);
        // Enable Javascript
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
//faster to load
        //  webView.getSettings().setRenderPriority(WebSettings.RenderPriority.HIGH);
        // webView.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
        // webView.getSettings().setAppCacheEnabled(true);
        webView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        //   webSettings.setDomStorageEnabled(true);
        webSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NARROW_COLUMNS);
        webSettings.setUseWideViewPort(true);
        //  webSettings.setSavePassword(true);
        //   webSettings.setSaveFormData(true);
        webView.loadUrl("https://easylab.pk/islamabad-diagnostic/");


         swipeRefreshLayout.setRefreshing(true);


        //back button implimetation
        webView.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if ((keyCode == KeyEvent.KEYCODE_BACK) && webView.canGoBack()) {
                    webView.goBack();
                    return true;
                }
                return false;
            }
        });
        // Force links and redirects to open in the WebView instead of in a browser
        webView.setWebViewClient(new WebViewClient());
        webView.setWebViewClient(new WebViewClient() {

            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {

                Toast.makeText(getContext(), "Failed loading app!, No Internet Connection found.", Toast.LENGTH_SHORT).show();
            }

            public void onPageFinished(WebView view, String url) {

                //Hide the SwipeReefreshLayout

                swipeRefreshLayout.setRefreshing(false);

            }


            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                if (url.startsWith("https://easylab.pk/"))
                {
                    view.loadUrl(url);
                    return true;
                }
                else{ if(url.startsWith("https://m.facebook.com"))


                {
                    Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse("fb://messaging/117510536301396"));
                    startActivity(i);
                    return  true;}
                else {
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                    startActivity(intent);
                    return true;
                }

                }

            }


        });


    }
}*/
