package edu.uw.tcss450.stran373.services;

import android.app.ActivityManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import androidx.core.app.NotificationCompat;
import androidx.navigation.Navigation;

import org.json.JSONException;

import edu.uw.tcss450.stran373.AuthActivity;
import edu.uw.tcss450.stran373.MainActivity;
import edu.uw.tcss450.stran373.R;
import edu.uw.tcss450.stran373.ui.Chat.Message.ChatMessage;
import me.pushy.sdk.Pushy;

import static android.app.ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND;
import static android.app.ActivityManager.RunningAppProcessInfo.IMPORTANCE_VISIBLE;
import static edu.uw.tcss450.stran373.MainActivity.CHAT_ID;
import static edu.uw.tcss450.stran373.MainActivity.NEW_MESSAGE;

public class PushReceiver extends BroadcastReceiver {

    public static final String RECEIVED_NEW_MESSAGE = "new message from pushy";
    public static final String RECEIVED_CHAT_CREATION = "chatcreation";
    public static final String RECEIVED_NEW_INVITE = "new invite from pushy";

    private static final String CHANNEL_ID = "1";

    @Override
    public void onReceive(Context context, Intent intent) {

        String typeOfMessage = intent.getStringExtra("type");
        if (typeOfMessage.equals(RECEIVED_CHAT_CREATION)) {

            Intent i = new Intent(context, AuthActivity.class);
            i.putExtras(intent.getExtras());

            PendingIntent pendingIntent = PendingIntent.getActivity(context, 0,
                    i, PendingIntent.FLAG_UPDATE_CURRENT);

            ActivityManager.RunningAppProcessInfo appProcessInfo = new ActivityManager.RunningAppProcessInfo();
            ActivityManager.getMyMemoryState(appProcessInfo);

            Log.d("OK GOOGLE", intent.getStringExtra("groupname"));

            // App is in background, build the notification
            if (!(appProcessInfo.importance == IMPORTANCE_FOREGROUND) && !(appProcessInfo.importance == IMPORTANCE_VISIBLE)) {

                NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
                        .setAutoCancel(true)
                        .setSmallIcon(R.drawable.ic_baseline_group_notification)
                        .setContentTitle("Chat Creation")
                        .setContentText("You are now part of " + intent.getStringExtra("groupname"))
                        .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                        .setContentIntent(pendingIntent);

                // Automatically configure a ChatMessageNotification Channel for devices running Android O+
                Pushy.setNotificationChannel(builder, context);

                // Get an instance of the NotificationManager service
                NotificationManager notificationManager =
                        (NotificationManager) context.getSystemService(context.NOTIFICATION_SERVICE);

                // Build the notification and display it
                notificationManager.notify(1, builder.build());
            } else {
                // Just update the chats page
            }

        }
        else if (typeOfMessage.equals("msg")) {
            ChatMessage message = null;
            int chatId = -1;
            try {
                message = ChatMessage.createFromJsonString(intent.getStringExtra("message"));
                chatId = intent.getIntExtra("chatid", -1);
            } catch (JSONException e) {
                //Web service sent us something unexpected...I can't deal with this.
                throw new IllegalStateException("Error from Web Service. Contact Dev Support");
            }

            ActivityManager.RunningAppProcessInfo appProcessInfo = new ActivityManager.RunningAppProcessInfo();
            ActivityManager.getMyMemoryState(appProcessInfo);

            if (appProcessInfo.importance == IMPORTANCE_FOREGROUND || appProcessInfo.importance == IMPORTANCE_VISIBLE) {
                //app is in the foreground so send the message to the active Activities
                Log.d("PUSHY", "Message received in foreground: " + message);

                //create an Intent to broadcast a message to other parts of the app.
                Intent i = new Intent(RECEIVED_NEW_MESSAGE);
                i.putExtra("chatMessage", message);
                i.putExtra("chatid", chatId);
                i.putExtras(intent.getExtras());

                context.sendBroadcast(i);

            } else {
                //app is in the background so create and post a notification
                Log.d("PUSHY", "Message received in background: " + message.getMessage());

                Intent i = new Intent(context, AuthActivity.class);
                i.putExtras(intent.getExtras());

                PendingIntent pendingIntent = PendingIntent.getActivity(context, 0,
                        i, PendingIntent.FLAG_UPDATE_CURRENT);

                //research more on notifications the how to display them
                //https://developer.android.com/guide/topics/ui/notifiers/notifications
                NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
                        .setAutoCancel(true)
                        .setSmallIcon(R.drawable.ic_chat_notification)
                        .setContentTitle("Message from: " + message.getSender())
                        .setContentText(message.getMessage())
                        .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                        .setContentIntent(pendingIntent);

                // Automatically configure a ChatMessageNotification Channel for devices running Android O+
                Pushy.setNotificationChannel(builder, context);

                // Get an instance of the NotificationManager service
                NotificationManager notificationManager =
                        (NotificationManager) context.getSystemService(context.NOTIFICATION_SERVICE);

                // Build the notification and display it
                notificationManager.notify(1, builder.build());
                CHAT_ID = chatId;
                NEW_MESSAGE = 1;
            }
        }
        else if (typeOfMessage.equals("invite")) {
            ActivityManager.RunningAppProcessInfo appProcessInfo = new ActivityManager.RunningAppProcessInfo();
            ActivityManager.getMyMemoryState(appProcessInfo);

            if (appProcessInfo.importance == IMPORTANCE_FOREGROUND || appProcessInfo.importance == IMPORTANCE_VISIBLE) {
                //app is in the foreground so send the message to the active Activities
                Log.d("PUSHY", "Invite received in foreground");

                //create an Intent to broadcast a message to other parts of the app.
                Intent j = new Intent(RECEIVED_NEW_INVITE);
                j.putExtra("INVITE", "new invite");

                context.sendBroadcast(j);
                Log.d("PUSHY", "sent invite broadcast");
            } else {
                //app is in the background so create and post a notification
                Log.d("PUSHY", "Invite received in background");

                Intent i = new Intent(context, AuthActivity.class);
                i.putExtras(intent.getExtras());

                PendingIntent pendingIntent = PendingIntent.getActivity(context, 0,
                        i, PendingIntent.FLAG_UPDATE_CURRENT);

                //research more on notifications the how to display them
                //https://developer.android.com/guide/topics/ui/notifiers/notifications
                NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
                        .setAutoCancel(true)
                        .setSmallIcon(R.drawable.ic_baseline_invite_notifications_24)
                        .setContentTitle("New Invite!")
                        .setContentText("You have received a new pending request.")
                        .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                        .setContentIntent(pendingIntent);

                // Automatically configure a ChatMessageNotification Channel for devices running Android O+
                Pushy.setNotificationChannel(builder, context);

                // Get an instance of the NotificationManager service
                NotificationManager notificationManager =
                        (NotificationManager) context.getSystemService(context.NOTIFICATION_SERVICE);

                // Build the notification and display it
                notificationManager.notify(1, builder.build());
            }
        }
    }
}