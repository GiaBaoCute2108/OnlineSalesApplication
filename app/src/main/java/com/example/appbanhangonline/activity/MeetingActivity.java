package com.example.appbanhangonline.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.appbanhangonline.R;
import com.example.appbanhangonline.fragment.SpeakerFragment;
import com.example.appbanhangonline.fragment.ViewerFragment;

import live.videosdk.rtc.android.Meeting;
import live.videosdk.rtc.android.VideoSDK;
import live.videosdk.rtc.android.listeners.MeetingEventListener;

public class MeetingActivity extends AppCompatActivity {
    private Meeting meeting;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meeting);

        final String meetingId = getIntent().getStringExtra("meetingId");
        String token = getIntent().getStringExtra("token");
        String mode = getIntent().getStringExtra("mode");
        String localParticipantName = "John Doe";
        boolean streamEnable = mode.equals("CONFERENCE");

        // initialize VideoSDK
        VideoSDK.initialize(getApplicationContext());

        // Configuration VideoSDK with Token
        VideoSDK.config(token);

        // Initialize VideoSDK Meeting
        meeting = VideoSDK.initMeeting(
                MeetingActivity.this, meetingId, localParticipantName,
                streamEnable, streamEnable, null, mode, false, null);

        // join Meeting
        meeting.join();

        // if mode is CONFERENCE than replace mainLayout with SpeakerFragment otherwise with ViewerFragment
        meeting.addEventListener(new MeetingEventListener() {
            @Override
            public void onMeetingJoined() {
                if (meeting != null) {
                    if (mode.equals("CONFERENCE")) {
                        //pin the local participant
                        meeting.getLocalParticipant().pin("SHARE_AND_CAM");
                        getSupportFragmentManager()
                                .beginTransaction()
                                .replace(R.id.mainLayout, new SpeakerFragment(), "MainFragment")
                                .commit();
                    } else if (mode.equals("VIEWER")) {
                        getSupportFragmentManager()
                                .beginTransaction()
                                .replace(R.id.mainLayout, new ViewerFragment(), "viewerFragment")
                                .commit();
                    }
                }
            }
        });
    }

    public Meeting getMeeting() {
        return meeting;
    }
}