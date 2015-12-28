package com.loginapplication;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.paypal.android.MEP.PayPal;
import com.paypal.android.MEP.PayPalActivity;
import com.paypal.android.MEP.PayPalPayment;

import java.math.BigDecimal;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by blakshma on 10/17/2015.
 */
public class DisplayGarage extends AppCompatActivity {

   // private Integer images = R.drawable.garagea;
    int[] x1= new int[]{20,220,420,630,840,1035,1235,350,350,350,350,350,350,735,735,735,735,735,735};
    int[] x2= new int[]{180,380,600,810,1000,1200,1420,700,700,700,700,700,700,1080,1080,1080,1080,1080,1080};
    int[] y1= new int[]{20,20,20,20,20,20,20,770,1030,1290,1575,1850,2115,770,1030,1290,1575,1850,2115};
    int[] y2= new int[]{380,380,380,380,380,380,380,980,1250,1530,1810,2070,2345,980,1250,1530,1810,2070,2345};
    String userName;
    String user=null,phone=null,email=null,plateDetail=null,usage=null,cost=null;
    boolean[] occupancy;
    final int PAYPAL_RESPONSE=100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Bundle bundle = getIntent().getExtras();
        userName = bundle.getString("username");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.displa);
        drawCanvas();
        initLibrary();

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.displa, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.update_display:
                drawCanvas();
               return true;
            case R.id.logout:
            {
                Intent intentLogout=new Intent(getApplicationContext(),HomeActivity.class);
                startActivity(intentLogout);
            }
                return true;
            case R.id.menu_settings:
                return true;
            case R.id.account:
            {
                Util.validateCredentials("http://192.168.2.16:3000/userdetails" + "?phone=" + userName);

                while(Util.polled==false);
                try {
                    JSONObject resultsObject = new JSONObject(Util.sb.toString());
                    user=resultsObject.getString("name");
                    phone=resultsObject.getString("phone");
                    email=resultsObject.getString("email");
                    plateDetail=resultsObject.getString("licensePlate");
                    usage=resultsObject.getString("timeSpent");
                    cost = resultsObject.getString("payment");

                }catch (JSONException e) {
                    e.printStackTrace();
                }
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
                alertDialogBuilder.setTitle("Hello "+user+"!");
                alertDialogBuilder.setMessage("You have used "+usage+" mins\nTotal: $"+cost);

                alertDialogBuilder.setPositiveButton("Pay Now", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        //String recipientEmail="barathblue@gmail.com";
                        String recipientEmail="barath.lakshmanan-facilitator@okstate.edu";
                        initLibrary();
                        PayPalButtonClick(recipientEmail);

                    }
                });

                alertDialogBuilder.setNegativeButton("Ok",new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //finish();
                    }
                });

                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();
            }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void drawCanvas() {

        Util.validateCredentials("http://192.168.2.16:3000/findlot" + "?phone=" + userName);
        int cameraId=-1;
        String licensePlate=null;
        while(Util.polled==false);
        try {
            JSONObject resultsObject = new JSONObject(Util.sb.toString());
            cameraId = Integer.parseInt(resultsObject.getString("cameraId"));
            licensePlate = resultsObject.getString("licensePlate");
            occupancy = new boolean[21];

            for(int i=0;i<21;i++)
            {
                JSONArray jsonArray = resultsObject.getJSONArray("results");
                int resultantId = jsonArray.getJSONObject(i).getInt("cameraId");
                occupancy[resultantId] = jsonArray.getJSONObject(i).getBoolean("occupied");


            }
            Log.d("", "");


        }catch (JSONException e) {
            e.printStackTrace();
        }




        Paint paint = new Paint();
        Resources res = getResources();
        Bitmap bitmap = BitmapFactory.decodeResource(res, R.drawable.garagea);
        Bitmap bg = bitmap.copy(Bitmap.Config.ARGB_8888, true);
        Canvas canvas = new Canvas(bg);
        for (int tt=1;tt<x1.length+1;tt++) {
            if (occupancy[tt])
                paint.setColor(Color.parseColor("#CD5C5C"));
            else
                paint.setColor(Color.parseColor("#22cc33"));
            canvas.drawRect(x1[tt-1]/2, y1[tt-1]/2, x2[tt-1]/2, y2[tt-1]/2, paint);
        }
        if (cameraId!=0) {
            paint.setColor(Color.parseColor("#22ccff"));
            canvas.drawRect(x1[cameraId - 1] / 2, y1[cameraId - 1] / 2, x2[cameraId - 1] / 2, y2[cameraId - 1] / 2, paint);
            paint.setColor(Color.BLACK);
            paint.setTextSize(24);
            canvas.drawText(licensePlate, (x1[cameraId-1] / 2) + 20, (y1[cameraId-1] + y2[cameraId-1]) / 4, paint);
        }
        LinearLayout ll = (LinearLayout) findViewById(R.id.rect);
        ll.setBackground(new BitmapDrawable(getResources(), bg));

    }

    public void initLibrary() {
        PayPal pp = PayPal.getInstance();
        if (pp == null) {
            pp = PayPal.initWithAppID(this, Util.sand_box_id, PayPal.ENV_SANDBOX);
            pp.setLanguage("en_US");
            Toast.makeText(getApplicationContext(), "PayPal Initialized", Toast.LENGTH_LONG).show();
        }
    }

    public void PayPalButtonClick(String primary_id) {
        PayPalPayment payment = new PayPalPayment();
        // Set the currency type
        payment.setCurrencyType("USD");
        // Set the recipient for the payment (can be a phone number)
        payment.setRecipient(primary_id);
        payment.setPaymentType(PayPal.PAYMENT_TYPE_GOODS);
        if(cost.equals("") || cost.equals("0")) {
            Toast.makeText(getApplicationContext(), "Required Fields", Toast.LENGTH_LONG).show();
        }
        else {
            // Set the payment amount, excluding tax and shipping costs
            payment.setSubtotal(new BigDecimal("10.5"));
            Intent paypalIntent = PayPal.getInstance().checkout(payment, this);
            this.startActivityForResult(paypalIntent, PAYPAL_RESPONSE);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PAYPAL_RESPONSE) {
            switch (resultCode) {
                case Activity.RESULT_OK:
                    //The payment succeeded
                    String payKey =data.getStringExtra(PayPalActivity.EXTRA_PAY_KEY);
                    Toast.makeText(getApplicationContext(), "Payment done succesfully ", Toast.LENGTH_LONG).show();
                    break;
                case Activity.RESULT_CANCELED:
                    Toast.makeText(getApplicationContext(), "Payment Canceled , Try again ", Toast.LENGTH_LONG).show();
                    break;
                case PayPalActivity.RESULT_FAILURE:
                    String errorID = data.getStringExtra(PayPalActivity.EXTRA_ERROR_ID);
                    String errorMessage = data.getStringExtra(PayPalActivity.EXTRA_ERROR_MESSAGE);
                    Toast.makeText(getApplicationContext(), "Payment failed "+errorID+" "+errorMessage, Toast.LENGTH_LONG).show();
                    break;
            }
        }
    }
}