package handong.cconma.cconmaadmin;

import android.app.Service;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.IBinder;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.Toast;

/**
 * Created by √÷º≠¿≤ on 2015-07-07.
 */
public class IconService extends Service {

    private WindowManager.LayoutParams params;
    private WindowManager windowManager;
    private ImageView chatHead;

    private float mTouchX, mTouchY;
    private int mViewX, mViewY;

    @Override public IBinder onBind(Intent intent) {
        // Not used
        return null;
    }

    @Override public void onCreate() {
        super.onCreate();

        params = new WindowManager.LayoutParams(
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.TYPE_PHONE,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                PixelFormat.TRANSLUCENT);
        windowManager = (WindowManager) getSystemService(WINDOW_SERVICE);

        chatHead = new ImageView(this);
        chatHead.setImageResource(R.drawable.cconma);

        chatHead.setClickable(true);
        chatHead.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            }
        });

        chatHead.setOnTouchListener(new View.OnTouchListener() {

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
                        windowManager.updateViewLayout(chatHead, params);
                        break;
                    case MotionEvent.ACTION_UP:
                        if(Math.abs((int)(event.getRawX() - mTouchX)) <= 5 && Math.abs((int)(event.getRawY() - mTouchY)) <= 5)
                            Toast.makeText(getApplicationContext(), "unread message : 5", Toast.LENGTH_SHORT).show();
                        break;
                }
                return true;
            }
        });

        params.gravity = Gravity.CENTER;
        params.x = 0;
        params.y = 100;

        windowManager.addView(chatHead, params);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(chatHead != null)
            windowManager.removeView(chatHead);
    }
}