package com.basusingh.nsspgdav.fcm;

import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.widget.Toast;

import com.basusingh.nsspgdav.activity.BlogViewer;
import com.basusingh.nsspgdav.activity.NewsView;
import com.basusingh.nsspgdav.database_preference.AppData;
import com.basusingh.nsspgdav.database_preference.TableController_Event;
import com.basusingh.nsspgdav.database_preference.UserPreference;
import com.basusingh.nsspgdav.helper.Constants;
import com.basusingh.nsspgdav.helper.ObjectEvents;
import com.basusingh.nsspgdav.utils.NotificationUtils;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import org.json.JSONObject;

import java.util.Map;

/**
 * Created by Basu Singh on 10/10/2016.
 */
public class MessagingService extends FirebaseMessagingService {


    NotificationUtils notificationUtils;

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        notificationUtils = new NotificationUtils(getApplicationContext());
        Log.d("Notification : ", remoteMessage.getData().toString());
        String type;
        Map<String, String> params = remoteMessage.getData();

        try{
            JSONObject o = new JSONObject(params);
            type = o.getString("notification_type");
            switch (type){
                case "top_post":

                    String image1 = o.getString("image1");
                    String image2 = o.getString("image2");
                    String image3 = o.getString("image3");
                    String image4 = o.getString("image4");
                    String image5 = o.getString("image5");
                    updateTopPost(image1, image2, image3, image4, image5);
                    break;

                case "thought":
                    String thought = o.getString("thought");
                    String tId = o.getString("id");
                    showThought(tId, thought);
                    break;

                case "news":
                    String id = o.getString("id");
                    String heading = o.getString("heading");
                    String description = o.getString("description");
                    String mType = o.getString("type");
                    String imageurl = o.getString("imageurl");
                    String time_stamp = o.getString("time_stamp");
                    showNews(id, heading, description, mType, imageurl, time_stamp);

                    break;

                case "blog":
                    showBlog(o.getString("id"), o.getString("heading"), o.getString("description"), o.getString("imageurl"), o.getString("image1"), o.getString("image2"), o.getString("sub_description"), o.getString("time_stamp"));
                    break;

                case "app_update":
                    String version = o.getString("version");
                    new UserPreference(getApplicationContext()).setUpdateVersionRequired(version);
                    break;

                case "popup_dialog":
                    show_popup_dialog(o.getString("imageurl"), o.getString("heading"), o.getString("description"));
                    break;

            }
        } catch (Exception e){
            e.printStackTrace();
        }
    }


    public void show_popup_dialog(String imageurl, String heading, String description){
        Intent intent = new Intent(Constants.LBM_POPUP_FESTIVAL);
        intent.putExtra("heading", heading);
        intent.putExtra("description", description);
        intent.putExtra("imageurl", imageurl);
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
    }

    public void updateTopPost(String one, String two, String three, String four, String five){
        new AppData(getApplicationContext()).updateTopImages(one, two, three, four, five);
    }

    public void showThought(String id, String thought){
        notificationUtils.showThought(id, thought);
    }

    public void showNews(String id, String heading, String description, String type, String imageurl, String time_stamp){
        try{
            ObjectEvents o = new ObjectEvents();
            o.setId(id);
            o.setHeading(heading);
            o.setDescription(description);
            o.setTimeStamp(time_stamp);
            o.setType(type);
            o.setImageUrl(imageurl);
            new TableController_Event(getApplicationContext()).addSingleData(o);
        } catch (Exception e){
            e.printStackTrace();
        }
        Intent i = new Intent(getApplicationContext(), NewsView.class);
        i.putExtra("id", id);
        i.putExtra("heading", heading);
        i.putExtra("description", description);
        i.putExtra("time_stamp", time_stamp);
        i.putExtra("type", type);
        i.putExtra("imageurl", imageurl);
        notificationUtils.showEventNotification(heading, description, i);
    }

    public void showBlog(String id, String imageurl, String heading, String description, String image1, String image2, String sub_description, String time_stamp){
        Intent i = new Intent(getApplicationContext(), BlogViewer.class);
        i.putExtra("id", id);
        i.putExtra("heading", heading);
        i.putExtra("description", description);
        i.putExtra("imageurl", imageurl);
        i.putExtra("image1", image1);
        i.putExtra("image2", image2);
        i.putExtra("sub_description", sub_description);
        i.putExtra("time_stamp", time_stamp);
        notificationUtils.showBlogNotification(heading, description, i);
    }

}
