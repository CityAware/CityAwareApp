package com.example.cityaware



import android.annotation.SuppressLint
import android.app.DatePickerDialog
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



class AboutFragment : Fragment() {
    var binding: FragmentAboutBinding? = null
    var d = 1
    var m = 0
    var y = 2023
    @SuppressLint("UseRequireInsteadOfGet", "ClickableViewAccessibility")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAboutBinding.inflate(inflater, container, false)
        setDate()
        binding!!.dateEditBtn.setOnClickListener { view ->
            val dialog: Dialog = DatePickerDialog(
                context!!,
                { datePicker: DatePicker?, yy: Int, mm: Int, dd: Int ->
                    y = yy
                    m = mm
                    d = dd
                    setDate()
                },
                y,
                m,
                d
            )
            dialog.show()
        }
        binding!!.dateInputEt.setOnTouchListener(OnTouchListener { view, motionEvent ->
            if (motionEvent.action == MotionEvent.ACTION_DOWN) {
                val dialog: Dialog = DatePickerDialog(
                    context!!,
                    { datePicker: DatePicker?, yy: Int, mm: Int, dd: Int ->
                        y = yy
                        m = mm
                        d = dd
                        setDate2()
                    }, y, m, d
                )
                dialog.show()
                return@OnTouchListener true
            }
            false
        })
        return binding!!.getRoot()
    }

    @SuppressLint("SetTextI18n")
    fun setDate2() {
        binding!!.dateInputEt.setText("" + d + "/" + (m + 1) + "/" + y)
    }

    @SuppressLint("SetTextI18n")
    fun setDate() {
        binding!!.dateTv.text = "" + d + "/" + (m + 1) + "/" + y
    }
}