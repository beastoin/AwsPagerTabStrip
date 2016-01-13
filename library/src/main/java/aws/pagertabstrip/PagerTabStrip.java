package aws.pagertabstrip;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.os.Build;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import aws.pagertabstrip.lib.R;

/**
 * Created by thinh on 12/27/15.
 */
public class PagerTabStrip extends RelativeLayout
        implements ViewPager.OnPageChangeListener, ViewTreeObserver.OnScrollChangedListener
{
    ViewPager mPager;
    PagerTabStripAdapter mTabStripAdapter;
    LinearLayout mContainerView;
    HorizontalScrollView mScrollView;
    PagerTabStripViewItem mSelectedTabStripView;
    //View mSlidingBar;

    PagerTabStripType mType = PagerTabStripType.WRAP;

    public PagerTabStrip(Context context)
    {
        super(context);
        init(context);
    }

    public PagerTabStrip(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        init(context);
        bindAttributes(context, attrs);
    }

    public PagerTabStrip(Context context, AttributeSet attrs, int defStyleAttr)
    {
        super(context, attrs, defStyleAttr);
        init(context);
        bindAttributes(context, attrs);
    }

    @TargetApi (Build.VERSION_CODES.LOLLIPOP)
    public PagerTabStrip(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes)
    {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context);
        bindAttributes(context, attrs);
    }

    private void init(Context context)
    {
        //mSlidingBar = initSlidingBar(context);

        mContainerView = new LinearLayout(context);
        mContainerView.setOrientation(LinearLayout.HORIZONTAL);

        mScrollView = new HorizontalScrollView(context);
        mScrollView.setVerticalScrollBarEnabled(false);
        mScrollView.setHorizontalScrollBarEnabled(false);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
        {
            mScrollView.setOnScrollChangeListener(new OnScrollChangeListener()
            {
                @Override
                public void onScrollChange(View v, int scrollX, int scrollY, int oldScrollX, int oldScrollY)
                {
                    onScrollChanged();
                }
            });
        }
        else
        {
            mScrollView.getViewTreeObserver().addOnScrollChangedListener(this);
        }

        mScrollView.getViewTreeObserver().addOnGlobalLayoutListener(
                new ViewTreeObserver.OnGlobalLayoutListener()
                {
                    @Override
                    public void onGlobalLayout()
                    {
                        updateChildViewLayout();
                    }
                });

        mScrollView.addView(mContainerView);
        addView(mScrollView);
        //addView(mSlidingBar);
    }

    private void bindAttributes(Context context, AttributeSet attrs)
    {
        /*
        TypedArray a = context.getTheme().obtainStyledAttributes(
                attrs,
                R.styleable.PieChart,
                0, 0);

        try {
            mShowText = a.getBoolean(R.styleable.PieChart_showText, false);
            mTextPos = a.getInteger(R.styleable.PieChart_labelPosition, 0);
        } finally {
            a.recycle();
        }
        */
    }

    protected View initSlidingBar(Context context)
    {
        PagerTabStrip.LayoutParams lp;
        View bar = new View(context);
        bar.setBackgroundColor(Color.GREEN);
        lp = new PagerTabStrip.LayoutParams(20, 20);
        lp.addRule(PagerTabStrip.ALIGN_PARENT_BOTTOM);
        bar.setLayoutParams(lp);

        return bar;
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels)
    {
        if (position < mPager.getAdapter().getCount() - 1)
            updateScrollView(position, positionOffset, positionOffsetPixels);
    }

    private void updateScrollView(int position, float positionOffset, int positionOffsetPixels)
    {
        PagerTabStripViewItem fromView = (PagerTabStripViewItem) mContainerView.getChildAt(position);
        PagerTabStripViewItem toView = (PagerTabStripViewItem) mContainerView.getChildAt(position + 1);

        int from = fromView.getLeft() + fromView.getWidth() / 2;
        int to = toView.getLeft() + toView.getWidth() / 2;

        int focusTo = mScrollView.getWidth() / 2;

        int transX = (int) (from + (to - from) * positionOffset);
        int scrollX = transX - focusTo;

        fromView.onSelectedProgress(1 - positionOffset);
        toView.onSelectedProgress(positionOffset);

        mScrollView.smoothScrollTo(scrollX, 0);

        //mSlidingBar.setTranslationX(
        //        fromView.getLeft() + (toView.getLeft() - fromView.getLeft()) * positionOffset - mScrollView.getScrollX());
    }

    @Override
    public void onPageSelected(int position)
    {
        for (int i = 0; i < mContainerView.getChildCount(); i++)
        {
            PagerTabStripViewItem pagerViewItem = (PagerTabStripViewItem) mContainerView.getChildAt(i);
            if (i == position)
            {
                pagerViewItem.onSelected();
                mSelectedTabStripView = pagerViewItem;

                /*
                PagerTabStrip.LayoutParams lp = (PagerTabStrip.LayoutParams) mSlidingBar.getLayoutParams();
                lp.width = pagerViewItem.getWidth();
                mSlidingBar.setLayoutParams(lp);
                */
            }
            else
            {
                pagerViewItem.onDeselected();
            }
        }
    }

    @Override
    public void onPageScrollStateChanged(int state)
    {
    }

    @Override
    public void onScrollChanged()
    {
        int[] loc = new int[2];
        mSelectedTabStripView.getLocationInWindow(loc);

        //mSlidingBar.setTranslationX(loc[0]);
    }

    @Override
    protected void onDetachedFromWindow()
    {
        destroy();
        super.onDetachedFromWindow();
    }

    public void setPager(ViewPager pager, PagerTabStripAdapter tabStripAdapter)
    {
        this.mPager = pager;
        this.mTabStripAdapter = tabStripAdapter;

        notifyPagerChanged();
    }

    private void notifyPagerChanged()
    {
        if (this.mPager != null)
            this.mPager.removeOnPageChangeListener(this);

        this.mPager.addOnPageChangeListener(this);

        // Update tab views
        mContainerView.removeAllViews();
        for (int i = 0; i < this.mTabStripAdapter.getCount(); i++)
        {
            final int position = i;
            PagerTabStripViewItem tabStripViewItem = this.mTabStripAdapter.getView(position, null, null);
            tabStripViewItem.setOnClickListener(new OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    mPager.setCurrentItem(position);
                }
            });
            mContainerView.addView(tabStripViewItem);
        }

        // Fire selected event
        getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener()
        {
            @Override
            public void onGlobalLayout()
            {
                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN)
                {
                    getViewTreeObserver().removeGlobalOnLayoutListener(this);
                }
                else
                {
                    getViewTreeObserver().removeOnGlobalLayoutListener(this);
                }

                onPageSelected(mPager.getCurrentItem());
            }
        });
    }

    private void updateChildViewLayout()
    {
        switch (mType)
        {
            case WRAP:
                break;

            case TILE:
                int itemWidth = mScrollView.getWidth() / mContainerView.getChildCount();
                for (int i = 0; i < mContainerView.getChildCount(); i++)
                {
                    PagerTabStripViewItem item = (PagerTabStripViewItem) mContainerView.getChildAt(i);
                    if (itemWidth - item.getWidth() == 0)
                        continue;

                    LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) item.getLayoutParams();
                    lp.width = itemWidth;
                    item.setLayoutParams(lp);
                }
                break;

            default:
                break;
        }
    }

    private void destroy()
    {
        this.mPager.removeOnPageChangeListener(this);
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M)
        {
            mScrollView.getViewTreeObserver().removeOnScrollChangedListener(this);
        }
    }

    public void setItemViewType(PagerTabStripType type)
    {
        mType = type;
    }
}
