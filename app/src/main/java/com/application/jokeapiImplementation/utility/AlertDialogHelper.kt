package com.application.jokeapiImplementation.utility

import android.app.AlertDialog
import android.content.Context

object AlertDialogHelper {
    fun showAlert(context: Context?,title : String ,message : String,callback: Callback) {
        AlertDialog.Builder(context).setTitle(title).setMessage(message)
            .setPositiveButton(
                "OK"
            ) { _, _ -> callback.onSucess(0) }
            .show()
    }

    interface Callback {
        fun onSucess(t: Int)
    }
}
