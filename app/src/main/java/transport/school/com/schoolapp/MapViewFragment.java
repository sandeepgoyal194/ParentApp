package transport.school.com.schoolapp;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.akexorcist.googledirection.DirectionCallback;
import com.akexorcist.googledirection.GoogleDirection;
import com.akexorcist.googledirection.constant.TransportMode;
import com.akexorcist.googledirection.model.Direction;
import com.akexorcist.googledirection.model.Leg;
import com.akexorcist.googledirection.util.DirectionConverter;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.maps.android.ui.IconGenerator;

import java.util.ArrayList;
import java.util.List;

import frameworks.locationmanager.LocationManagerService;
import frameworks.locationmanager.Locations;
import frameworks.retrofit.ResponseResolver;
import frameworks.retrofit.RestError;
import frameworks.retrofit.WebServicesWrapper;
import retrofit2.Response;
import transport.school.com.schoolapp.bean.LocationUpdateRequest;
import transport.school.com.schoolapp.bean.Route;
import transport.school.com.schoolapp.bean.Routestop;
import transport.school.com.schoolapp.bean.Stop;
import transport.school.com.schoolapp.bean.StopResponse;
import transport.school.com.schoolapp.bean.Student;

import static transport.school.com.schoolapp.Constants.ZOOM_LEVEL_STREETS;
public class MapViewFragment extends Fragment implements GoogleMap.OnMarkerClickListener {
    private static final String TAG = "SchoolApp";
    MapView mMapView;
    private GoogleMap googleMap;
    private MarkerOptions mMarkerOptions = null;
    private Marker mMarker = null;
    IconGenerator iconFactory;// = new IconGenerator(this);
    Handler h = new Handler();

    public static MapViewFragment newInstance(Student student) {
        MapViewFragment fragment = new MapViewFragment();
        return fragment;
    }

    Student student;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.location_fragment, container, false);
        mMapView = (MapView) rootView.findViewById(R.id.mapView);
        mMapView.onCreate(savedInstanceState);
        mMapView.onResume();
        iconFactory = new IconGenerator(getContext());// needed to get the map to display immediately
        try {
            MapsInitializer.initialize(getActivity().getApplicationContext());
        } catch (Exception e) {
            e.printStackTrace();
        }
        mMapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap mMap) {
                googleMap = mMap;
                googleMap.setOnMarkerClickListener(MapViewFragment.this);
                mMarkerOptions = null;
//                AppBaseApplication.getApplication().getSession().getTeacher().get(0).
            }
        });
        return rootView;
    }

    public void setStudent(Student student) {
        this.student = student;
        Stop stop = new Stop();
        stop.setStopid(student.getStopid());
        WebServicesWrapper.getInstance().getRoute(stop, new ResponseResolver<StopResponse>() {
            @Override
            public void onSuccess(StopResponse stopResponse, Response response) {
                List<Routestop> routestops = stopResponse.getRoutestops();
                drawRoute(routestops);
                h.postDelayed(locationCHanger,0);
            }

            @Override
            public void onFailure(RestError error, String msg) {
            }
        });
    }

    public void drawRoute(final List<Routestop> list) {
        final List<LatLng> latLngs = new ArrayList<>();
        for (Routestop routestop : list) {
            latLngs.add(new LatLng(Double.parseDouble(routestop.getLatitude()), Double.parseDouble(routestop.getLongitude())));
        }
        final LatLng origin = new LatLng(latLngs.get(0).latitude, latLngs.get(0).longitude);
        final LatLng destination = new LatLng(latLngs.get(list.size() - 1).latitude, latLngs.get(list.size() - 1).longitude);
        GoogleDirection.withServerKey("AIzaSyAUmVRXx43uVLZomeU1tRR5OYYkGuW6bew")
                .from(origin)
                .and(latLngs)
                .to(destination)
                .transportMode(TransportMode.DRIVING)
                .execute(new DirectionCallback() {
                    @Override
                    public void onDirectionSuccess(Direction direction, String rawBody) {
                        int i = 0;
                        if (direction.isOK()) {
                            com.akexorcist.googledirection.model.Route route = direction.getRouteList().get(0);
                            iconFactory.setColor(Color.GREEN);
                            addIcon(iconFactory, list.get(i).getStopname(), list.get(i++).getStopid(), origin);
                            iconFactory.setColor(Color.RED);
                            addIcon(iconFactory, list.get(list.size() - 1).getStopname(), list.get(list.size() - 1).getStopid(), destination);
                            for (LatLng position : latLngs.subList(1, list.size() - 1)) {
                                iconFactory.setColor(Color.BLUE);
                                addIcon(iconFactory, list.get(i).getStopname(), list.get(i++).getStopid(), position);
                            }
                            for (Leg leg : route.getLegList()) {
                                //List<Step> stepList = leg.getStepList();
                                PolylineOptions polylineOptions = DirectionConverter.createPolyline(getContext(), leg.getDirectionPoint(), 5, Color.RED);
                                googleMap.addPolyline(polylineOptions);
                            }
                            setCameraWithCoordinationBounds(route);
                        } else {
                        }
                    }

                    @Override
                    public void onDirectionFailure(Throwable t) {
                        // Do something
                    }
                });
    }

    private void addIcon(IconGenerator iconFactory, String title, CharSequence text, LatLng position) {
        MarkerOptions markerOptions = new MarkerOptions().
                icon(BitmapDescriptorFactory.fromBitmap(iconFactory.makeIcon(title))).
                position(position).
                anchor(iconFactory.getAnchorU(), iconFactory.getAnchorV());
        googleMap.addMarker(markerOptions).setTag(text);
    }

    private void setCameraWithCoordinationBounds(com.akexorcist.googledirection.model.Route route) {
        LatLng southwest = route.getBound().getSouthwestCoordination().getCoordination();
        LatLng northeast = route.getBound().getNortheastCoordination().getCoordination();
        LatLngBounds bounds = new LatLngBounds(southwest, northeast);
        googleMap.animateCamera(CameraUpdateFactory.newLatLngBounds(bounds, 100));
    }

    void setLocationOnMap(Locations location, float bearing) {
        if (googleMap == null) {
            Log.i(TAG, "Map is null");
            return;
        }
        LatLng currentLocationLatLong = new LatLng(location.getLattitude(), location.getLongitude());
        if (mMarkerOptions == null || !mMarker.isVisible()) {
            mMarkerOptions = new MarkerOptions().icon(getCarMapIcon(R.drawable.car_icon)).rotation(bearing).position(currentLocationLatLong);
            mMarker = googleMap.addMarker(mMarkerOptions);
        } else if (!mMarker.getPosition().equals(currentLocationLatLong)) {
            mMarker.setPosition(currentLocationLatLong);
        }
        //setZoomLevel(currentLocationLatLong);
    }

    public void setZoomLevel(LatLng currentLocationLatLong) {
        googleMap.moveCamera(CameraUpdateFactory.zoomTo(ZOOM_LEVEL_STREETS));
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(currentLocationLatLong));
    }

    @Override
    public void onResume() {
        super.onResume();
        mMapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mMapView.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mMapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mMapView.onLowMemory();
    }

    public void onLocationChanged(Location location) {
        float bearing = 0.0f;
        if(prevLocation != null ) {
            bearing = prevLocation.bearingTo(location);
        }
        prevLocation = location;
        Locations locations = LocationManagerService.getInstance().getCurrentLocation();
        setLocationOnMap(locations, bearing);
    }

    private BitmapDescriptor getCarMapIcon(int resourceId) {
        Bitmap imageBitmap = BitmapFactory.decodeResource(getResources(), resourceId);
        Bitmap resizedBitmap = Bitmap.createScaledBitmap(imageBitmap, getResources().getDimensionPixelSize(R.dimen.car_marker_width), getResources().getDimensionPixelSize(R.dimen.car_marker_height), false);
        return BitmapDescriptorFactory.fromBitmap(resizedBitmap);
    }

    Location prevLocation = null;
    Runnable locationCHanger = new Runnable() {


        @Override
        public void run() {
            Route route = new Route();
            route.setRouteid(student.getRouteid());
            WebServicesWrapper.getInstance().getlocation(route, new ResponseResolver<LocationUpdateRequest>() {
                @Override
                public void onSuccess(LocationUpdateRequest locationUpdateRequest, Response response) {
                    Location location = new Location("New Location");
                    location.setLatitude(locationUpdateRequest.getLattitude());
                    location.setLongitude(locationUpdateRequest.getLongitude());
                    LocationManagerService.getInstance().setCurrentLocation(location);
                    onLocationChanged(location);
                    h.postDelayed(locationCHanger,5000);
                }

                @Override
                public void onFailure(RestError error, String msg) {
                }
            });
        }
    };

    @Override
    public boolean onMarkerClick(Marker marker) {
        return true;
    }
}