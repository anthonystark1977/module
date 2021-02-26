import androidx.recyclerview.widget.PagerSnapHelper
import com.lovely.deer.listener.SnapPagerScrollListener

import java.util.concurrent.atomic.AtomicInteger

fun PagerSnapHelper.getPagerScrollListener(pos: AtomicInteger): SnapPagerScrollListener {
    return SnapPagerScrollListener(
        snapHelper = this,
        type = SnapPagerScrollListener.ON_SETTLED,
        notifyOnInit = true,
        listener = object : SnapPagerScrollListener.OnChangeListener {
            override fun onSnapped(position: Int) {
                pos.set(position)
            }
        }
    )
}