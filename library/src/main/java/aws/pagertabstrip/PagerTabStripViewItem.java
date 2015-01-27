package aws.pagertabstrip;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RelativeLayout;

/**
 * Created by thinh on 12/27/15.
 */
public abstract class PagerTabStripViewItem extends RelativeLayout
{
    public PagerTabStripViewItem(Context context)
    {
        super(context);
        init(context);
    }

    public PagerTabStripViewItem(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        init(context);
    }

    public PagerTabStripViewItem(Context context, AttributeSet attrs, int defStyleAttr)
    {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    @TargetApi (Build.VERSION_CODES.LOLLIPOP)
    public PagerTabStripViewItem(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes)
    {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context);
    }

    private void init(Context context)
    {
        addView(initView(context));
    }

    protected abstract View initView(Context context);

    protected abstract void onSelected();

    protected abstract void onDeselected();

    protected abstract void onSelectedProgress(float progress);
}
