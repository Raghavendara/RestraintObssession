package com.raghav.restraintobsession.listeners;

import com.raghav.restraintobsession.models.User;

public interface UsersListener {

    void initiateVideoMeeting(User user);

    void initiateAudioMeeting(User user);

    void onMultipleUsersAction(Boolean isMultipleUsersSelected);


}
