package practice.kshitij.com.crashylytics_2;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import com.crashlytics.android.Crashlytics;
import com.crashlytics.android.ndk.CrashlyticsNdk;
import com.paytm.pgsdk.PaytmMerchant;
import com.paytm.pgsdk.PaytmOrder;
import com.paytm.pgsdk.PaytmPGService;
import com.paytm.pgsdk.PaytmPaymentTransactionCallback;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import io.fabric.sdk.android.Fabric;

public class MainActivity extends AppCompatActivity {
   // Integer i = null;
   private int randomInt = 0;
    private PaytmPGService Service = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fabric.with(this, new Crashlytics(), new CrashlyticsNdk());
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Random randomGenerator = new Random();
        randomInt = randomGenerator.nextInt(1000000000);

        //Service = PaytmPGService.getStagingService(); //for testing environment

        Service = PaytmPGService.getProductionService();


		/*PaytmMerchant constructor takes two parameters
		1) Checksum generation url
		2) Checksum verification url
		Merchant should replace the below values with his values*/

        PaytmMerchant Merchant = new PaytmMerchant("<checksum_signing_url>","<checksum_validation_url>");

        //below parameter map is required to construct PaytmOrder object, Merchant should replace below map values with his own values

        Map<String, String> paramMap = new HashMap<String, String>();

        //these are mandatory parameters
        paramMap.put("REQUEST_TYPE", "DEFAULT");
        paramMap.put("ORDER_ID", String.valueOf(randomInt));
        paramMap.put("MID", "mymerchantid");
        paramMap.put("CUST_ID", "CUST123");
        paramMap.put("CHANNEL_ID", "WAP");
        paramMap.put("INDUSTRY_TYPE_ID", "Retail120");
        paramMap.put("WEBSITE", "mywebsite");
        paramMap.put("TXN_AMOUNT", "1");
        paramMap.put("THEME", "merchant");



        PaytmOrder Order = new PaytmOrder(paramMap);


        Service.initialize(Order, Merchant, null);


        Service.startPaymentTransaction(this, false, true, new PaytmPaymentTransactionCallback() {
            @Override
            public void onTransactionSuccess(Bundle bundle) {


                Log.i("Success", "onTransactionSuccess :" + bundle);
            }

            @Override
            public void onTransactionFailure(String s, Bundle bundle) {
                Log.i("Failure", "onTransactionFailure " + s);
            }

            @Override
            public void networkNotAvailable() {
                Log.i("Failure", "networkNotAvailable");
            }

            @Override
            public void clientAuthenticationFailed(String s) {
                Log.i("Failure", "clientAuthenticationFailed " + s);
            }

            @Override
            public void someUIErrorOccurred(String s) {
                Log.i("Failure", "someUIErrorOccurred " + s);
            }

            @Override
            public void onErrorLoadingWebPage(int i, String s, String s1) {
                Log.i("Failure", "onErrorLoadingWebPage" + s + " " + s1);
            }

            @Override
            public void onBackPressedCancelTransaction() {

            }
        });




       // i.byteValue();

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
