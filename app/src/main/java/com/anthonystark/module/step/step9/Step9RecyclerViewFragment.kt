package com.anthonystark.module.step.step9

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.OvershootInterpolator
import android.widget.TextView
import androidx.annotation.NonNull
import androidx.annotation.Nullable
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.anthonystark.module.R
import com.attractive.deer.util.layout.expandable.ExpandableLayout

class Step9RecyclerViewFragment : Fragment() {
    @Nullable
    override fun onCreateView(
        @NonNull inflater: LayoutInflater,
        @Nullable container: ViewGroup?,
        @Nullable savedInstanceState: Bundle?
    ): View {
        val rootView: View = inflater.inflate(R.layout.activity_step9_recycler_view_fragment, container, false)
        val recyclerView: RecyclerView = rootView.findViewById(R.id.recycler_view)
        recyclerView.setLayoutManager(LinearLayoutManager(getContext()))
        recyclerView.setAdapter(SimpleAdapter(recyclerView))
        return rootView
    }

    private class SimpleAdapter(recyclerView: RecyclerView) :
        RecyclerView.Adapter<SimpleAdapter.ViewHolder?>() {
        private val recyclerView: RecyclerView = recyclerView
        private var selectedItem = UNSELECTED
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val itemView: View = LayoutInflater.from(parent.context)
                .inflate(R.layout.activity_step9_recycler_item, parent, false)
            return ViewHolder(itemView)
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            holder.bind()
        }

        override fun getItemCount(): Int {
            return 100
        }

        inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView),
            View.OnClickListener, ExpandableLayout.OnExpansionUpdateListener {

            private val expandableLayout: ExpandableLayout = itemView.findViewById(R.id.expandable_layout)
            private val expandButton: TextView

            fun bind() {
                expandButton.text = "$adapterPosition. Tap to expand"
                expandButton.isSelected = false
                expandableLayout.setExpanded(expand = false, animate = false)
            }

            init {
                expandableLayout.setInterpolator(OvershootInterpolator())
                expandableLayout.setOnExpansionUpdateListener(this)
                expandButton = itemView.findViewById(R.id.expand_button)
                expandButton.setOnClickListener(this)
            }

            override fun onClick(view: View) {
                if(!expandButton.isSelected){
                    expandButton.isSelected = true
                    expandableLayout.expand()
                }else{
                    expandButton.isSelected = false
                    expandableLayout.collapse()
                }
            }

            override fun onExpansionUpdate(expansionFraction: Float, state: Int) {
                Log.d("ExpandableLayout", "State: $state")
                if (state == ExpandableLayout.State.EXPANDING) {
                    recyclerView.smoothScrollToPosition(adapterPosition)
                }
            }
        }

        companion object {
            private const val UNSELECTED = -1
        }

    }
}
