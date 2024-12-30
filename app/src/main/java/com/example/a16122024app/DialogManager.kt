package com.example.a16122024app

import android.content.Context
import androidx.appcompat.app.AlertDialog
import okhttp3.internal.http2.Http2Connection.Listener

object DialogManager {
    fun locationSettingsDialog(context: Context, listener: Listener){
        val builder = AlertDialog.Builder(context)
        val dialog = builder.create()
        dialog.setTitle("Enable Location?")
        dialog.setMessage("Location is not enabled, do you want to enable it?")
        dialog.setButton(AlertDialog.BUTTON_POSITIVE,"Yes"){ _,_,->
            listener.onClick()
            dialog.dismiss()
        }

        dialog.setButton(AlertDialog.BUTTON_NEGATIVE,"No"){_,_,->
            dialog.dismiss()
        }
        dialog.show()
    }

    interface Listener{
        fun onClick()
    }
}