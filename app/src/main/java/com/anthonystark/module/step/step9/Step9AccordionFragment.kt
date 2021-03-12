package com.anthonystark.module.step.step9

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.NonNull
import androidx.annotation.Nullable
import androidx.fragment.app.Fragment
import com.anthonystark.module.R
import com.attractive.deer.util.layout.expandable.ExpandableLayout

class Step9AccordionFragment : Fragment(), View.OnClickListener {
    private lateinit var expandableLayout0: ExpandableLayout
    private lateinit var expandableLayout1: ExpandableLayout
    @Nullable
    override fun onCreateView(
        @NonNull inflater: LayoutInflater,
        @Nullable container: ViewGroup?,
        @Nullable savedInstanceState: Bundle?
    ): View {
        val rootView: View = inflater.inflate(R.layout.activity_step9_accordion_fragment, container, false)
        expandableLayout0 = rootView.findViewById(R.id.expandable_layout_0)
        expandableLayout1 = rootView.findViewById(R.id.expandable_layout_1)
        expandableLayout0.setOnExpansionUpdateListener(object : ExpandableLayout.OnExpansionUpdateListener{
            override fun onExpansionUpdate(expansionFraction: Float, state: Int) {
                Log.d("ExpandableLayout0", "State: $state")
            }
        })
        expandableLayout1.setOnExpansionUpdateListener(object : ExpandableLayout.OnExpansionUpdateListener {
            override fun onExpansionUpdate(expansionFraction: Float, state: Int) {
                Log.d("ExpandableLayout1", "State: $state")
            }
        })
        rootView.findViewById<View>(R.id.expand_button_0).setOnClickListener(this)
        rootView.findViewById<View>(R.id.expand_button_1).setOnClickListener(this)
        return rootView
    }

    override fun onClick(view: View) {
        if (view.id == R.id.expand_button_0) {
            expandableLayout0.expand()
            expandableLayout1.collapse()
        } else {
            expandableLayout0.collapse()
            expandableLayout1.expand()
        }
    }
}
