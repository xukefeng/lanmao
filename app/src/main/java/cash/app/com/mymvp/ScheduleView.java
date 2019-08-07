package cash.app.com.mymvp;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

public class ScheduleView extends View {
    private String[] numbers = new String[]{"0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "10"};
    private Paint mPaint_01;//最外层圆弧
    private Paint mPaint_02;//里面圆弧
    private Paint mPaint_03;//绘制一个小圆点
    private Paint mPaint_04;
    private Paint mPaint_05;
    private Paint mPaintText;
    private Paint mPaintScale;
    private int width;//View 的宽
    private int height;//View 的高
    private String text = "5000";

    int radius;
    int radius01;
    int radius02;

    public ScheduleView(Context context) {
        this(context, null);
    }

    public ScheduleView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ScheduleView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        //#FE5C23
        mPaint_01 = new Paint();
        mPaint_01.setAntiAlias(true);
        mPaint_01.setStyle(Paint.Style.STROKE);
        mPaint_01.setStrokeWidth(4);
        mPaint_01.setColor(Color.parseColor("#FEB7AA"));

        mPaint_02 = new Paint();
        mPaint_02.setAntiAlias(true);
        mPaint_02.setColor(Color.parseColor("#FFE5E2"));
        mPaint_02.setStyle(Paint.Style.STROKE);
        mPaint_02.setStrokeWidth(1);
        mPaint_03 = new Paint();
        mPaint_03.setAntiAlias(true);
        mPaint_03.setStyle(Paint.Style.FILL);
        mPaint_03.setColor(Color.parseColor("#FEB7AA"));
        mPaint_03.setStrokeWidth(12);

        mPaint_04 = new Paint();
        mPaint_04.setAntiAlias(true);
        mPaint_04.setStyle(Paint.Style.STROKE);
        mPaint_04.setColor(Color.parseColor("#FF7200"));
        mPaint_04.setStrokeWidth(30);

        mPaint_05 = new Paint();
        mPaint_05.setAntiAlias(true);
        mPaint_05.setStyle(Paint.Style.FILL);
        mPaint_05.setStrokeWidth(6);
        mPaint_05.setColor(Color.parseColor("#F24226"));

        mPaintText = new Paint();
        mPaintText.setAntiAlias(true);
        mPaintText.setTextSize(46);
        mPaintText.setStrokeWidth(20);
        mPaintText.setColor(Color.parseColor("#F24226"));

        mPaintScale = new Paint();
        mPaintScale.setAntiAlias(true);
        mPaintScale.setTextSize(24);
        mPaintScale.setStrokeWidth(20);
        mPaintScale.setColor(Color.parseColor("#109B3B"));
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        this.width = w;
        this.height = h;
        //绘制x，y左边
        radius = Math.min(width, height) / 2 - 50;
        radius01 = radius - 30;
        radius02 = radius01 - 30;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawArc01(canvas);
        drawScaleText(canvas);
        drawText(canvas);
    }

    /**
     * 绘制外层的圆弧 api19出来的
     */
    @SuppressLint("NewApi")
    private void drawArc01(Canvas canvas) {
        canvas.translate(width / 2, height / 2);
        RectF rectF = new RectF(-radius, -radius, radius, radius);
        //从-225度开始，画270度，正常情况下一x轴从何，顺时针画弧
        Path path = new Path();
        path.addArc(rectF, -225, 270);
        PathMeasure measure = new PathMeasure(path, false);
        canvas.drawPath(path, mPaint_01);

        RectF rectF01 = new RectF(-radius01, -radius01, radius01, radius01);
        Path path1 = new Path();
        path1.addArc(rectF01, -225, 270);
        canvas.drawPath(path1, mPaint_02);

        RectF rectF02 = new RectF(-radius02, -radius02, radius02, radius02);
        Path path2 = new Path();
        path2.addArc(rectF02, -225, 270);
        canvas.drawPath(path2, mPaint_02);

        RectF rectF03 = new RectF(-radius02 - 15, -radius02 - 15, radius02 + 15, radius02 + 15);
        Path path3 = new Path();
        path3.addArc(rectF03, -225, 270);
        canvas.drawPath(path3, mPaint_04);

        //得到最外层path的长度
        int length = (int) measure.getLength();
        //在最外层圆弧上绘制一个点
        float[] pos = new float[2];                // 当前点的实际位置
        float[] tan = new float[2];// 当前点的tangent值,用于计算图片所需旋转的角度
        measure.getPosTan(length / 10 * 5, pos, tan);//distance离七点的长度
        canvas.drawCircle(pos[0], pos[1], 8, mPaint_03);
        canvas.rotate(-225);
        for (int i = 0; i <= 30; i++) {
            if (i % 3 == 0) {
                mPaint_05.setColor(Color.parseColor("#FEB7AA"));
                canvas.drawLine(radius01, 0, radius02 - 20, 0, mPaint_05);
            } else {
                mPaint_05.setColor(Color.parseColor("#F24226"));
                canvas.drawLine(radius01, 0, radius02, 0, mPaint_05);
            }
            canvas.rotate(9.0f);
        }
    }

    private void drawScaleText(Canvas canvas) {
        canvas.rotate(-9.0f);//还原绘制刻度线的最后一次旋转
        for (int i = 0; i <= 30; i++) {
            if (i % 3 == 0) {
                float lengthText = mPaintScale.measureText(numbers[i / 3]);
                canvas.drawText(numbers[i / 3], -lengthText / 2, radius02 - 30, mPaintScale);
            }
            canvas.rotate(9.0f);
        }
    }

    /**
     * 绘制文本
     *
     * @param canvas
     */
    private void drawText(Canvas canvas) {
        canvas.rotate(36);//恢复坐标位置
        float lengthText = mPaintText.measureText(text);//获取字符串占据的屏幕像素
        canvas.drawText(text, -lengthText / 2, 0, mPaintText);

        mPaintText.setTextSize(36);//设置字体大小一定要在测量之前
        float lengthText01 = mPaintText.measureText("码表");
        canvas.drawText("码表", -lengthText01 / 2, Math.min(width, height) / 2 - 50, mPaintText);

        float lengthText02 = mPaintScale.measureText("单位(km/h)");
        canvas.drawText("单位(km/h)", -lengthText02 / 2, -(radius02 - 100), mPaintScale);
    }
}
