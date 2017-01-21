package com.timer.app.base;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.timer.app.base.service.AppService;

import static android.content.res.Configuration.ORIENTATION_LANDSCAPE;

public class TimerCanvas extends View {

    private static String tag=TimerCanvas.class.getSimpleName();

    private final int DEGREES = 6;  // degree transition for easch line when creating watch face
    private final int COLOR_WATCH_FACE = Color.RED;
    private final int COLOR_WATCH_TICK = Color.BLUE;
    private final int COLOR_WATCH_TEXT = Color.BLUE;
    private final int TEXT_SIZE_PX = 40;
    private final int SHADOW_RADIUS = 20;

    private Context context;
    private int seconds = 0;
    private String timeText = "00:00:00";
    private int canvasHeight;
    private int canvasWidth;
    private float middle_x;
    private float middle_y;
    private int biggerSide;
    private int length;  //how the line is long (watch face)
    private int space;   //when the line starts (watch face)
    private Paint watchPaint = new Paint(); //info about text properties
    private Bitmap bitmap = null; //Contains background image
    private AppService app;

    /**
     * Draws a watch face with time for timer actitivy
     *
     * @param c
     * @param attrs
     */
    public TimerCanvas(Context c, AttributeSet attrs) {
        super(c, attrs);
        app = AppService.getInstance();
        context = c;
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvasHeight = canvas.getHeight();
        canvasWidth = canvas.getWidth();
        middle_x = canvasWidth / 2;
        middle_y = canvasHeight / 2;
        watchPaint.setColor(COLOR_WATCH_FACE);
        watchPaint.setShadowLayer(SHADOW_RADIUS / 2, 0, 0, COLOR_WATCH_FACE);
        watchPaint.setStyle(Paint.Style.STROKE);
        watchPaint.setStrokeWidth(4f);
        biggerSide = canvasHeight > canvasWidth ? canvasWidth : canvasHeight;
        length = biggerSide / 3;
        space = (int) (length * 0.9);
        if (bitmap == null) {  //not background has been created yet, create it now
            if (canvasHeight == 0 || canvasWidth == 0) {
                canvasHeight = 500;
                canvasWidth = 500;
            }
            bitmap = Bitmap.createBitmap(canvasWidth, canvasHeight, Bitmap.Config.RGB_565);
            Canvas bitMapCanvas = new Canvas(bitmap);
            drawBackGround(bitMapCanvas);
            drawWatchFace(bitMapCanvas);
        }
        //bitmap has been created, draw bitmap as background
        canvas.drawBitmap(bitmap, 0, 0, new Paint());
        watchPaint.setColor(COLOR_WATCH_TICK);
        watchPaint.setShadowLayer(SHADOW_RADIUS, 0, 0, COLOR_WATCH_TICK);
        drawLine((seconds * DEGREES + 270) % 360, canvas, watchPaint);
        Paint textPaint = new Paint();
        if (!app.getConfig().isTimeWhiteColor()) {
            textPaint.setColor(COLOR_WATCH_TEXT);
        } else {
            textPaint.setColor(Color.WHITE);
        }
        final float scale = context.getResources().getDisplayMetrics().density;
        int textSize = (int) (TEXT_SIZE_PX * scale + 0.5f);
        if (context.getResources().getConfiguration().orientation == ORIENTATION_LANDSCAPE) {
            textPaint.setTextSize(textSize * 1.5f);
        } else {
            textPaint.setTextSize(textSize);
        }
        textPaint.setTextAlign(Paint.Align.CENTER);
        if (!app.getConfig().isTimeWhiteColor()) {
            textPaint.setShadowLayer(SHADOW_RADIUS, 0, 0, COLOR_WATCH_TEXT);
        } else{
            textPaint.setShadowLayer(10, 0, 0, Color.WHITE);
        }

        int textYpos = (int) ((canvas.getHeight() / 2) - ((textPaint.descent() + textPaint.ascent()) / 2));
        canvas.drawText(timeText, middle_x, textYpos, textPaint);
    }


    public void setTimeText(String timeText) {
        this.timeText = timeText;
    }

    private void drawWatchFace(Canvas canvas) {

        int currDegree = 0;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            setLayerType(LAYER_TYPE_SOFTWARE, watchPaint);
        }
        while (currDegree < 360) {
            drawLine(currDegree, canvas, watchPaint);
            currDegree += DEGREES;
        }
    }

    private void drawLine(int currDegree, Canvas canvas, Paint p) {
        double angle = currDegree * Math.PI / 180;
        double xStart;
        double yStart;
        double xEnd;
        double yEnd;
        if (currDegree % 30 == 0) {
            xStart = middle_x + Math.cos(angle) * (space - 20);
            yStart = middle_y + Math.sin(angle) * (space - 20);
            xEnd = middle_x + Math.cos(angle) * length;
            yEnd = middle_y + Math.sin(angle) * length;
        } else {
            xStart = middle_x + Math.cos(angle) * space;
            yStart = middle_y + Math.sin(angle) * space;
            xEnd = middle_x + Math.cos(angle) * length;
            yEnd = middle_y + Math.sin(angle) * length;
        }
        canvas.drawLine((float) xStart, (float) yStart, (float) xEnd, (float) yEnd, p);
        ;
    }

    public void setSeconds(int seconds) {
        if (seconds >= 60) {
            Log.e(tag, "Invalid number of seconds", new Exception("Invalid number of seconds. Max is 59 but was " + seconds));
        }
        this.seconds = seconds;
    }

    private void drawBackGround(Canvas canvas) {
        int circleSize = 500;
        int[] colors = {0xff001f51, 0xff000000, 0xff000000, 0xff001f51};
        float[] positions = {0f, 0.4f, 0.6f, 1f};
        LinearGradient gradient = new LinearGradient(0, 0, canvasWidth, canvasHeight, colors, positions, android.graphics.Shader.TileMode.CLAMP);
        //Bitmap bitmap = Bitmap.createBitmap(400, 400, Bitmap.Config.ARGB_8888);
        // canvas.drawColor(Color.BLACK);
        Paint pa = new Paint();
        pa.setDither(true);
        pa.setShader(gradient);
        canvas.drawRect(new RectF(0, 0, canvasWidth, canvasHeight), pa);
    }
}
