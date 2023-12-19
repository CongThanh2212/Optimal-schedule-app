package firebase.gopool.api;

import firebase.gopool.model.Trip;
import firebase.gopool.model.Waypoint;
import firebase.gopool.model.request.BookOnlineRequest;
import firebase.gopool.model.request.MessageResponse;
import firebase.gopool.model.request.PasswordRequest;
import firebase.gopool.model.request.ShareRequest;
import firebase.gopool.model.request.SignupRequest;
import firebase.gopool.model.response.DriverResponse;
import firebase.gopool.model.response.FrequentResponse;
import firebase.gopool.model.response.LoginResponse;
import firebase.gopool.model.response.PassengerResponse;
import firebase.gopool.model.response.RideResponse;
import firebase.gopool.model.response.ScheduleResponse;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface API {

    Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();

    API api = new Retrofit.Builder()
            .baseUrl("http://192.168.0.19:8081") // Physical device
//            .baseUrl("http://192.168.55.217:8081")
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
            .create(API.class);

    @GET("/auth/login")
    Call<LoginResponse> login(@Query("email") String email, @Query("password") String password);

    @GET("/auth/info_driver")
    Call<DriverResponse> infoDriver(@Header("Authorization") String authorizationHeader);

    @GET("/auth/info_passenger")
    Call<PassengerResponse> infoPassenger(@Header("Authorization") String authorizationHeader);

    @POST("/auth/change_info_driver")
    Call<MessageResponse> changeInfoDriver(@Header("Authorization") String authorizationHeader, @Body DriverResponse driver);

    @POST("/auth/change_info_passenger")
    Call<MessageResponse> changeInfoPassenger(@Header("Authorization") String authorizationHeader, @Body PassengerResponse passenger);

    @GET("/ride")
    Call<ArrayList<RideResponse>> getAllRideByAccountId(@Header("Authorization") String authorizationHeader);

    @GET("/frequent")
    Call<ArrayList<FrequentResponse>> getAllRouteFrequentByAccountId(@Header("Authorization") String authorizationHeader);

    @POST("/share/share_frequent")
    Call<MessageResponse> shareFrequent(@Header("Authorization") String authorizationHeader, @Body ShareRequest shareRequest);

    @POST("/insert/prune_greedy_linear_dp")
    Call<RideResponse> bookingLinear(@Header("Authorization") String authorizationHeader,
                                    @Body BookOnlineRequest bookOnline);

    @POST("share/trip")
    Call<Trip> addTrip(@Header("Authorization") String authorizationHeader,
                       @Body Trip trip);

    @POST("share/waypoint")
    Call<Waypoint> addWaypoint(@Header("Authorization") String authorizationHeader,
                               @Body Waypoint waypoint);

    @POST("auth/signup_passenger")
    Call<MessageResponse> signupPassenger(@Body SignupRequest passenger);

    @GET("auth/create_otp")
    Call<MessageResponse> requireForget(@Query("email") String email);

    @GET("auth/verify_otp")
    Call<MessageResponse> sendOTP(@Query("otp") int otp, @Query("email") String email);

    @POST("auth/change_password")
    Call<LoginResponse> changePassword(@Body PasswordRequest passwordRequest);

    @GET("schedule/driver")
    Call<ArrayList<ScheduleResponse>> scheduleOfDriver(@Header("Authorization") String authorizationHeader);

}
