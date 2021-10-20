package helper;

import android.content.Context;
import android.util.AttributeSet;

import androidx.recyclerview.widget.LinearLayoutManager;

public class CustomLayout extends LinearLayoutManager {
    public CustomLayout(Context context) {
        super(context);
    }

    public CustomLayout(Context context, int orientation, boolean reverseLayout) {
        super(context, orientation, reverseLayout);
    }

    public CustomLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    public boolean supportsPredictiveItemAnimations() {
        return false;
    }
}
