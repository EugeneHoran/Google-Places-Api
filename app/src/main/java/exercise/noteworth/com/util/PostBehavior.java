package exercise.noteworth.com.util;

import android.content.Context;
import android.support.annotation.Keep;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;


public class PostBehavior extends CoordinatorLayout.Behavior<TextView> {

    @Keep
    public PostBehavior(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean layoutDependsOn(CoordinatorLayout parent, TextView child, View dependency) {
        return dependency instanceof AppBarLayout;
    }

    @Override
    public boolean onDependentViewChanged(CoordinatorLayout parent, TextView child, View dependency) {
        if (dependency.getBottom() == child.getTop()) return false;
        child.offsetTopAndBottom(dependency.getBottom() - child.getTop());
        return true;
    }
}