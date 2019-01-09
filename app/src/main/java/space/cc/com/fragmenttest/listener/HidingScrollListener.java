package space.cc.com.fragmenttest.listener;

import android.util.Log;

import androidx.recyclerview.widget.RecyclerView;

public abstract class HidingScrollListener extends RecyclerView.OnScrollListener {
    private static final String TAG = "HidingScrollListener";
    //隐藏的临界值
    private static final int HIDE_THRESHOLD = 20;
    private int scrolledDistance = 0;
    private boolean controlsVisible = true;

    @Override
    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {

        super.onScrolled(recyclerView, dx, dy);


        Log.d(TAG,"scrolledDistance="+scrolledDistance+",dy="+dy);
        if (scrolledDistance > HIDE_THRESHOLD && controlsVisible) {
//            onHide();
            controlsVisible = false;
            scrolledDistance = 0;
        } else if (scrolledDistance < -HIDE_THRESHOLD && !controlsVisible) {
//            往上滑动 显示
//            onShow();
            controlsVisible = true;
            scrolledDistance = 0;
        }
//        纵向滚动
        if((controlsVisible && dy>0) || (!controlsVisible && dy<0)) {
            scrolledDistance += dy;
        }

    }
    /**
         * @author  CF
         * @date   2019/1/9
         * @description
         *
         */
    public abstract void onHide(boolean isSlideToBottom,boolean isSlideToTop);
    /**
     * @author  CF
     * @date   2019/1/9
     * @description
     *
     */
    public abstract void onShow(boolean isSlideToBottom,boolean isSlideToTop);
    /**
     * @author  CF
     * @date   2019/1/9
     * @description
     *
     */
    public abstract void onScrollDistanceThresholdHide(boolean isSlideToBottom,boolean isSlideToTop);
    /**
     * @author  CF
     * @date   2019/1/9
     * @description
     *
     */
    public abstract void onScrollDistanceThresholdShow(boolean isSlideToBottom,boolean isSlideToTop);
    /**
     * @author  CF
     * @date   2019/1/9
     * @description
     *
     */
    protected boolean isSlideToBottom(RecyclerView recyclerView) {
        if (recyclerView == null) return false;
        if (recyclerView.computeVerticalScrollExtent() + recyclerView.computeVerticalScrollOffset() >= recyclerView.computeVerticalScrollRange())
            return true;
        return false;
    }

   /* computeVerticalScrollOffset：计算控件垂直方向的偏移值，

    computeVerticalScrollExtent：计算控件可视的区域，

    computeVerticalScrollRange：计算控件垂直方向的滚动范围*/
    protected boolean isSlideToTop(RecyclerView recyclerView) {
        if (recyclerView == null) return false;
        if (recyclerView.computeVerticalScrollExtent() + recyclerView.computeVerticalScrollOffset() >= recyclerView.computeVerticalScrollRange())
            return true;
        return false;
    }

/*    (ViewCompat.canScrollVertically(recyclerView, -1)  顶部是否可以滚动
      !ViewCompat.canScrollVertically(recyclerView, 1)  底部是否可以滚动
      */
}