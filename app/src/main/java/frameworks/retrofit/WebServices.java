package frameworks.retrofit;import java.util.List;import retrofit2.Call;import retrofit2.http.Body;import retrofit2.http.POST;import transport.school.com.schoolapp.bean.ActiveRouteReply;import transport.school.com.schoolapp.bean.AttendanceRecord;import transport.school.com.schoolapp.bean.AttendanceUpdateResponse;import transport.school.com.schoolapp.bean.DeviceTokenRegister;import transport.school.com.schoolapp.bean.LocationUpdateReply;import transport.school.com.schoolapp.bean.LocationUpdateRequest;import transport.school.com.schoolapp.bean.LoginRequest;import transport.school.com.schoolapp.bean.LoginResponse;import transport.school.com.schoolapp.bean.Route;import transport.school.com.schoolapp.bean.RouteReply;import transport.school.com.schoolapp.bean.RouteStudentList;import transport.school.com.schoolapp.bean.Stop;import transport.school.com.schoolapp.bean.StopResponse;public interface WebServices {    @POST("?device=phone&api=login")    public Call<LoginResponse> login(@Body LoginRequest loginRequest);    @POST("?device=phone&api=routestudents")    public Call<RouteStudentList> getStudentListForRoute(@Body Route route);    @POST("?device=phone&api=attendance")    public Call<AttendanceUpdateResponse> postStudentAttendence(@Body AttendanceRecord attendanceRecord);    @POST("?device=phone&api=absent")    public Call<AttendanceUpdateResponse> postStudentAbsent(@Body AttendanceRecord attendanceRecord);    @POST("?device=phone&api=stoproute")    public Call<RouteReply> stopRoute(@Body Route route);    @POST("?device=phone&api=startroute")    public Call<RouteReply> startRoute(@Body Route route);    @POST("?device=phone&api=activeroute")    public Call<ActiveRouteReply> getActiveRoute(@Body Route route);    @POST("?device=phone&api=getlocation")    public Call<LocationUpdateRequest> getlocation(@Body Route route);    @POST("?device=phone&api=getattendance")    public Call<RouteStudentList> getAttendence(@Body AttendanceRecord attendanceRecord);    @POST("?device=phone&api=route")    public Call<StopResponse> getRoute(@Body Stop stop);    @POST("?device=phone&api=device")    public Call<String> postToken(@Body DeviceTokenRegister deviceTokenRegister);}