package com.autumn.framework.View;


import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.Scroller;
import android.widget.TextView;

import com.autumn.reptile.R;

import java.util.Calendar;


/**
 * 刷新控制view
 * 
 * @author Nono
 * 
 */
public class RefreshableView extends LinearLayout {

    private static final String TAG = "LILITH";
    private Scroller scroller;
    private View refreshView;
    private ImageView refreshIndicatorView;
    //private int refreshTargetTop = -60;
    private int refreshTargetTop = -200;
    private ProgressBar bar;
    private TextView downTextView;
    private TextView timeTextView;
    private LinearLayout reFreshTimeLayout;//显示上次刷新时间的layout
    private RefreshListener refreshListener;

    private String downTextString;
    private String releaseTextString;

 //   private Long refreshTime = null;
    private int lastX;
    private int lastY;
    // 拉动标记
    private boolean isDragging = false;
    // 是否可刷新标记
    private boolean isRefreshEnabled = true;
    // 在刷新中标记
    private boolean isRefreshing = false;
    Calendar LastRefreshTime;
    
    private Context mContext;
    public RefreshableView(Context context) {
        super(context);
        mContext = context;
        
    }
    public RefreshableView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        init();
        
    }
    private void init() {
        // TODO Auto-generated method stub
        //滑动对象，
        LastRefreshTime=Calendar.getInstance();
        scroller = new Scroller(mContext);
        
        //刷新视图顶端的的view
         refreshView = LayoutInflater.from(mContext).inflate(R.layout.refresh_top_item, null);
        //指示器view
         refreshIndicatorView = (ImageView) refreshView.findViewById(R.id.indicator);
        //刷新bar
        bar = (ProgressBar) refreshView.findViewById(R.id.progress);
        //下拉显示text
         downTextView = (TextView) refreshView.findViewById(R.id.refresh_hint);
        //下来显示时间
         timeTextView = (TextView) refreshView.findViewById(R.id.refresh_time);
         reFreshTimeLayout=(LinearLayout)refreshView.findViewById(R.id.refresh_time_layout);
         
        LayoutParams lp = new LayoutParams(LayoutParams.FILL_PARENT, -refreshTargetTop);
        lp.topMargin = refreshTargetTop;
        lp.gravity = Gravity.CENTER;
        addView(refreshView, lp);
        downTextString = mContext.getResources().getString(R.string.refresh_down_text);
        releaseTextString = mContext.getResources().getString(R.string.refresh_release_text);
    }




    /**
     * 设置上次刷新时间
     * @param time
     */
    private void setLastRefreshTimeText() {
        // TODO Auto-generated method stub
        reFreshTimeLayout.setVisibility(View.VISIBLE);
        Calendar NowTime=Calendar.getInstance();
        long l=NowTime.getTimeInMillis()-LastRefreshTime.getTimeInMillis();
        int days=new Long(l/(1000*60*60*24)).intValue();
        int hour=new Long(l/(1000*60*60)).intValue();
        int min=new Long(l/(1000*60)).intValue();
        if(days!=0)
        {
            timeTextView.setText(days+"天"); 
        }
        else  if(hour!=0)
        {
            timeTextView.setText(hour+"小时"); 
        }
        else if(min!=0)
        {
            timeTextView.setText(min+"分钟"); 
        }
       
         
        //timeTextView.setText(time);
    }


    
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        
        int y= (int) event.getRawY();
        
        
        switch (event.getAction()) {
        case MotionEvent.ACTION_DOWN:
            //记录下y坐标
            lastY = y;
            break;

        case MotionEvent.ACTION_MOVE:
            Log.i(TAG, "ACTION_MOVE");
            //y移动坐标
            int m = y - lastY;
            if(((m < 6) && (m > -1)) || (!isDragging )){
                setLastRefreshTimeText();
                 doMovement(m);
            }
            //记录下此刻y坐标
            this.lastY = y;
            break;
            
        case MotionEvent.ACTION_UP:
            Log.i(TAG, "ACTION_UP");
            
            fling();
            
            break;
        }
        return true;
    }


    /**
     * up事件处理
     */
    private void fling() {
        // TODO Auto-generated method stub
        LayoutParams lp = (LayoutParams) refreshView.getLayoutParams();
        Log.i(TAG, "fling()" + lp.topMargin);
        if(lp.topMargin > 0){//拉到了触发可刷新事件
            refresh();  
        }else{
            returnInitState();
        }
    }
    

    
    private void returnInitState() {
        // TODO Auto-generated method stub
         LayoutParams lp = (LayoutParams)this.refreshView.getLayoutParams();
         int i = lp.topMargin;
         scroller.startScroll(0, i, 0, refreshTargetTop);
         invalidate();
    }
    private void refresh() {
        // TODO Auto-generated method stub
         LayoutParams lp = (LayoutParams)this.refreshView.getLayoutParams();
         int i = lp.topMargin;
         reFreshTimeLayout.setVisibility(View.GONE);
         refreshIndicatorView.setVisibility(View.GONE);
         bar.setVisibility(View.VISIBLE);
         timeTextView.setVisibility(View.GONE);
         downTextView.setVisibility(View.GONE);
         scroller.startScroll(0, i, 0, 0-i);
         invalidate();
         if(refreshListener !=null){
             refreshListener.onRefresh(this);
             isRefreshing = true;
         }
    }
    
    /**
     * 
     */
    @Override
    public void computeScroll() {
        // TODO Auto-generated method stub
        if(scroller.computeScrollOffset()){
            int i = this.scroller.getCurrY();
              LayoutParams lp = (LayoutParams)this.refreshView.getLayoutParams();
              int k = Math.max(i, refreshTargetTop);
              lp.topMargin = k;
              this.refreshView.setLayoutParams(lp);
              this.refreshView.invalidate();
              invalidate();
        }
    }
    /**
     * 下拉move事件处理
     * @param moveY
     */
    private void doMovement(int moveY) {
        // TODO Auto-generated method stub
        LayoutParams lp = (LayoutParams) refreshView.getLayoutParams();
        if(moveY>0){
            //获取view的上边距
            float f1 =lp.topMargin;
            float f2 = moveY * 0.3F;
            int i = (int)(f1+f2);
            //修改上边距
            lp.topMargin = i;
            //修改后刷新
            refreshView.setLayoutParams(lp);
            refreshView.invalidate();
            invalidate();
        }
        else 
        {
            float f1 =lp.topMargin;
            int i=(int)(f1+moveY*0.9F);
            Log.i("aa", String.valueOf(i));
            if(i>=refreshTargetTop)
            {
                lp.topMargin = i;
                //修改后刷新
                refreshView.setLayoutParams(lp);
                refreshView.invalidate();
                invalidate();
            }
            else 
            {
                
            }
        }
        
        timeTextView.setVisibility(View.VISIBLE);
//        if(refreshTime!= null){
//            setRefreshTime(refreshTime);
//        }
        downTextView.setVisibility(View.VISIBLE);
        
        refreshIndicatorView.setVisibility(View.VISIBLE);
        bar.setVisibility(View.GONE);
        if(lp.topMargin >  0){
            downTextView.setText(R.string.refresh_release_text);
            refreshIndicatorView.setImageResource(R.drawable.refresh_arrow_up);
        }else{
            downTextView.setText(R.string.refresh_down_text);
            refreshIndicatorView.setImageResource(R.drawable.refresh_arrow_down);
        }
            
    }

    public void setRefreshEnabled(boolean b) {
        this.isRefreshEnabled = b;
    }

    public void setRefreshListener(RefreshListener listener) {
        this.refreshListener = listener;
    }


    /**
     * 结束刷新事件
     */
    public void finishRefresh(){
        Log.i(TAG, "执行了=====finishRefresh");
         LayoutParams lp= (LayoutParams)this.refreshView.getLayoutParams();
            int i = lp.topMargin;
            refreshIndicatorView.setVisibility(View.VISIBLE);
            timeTextView.setVisibility(View.VISIBLE);
            scroller.startScroll(0, i, 0, refreshTargetTop);
            invalidate();
            isRefreshing = false;  
            LastRefreshTime=Calendar.getInstance();
    }

    
    /*该方法一般和ontouchEvent 一起用
     * (non-Javadoc)
     * @see android.view.ViewGroup#onInterceptTouchEvent(android.view.MotionEvent)
     */
    @Override
    public boolean onInterceptTouchEvent(MotionEvent e) {
        // TODO Auto-generated method stub
        int action = e.getAction();
        int y= (int) e.getRawY();
        switch (action) {
        case MotionEvent.ACTION_DOWN:
            lastY = y;
            break;

        case MotionEvent.ACTION_MOVE:
            //y移动坐标
            int m = y - lastY;

            //记录下此刻y坐标
            this.lastY = y;
             if(m > 6 &&  canScroll()){
                 return true;
             }
            break;
        case MotionEvent.ACTION_UP:
            
            break;
            
    case MotionEvent.ACTION_CANCEL:
            
            break;
        }
        return false;
    }
    private boolean canScroll() {
        // TODO Auto-generated method stub
        View childView;
        if(getChildCount()>1){
            childView = this.getChildAt(1);
            if(childView instanceof ListView){
                int top =((ListView)childView).getChildAt(0).getTop(); 
                int pad =((ListView)childView).getListPaddingTop(); 
                if((Math.abs(top-pad)) < 3&&
                        ((ListView) childView).getFirstVisiblePosition() == 0){
                    return true;
                }else{
                    return false;
                }
            }else if(childView instanceof ScrollView){
                if(((ScrollView)childView).getScrollY() == 0){
                    return true;
                }else{
                    return false;
                }
            }
            
        }
        return false;
    }
    /**
     * 刷新监听接口
     * @author Nono
     *
     */
    public interface RefreshListener{
        public void onRefresh(RefreshableView view);
    }


}
