package com.example.root.myapplication;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RadialGradient;
import android.graphics.RectF;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;

public class TimerCanvas extends View {

    public int width;
    public int height;
    private Bitmap mBitmap;
    private Canvas mCanvas;
    private Path mPath;
    Context context;
    private float mX, mY;
    private static final float TOLERANCE = 5;
    private int seconds = 0;
    private String timeText="00:00:00";
    private boolean isFirstDraw = true;
    int canvasHeight;
    int canvasWidth;
    float x = canvasWidth;
    float y = canvasHeight;
    int biggerSide;
    int length;
    int space;
    int DEGREES = 6;
    private Paint p = new Paint();
    Bitmap bitmap=null;

    public TimerCanvas(Context c, AttributeSet attrs) {
        super(c, attrs);
        context = c;
    }

    // override onSizeChanged
    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
    }

    // override onDraw
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvasHeight = canvas.getHeight();
        canvasWidth = canvas.getWidth();
        x = canvasWidth / 2;
        y = canvasHeight / 2;
        p.setColor(Color.RED);
        p.setShadowLayer(10, 0, 0, Color.RED);
        p.setStyle(Paint.Style.STROKE);
        p.setStrokeWidth(4f);
        biggerSide = canvasHeight > canvasWidth ? canvasWidth : canvasHeight;
        length = biggerSide / 3;
        space = (int) (length * 0.9);
        if (bitmap ==null) {
            if(canvasHeight == 0 || canvasWidth == 0){
                canvasHeight=500;
                canvasWidth=500;
            }
            bitmap = Bitmap.createBitmap(canvasWidth, canvasHeight, Bitmap.Config.RGB_565);
            Canvas bitMapCanvas = new Canvas(bitmap);
            drawBackGround(bitMapCanvas);
            drawWatchFace(bitMapCanvas);
        }
            canvas.drawBitmap(bitmap,0,0,new Paint());
            p.setColor(Color.BLUE);
            p.setShadowLayer(20, 0, 0, Color.BLUE);
            drawLine((seconds * DEGREES + 270) % 360,canvas,p);
        Paint textPaint = new Paint();
        textPaint.setColor(Color.BLUE);
        final float scale = context.getResources().getDisplayMetrics().density;
        int textsize = (int) (40 * scale + 0.5f);
        textPaint.setTextSize(textsize);
        textPaint.setTextAlign(Paint.Align.CENTER);
        textPaint.setShadowLayer(20, 0, 0, Color.BLUE);
        int textYpos= (int) ((canvas.getHeight() / 2) - ((textPaint.descent() + textPaint.ascent()) / 2)) ;
        System.out.println(timeText + "x"+x+"x"+textYpos);
        canvas.drawText(timeText,x,textYpos,textPaint);

    }


    public void setTimeText(String timeText){
        this.timeText=timeText;
    }
    private void drawWatchFace(Canvas canvas) {

        int currDegree = 0;
        int counter = 0;

        Paint _paintBlur = new Paint();

       // canvas.drawColor(Color.BLACK);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            setLayerType(LAYER_TYPE_SOFTWARE, p);
        }
        while (currDegree < 360) {
            drawLine(currDegree,canvas,p);
            currDegree += DEGREES;
        }
    }

    private void drawLine(int currDegree, Canvas canvas,Paint p) {
        double angle = currDegree * Math.PI / 180;
        double xStart;
        double yStart;
        double xEnd;
        double yEnd;
        if (currDegree % 30 == 0) {
            xStart = x + Math.cos(angle) * (space - 20);
            yStart = y + Math.sin(angle) * (space - 20);
            xEnd = x + Math.cos(angle) * length;
            yEnd = y + Math.sin(angle) * length;
        } else {
            xStart = x + Math.cos(angle) * space;
            yStart = y + Math.sin(angle) * space;
            xEnd = x + Math.cos(angle) * length;
            yEnd = y + Math.sin(angle) * length;
        }
        canvas.drawLine((float) xStart, (float) yStart, (float) xEnd, (float) yEnd, p);;
    }

    public void setSeconds(int seconds) {
        if (seconds >= 60) {
            throw new RuntimeException("Invalid number of seconds. Max is 59 but was " + seconds);
        }
        this.seconds = seconds;
    }

    private void drawBackGround(Canvas canvas) {
        int circleSize=500;
        int[] colors = {0xff001f51,0xff000000,0xff000000,0xff001f51};
        float[] positions={0f,0.4f,0.6f,1f};
        LinearGradient gradient = new LinearGradient(0, 0, canvasWidth,canvasHeight, colors,positions, android.graphics.Shader.TileMode.CLAMP);
        //Bitmap bitmap = Bitmap.createBitmap(400, 400, Bitmap.Config.ARGB_8888);
        // canvas.drawColor(Color.BLACK);
       Paint pa = new Paint();
        pa.setDither(true);
        pa.setShader(gradient);
        canvas.drawRect(new RectF(0, 0, canvasWidth,canvasHeight),  pa);
    }


    private Bitmap makeRadGrad() {
        // canvas.drawColor(Color.BLACK);
        RadialGradient gradient = new RadialGradient(200, 200, 200, 0xff002156,
                0xff002156, android.graphics.Shader.TileMode.CLAMP);
        Paint p = new Paint();
        p.setDither(true);
        p.setShader(gradient);

        Bitmap bitmap = Bitmap.createBitmap(400, 400, Bitmap.Config.ARGB_8888);
        Canvas c = new Canvas(bitmap);
        c.drawCircle(200, 200, 200, p);

        return bitmap;
    }
}
