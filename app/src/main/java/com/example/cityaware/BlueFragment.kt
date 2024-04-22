package com.example.cityaware

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation.findNavController


class BlueFragment : Fragment() {
//    var titleTv: TextView? = null
//    var title: String? = null
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        val bundle = arguments
//        if (bundle != null) {
//            title = bundle.getString("TITLE")
//        }
//    }
//
//    override fun onCreateView(
//        inflater: LayoutInflater, container: ViewGroup?,
//        savedInstanceState: Bundle?
//    ): View? {
//        // Inflate the layout for this fragment
//        val view = inflater.inflate(R.layout.fragment_blue, container, false)
//        title = BlueFragmentArgs.fromBundle(arguments).getBlueTitle()
//        val titleTv = view.findViewById<TextView>(R.id.bluefrag_title_tv)
//        if (title != null) {
//            titleTv.text = title
//        }
//        val button = view.findViewById<View>(R.id.bluefrag_back_btn)
//        button.setOnClickListener { view1: View? ->
//            findNavController(
//                view1!!
//            ).popBackStack()
//        }
//        return view
//    }
//
//    fun setTitle(title: String?) {
//        this.title = title
//        if (titleTv != null) {
//            titleTv!!.text = title
//        }
//    }
//
//    companion object {
//        fun newInstance(title: String?): BlueFragment {
//            val frag = BlueFragment()
//            val bundle = Bundle()
//            bundle.putString("TITLE", title)
//            frag.setArguments(bundle)
//            return frag
//        }
//    }
}