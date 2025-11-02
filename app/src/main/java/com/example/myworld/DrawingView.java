package com.example.myworld;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.*;
import android.view.View;
import android.view.animation.LinearInterpolator;

public class DrawingView extends View {
    private Paint paint;
    private float waveOffset = 0f;

    public DrawingView(Context context) {
        super(context);
        paint = new Paint();
        paint.setAntiAlias(true);
        startWaveAnimation();
    }

    private void startWaveAnimation() {
        ValueAnimator animator = ValueAnimator.ofFloat(0f, 80f);
        animator.setDuration(2500);
        animator.setRepeatCount(ValueAnimator.INFINITE);
        animator.setInterpolator(new LinearInterpolator());
        animator.addUpdateListener(a -> {
            waveOffset = (float) a.getAnimatedValue();
            invalidate();
        });
        animator.start();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        int w = getWidth();
        int h = getHeight();

        // ===== ТЁМНОЕ НЕБО =====
        Paint sky = new Paint();
        sky.setShader(new LinearGradient(0, 0, 0, h / 2f,
                Color.rgb(30, 50, 80), Color.rgb(10, 20, 40),
                Shader.TileMode.CLAMP));
        canvas.drawRect(0, 0, w, h / 2f, sky);

        // ===== СОЛНЦЕ (едва видно сквозь тучи) =====
        paint.setShader(null);
        paint.setColor(Color.argb(120, 255, 230, 150));
        paint.setStyle(Paint.Style.FILL);
        canvas.drawCircle(w * 0.5f, h * 0.25f, 70, paint);

        // ===== МНОГОСЛОЙНЫЙ ОКЕАН =====
        Paint oceanDeep = new Paint();
        oceanDeep.setShader(new LinearGradient(0, h / 2f, 0, h,
                Color.rgb(0, 30, 60), Color.rgb(0, 15, 30),
                Shader.TileMode.CLAMP));
        canvas.drawRect(0, h / 2f, w, h * 0.85f, oceanDeep);

        Paint oceanSurface = new Paint();
        oceanSurface.setShader(new LinearGradient(0, h / 2f, 0, h * 0.8f,
                Color.rgb(0, 90, 130), Color.rgb(0, 40, 80),
                Shader.TileMode.CLAMP));
        canvas.drawRect(0, h / 2f, w, h * 0.8f, oceanSurface);

        // ===== ВОЛНЫ (слоистые, полупрозрачные) =====
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(3);

        for (int i = 0; i < 8; i++) {
            float y = h / 2f + i * 35;
            Path wave = new Path();
            wave.moveTo(0, y);
            for (int x = 0; x <= w; x += 40) {
                float amp = 8 + (float) Math.sin(i + x * 0.05) * 4; // разная высота волн
                wave.quadTo(x + 20, y - amp + (float) Math.sin((x + waveOffset) * 0.08) * 10, x + 40, y);
            }
            paint.setColor(Color.argb(100, 255, 255, 255));
            canvas.drawPath(wave, paint);
        }

        // ===== БЕЛАЯ ПЕНА НА ВОЛНАХ =====
        for (int i = 0; i < 4; i++) {
            float y = h / 2f + i * 60 + 20;
            Path foam = new Path();
            foam.moveTo(0, y);
            for (int x = 0; x <= w; x += 60) {
                foam.quadTo(x + 30, y - 5 + (float) Math.sin((x + waveOffset * 1.5) * 0.1) * 5, x + 60, y);
            }
            paint.setColor(Color.argb(180, 255, 255, 255));
            paint.setStrokeWidth(2);
            canvas.drawPath(foam, paint);
        }

        // ===== ПЕСОК =====
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(Color.rgb(194, 178, 128));
        canvas.drawRect(0, h * 0.85f, w, h, paint);

        // ===== ЧЕЛОВЕК (вид сзади) =====
        float cx = w * 0.5f;
        float baseY = h * 0.85f;

        // Голова
        paint.setColor(Color.rgb(255, 220, 177));
        canvas.drawCircle(cx, baseY - 180, 25, paint);

        // Тело
        paint.setColor(Color.rgb(30, 60, 120));
        canvas.drawRect(cx - 20, baseY - 155, cx + 20, baseY - 85, paint);

        // Руки
        paint.setStrokeWidth(8);
        paint.setColor(Color.rgb(255, 220, 177));
        canvas.drawLine(cx - 20, baseY - 145, cx - 55, baseY - 110, paint);
        canvas.drawLine(cx + 20, baseY - 145, cx + 55, baseY - 110, paint);

        // Ноги
        paint.setStrokeWidth(10);
        paint.setColor(Color.rgb(10, 20, 60));
        canvas.drawLine(cx - 10, baseY - 85, cx - 10, baseY - 30, paint);
        canvas.drawLine(cx + 10, baseY - 85, cx + 10, baseY - 30, paint);
    }
}
