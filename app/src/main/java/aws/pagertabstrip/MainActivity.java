package aws.pagertabstrip;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity
{
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        ViewPager pager = (ViewPager) findViewById(R.id.pager);
        pager.setAdapter(new TextPagerAdapter(getSupportFragmentManager()));

        PagerTabStrip tabStrip = (PagerTabStrip) findViewById(R.id.tabstrip);
        tabStrip.setItemViewType(PagerTabStripType.WRAP);
        tabStrip.setPager(pager, new TitlePagerTabStripAdapter());
    }

    @SuppressLint ("ValidFragment")
    class TextFragment extends Fragment
    {
        String mText;

        public TextFragment(String text)
        {
            mText = text;
        }

        @Nullable
        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container
                , Bundle savedInstanceState)
        {
            TextView txv = new TextView(container.getContext());
            txv.setText(mText);
            txv.setTextSize(200);

            return txv;
        }
    }

    class TextPagerAdapter extends FragmentPagerAdapter
    {
        String[] mTabs = new String[]{"Chúc", "Mừng", "Năm", "Mới", "Tân", "Xuân", "Đại", "Cát"};

        public TextPagerAdapter(FragmentManager fm)
        {
            super(fm);
        }

        @Override
        public Fragment getItem(int position)
        {
            return new TextFragment(mTabs[position]);
        }

        @Override
        public int getCount()
        {
            return mTabs.length;
        }
    }


    class TitlePagerTabStripAdapter extends PagerTabStripAdapter
    {
        String[] mTabs = new String[]{"Chúc", "Mừng", "Năm", "Mới", "Tân", "Xuân", "Đại", "Cát"};

        @Override
        public String getItem(int position)
        {
            return mTabs[position];
        }

        @Override
        public long getItemId(int position)
        {
            return position;
        }

        @Override
        public int getCount()
        {
            return mTabs.length;
        }

        @Override
        public PagerTabStripViewItem getView(int position, View convertView, ViewGroup parent)
        {
            TextTabStripViewItem tab = new TextTabStripViewItem(getBaseContext());
            tab.setText(getItem(position));
            return tab;
        }
    }

    class TextTabStripViewItem extends PagerTabStripViewItem
    {
        TextView mTextView;

        public TextTabStripViewItem(Context context)
        {
            super(context);
        }

        public TextTabStripViewItem(Context context, AttributeSet attrs)
        {
            super(context, attrs);
        }

        public TextTabStripViewItem(Context context, AttributeSet attrs, int defStyleAttr)
        {
            super(context, attrs, defStyleAttr);
        }

        public TextTabStripViewItem(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes)
        {
            super(context, attrs, defStyleAttr, defStyleRes);
        }

        public void setText(String text)
        {
            mTextView.setText(text);
        }

        @Override
        protected View initView(Context context)
        {
            mTextView = new TextView(context);
            mTextView.setTextSize(20);
            mTextView.setGravity(Gravity.CENTER);

            PagerTabStripViewItem.LayoutParams lp = new PagerTabStripViewItem.LayoutParams(PagerTabStripViewItem.LayoutParams.WRAP_CONTENT, PagerTabStripViewItem.LayoutParams.WRAP_CONTENT);
            mTextView.setLayoutParams(lp);
            lp.height = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 48, getResources().getDisplayMetrics());
            lp.setMargins(50, 0, 50, 0);
            lp.addRule(PagerTabStripViewItem.CENTER_IN_PARENT);

            return mTextView;
        }

        @Override
        public void onSelected()
        {
            this.setBackgroundColor(Color.RED);
            this.mTextView.setTextColor(Color.WHITE);
            this.setAlpha(1f);
        }

        @Override
        public void onDeselected()
        {
            this.setBackgroundColor(Color.BLUE);
            this.mTextView.setTextColor(Color.GRAY);
            this.setAlpha(0.5f);
        }

        @Override
        public void onSelectedProgress(float progress)
        {
            this.setAlpha(progress + 0.5f);
        }
    }
}
