package com.example.filterapp;

import android.os.Bundle;
import android.widget.LinearLayout;
import android.content.Intent;
import android.widget.TextView;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;
import androidx.viewpager.widget.ViewPager.OnPageChangeListener;

public class ViewPagerMain extends AppCompatActivity {

    private ViewPager viewPagerMain;
    private OnboardingAdapter onboardingAdapter;
    private LinearLayout dotsLayout;
    private TextView[] dots;
    private int[] layouts;
    private Button startButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_pager);
        startButton = findViewById(R.id.start_button);
        viewPagerMain = findViewById(R.id.view_pager);
        dotsLayout = findViewById(R.id.dots_layout);

        // Array de layouts para las pantallas del onboarding
        layouts = new int[]{
                R.layout.screen1,
                R.layout.screen2,
                R.layout.screen3
        };

        onboardingAdapter = new OnboardingAdapter(this, layouts);
        viewPagerMain.setAdapter(onboardingAdapter);

        addDotsIndicator(0);
        viewPagerMain.addOnPageChangeListener(viewPagerPageChangeListener);

        startButton.setOnClickListener(v -> openNextActivity());
    }

    private void addDotsIndicator(int position) {
        dots = new TextView[layouts.length];
        dotsLayout.removeAllViews();

        for (int i = 0; i < dots.length; i++) {
            dots[i] = new TextView(this);
            dots[i].setText("•");
            dots[i].setTextSize(35);
            dots[i].setTextColor(getResources().getColor(R.color.black));
            dotsLayout.addView(dots[i]);
        }

        if (dots.length > 0) {
            dots[position].setTextColor(getResources().getColor(R.color.green));
        }
    }

    private ViewPager.OnPageChangeListener viewPagerPageChangeListener = new ViewPager.OnPageChangeListener() {

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        }

        @Override
        public void onPageSelected(int position) {
            addDotsIndicator(position);
        }

        @Override
        public void onPageScrollStateChanged(int state) {
        }
    };

    private void openNextActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}
