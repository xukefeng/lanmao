package cash.app.com.mymvp;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

public class KeyBordView extends View {
    private String[] numbers = new String[]{"1", "2", "3", "4", "5", "6", "7", "8", "9", "0"};
    private int columnCount = 3;//多少列

    private int rowCount = 4;//多少行

    private int width;//View 高度

    private int height;//View 宽度

    private int childWidth;//每一格的宽度

    private int childHeight;//每一格的高度

    private Paint textPaint;//绘制文字的画笔

    private Paint backPaint;//绘制返回按钮的画笔

    private Point[] points;

    public KeyBordView(Context context) {
        this(context, null);
    }

    public KeyBordView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public KeyBordView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }


    private void init() {
        textPaint = new Paint();
        textPaint.setStyle(Paint.Style.FILL);
        textPaint.setAntiAlias(true);
        textPaint.setTextSize(72);
        textPaint.setStrokeWidth(5);
        textPaint.setColor(Color.parseColor("#333333"));
        backPaint = new Paint();
        backPaint.setColor(Color.WHITE);
        backPaint.setStyle(Paint.Style.FILL);
        //加1是因为后面还有一个删除按钮的坐标
        points = new Point[numbers.length + 1];
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        this.width = w - getPaddingLeft() - getPaddingRight();
        this.height = h - getPaddingBottom() - getPaddingTop();
        this.childWidth = this.width / columnCount;
        this.childHeight = this.height / rowCount;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawNumber(canvas);

    }

    private void drawNumber(Canvas canvas) {
        int xWidth = 0;
        int yHeight = 0;
        for (int i = 0; i < numbers.length; i++) {
            canvas.drawRect(xWidth, yHeight, xWidth + childWidth, yHeight + childHeight, backPaint);
            canvas.drawText(numbers[i], childWidth / 2 + xWidth, childHeight / 2 + yHeight, textPaint);
            //这里只存储x,y轴的开始坐标
            points[i] = new Point(xWidth, yHeight);
            if (i == numbers.length - 1) {
                points[i + 1] = new Point(xWidth + childWidth, yHeight);
                canvas.drawRect(0, yHeight, childWidth, yHeight + childHeight, backPaint);
                canvas.drawRect(xWidth + childWidth, yHeight, xWidth + childWidth * 2, yHeight + childHeight, backPaint);
                Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.icon_del);
                canvas.drawBitmap(bitmap, xWidth + childWidth + childWidth / 2, childHeight / 2 + yHeight, backPaint);
                bitmap.recycle();
            }
            xWidth += childWidth;
            if ((i + 1) % columnCount == 0) {
                xWidth = 0;
                yHeight += childHeight;
            }
            if (i == numbers.length - 2) {
                xWidth += childWidth;
            }
        }
    }

    float x = 0;
    float y = 0;

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                x = event.getX();
                y = event.getY();
                break;
            case MotionEvent.ACTION_UP:
                for (int i = 0; i < points.length; i++) {
                    if (i != points.length - 1) {
                        if (points[i].x < x && x <= points[i].x + childWidth && points[i].y < y && y <= points[i].y + childHeight) {
                            Toast.makeText(getContext(), numbers[i], Toast.LENGTH_LONG).show();
                        }
                    } else {
                        if (points[i].x < x && x <= points[i].x + childWidth && points[i].y < y && y <= points[i].y + childHeight) {
                            Toast.makeText(getContext(), "Delete", Toast.LENGTH_LONG).show();
                        }
                    }
                }
                break;

            case MotionEvent.ACTION_CANCEL:
                //在执行ACTION_UP前被中断执行此地方
                for (int i = 0; i < points.length; i++) {
                    if (i != points.length - 1) {
                        if (points[i].x < x && x <= points[i].x + childWidth && points[i].y < y && y <= points[i].y + childHeight) {
                            Toast.makeText(getContext(), numbers[i], Toast.LENGTH_LONG).show();
                        }
                    } else {
                        if (points[i].x < x && x <= points[i].x + childWidth && points[i].y < y && y <= points[i].y + childHeight) {
                            Toast.makeText(getContext(), "Delete", Toast.LENGTH_LONG).show();
                        }
                    }
                }
                break;
        }
        return true;
    }
}
