package app.mjordan.projectfrs;

import retrofit2.Call;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

interface ApiConfig {
    @Multipart
    @POST("FRS/user/upload_image.php")
    Call<ServerResponse> uploadFile(@Part MultipartBody.Part file);
}
