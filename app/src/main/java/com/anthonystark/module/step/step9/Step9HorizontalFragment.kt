package com.anthonystark.module.step.step9

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.annotation.NonNull
import androidx.annotation.Nullable
import androidx.fragment.app.Fragment
import com.anthonystark.module.R
import com.lovely.deer.util.layout.expandable.ExpandableLayout

class Step9HorizontalFragment : Fragment(), View.OnClickListener,
    ExpandableLayout.OnExpansionUpdateListener {
    private lateinit var expandableLayout: ExpandableLayout
    private lateinit var expandButton: ImageView
    @Nullable
    override fun onCreateView(
        @NonNull inflater: LayoutInflater,
        @Nullable container: ViewGroup?,
        @Nullable savedInstanceState: Bundle?
    ): View {
        val rootView: View = inflater.inflate(R.layout.activity_step9_horizontal_fragment, container, false)
        expandableLayout = rootView.findViewById(R.id.expandable_layout)
        expandButton = rootView.findViewById(R.id.expand_button)
        expandableLayout.setOnExpansionUpdateListener(this)
        expandButton.setOnClickListener(this)
        return rootView
    }

    override fun onExpansionUpdate(expansionFraction: Float, state: Int) {
        Log.d("ExpandableLayout", "State: $state")
        expandButton!!.rotation = expansionFraction * 180
    }

    override fun onClick(view: View) {
        expandableLayout.toggle()
    }
}
