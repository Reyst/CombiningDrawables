package com.example.reyst.innerdrawable;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MERGE_DRAWABLE";
    private ImageView img1;
    private ImageView img2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        img1 = findViewById(R.id.img1);
        img2 = findViewById(R.id.img2);

        final Resources resources = getResources();
        final Drawable pin = resources.getDrawable(R.drawable.pin_vehicle_available);

        final Drawable vehicle1 = resources.getDrawable(R.drawable.ic_v_size_long);
        vehicle1.setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_IN);
        img1.setImageBitmap(createSingleImageFromMultipleImages(pin, vehicle1, 12, 12));

        final Drawable vehicle2 = resources.getDrawable(R.drawable.ic_v_size_bike);
        vehicle2.setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_IN);
        img2.setImageBitmap(createSingleImageFromMultipleImages(pin, vehicle2, 12, 12));

    }

    private Bitmap createSingleImageFromMultipleImages(Drawable outer, Drawable inner, int sidePadding, int topPadding) {

        Bitmap outerBitmap = drawableToBitmap(outer);

        final int width = outerBitmap.getWidth();
        Bitmap result = Bitmap.createBitmap(width, outerBitmap.getHeight(), outerBitmap.getConfig());
        Canvas canvas = new Canvas(result);
        canvas.drawBitmap(outerBitmap, 0f, 0f, null);

        final Bitmap innerBitmap = drawableToBitmap(inner);

        float originalWidth = innerBitmap.getWidth();
        //float originalHeight = innerBitmap.getHeight();

        float scale = (width - 2 * sidePadding) / originalWidth;

        float xTranslation = (float) sidePadding;
        float yTranslation = (float) topPadding;

        Matrix transformation = new Matrix();
        transformation.postTranslate(xTranslation, yTranslation);
        transformation.preScale(scale, scale);

        Paint paint = new Paint();
        paint.setFilterBitmap(true);

        canvas.drawBitmap(innerBitmap, transformation, paint);

        return result;
    }

    private Bitmap drawableToBitmap(Drawable drawable) {
        Bitmap bitmap;

        if (drawable instanceof BitmapDrawable) {
            BitmapDrawable bitmapDrawable = (BitmapDrawable) drawable;
            if (bitmapDrawable.getBitmap() != null) {
                return bitmapDrawable.getBitmap();
            }
        }

        if (drawable.getIntrinsicWidth() <= 0 || drawable.getIntrinsicHeight() <= 0) {
            bitmap = Bitmap.createBitmap(1, 1, Bitmap.Config.ARGB_8888); // Single color bitmap will be created of 1x1 pixel
        } else {
            bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(),
                    drawable.getIntrinsicHeight(),
                    Bitmap.Config.ARGB_8888);
        }

        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);
        return bitmap;
    }

    private Bitmap createSingleImageFromMultipleImages(Bitmap firstImage, Bitmap secondImage) {

        Bitmap result = Bitmap.createBitmap(firstImage.getWidth(), firstImage.getHeight(), firstImage.getConfig());
        Canvas canvas = new Canvas(result);
        canvas.drawBitmap(firstImage, 0f, 0f, null);
        canvas.drawBitmap(secondImage, 10, 10, null);
        return result;
    }
}
