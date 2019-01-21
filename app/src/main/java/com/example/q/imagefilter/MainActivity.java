package com.example.q.imagefilter;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.DrawableRes;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.LinkedList;
import java.util.List;
import java.util.Locale;

import hu.don.easylut.EasyLUT;
import hu.don.easylut.filter.Filter;
import hu.don.easylut.filter.LutFilterFromResource;
import hu.don.easylut.lutimage.CoordinateToColor;
import hu.don.easylut.lutimage.LutAlignment;


public class MainActivity extends AppCompatActivity implements FilterAdapter.OnFilterSelected {

    private static final String TAG = MainActivity.class.getSimpleName();

    private ImageView ivImage;
    private ImageView beforeImage;
    private TextView tvName;
    private ProgressBar pbBusy;
    private RecyclerView rvFilters;
    private ViewPager viewPager;
    private Bitmap originalBitmap, filterBitmap;
    public static List<FilterSelection> effectItems = new LinkedList<>();
    private FilterSelection lastFilterSelection;
    public static Bitmap userImageBitmap;
    public static Resources resources;
    private boolean fullRes = false;
    private String dir;
    private int curFilter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        resources = getResources();
        curFilter = 1;
//        tvName = findViewById(R.id.tv_name);
        ivImage = findViewById(R.id.iv_image2);
        pbBusy = findViewById(R.id.pb_busy);
        final Resources resources = getResources();
//        setImage(R.drawable.europe);

        rvFilters = findViewById(R.id.rv_filters);

        LutFilterFromResource.Builder squareBgr =
                EasyLUT.fromResourceId().withColorAxes(CoordinateToColor.Type.RGB_TO_ZYX).withResources(resources);
        LutFilterFromResource.Builder squareRgb =
                EasyLUT.fromResourceId().withColorAxes(CoordinateToColor.Type.RGB_TO_XYZ).withResources(resources);
        LutFilterFromResource.Builder squareBrg =
                EasyLUT.fromResourceId().withColorAxes(CoordinateToColor.Type.RGB_TO_YZX).withResources(resources);
        LutFilterFromResource.Builder haldRgb =
                EasyLUT.fromResourceId().withColorAxes(CoordinateToColor.Type.RGB_TO_XYZ).withResources(resources)
                        .withAlignmentMode(LutAlignment.Mode.HALD);

        addFilter("none", EasyLUT.createNonFilter());
        addFilter("identity_square_8", squareRgb.withLutBitmapId(R.drawable.identity_square_8).createFilter());
        addFilter("identity_hald_8", haldRgb.withLutBitmapId(R.drawable.identity_hald_8).createFilter());
        addFilter("square_4_brg", squareBrg.withLutBitmapId(R.drawable.filter_square_4_brg).createFilter());
        addFilter("square_8_00", squareRgb.withLutBitmapId(R.drawable.filter_square_8_00).createFilter());
        addFilter("square_8_01", squareRgb.withLutBitmapId(R.drawable.filter_square_8_01).createFilter());
        addFilter("square_8_02", squareRgb.withLutBitmapId(R.drawable.filter_square_8_02).createFilter());
        addFilter("square_8_03", squareRgb.withLutBitmapId(R.drawable.filter_square_8_03).createFilter());
        addFilter("square_8_04", squareRgb.withLutBitmapId(R.drawable.filter_square_8_04).createFilter());
        addFilter("square_8_05", squareRgb.withLutBitmapId(R.drawable.filter_square_8_05).createFilter());
        addFilter("square_8_06", squareRgb.withLutBitmapId(R.drawable.filter_square_8_06).createFilter());
        addFilter("square_8_07", squareRgb.withLutBitmapId(R.drawable.filter_square_8_07).createFilter());
        addFilter("square_8_08", squareRgb.withLutBitmapId(R.drawable.filter_square_8_08).createFilter());
        addFilter("square_8_09", squareRgb.withLutBitmapId(R.drawable.filter_square_8_09).createFilter());
        addFilter("square_8_09", squareRgb.withLutBitmapId(R.drawable.filter_square_8_09).createFilter());
        addFilter("wide_4_00", squareRgb.withLutBitmapId(R.drawable.filter_wide_4_00).createFilter());
        addFilter("wide_4_01", squareRgb.withLutBitmapId(R.drawable.filter_wide_4_01).createFilter());
        addFilter("wide_4_02", squareRgb.withLutBitmapId(R.drawable.filter_wide_4_02).createFilter());
        addFilter("wide_4_03", squareRgb.withLutBitmapId(R.drawable.filter_wide_4_03).createFilter());
        addFilter("wide_4_04", squareRgb.withLutBitmapId(R.drawable.filter_wide_4_04).createFilter());
        addFilter("wide_4_05", squareRgb.withLutBitmapId(R.drawable.filter_wide_4_05).createFilter());
        addFilter("wide_4_06", squareRgb.withLutBitmapId(R.drawable.filter_wide_4_06).createFilter());
        addFilter("wide_4_07", squareRgb.withLutBitmapId(R.drawable.filter_wide_4_07).createFilter());
        addFilter("wide_8_bgr", squareBgr.withLutBitmapId(R.drawable.filter_wide_8_bgr).createFilter());
        addFilter("hald_8_00", haldRgb.withLutBitmapId(R.drawable.filter_hald_8_00).createFilter());
        addFilter("hald_8_01", haldRgb.withLutBitmapId(R.drawable.filter_hald_8_01).createFilter());

        rvFilters.setLayoutManager(new LinearLayoutManager(this));
        rvFilters.setAdapter(new FilterAdapter(effectItems, this));
        userImageBitmap = BitmapFactory.decodeResource(resources,R.drawable.europe);
//        R.integer.maximum
        ivImage.post(new Runnable() {
            @Override
            public void run() {
                setImage(userImageBitmap);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        switch (item.getItemId()) {
//            case R.id.action_identity:
//                item.setChecked(!item.isChecked());
//                if (item.isChecked()) {
//                    setImage(R.drawable.identity_square_8);
//                } else {
//                    setImage(R.drawable.landscape);
//                }
//                return true;
//            case R.id.action_full_res:
//                item.setChecked(!item.isChecked());
//                fullRes = item.isChecked();
//                setImage(originalBitmap, 0);
//                return true;
//        }
//        return super.onOptionsItemSelected(item);
//    }

    private void addFilter(String name, Filter filter) {
        effectItems.add(new FilterSelection(name.toUpperCase(Locale.ENGLISH), filter));
    }

    private void setBusy(boolean busy, boolean removeImage) {
        if (busy) {
            pbBusy.animate().alpha(1f).start();
            pbBusy.setVisibility(View.VISIBLE);
            ivImage.animate().alpha(removeImage ? 0f : 0.5f).start();
//            tvName.animate().alpha(0f).start();
        } else {
            ivImage.animate().alpha(1f).start();
//            tvName.animate().alpha(1f).start();
            pbBusy.animate().alpha(0f).start();
        }
    }

//    private void setImage(@DrawableRes final int resource) {
//        setImage(null, resource);
//    }

    private void setImage(Bitmap bitmap) {
        new AsyncTask<Bitmap, Bitmap, Bitmap[]>() {

            long start;

            @Override
            protected void onPreExecute() {
                Log.d("***check","preexecute for setimage");
                setBusy(true, true);
                start = System.nanoTime();
            }

            @Override
            protected Bitmap[] doInBackground(Bitmap... bitmaps) {
                Log.d("***check","background job for setimage");
                Bitmap bitmap1 = bitmaps[0];
                if (bitmap1 == null) {
                    Log.d("***check","null bitmap");
                }
                Bitmap mask = BitmapFactory.decodeResource(getResources(),R.drawable.mask);
                int intWidth = bitmap1.getWidth();
                int intHeight = bitmap1.getHeight();
                Bitmap resultMaskBitmap = Bitmap.createBitmap(intWidth,intHeight,Bitmap.Config.ARGB_8888);
                Bitmap getMaskBitmap = Bitmap.createScaledBitmap(mask,intWidth,intHeight,true);
                Canvas mCanvas = new Canvas(resultMaskBitmap);
                Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
                paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_IN));
                mCanvas.drawBitmap(bitmap1, 0, 0, null);
                mCanvas.drawBitmap(getMaskBitmap, 0, 0, paint);
                paint.setXfermode(null);
                bitmap1 =  resultMaskBitmap;
                Bitmap bitmap2 = resultMaskBitmap;
                publishProgress(bitmap1);
                if (bitmap1 != null && !fullRes) {
                    int measuredHeight = ivImage.getMeasuredHeight();
                    int measuredWidth = ivImage.getMeasuredWidth();
                    if (measuredWidth != 0 && measuredHeight != 0 && (bitmap1.getHeight() >= measuredHeight || bitmap1.getWidth() >= measuredWidth)) {
                        float originalRatio = (float) bitmap1.getWidth() / (float) bitmap1.getHeight();
                        float measuredRatio = (float) measuredWidth / (float) measuredHeight;
                        if (originalRatio > measuredRatio) {
                            measuredWidth = (int) (measuredHeight * originalRatio);
                        } else {
                            measuredHeight = (int) (measuredWidth / originalRatio);
                        }
                        DisplayMetrics metrics = new DisplayMetrics();
                        getWindowManager().getDefaultDisplay().getMetrics(metrics);
                        bitmap2 = Bitmap.createScaledBitmap(bitmap1, measuredWidth, measuredHeight, true);
                    }
                }
                return new Bitmap[]{bitmap1, bitmap2};
            }

            @Override
            protected void onProgressUpdate(Bitmap... bitmaps) {
                Log.d("***check","progressupdate for setimage");
                ivImage.setImageBitmap(bitmaps[0]);
            }

            @Override
            protected void onPostExecute(Bitmap[] bitmap) {
                Log.d("***check","postexecute for setimage");
                originalBitmap = bitmap[0];
                filterBitmap = bitmap[1];
                ivImage.setImageBitmap(filterBitmap);
                setBusy(false, true);
                ivImage.setOnTouchListener(new OnSwipeTouchListener(MainActivity.this){
                    public void onSwipeRight(){
                        dir = "right";
                        curFilter = (curFilter+1)%effectItems.size();
                        lastFilterSelection = effectItems.get(curFilter);
                        onFilterClicked(lastFilterSelection);
                    }
                    public void onSwipeLeft(){
                        dir = "left";
                        curFilter = (curFilter-1+effectItems.size())%effectItems.size();
                        lastFilterSelection = effectItems.get(curFilter);
                        onFilterClicked(lastFilterSelection);
                    }
                });
//                onFilterClicked(lastFilterSelection);
                if (filterBitmap == null) {
                    Log.d(TAG, String.format("loading bitmap failed in %.2fms", (System.nanoTime() - start) / 1e6f));
                } else {
                    Log.d(TAG, String.format("loaded %dx%d bitmap in %.2fms", filterBitmap.getWidth(), filterBitmap.getHeight(), (System.nanoTime() - start) / 1e6f));
                }
            }
        }.execute(bitmap);
    }

    @Override
    public void onFilterClicked(FilterSelection filterSelection) {
        lastFilterSelection = filterSelection;
//        tvName.setVisibility(View.VISIBLE);
//        tvName.setText(filterSelection == null ? "NONE" : filterSelection.name);
//        Toast.makeText(getApplicationContext(),"Filter "+filterSelection == null ? "NONE" : filterSelection.name +" is selected",Toast.LENGTH_SHORT).show();
        new AsyncTask<Void, Void, Bitmap>() {

            long start;

            @Override
            protected void onPreExecute() {
                Log.d("***check","preexecute for filterclicked");
                setBusy(true, false);
                start = System.nanoTime();
            }

            @Override
            protected Bitmap doInBackground(Void... voids) {

                Log.d("***check","background job for filterclicked");

//                Bitmap bitmap2 = resultMaskBitmap;
//                publishProgress(bitmap1);
                if (lastFilterSelection == null || filterBitmap == null) {
                    return filterBitmap;
                }
                Log.i("***TEST", filterBitmap.toString());
                Log.i("***TEST", lastFilterSelection.filter.toString());
                try {
                    return lastFilterSelection.filter.apply(filterBitmap);
                } catch (Exception e){
                    e.printStackTrace();
//                    String str = e.getMessage();
//                    Log.i("***TEST", e);
                    return null;
                }
            }

            @Override
            protected void onPostExecute(Bitmap bitmap) {

                Log.d("***check","post execute for filterclicked");
                Log.d("***check","bitmap :"+ bitmap.toString());
                Log.d("***check","filteredbitmap : "+filterBitmap.toString() );
                Bitmap mask = BitmapFactory.decodeResource(getResources(),R.drawable.mask);
                int intWidth = bitmap.getWidth();
                int intHeight = bitmap.getHeight();
                Bitmap resultMaskBitmap = Bitmap.createBitmap(intWidth,intHeight,Bitmap.Config.ARGB_8888);
                Bitmap getMaskBitmap = Bitmap.createScaledBitmap(mask,intWidth,intHeight,true);
                Canvas mCanvas = new Canvas(resultMaskBitmap);
                Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
                paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_IN));
                mCanvas.drawBitmap(bitmap, 0, 0, null);
                mCanvas.drawBitmap(getMaskBitmap, 0, 0, paint);
                paint.setXfermode(null);
                bitmap =  resultMaskBitmap;
                ivImage.setImageBitmap(bitmap);
                setBusy(false, false);
                if (bitmap == null) {
                    Log.d(TAG, String.format("processing bitmap failed in %.2fms", (System.nanoTime() - start) / 1e6f));
                } else {
                    Log.d(TAG, String.format("processed %dx%d bitmap in %.2fms", bitmap.getWidth(), bitmap.getHeight(), (System.nanoTime() - start) / 1e6f));
                }
            }
        }.execute();
    }

}
