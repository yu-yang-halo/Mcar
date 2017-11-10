package view.extend;

import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.AttrRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.annotation.StyleRes;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;

import com.carbeauty.DensityUtil;
import com.carbeauty.R;

import cn.qqtheme.framework.util.ConvertUtils;
import cn.service.Util;

/**
 * Created by Administrator on 2017/10/28 0028.
 * 布局选择器
 * 可选择 尺码
 *
 */

public class UILayoutSelector extends FrameLayout {
    public  final int CELL_HEIGHT=  DensityUtil.dip2px(getContext(),40);
    private String[] arrs;
    private ColorButton[] btns;
    private Handler mainHandler=new Handler(Looper.getMainLooper());

    public void setOnSelectedListerser(OnSelectedListerser onSelectedListerser) {
        this.onSelectedListerser = onSelectedListerser;
    }

    private OnSelectedListerser onSelectedListerser;
    public UILayoutSelector(@NonNull Context context) {
        super(context);
    }

    public UILayoutSelector(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public UILayoutSelector(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public UILayoutSelector(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr, @StyleRes int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    /**
     *    测量  ->  布局  -> 绘制
     *
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);

    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mainHandler.post(new Runnable() {
            @Override
            public void run() {
                updateUI();
            }
        });
    }
    private void updateUI(){
        int height=getHeight();
        int width=getWidth();

        if(arrs!=null&&arrs.length>0){

            int len=arrs.length;

            int cellWidth=width/2;
            int cellHeight=CELL_HEIGHT;

            btns=new ColorButton[len];

            for(int i=0;i<len;i++){
                ColorButton btn=new ColorButton(getContext());

                btn.setTextSize(10);
                btn.setTag(i);
                btn.setText(arrs[i]);
                btn.setTextColor(Color.BLACK);
                btn.setBackgroundResource(R.drawable.button_time_color);
                btn.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        initBtnUnSelector();
                        v.setSelected(true);

                        int pos=-1;
                        try {
                            pos= Integer.parseInt(v.getTag().toString());
                        }catch (Exception e){

                        }

                        onSelectedListerser.onSelected(pos);

                    }
                });

                btn.setGravity(Gravity.CENTER);
                btns[i]=btn;

                FrameLayout.LayoutParams params=new LayoutParams(cellWidth,cellHeight);
                params.leftMargin=(cellWidth)*(i%2);
                params.topMargin=cellHeight*(i/2);
                params.gravity= Gravity.TOP|Gravity.START;
                addView(btn,params);
            }

        }
    }

    private void initBtnUnSelector(){
        if(btns==null){
            return;
        }

        for(Button btn:btns){
            btn.setSelected(false);
        }
    }

    public void initSelector(String[] arrs){
        this.arrs=arrs;
    }



    public static interface  OnSelectedListerser{
        public void onSelected(int pos);
    }

}
