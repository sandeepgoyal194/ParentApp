package transport.school.com.schoolapp;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuItem;

import butterknife.BindView;
import frameworks.basemvp.AppBaseActivity;
import frameworks.basemvp.IPresenter;
import transport.school.com.schoolapp.bean.Student;
public class MainActivity extends AppBaseActivity {
    SectionsPagerAdapter mSectionsPagerAdapter;
    private ViewPager mViewPager;
    private MapViewFragment mapFragment;
    @BindView(R.id.tabs)
    TabLayout mMainTab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mapFragment = MapViewFragment.newInstance((Student) getIntent().getSerializableExtra("student"));
        super.onCreate(savedInstanceState);
        loadFragments();
    }

    void loadFragments() {
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
        mViewPager = (ViewPager) findViewById(R.id.view_pager);
        mViewPager.setAdapter(mSectionsPagerAdapter);
        mMainTab.setupWithViewPager(mViewPager);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public int getViewToCreate() {
        return R.layout.app_bar_main;
    }

    @Override
    public IPresenter getPresenter() {
        return null;
    }

    public class SectionsPagerAdapter extends FragmentPagerAdapter {
        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    return mapFragment;
                case 1:
                    return new StudentAttendanceFragment();
            }
            return new StudentAttendanceFragment();
        }

        @Override
        public int getCount() {
            return 1;
        }

        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "Navigation";
                case 1:
                    return "Attendence";
            }
            return "Attendence";
        }
    }

    @Override
    public boolean isLocationNeeded() {
        return false;
    }

    @Override
    public void onLocationChanged(Location location) {
        super.onLocationChanged(location);
        if (mapFragment != null) {
            mapFragment.onLocationChanged(location);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.endroute_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.end_route:
                return super.onOptionsItemSelected(item);
            case R.id.logout:
        }
        return true;
    }
}
