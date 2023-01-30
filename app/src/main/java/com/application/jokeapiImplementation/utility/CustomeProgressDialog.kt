package com.application.jokeapiImplementation.utility

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import com.application.jokeapiImplementation.R


class CustomProgressDialog(context: Context?) : Dialog(context!!) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dialog_progress)
        setCancelable(false)
        setCanceledOnTouchOutside(false)
    }

}