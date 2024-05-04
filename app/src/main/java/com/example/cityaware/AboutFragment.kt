package com.example.cityaware

import android.app.DatePickerDialog
import android.app.DatePickerDialog.OnDateSetListener
import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.View.OnTouchListener
import android.view.ViewGroup
import android.widget.DatePicker
import androidx.fragment.app.Fragment
import com.example.cityaware.databinding.FragmentAboutBinding

class AboutFragment constructor() : Fragment() {
    var binding: FragmentAboutBinding? = null
    var d: Int = 1
    var m: Int = 0
    var y: Int = 2023
    public override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAboutBinding.inflate(inflater, container, false)
        setDate()
        binding!!.dateEditBtn.setOnClickListener(View.OnClickListener({ view: View? ->
            val dialog: Dialog = DatePickerDialog(
                requireContext(),
                OnDateSetListener({ datePicker: DatePicker?, yy: Int, mm: Int, dd: Int ->
                    y = yy
                    m = mm
                    d = dd
                    setDate()
                }),
                y,
                m,
                d
            )
            dialog.show()
        }))
        binding!!.dateInputEt.setOnTouchListener(object : OnTouchListener {
            public override fun onTouch(view: View, motionEvent: MotionEvent): Boolean {
                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                    val dialog: Dialog = DatePickerDialog(
                        requireContext(),
                        OnDateSetListener({ datePicker: DatePicker?, yy: Int, mm: Int, dd: Int ->
                            y = yy
                            m = mm
                            d = dd
                            setDate2()
                        }),
                        y,
                        m,
                        d
                    )
                    dialog.show()
                    return true
                }
                return false
            }
        })
        return binding!!.getRoot()
    }

    fun setDate2() {
        binding!!.dateInputEt.setText("" + d + "/" + (m + 1) + "/" + y)
    }

    fun setDate() {
        binding!!.dateTv.setText("" + d + "/" + (m + 1) + "/" + y)
    }
}