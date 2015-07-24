package com.android.bizvoxexam;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;


public class MainActivity extends Activity {

    public static ListShotsApiDribbble dribbbleListShots = null;

    public static Context context;
    public static ListView listView ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;
        dribbbleListShots = new ListShotsApiDribbble(MainActivity.this);
        setContentView(R.layout.activity_main);

        listView = (ListView) findViewById(R.id.listShots);

        if (isNetworkAvailable()){
            dribbbleListShots.receiveShotsList();
        }
        else {
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
            alertDialogBuilder.setTitle("Error");
            alertDialogBuilder
                    .setMessage("You should have internet access !")
                    .setCancelable(false)
                    .setPositiveButton("Exit",new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog,int id) {
                            // if this button is clicked, close
                            // current activity
                            MainActivity.this.finish();
                        }
                    });

            AlertDialog alertDialog = alertDialogBuilder.create();
            alertDialog.show();
        }

    }

    public static void loadDribbbleData(){

        listView.setAdapter(new ShotsListAdapter(context, R.layout.list_shots_row, dribbbleListShots.getShotsList()));
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent();
                intent.putExtra("id", dribbbleListShots.getShotId(position));
                intent.setClass(context, GetAShotActivity.class);
                context.startActivity(intent);
            }
        });

    }


    private boolean isNetworkAvailable(){
        boolean available = false;
        ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

        if(networkInfo !=null && networkInfo.isAvailable())
            available = true;

        return available;
    }


}
