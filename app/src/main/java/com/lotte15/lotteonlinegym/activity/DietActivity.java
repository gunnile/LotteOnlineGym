package com.lotte15.lotteonlinegym.activity;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.lotte15.lotteonlinegym.R;
import com.lotte15.lotteonlinegym.adapter.DietAdapter;
import com.lotte15.lotteonlinegym.type.DietItem;
import com.lotte15.lotteonlinegym.view.SlidingTabLayout;

import java.util.ArrayList;

public class DietActivity extends FragmentActivity {

    private DietAdapter dietAdapter;

    private SlidingTabLayout mSlidingTabLayout;

    private ViewPager mViewPager;

    private DietItem dietFoods;

    private String[] foodImages;
    private String[] foodTitles;
    private String[] foodDescriptions;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_sample);
        setTitle("식단");

        mViewPager = (ViewPager) findViewById(R.id.viewpager);
        mViewPager.setAdapter(new SamplePagerAdapter());

        mSlidingTabLayout = (SlidingTabLayout) findViewById(R.id.sliding_tabs);
        mSlidingTabLayout.setViewPager(mViewPager);
    }

    class SamplePagerAdapter extends PagerAdapter {

        public ArrayList<DietItem> items = new ArrayList<>();

        @Override
        public int getCount() {
            return 3;
        }

        @Override
        public boolean isViewFromObject(View view, Object o) {
            return o == view;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            String title;

            if(position == 0){
                title = "아침";
            }else if(position == 1){
                title = "점심";
            }else{
                title = "저녁";
            }

            return title;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            View view = getLayoutInflater().inflate(R.layout.fragment_diet,
                    container, false);



            ListView listview = (ListView) view.findViewById(R.id.diet_listview);

            //data added
            foodImages = getResources().getStringArray(R.array.food_image_array);
            foodDescriptions = getResources().getStringArray(R.array.food_description_array);
            foodTitles = getResources().getStringArray(R.array.food_title_array);

            if(items.isEmpty() == true){
                for(int i=0; i < 4; i++){

                    dietFoods = new DietItem();

                    dietFoods.foodImage = foodImages[i];
                    dietFoods.foodDescription = foodDescriptions[i];
                    dietFoods.foodTitle = foodTitles[i];

                    items.add(dietFoods);

                    dietFoods = null;
                }
            }


            dietAdapter = new DietAdapter(getBaseContext(), R.layout.diet_adapter, items);

            listview.setAdapter(dietAdapter);

            container.addView(view);

            return view;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

    }



}
