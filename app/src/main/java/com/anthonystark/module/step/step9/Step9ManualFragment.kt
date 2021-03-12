package com.anthonystark.module.step.step9

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import android.widget.SeekBar.OnSeekBarChangeListener
import androidx.annotation.Nullable
import androidx.fragment.app.Fragment
import com.anthonystark.module.R
import com.attractive.deer.util.layout.expandable.ExpandableLayout

class Step9ManualFragment : Fragment(), ExpandableLayout.OnExpansionUpdateListener,
    OnSeekBarChangeListener {
    private lateinit var seekbar: SeekBar
    private lateinit var expandableLayout: ExpandableLayout
    private lateinit var content: View
    @Nullable
    override fun onCreateView(
        inflater: LayoutInflater,
        @Nullable container: ViewGroup?,
        @Nullable savedInstanceState: Bundle?
    ): View {
        val rootView: View = inflater.inflate(R.layout.activity_step9_manual_fragment, container, false)
        seekbar = rootView.findViewById(R.id.seek_bar)
        seekbar.setOnSeekBarChangeListener(this)
        expandableLayout = rootView.findViewById(R.id.expandable_layout)
        expandableLayout.setOnExpansionUpdateListener(this)
        content = rootView.findViewById(R.id.content)
        return rootView
    }

    override fun onExpansionUpdate(expansionFraction: Float, state: Int) {
        Log.d("ExpandableLayout", "State: $state expansionFraction: $expansionFraction")
        content!!.alpha = expansionFraction
    }

    override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
        expandableLayout.setExpansion(seekbar!!.progress / seekbar!!.max.toFloat())
    }

    override fun onStartTrackingTouch(seekBar: SeekBar) {}
    override fun onStopTrackingTouch(seekBar: SeekBar) {}
}
