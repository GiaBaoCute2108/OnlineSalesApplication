package com.example.appbanhangonline.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.example.appbanhangonline.R;

import org.json.JSONException;
import org.json.JSONObject;

public class JoinActivity extends AppCompatActivity {
    private String sampleToken ="eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJhcGlrZXkiOiIwNjc1ZTFmMS01N2EwLTQyMmYtOTNjYi1jZDk0YmM4MTRkZDgiLCJwZXJtaXNzaW9ucyI6WyJhbGxvd19qb2luIl0sImlhdCI6MTcxOTk5NzM1NywiZXhwIjoxNzIwNjAyMTU3fQ.zZbmuCtVEk2kCMIl8fK-J4DQyF3UJLfKAi7Y3fnu40Y";
    private static final int PERMISSION_REQ_ID = 22;

    private static final String[] REQUESTED_PERMISSIONS = {
            Manifest.permission.RECORD_AUDIO,
            Manifest.permission.CAMERA,
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join);
        checkSelfPermission(REQUESTED_PERMISSIONS[0], PERMISSION_REQ_ID);
        checkSelfPermission(REQUESTED_PERMISSIONS[1], PERMISSION_REQ_ID);
        final Button btnCreate = findViewById(R.id.btnCreateMeeting);
        final Button btnJoinHost = findViewById(R.id.btnJoinHostMeeting);
        final Button btnJoinViewer = findViewById(R.id.btnJoinViewerMeeting);
        final EditText etMeetingId = findViewById(R.id.etMeetingId);
        @SuppressLint({"MissingInflatedId", "LocalSuppress"}) final Button btnLeave = findViewById(R.id.btnLeaveMeeting);
        btnLeave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent leave = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(leave);
                finish();
            }
        });

        // create meeting and join as Host
        btnCreate.setOnClickListener(v -> createMeeting(sampleToken));

        // Join as Host
        btnJoinHost.setOnClickListener(v -> {
            Intent intent = new Intent(JoinActivity.this, MeetingActivity.class);
            intent.putExtra("token", sampleToken);
            intent.putExtra("meetingId", etMeetingId.getText().toString().trim());
            intent.putExtra("mode", "CONFERENCE");
            startActivity(intent);
        });
    }

    private void createMeeting(String token) {
        // we will make an API call to VideoSDK Server to get a roomId
        AndroidNetworking.post("https://api.videosdk.live/v2/rooms")
                .addHeaders("Authorization", token) //we will pass the token in the Headers
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            // response will contain `roomId`
                            final String meetingId = response.getString("roomId");

                            // starting the MeetingActivity with received roomId and our sampleToken
                            Intent intent = new Intent(JoinActivity.this, MeetingActivity.class);
                            intent.putExtra("token", sampleToken);
                            intent.putExtra("meetingId", meetingId);
                            intent.putExtra("mode", "CONFERENCE");
                            startActivity(intent);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        anError.printStackTrace();
                        Toast.makeText(JoinActivity.this, anError.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void checkSelfPermission(String permission, int requestCode) {
        if (ContextCompat.checkSelfPermission(this, permission) !=
                PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, REQUESTED_PERMISSIONS, requestCode);
        }
    }
}