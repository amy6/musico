package example.com.musico.data;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.support.annotation.ColorInt;
import android.support.v7.widget.AppCompatSeekBar;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;

import example.com.musico.R;

public class ArcSeekBar extends AppCompatSeekBar {

    Paint paint;
    RectF oval;


    public ArcSeekBar(Context context) {
        super(context);
        init();
    }

    public ArcSeekBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public ArcSeekBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init(){
        paint = new Paint();
        paint.setColor(Color.BLUE);
        paint.setStrokeWidth(1);
        paint.setStyle(Paint.Style.STROKE);

        oval = new RectF();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        // TODO Auto-generated method stub
        super.onDraw(canvas);

        paint.setStyle(Paint.Style.STROKE);
        /*
         * drawArc(RectF oval, float startAngle, float sweepAngle, boolean useCenter, Paint paint)
         *
         * oval - The bounds of oval used to define the shape and size of the arc
         * startAngle - Starting angle (in degrees) where the arc begins
         * sweepAngle - Sweep angle (in degrees) measured clockwise
         * useCenter - If true, include the center of the oval in the arc, and close it if it is being stroked. This will draw a wedge
         * paint - The paint used to draw the arc
         */
        /*oval.set(50, 50, 150, 150);
        canvas.drawArc(oval, 0, 45, true, paint);

        oval.set(200, 150, 450, 350);
        canvas.drawArc(oval, 0, 270, true, paint);*/

        oval.set(200, 400, 450, 600);
        canvas.drawArc(oval, 0, 270, false, paint);


    }

}

