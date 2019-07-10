package edu.hope.cs.bilancioandroid.Controller;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class MyReceiver extends BroadcastReceiver {
    String title;
    String text;
    @Override
    public void onReceive(Context context, Intent intent)
    {
      /*Intent service1 = new Intent(context, MyAlarmService.class);
        context.startService(service1);*/
        Log.i("App", "called receiver method");
        String type = intent.getStringExtra("type");
        if(type.equals("Bill")){
            title = intent.getStringExtra("title");
            text = intent.getStringExtra("title")+ " is due. Have you paid it?";
        }
        else if (type.equals("Income")){
            title = intent.getStringExtra("title");
            text = "Have you recorded your income?";
        }
        else{
            title = intent.getStringExtra("title");
            text = "Have you recorded your transactions for the day?";
        }
        try{
            Utils.generateNotification(context, title, text);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
}
