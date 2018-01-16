package transport.school.com.schoolapp;
import android.os.Bundle;
import android.support.annotation.Nullable;

import frameworks.basemvp.AppBaseActivity;
import frameworks.basemvp.IPresenter;
public class StopStudentAttendance extends AppBaseActivity {

    @Override
    public int getViewToCreate() {
        return R.layout.activity_stop_student_attendance;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addFrgment(StudentAttendanceFragment.newInstance(), "Student_Fragment", R.id.container, false);
    }

    @Override
    public IPresenter getPresenter() {
        return null;
    }
}
