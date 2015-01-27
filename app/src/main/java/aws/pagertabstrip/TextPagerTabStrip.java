package aws.pagertabstrip;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by thinh on 12/27/15.
 */
public class TextPagerTabStrip extends PagerTabStrip
{

    public TextPagerTabStrip(Context context)
    {
        super(context);
    }

    public TextPagerTabStrip(Context context, AttributeSet attrs)
    {
        super(context, attrs);
    }

    public TextPagerTabStrip(Context context, AttributeSet attrs, int defStyleAttr)
    {
        super(context, attrs, defStyleAttr);
    }

    public TextPagerTabStrip(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes)
    {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    protected View initSlidingBar(Context context)
    {
        // Custom here
        PagerTabStrip.LayoutParams lp;
        View bar = new View(context);
        bar.setBackgroundColor(Color.WHITE);
        lp = new PagerTabStrip.LayoutParams(20, 8);
        lp.addRule(PagerTabStrip.ALIGN_PARENT_BOTTOM);
        bar.setLayoutParams(lp);

        return bar;
    }
}

