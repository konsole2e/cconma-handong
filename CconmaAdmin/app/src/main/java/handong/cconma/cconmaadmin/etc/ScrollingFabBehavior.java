package handong.cconma.cconmaadmin.etc;

import android.content.Context;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

/**
 * Created by Young Bin Kim on 2015-08-12.
 */
public class ScrollingFabBehavior extends CoordinatorLayout.Behavior<FloatingActionButton> {
    private int toolbarHeight;

    public ScrollingFabBehavior(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.toolbarHeight = 48;
    }

    @Override
    public boolean layoutDependsOn(CoordinatorLayout parent, FloatingActionButton fab, View dependency) {
        return dependency instanceof AppBarLayout;
    }

    @Override
    public boolean onDependentViewChanged(CoordinatorLayout parent, FloatingActionButton fab, View dependency) {
        if (dependency instanceof AppBarLayout) {
            CoordinatorLayout.LayoutParams lp = (CoordinatorLayout.LayoutParams) fab.getLayoutParams();
            int fabBottomMargin = lp.bottomMargin;
            int distanceToScroll = fab.getHeight();
            Log.d("debugging", "dependency: " + dependency.getY() + " ");
            float ratio = dependency.getY() / 140;
            fab.setTranslationY(-distanceToScroll * ratio);
        }
        return true;
    }
}
