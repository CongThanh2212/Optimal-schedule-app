package firebase.gopool.MapRemote;

import firebase.gopool.model.request.Sender;
import firebase.gopool.model.response.FCMResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface IFCMService {
    @Headers({
            "Content-Type:application/json",
            "Authorization:key=AAAAI4ylros:APA91bE0EH5ero7wDmR5ZGjXwQWmDMdSWfdcUjjvRXdZGJU5QYDj85xu2qfSYPw3kjadFUjBOvG6zwlyezaxNcz9xXvspsYSRkWC2QRv-aAi9jzDFWh8zIIthjFmr-kC-fm2wHVGcchk"
    })
    @POST("fcm/send")
    Call<FCMResponse> sendMessage(@Body Sender body);
}
