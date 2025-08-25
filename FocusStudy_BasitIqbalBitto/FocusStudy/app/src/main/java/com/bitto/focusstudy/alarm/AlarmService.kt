
package com.bitto.focusstudy.alarm

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Intent
import android.media.Ringtone
import android.media.RingtoneManager
import android.os.Build
import android.os.IBinder
import androidx.core.app.NotificationCompat

class AlarmService : Service() {
    private var ringtone: Ringtone? = null

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val nm = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        val channelId = "alarm_channel"
        if (Build.VERSION.SDK_INT >= 26) {
            nm.createNotificationChannel(
                NotificationChannel(channelId, "Alarm", NotificationManager.IMPORTANCE_HIGH)
            )
        }
        val n: Notification = NotificationCompat.Builder(this, channelId)
            .setContentTitle("Study Alarm")
            .setContentText("Show a book to stop the alarm")
            .setSmallIcon(android.R.drawable.ic_lock_idle_alarm)
            .setOngoing(true)
            .build()
        startForeground(1, n)

        val uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM)
        ringtone = RingtoneManager.getRingtone(applicationContext, uri)
        ringtone?.isLooping = true
        ringtone?.play()

        return START_STICKY
    }

    override fun onDestroy() {
        ringtone?.stop()
        ringtone = null
        super.onDestroy()
    }

    override fun onBind(intent: Intent?): IBinder? = null
}
