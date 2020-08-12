package com.befikey.user.view.services

import android.annotation.SuppressLint
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import android.os.Build
import android.support.v4.app.NotificationCompat
import android.util.Log
import android.widget.RemoteViews
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.teknosols.a3orrsy.R
import com.teknosols.a3orrsy.other.eventbus.EventBusFactory
import com.teknosols.a3orrsy.other.util.Parser
import com.teknosols.a3orrsy.view.activites.GlobalNavigationActivity
import com.teknosols.a3orrsy.view.activites.LandingActivity
import org.json.JSONObject
import java.util.*

class MyFirebaseMessagingService: FirebaseMessagingService() {

    val TAG = "FirebaseMessaging"

    override fun onNewToken(p0: String?) {
        super.onNewToken(p0)
        //TODO Send token to server
    }


    override fun onMessageReceived(remoteMessage: RemoteMessage?) {
        super.onMessageReceived(remoteMessage)
        // ...
        // TODO(developer): Handle FCM messages here.
        // Not getting messages here? See why this may be: https://goo.gl/39bRNJ
        Log.d(TAG, "From: " + remoteMessage?.getFrom());

        // Check if message contains a data payload.
        if (remoteMessage?.getData() != null && remoteMessage?.getData()!!.size > 0) {
            Log.d(TAG, "Message data payload: " + remoteMessage.getData());

           /* try {
                if (remoteMessage.data.containsKey("data")) {
                    val dataJson = remoteMessage.data.get("data")
                    val jsonObj = JSONObject(dataJson)
                    val jobStr = jsonObj.optString("job", "")
                    if (jobStr.isNotEmpty()) {
                        val booking = Parser.toBooking(jobStr ?: "")
//                        EventBusFactory.getEventBus().post(booking)
                    }
                }
            }catch (ex: Exception){
                ex.printStackTrace()
            }*/

            EventBusFactory.getEventBus().post("Testing")

        }

        // Check if message contains a notification payload.
        if (remoteMessage?.getNotification() != null) {
            Log.d(TAG, "Message Notification Body: " + remoteMessage?.getNotification()?.getBody());
        }

        // Also if you intend on generating your own notifications as a result of a received FCM
        // message, here is where that should be initiated. See sendNotification method below.
        showNotification(remoteMessage!!.getNotification()!!.body)
    }

    private fun showNotification(message: String?) {

        val intent: Intent
        intent = Intent(this, LandingActivity::class.java)

        intent.putExtra("message", message)
        //        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        val pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT)

        val uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        Log.i(TAG, "MyCheck  " + "OUT")

        val contentView = RemoteViews(packageName, R.layout.custom_notification)
        contentView.setImageViewResource(R.id.image, R.drawable.logo)
        contentView.setTextViewText(R.id.title, "3orrsy")
        contentView.setTextViewText(R.id.text, message)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            @SuppressLint("WrongConstant") val channel = NotificationChannel(
                "3orrsy_admin_my_channel1", "3orrsy Notifications",
                NotificationManager.IMPORTANCE_DEFAULT
            )
            notificationManager.createNotificationChannel(channel)

            val id = channel.id

            val notification = Notification.Builder(this, channel.id)
                .setSmallIcon(R.drawable.logo)
                .setSound(uri)
                .setCustomBigContentView(contentView)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true)
                .setPriority(Notification.PRIORITY_MAX)
                .setStyle(Notification.DecoratedCustomViewStyle())
                .build()

            notificationManager.notify(1, notification)


        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            val notification = Notification.Builder(this)
                .setSmallIcon(R.drawable.logo)
                .setSound(uri)
                .setCustomBigContentView(contentView)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true)
                .setPriority(Notification.PRIORITY_MAX)
                .setStyle(Notification.DecoratedCustomViewStyle())
                .build()
            notificationManager.notify(1, notification)
        } else {
            val notification = NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.logo)
                .setCustomContentView(contentView)
                .setSound(uri)
                .setCustomBigContentView(contentView)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true)
                .setPriority(Notification.PRIORITY_MAX)
                .setStyle(NotificationCompat.BigPictureStyle())
                .build()

            val number = (Date().time / 1000L % Integer.MAX_VALUE).toInt()
            notificationManager.notify(1, notification)//notification);
        }

    }
}