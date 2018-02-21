package transport.school.com.schoolapp;
import android.content.Intent;
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
import android.widget.Toast;

import java.util.List;

import butterknife.BindView;
import frameworks.appsession.AppBaseApplication;
import frameworks.basemvp.AppBaseActivity;
import frameworks.basemvp.IPresenter;
import frameworks.retrofit.ResponseResolver;
import frameworks.retrofit.RestError;
import frameworks.retrofit.WebServicesWrapper;
import retrofit2.Response;
import transport.school.com.schoolapp.bean.ActiveRouteReply;
import transport.school.com.schoolapp.bean.Route;
import transport.school.com.schoolapp.bean.Student;
public class MainActivity extends AppBaseActivity {
    SectionsPagerAdapter mSectionsPagerAdapter;
    private ViewPager mViewPager;
    private MapViewFragment mapFragment;
    @BindView(R.id.tabs)
    TabLayout mMainTab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Student student = (Student) getIntent().getSerializableExtra("student");
        mapFragment = MapViewFragment.newInstance(student);
        super.onCreate(savedInstanceState);
        Route route = new Route();
        route.setRouteid(student.getRouteid());

        WebServicesWrapper.getInstance().getActiveRoute(route, new ResponseResolver<ActiveRouteReply>() {
            @Override
            public void onSuccess(ActiveRouteReply activeRouteReply, Response response) {
                if (activeRouteReply.getStudents().get(0).getE().equals("0") && activeRouteReply.getStudents().get(0).getM().equals("0")) {
                    Toast.makeText(MainActivity.this,"No Route Available",Toast.LENGTH_LONG).show();
                } else {
                    Route route = new Route();
                    if (activeRouteReply.getStudents().get(0).getE().equals("1")) {
                        route.setmMorningEvening("e");
                    } else if (activeRouteReply.getStudents().get(0).getM().equals("1")) {
                        route.setmMorningEvening("m");
                    }
                    AppBaseApplication.getApplication().saveUser(route);
                    loadFragments();
                }
            }

            @Override
            public void onFailure(RestError error, String msg) {
            }
        });
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
        menu.clear();
        List<Student> studentList = AppBaseApplication.getApplication().getStudents();
        if(studentList.size()>1) {
            for (int i = 0; i < studentList.size(); i++) {
                Student student = studentList.get(i);
                menu.add(0, i, i, student.getStudentname());
            }
        }
        mapFragment.setStudent(AppBaseApplication.getApplication().getStudents().get(0));
        setTitleValue(AppBaseApplication.getApplication().getStudents().get(0));
        return true;
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        mapFragment.setStudent(AppBaseApplication.getApplication().getStudents().get(item.getItemId()));
        setTitleValue(AppBaseApplication.getApplication().getStudents().get(item.getItemId()));
        return true;
    }

    void setTitleValue(Student student) {
        setTitle(student.getStudentname() +","+student.getSchoolname());
    }
}
