
package com.bitto.focusstudy.alarm

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.bitto.focusstudy.ui.AlarmCaptureActivity

class AlarmReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent?) {
        val i = Intent(context, AlarmCaptureActivity::class.java).apply {
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP)
            action = "com.bitto.focusstudy.ACTION_ALARM"
        }
        context.startActivity(i)
    }
}
