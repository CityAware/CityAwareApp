package com.example.karenhub

import android.app.AlertDialog
import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import android.widget.Toast
import androidx.fragment.app.DialogFragment

class AlertDialogFragment constructor() : DialogFragment() {
    public override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        super.onCreateDialog(savedInstanceState)
        val builder: AlertDialog.Builder = AlertDialog.Builder(getActivity())
        builder.setTitle("This is my Alert!!!")
        builder.setMessage("This is the content of nmy alert")
        builder.setPositiveButton(
            "OK",
            DialogInterface.OnClickListener({ dialogInterface: DialogInterface?, i: Int ->
                Toast.makeText(
                    getContext(),
                    "Alert is OK",
                    Toast.LENGTH_LONG
                ).show()
            })
        )
        return builder.create()
    }
}