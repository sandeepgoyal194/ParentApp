package frameworks.appsession;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.provider.Settings;

import java.util.List;

import transport.school.com.schoolapp.LoginActivity;
import transport.school.com.schoolapp.bean.LoginResponse;
import transport.school.com.schoolapp.bean.Route;
import transport.school.com.schoolapp.bean.Student;
/**
 * Created by naveen on 21/01/2017.
 */
public class AppBaseApplication extends Application {
    private static AppBaseApplication mApplication;
    private Route mUser = null;
    AppUserManager mAppUserManager = null;
    AppSessionManager mAppSessionManager = null;
    public static String TAG = AppBaseApplication.class.getName();
    private LoginResponse mLoginResponse;

    @Override
    public void onCreate() {
        super.onCreate();
        mApplication = this;
        //  JodaTimeAndroid.init(this);
    }

    @Override
    protected void attachBaseContext(Context base) {
        //MultiDex.install(base);
        super.attachBaseContext(base);
        mAppUserManager = new AppUserManager(this);
        mAppSessionManager = new AppSessionManager(this);
        initSession();
    }

    public static AppBaseApplication getApplication() {
        return mApplication;
    }

    public void initSession() {
        if (mAppSessionManager.isRunningSession()) {
            mLoginResponse = mAppSessionManager.getSession();
        }
    }

    public LoginResponse getSession() {
        return mAppSessionManager.getSession();
    }

    public void saveUser(Route user) {
        mAppUserManager.saveUser(user);
        mUser = user;
    }

    public Route getUser() {
        if (mUser == null) {
            mUser = mAppUserManager.getUser();
        }
        return mUser;
    }

    public void setSession(LoginResponse loginResponse) {
        mAppSessionManager.saveSession(loginResponse);
        mLoginResponse = loginResponse;
    }

    public boolean isUserLogin() {
        if (mLoginResponse == null) {
            return false;
        }
        return true;
    }

    public String getmAuthID() {
        if (mLoginResponse == null) {
            startLogin();
            return null;
        }
        return mLoginResponse.getmToken();
    }

    public void onLogout() {
        clearSession();
        clearUser();
        startLogin();
    }

    public void startLogin() {
        Intent i = new Intent(mApplication.getBaseContext(), LoginActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        mApplication.getBaseContext().startActivity(i);
    }

    private void clearUser() {
        mAppUserManager.clearUser();
        mUser = null;
    }

    private void clearSession() {
        mAppSessionManager.clearSession();
        mLoginResponse = null;
    }

    public List<Student> getStudents() {
        return getSession().getmStudents();
    }

    public static String getDeviceID() {
        return Settings.Secure.getString(getApplication().getContentResolver(),
                Settings.Secure.ANDROID_ID);
    }

    public boolean isMorningRoute() {
        return (getUser().getmMorningEvening().equalsIgnoreCase("m"));

    }
    public boolean isEveningRoute() {
        return (getUser().getmMorningEvening().equalsIgnoreCase("e"));

    }

/*    public Route getRoute() {
        Route route = new Route();
        route.setRouteid(getSession().get().get(0).getRouteid());
       return return route;
    }*/
}
