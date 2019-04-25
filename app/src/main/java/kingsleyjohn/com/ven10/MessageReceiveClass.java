package kingsleyjohn.com.ven10;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.telephony.SmsMessage;
import android.util.Log;

public class MessageReceiveClass extends BroadcastReceiver {

    private static MessageListenerInterface mListener;

    @Override
    public void onReceive(Context context, Intent intent) {
        String venten = "Ven10";
        Log.e("String", "New message");
        try{
            if(intent.getAction().equals("android.provider.Telephony.SMS_RECEIVED")){
                Bundle data = intent.getExtras();
                Object[] pdus = (Object[]) data.get("pdus");
                for(int i=0; i<pdus.length; i++){
                    SmsMessage sms = SmsMessage.createFromPdu((byte[]) pdus[i]);
                    if(getContactName(sms.getDisplayOriginatingAddress(), context).equalsIgnoreCase(venten)){
                        mListener.messageReceived(sms.getMessageBody());
                        Intent activityIntent = new Intent(context, CodedMessage.class);
                        activityIntent.putExtra("message", sms.getDisplayMessageBody());
                        context.startActivity(activityIntent);
                    }

                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    public String getContactName(final String phoneNumber, Context context)
    {
        Uri uri=Uri.withAppendedPath(ContactsContract.PhoneLookup.CONTENT_FILTER_URI,Uri.encode(phoneNumber));

        String[] projection = new String[]{ContactsContract.PhoneLookup.DISPLAY_NAME};

        String contactName="";
        Cursor cursor=context.getContentResolver().query(uri,projection,null,null,null);

        if (cursor != null) {
            if(cursor.moveToFirst()) {
                contactName=cursor.getString(0);
            }
            cursor.close();
        }
        return contactName;
    }

    public static void bindListener(MessageListenerInterface listener){
        mListener = listener;
        Log.e("listener", "Binded");
    }
}
