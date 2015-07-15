package handong.cconma.cconmaadmin;

import android.app.Service;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.IBinder;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.Toast;

/**
 * Created by Seoyul on 2015-07-07.
 */
public class IconService extends Service {

    private WindowManager.LayoutParams params, params2;
    private WindowManager windowManager;
    private ImageView cconmaIcon;
    private ImageView xIcon;
    private float mTouchX, mTouchY;
    private int mViewX, mViewY;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {

        super.onCreate();

        windowManager = (WindowManager) getSystemService(WINDOW_SERVICE);

        cconmaIcon = new ImageView(this);
        cconmaIcon.setImageResource(R.drawable.testimage);
        cconmaIcon.setOnTouchListener(new View.OnTouchListener() {

            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        mTouchX = event.getRawX();
                        mTouchY = event.getRawY();
                        mViewX = params.x;
                        mViewY = params.y;
                        break;
                    case MotionEvent.ACTION_MOVE:
                        int x = (int) (event.getRawX() - mTouchX);
                        int y = (int) (event.getRawY() - mTouchY);
                        params.x = mViewX + x;
                        params.y = mViewY + y;
                        windowManager.updateViewLayout(cconmaIcon, params);
                        if (cconmaIcon != null) {
                            if (event.getRawY() > 800) {
                                xIcon.setVisibility(View.VISIBLE);
                            } else {
                                xIcon.setVisibility(View.INVISIBLE);
                            }
                            if (event.getRawY() > 900) {
                                windowManager.removeView(cconmaIcon);
                                xIcon.setVisibility(View.INVISIBLE);
                            }
                        }
                        break;
                    case MotionEvent.ACTION_UP:
                        if (Math.abs((int) (event.getRawX() - mTouchX)) <= 5 && Math.abs((int) (event.getRawY() - mTouchY)) <= 5) {

                            String weather = new Test().getWeather();

                            Toast.makeText(getApplicationContext(), weather, Toast.LENGTH_SHORT).show();
                            Log.e("location", "x : " + event.getRawX() + " y : " + event.getRawY());

                        }
                        break;
                }
                return true;
            }
        });

        xIcon = new ImageView(this);
        xIcon.setImageResource(R.drawable.x);

        params = new WindowManager.LayoutParams(
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.TYPE_PHONE,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                PixelFormat.TRANSLUCENT);
        params.gravity = Gravity.LEFT | Gravity.TOP;
        params.x = 0;
        params.y = 0;

        params2 = new WindowManager.LayoutParams(
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.TYPE_PHONE,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                PixelFormat.TRANSLUCENT);
        params2.gravity = Gravity.CENTER | Gravity.BOTTOM;

        windowManager.addView(cconmaIcon, params);
        windowManager.addView(xIcon, params2);

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(cconmaIcon != null)
            windowManager.removeView(cconmaIcon);
    }

}