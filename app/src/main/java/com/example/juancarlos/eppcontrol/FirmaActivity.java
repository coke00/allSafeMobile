package com.example.juancarlos.eppcontrol;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.os.Bundle;
import android.util.Base64;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;

public class FirmaActivity extends Activity {
    DrawingView dv ;
    private Paint mPaint;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        LinearLayout ll = new LinearLayout(this);
        ll.setOrientation(LinearLayout.VERTICAL);

        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 300);
        layoutParams.weight = 1.4f;
        //layoutParams.setMargins(0, 30, 0, 30);

        dv = new DrawingView(this);
        dv.setLayoutParams(layoutParams);
        ll.addView(dv);

        LinearLayout.LayoutParams layoutParamsbt = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 20);
        layoutParamsbt.weight = 0.1f;
        //layoutParamsbt.setMargins(0, 30, 0, 30);

        Button bt = new Button(this);
        bt.setText("GUARDAR FIRMA");
        bt.setLayoutParams(layoutParamsbt);
        bt.setTextColor(Color.WHITE);
        bt.setBackgroundColor(Color.BLACK);
        bt.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                dv.setDrawingCacheEnabled(true);
                dv.buildDrawingCache();
                Bitmap bitmap = Bitmap.createScaledBitmap(dv.getDrawingCache(), 320, 240, false);
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
                byte[] b = baos.toByteArray();
                ((Global)getApplicationContext()).firma =  Base64.encodeToString(b,Base64.NO_WRAP);
                Toast.makeText(getBaseContext(), "Firma almacenada con exito", Toast.LENGTH_LONG).show();
                finish();
            }
        });
        ll.addView(bt);

        setContentView(ll);
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setDither(true);
        mPaint.setColor(Color.BLACK);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeJoin(Paint.Join.ROUND);
        mPaint.setStrokeCap(Paint.Cap.ROUND);
        mPaint.setStrokeWidth(12);
    }

    public class DrawingView extends View {

        public int width;
        public  int height;
        private Bitmap  mBitmap;
        private Canvas mCanvas;
        private Path mPath;
        private Paint   mBitmapPaint;
        Context context;
        private Paint circlePaint;
        private Path circlePath;

        public DrawingView(Context c) {
            super(c);
            context=c;
            mPath = new Path();
            mBitmapPaint = new Paint(Paint.DITHER_FLAG);
            circlePaint = new Paint();
            circlePath = new Path();
            circlePaint.setAntiAlias(true);
            circlePaint.setColor(Color.BLUE);
            circlePaint.setStyle(Paint.Style.STROKE);
            circlePaint.setStrokeJoin(Paint.Join.MITER);
            circlePaint.setStrokeWidth(4f);
        }

        @Override
        protected void onSizeChanged(int w, int h, int oldw, int oldh) {
            super.onSizeChanged(w, h, oldw, oldh);

            mBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
            mCanvas = new Canvas(mBitmap);
        }

        @Override
        protected void onDraw(Canvas canvas) {
            super.onDraw(canvas);

            canvas.drawBitmap( mBitmap, 0, 0, mBitmapPaint);
            canvas.drawPath( mPath,  mPaint);
            canvas.drawPath( circlePath,  circlePaint);
        }

        private float mX, mY;
        private static final float TOUCH_TOLERANCE = 4;

        private void touch_start(float x, float y) {
            mPath.reset();
            mPath.moveTo(x, y);
            mX = x;
            mY = y;
        }

        private void touch_move(float x, float y) {
            float dx = Math.abs(x - mX);
            float dy = Math.abs(y - mY);
            if (dx >= TOUCH_TOLERANCE || dy >= TOUCH_TOLERANCE) {
                mPath.quadTo(mX, mY, (x + mX)/2, (y + mY)/2);
                mX = x;
                mY = y;

                circlePath.reset();
                circlePath.addCircle(mX, mY, 30, Path.Direction.CW);
            }
        }

        private void touch_up() {
            mPath.lineTo(mX, mY);
            circlePath.reset();
            // commit the path to our offscreen
            mCanvas.drawPath(mPath,  mPaint);
            // kill this so we don't double draw
            mPath.reset();
        }

        @Override
        public boolean onTouchEvent(MotionEvent event) {
            float x = event.getX();
            float y = event.getY();

            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    touch_start(x, y);
                    invalidate();
                    break;
                case MotionEvent.ACTION_MOVE:
                    touch_move(x, y);
                    invalidate();
                    break;
                case MotionEvent.ACTION_UP:
                    touch_up();
                    invalidate();
                    break;
            }
            return true;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add("GUARDAR");
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getTitle() == "GUARDAR") {
            dv.setDrawingCacheEnabled(true);
            dv.buildDrawingCache();
            Bitmap bitmap = Bitmap.createBitmap(dv.getDrawingCache());
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 20, baos);
            byte[] b = baos.toByteArray();
            ((Global)getApplicationContext()).firma =  Base64.encodeToString(b,Base64.NO_WRAP);
            Toast.makeText(getBaseContext(), "Firma almacenada con éxito", Toast.LENGTH_LONG).show();
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("¿Desea conservar la firma?")
                .setTitle("Confirmación")
                .setPositiveButton("Si", new DialogInterface.OnClickListener(){
                    public void onClick(DialogInterface dialog, int id) {
                        dv.setDrawingCacheEnabled(true);
                        dv.buildDrawingCache();
                        Bitmap bitmap = Bitmap.createBitmap(dv.getDrawingCache());
                        ByteArrayOutputStream baos = new ByteArrayOutputStream();
                        bitmap.compress(Bitmap.CompressFormat.PNG, 20, baos);
                        byte[] b = baos.toByteArray();
                        ((Global)getApplicationContext()).firma =  Base64.encodeToString(b,Base64.NO_WRAP);
                        Toast.makeText(getBaseContext(), "Firma almacenada con éxito", Toast.LENGTH_LONG).show();
                        finish();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        finish();
                    }
                });
        builder.create();
        builder.show();
    }
}
