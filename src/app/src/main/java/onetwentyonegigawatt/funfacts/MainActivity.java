package onetwentyonegigawatt.funfacts;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextSwitcher;
import android.widget.TextView;
import android.content.ClipboardManager;
import com.facebook.AppEventsLogger;
import com.facebook.UiLifecycleHelper;
import com.facebook.widget.FacebookDialog;

import java.lang.reflect.Type;


public class MainActivity extends Activity {

    private UiLifecycleHelper uiHelper;
    private Activity thisAcitivty = this;

    @Override
    protected void onRestart() {
        super.onRestart();
        AppEventsLogger.activateApp(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        AppEventsLogger.deactivateApp(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Context context = MainActivity.this;
        TextSwitcher textSwitcher = new TextSwitcher(context);
        final ColorWheel colorWheel = new ColorWheel();
        setContentView(R.layout.activity_main);
        final RelativeLayout relativeLayout = (RelativeLayout) findViewById(R.id.relativeLayout);
        final TypeWriter factLabel = (TypeWriter) findViewById(R.id.txtFact);
        final ImageButton btnFbook = (ImageButton) findViewById(R.id.btnShareFbook);
        Button showFactButton = (Button) findViewById(R.id.btnShowFact);
        final DownloadWebpageTask dWt = new DownloadWebpageTask(MainActivity.this);
        View.OnClickListener showFactButtonOnClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    relativeLayout.setBackgroundColor(Color.parseColor(colorWheel.GetColor()));
                    ConnectivityManager connMgr = (ConnectivityManager)
                            getSystemService(Context.CONNECTIVITY_SERVICE);
                    NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
                    if (networkInfo != null && networkInfo.isConnected()) {
                        new DownloadWebpageTask(MainActivity.this).execute("");
                    } else {
                        factLabel.animateText("No network connection available.");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        };
        showFactButton.setOnClickListener(showFactButtonOnClickListener);

        View.OnClickListener showFbookDialog = new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                ClipboardManager clipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("Wisdom",
                        factLabel.getText().toString() + " #wisdomofchopra #choprawisdom");
                clipboard.setPrimaryClip(clip);
                FacebookDialog shareDialog = new FacebookDialog.ShareDialogBuilder(thisAcitivty)
                        .setLink(null)
                        .setCaption(factLabel.getText().toString() + " #wisdomofchopra #choprawisdom")
                        .setDescription(factLabel.getText().toString() + " #wisdomofchopra #choprawisdom")
                        .build();
                uiHelper.trackPendingDialogCall(shareDialog.present());
                clipboard.getPrimaryClip();
            }
        };
        btnFbook.setOnClickListener(showFbookDialog);

        uiHelper = new UiLifecycleHelper(this, null){
            @Override
            public void onCreate(Bundle savedInstanceState) {
                super.onCreate(savedInstanceState);
                
            }

            @Override
            public void onResume() {
                super.onResume();
                uiHelper.onResume();
            }

            @Override
            public void onSaveInstanceState(Bundle outState) {
                super.onSaveInstanceState(outState);
                uiHelper.onSaveInstanceState(outState);
            }

            @Override
            public void onPause() {
                super.onPause();
                uiHelper.onPause();
            }

            @Override
            public void onDestroy() {
                super.onDestroy();
                uiHelper.onDestroy();
            }
            @Override
            public void onActivityResult(int requestCode, int resultCode, Intent data) {
                super.onActivityResult(requestCode, resultCode, data);

                uiHelper.onActivityResult(requestCode, resultCode, data, new FacebookDialog.Callback() {
                    @Override
                    public void onError(FacebookDialog.PendingCall pendingCall, Exception error, Bundle data) {
                        Log.e("Activity", String.format("Error: %s", error.toString()));
                    }

                    @Override
                    public void onComplete(FacebookDialog.PendingCall pendingCall, Bundle data) {
                        Log.i("Activity", "Success!");
                    }
                });
            }


        };
        uiHelper.onCreate(savedInstanceState);

    }


    private class DownloadWebpageTask extends AsyncTask<String, Void, String> {
        private ProgressDialog mDialog;
        Context mCntxt;
        public DownloadWebpageTask(Context context)
        {
            mCntxt = context;
        }
        @Override
        protected String doInBackground(String... strings) {
            try {
                ChopraWisdom cW =  new ChopraWisdom(getResources().getString(R.string.strBaseUrl));
                return cW.GetQuote();
            }
            catch(Exception e){
                e.printStackTrace();
                return e.getMessage();
            }
        }
        @Override
        protected void onPostExecute(String result) {
            try {
                TypeWriter factLabel = (TypeWriter) findViewById(R.id.txtFact);
                factLabel.animateText(result);
                mDialog.hide();
                ((Button) findViewById(R.id.btnShowFact)).setEnabled(true);
            }
            catch(Exception e){
                e.printStackTrace();
            }
        }
        @Override
        protected void onPreExecute(){
            ((Button) findViewById(R.id.btnShowFact)).setEnabled(false);
            new ProgressDialog(mCntxt);
            mDialog = ProgressDialog.show(mCntxt,"Invoking Chopra","Seeking Wisdom");

        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
