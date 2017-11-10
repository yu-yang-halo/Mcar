package view.extend;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.widget.Button;

import com.carbeauty.DensityUtil;

/**
 * Created by Administrator on 2017/10/28 0028.
 */

public class ColorButton extends android.support.v7.widget.AppCompatButton {
    private int mColor= Color.parseColor("#ffffff");
    private Paint mPaint=new Paint();
    private int padding= DensityUtil.dip2px(getContext(),3);
    private boolean colorEnable=false;

    public void setColorEnable(boolean colorEnable) {
        this.colorEnable = colorEnable;
        invalidate();
    }

    public ColorButton(Context context) {
        super(context);
    }

    public ColorButton(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ColorButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }
    public void cbColor(int color){
        this.mColor=color;
        invalidate();
    }
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if(colorEnable){
            int width=getWidth();
            int height=getHeight();
            mPaint.setColor(mColor);
            canvas.drawRect(padding,padding,width-padding,height-padding,mPaint);
        }

    }
}
