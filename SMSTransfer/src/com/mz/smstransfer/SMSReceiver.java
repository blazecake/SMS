package com.mz.smstransfer;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;
import android.text.TextUtils;
import android.widget.Toast;

public class SMSReceiver extends BroadcastReceiver {

	private static final String strRes = "android.provider.Telephony.SMS_RECEIVED";
	
	String SENT_SMS_ACTION="SENT_SMS_ACTION";
    String DELIVERED_SMS_ACTION="DELIVERED_SMS_ACTION";
	
	@Override
	public void onReceive(Context context, Intent intent) {
		if(strRes.equals(intent.getAction())){
			SharedPreferences sp=context.getSharedPreferences(context.getPackageName(), Activity.MODE_PRIVATE);
			
            StringBuilder sb = new StringBuilder();
            String phone=sp.getString(MainActivity.key_phone, "");
            if(TextUtils.isEmpty(phone)){
            	Toast.makeText(context, R.string.weishezhishoujihao, Toast.LENGTH_SHORT).show();
                return;
            }
            
            Bundle bundle = intent.getExtras();
            if(bundle!=null){
                Object[] pdus = (Object[])bundle.get("pdus");
                SmsMessage[] msg = new SmsMessage[pdus.length];
                for(int i = 0 ;i<pdus.length;i++){
                    msg[i] = SmsMessage.createFromPdu((byte[])pdus[i]);
                }
                
                for(SmsMessage curMsg:msg){
                    sb.append("From:");
                    sb.append(curMsg.getDisplayOriginatingAddress());
                    sb.append(" Content：");
                    sb.append(curMsg.getDisplayMessageBody());
                }
                sendSMS(context, phone, sb.toString());
            }
        }
	}
	
	/** 
     * Send SMS 
     * @param phoneNumber 
     * @param message 
     */  
    private void sendSMS(final Context context, String phoneNumber, String message) {
        //create the sentIntent parameter
        Intent sentIntent = new Intent(SENT_SMS_ACTION);  
        PendingIntent sentPI = PendingIntent.getBroadcast(context, 0, sentIntent,  
                0);  
        // create the deilverIntent parameter  
        Intent deliverIntent = new Intent(DELIVERED_SMS_ACTION);  
        PendingIntent deliverPI = PendingIntent.getBroadcast(context, 0,  
                deliverIntent, 0);  
  
        SmsManager sms = SmsManager.getDefault();  
        
        if (getLength(message) > (140-7)) {
            ArrayList<String> msgs = sms.divideMessage(message);
            for (String msg : msgs) {  
                sms.sendTextMessage(phoneNumber, null, msg, sentPI, deliverPI);  
            }
        } else {
            sms.sendTextMessage(phoneNumber, null, message, sentPI, deliverPI);  
        }
        Toast.makeText(context, "发送短信成功", Toast.LENGTH_SHORT).show();
          
        //register the Broadcast Receivers  
//        context.registerReceiver(new BroadcastReceiver(){
//            @Override  
//            public void onReceive(Context _context,Intent _intent)  
//            {  
//                switch(getResultCode()){  
//                    case Activity.RESULT_OK:  
//                        Toast.makeText(context,   
//                                "SMS sent success actions",  
//                                Toast.LENGTH_SHORT).show();  
//                        break;  
//                    case SmsManager.RESULT_ERROR_GENERIC_FAILURE:  
//                        Toast.makeText(context,   
//                                "SMS generic failure actions",  
//                                Toast.LENGTH_SHORT).show();  
//                        break;  
//                    case SmsManager.RESULT_ERROR_RADIO_OFF:  
//                        Toast.makeText(context,  
//                                "SMS radio off failure actions",  
//                                Toast.LENGTH_SHORT).show();  
//                        break;  
//                    case SmsManager.RESULT_ERROR_NULL_PDU:  
//                        Toast.makeText(context,   
//                                "SMS null PDU failure actions",  
//                                Toast.LENGTH_SHORT).show();  
//                        break;  
//                }  
//            }  
//        },  
//        new IntentFilter(SENT_SMS_ACTION));
//        context.registerReceiver(new BroadcastReceiver(){  
//            @Override  
//            public void onReceive(Context _context,Intent _intent)  
//            {  
//                Toast.makeText(context,   
//                        "SMS delivered actions",  
//                        Toast.LENGTH_SHORT).show();               
//            }
//        },
//        new IntentFilter(DELIVERED_SMS_ACTION));  
  
    }
    
    private int getLength(String msg){
    	if(msg==null){
    		return 0;
    	}
    	int count=0;
    	for(int i=0; i<msg.length(); i++){
    		if(msg.charAt(i)>255){
    			count+=2;
    		}else{
    			count+=1;
    		}
    	}
    	return count;
    }
    

}
