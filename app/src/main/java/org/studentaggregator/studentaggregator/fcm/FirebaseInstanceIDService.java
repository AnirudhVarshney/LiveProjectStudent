package org.studentaggregator.studentaggregator.fcm;

import org.studentaggregator.studentaggregator.helper.MyUtils;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;


public class FirebaseInstanceIDService extends FirebaseInstanceIdService {

    @Override
    public void onTokenRefresh() {

        String token = FirebaseInstanceId.getInstance().getToken();
        MyUtils.logThis("FirebaseInstanceIDService - Token = " + token);

        //registerToken(token);
    }


}

/*
private void registerToken(String token) {
        MyUtils.logThis("token = " + token);
        OkHttpClient client = new OkHttpClient();
        RequestBody body = new FormBody.Builder()
                .add("Token", token)
                .build();

        Request request = new Request.Builder()
                .url("http://192.168.43.224/fcm/register.php")
                .post(body)
                .build();


        try {
            client.newCall(request).execute();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
*/
