package com.basusingh.nsspgdav.fragments;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.ContactsContract;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.basusingh.nsspgdav.R;
import com.basusingh.nsspgdav.activity.ImageViewer;
import com.basusingh.nsspgdav.adapter.adapter_team_member;
import com.basusingh.nsspgdav.helper.Constants;
import com.basusingh.nsspgdav.helper.ObjectTeamMember;
import com.basusingh.nsspgdav.utils.AppController;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Basu Singh on 10/5/2016.
 */
public class TeamMemberFragment extends Fragment {

    TextView error_text;
    RecyclerView recyclerView;
    List<ObjectTeamMember> list;
    adapter_team_member adapter;
    String tag_str_req;
    ProgressBar progressBar;
    Button btn_reload;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savesInstanceState){
        View v = inflater.inflate(R.layout.fragment_team_member, parent, false);

        error_text = (TextView) v.findViewById(R.id.error_text);
        progressBar = (ProgressBar) v.findViewById(R.id.progressBar);
        recyclerView = (RecyclerView) v.findViewById(R.id.recyclerView);
        if(recyclerView != null){
            recyclerView.setHasFixedSize(true);
            recyclerView.setNestedScrollingEnabled(false);
        }
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setNestedScrollingEnabled(false);
        list = new ArrayList<>();

        v.findViewById(R.id.president_layout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showOfficeBearerDialog(1);
            }
        });

        v.findViewById(R.id.vice_president_layout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showOfficeBearerDialog(2);
            }
        });

        v.findViewById(R.id.secretary_layout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showOfficeBearerDialog(3);
            }
        });

        v.findViewById(R.id.joint_secretary).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showOfficeBearerDialog(4);
            }
        });

        v.findViewById(R.id.treasure_layout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showOfficeBearerDialog(5);
            }
        });

        fetchData();

        btn_reload = (Button) v.findViewById(R.id.btn_reload);
        btn_reload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                error_text.setVisibility(View.GONE);
                btn_reload.setVisibility(View.GONE);
                fetchData();
            }
        });
        return v;
    }




    public void showOfficeBearerDialog(final int pos){

        String mName = "", mMobile = "", mEmail = "", mPost = "", mFbUrl = "", mInstaUrl = "", mCourse = "", mYear = "";
            int mImageUrl = R.mipmap.ic_launcher;
        switch (pos){
            case 1:
                mName = "Anupam Gaurav";
                mMobile = "8586084733";
                mEmail = "anupam@nsspgdavcollege.com";
                mPost = "President";
                mImageUrl = R.drawable.president_small;
                mFbUrl = "http://www.facebook.com/anupam.gaurav10";
                mInstaUrl = "http://www.instagram.com/gauravanupam";
                mCourse = "B.com Program";
                break;
            case 2:
                mName = "Parveen Dhiman";
                mMobile = "8569902009";
                mEmail = "parveen@nsspgdavcollege.com";
                mPost = "Vice President";
                mImageUrl = R.drawable.vice_president_small;
                mFbUrl = "http://www.facebook.com/parveen.dhiman.338211";
                mInstaUrl = "http://www.instagram.com/parveen_dhiman_";
                mCourse = "B.com Program";
                break;
            case 3:
                mName = "Shaloo Bhansali";
                mMobile = "9540561965";
                mEmail = "shaloo@nsspgdavcollege.com";
                mPost = "Secretary";
                mImageUrl = R.drawable.secretary_small;
                mFbUrl = "http://www.facebook.com/shaloo.bhansali";
                mInstaUrl = "http://www.instagram.com/shaloobhansali";
                mCourse = "B.com program";
                break;
            case 4:
                mName = "Khushboo Goel";
                mMobile = "8901146800";
                mEmail = "khushboo@nsspgdavcollege.com";
                mPost = "Join-Secretary";
                mImageUrl = R.drawable.joint_secretary_small;
                mFbUrl = "http://www.facebook.com/khushboo.goel.338";
                mInstaUrl = "http://www.instagram.com/_khushboo_goel";
                mCourse = "B.com Program";
                break;
            case 5:
                mName = "Diksha Bhutani";
                mMobile = "9873165751";
                mEmail = "diksha@nsspgdavcollege.com";
                mPost = "Treasurer";
                mImageUrl = R.drawable.treasurer_small;
                mFbUrl = "http://www.facebook.com/dikshabhutani";
                mInstaUrl = "http://www.instagram.com/diksha_28";
                mCourse = "Bsc (Honors) statistics";
                break;
        }


        LayoutInflater li = LayoutInflater.from(getContext());
        final View view = li.inflate(R.layout.item_team_member_detail, null);

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                getContext());
        alertDialogBuilder.setView(view);

        TextView name = (TextView) view.findViewById(R.id.name);
        TextView mobile = (TextView) view.findViewById(R.id.mobile);
        TextView post = (TextView) view.findViewById(R.id.post);
        TextView course = (TextView) view.findViewById(R.id.course);

        ImageView image = (ImageView) view.findViewById(R.id.image);
        ImageView insta_image = (ImageView) view.findViewById(R.id.insta_image);
        ImageView facebook_image = (ImageView) view.findViewById(R.id.facebook_image);
        ImageView email_image = (ImageView) view.findViewById(R.id.email_image);

        try{

            name.setText(mName);
            mobile.setText(mMobile);
            post.setText(mPost);
            course.setText(mCourse);

            image.setImageResource(mImageUrl);
            image.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    openImageViewer("core", String.valueOf(pos));
                }
            });


            final String getEmail = mEmail;
            final String getFb = mFbUrl;
            final String getName = mName;
            final String getMobile = mMobile;
            final String getInsta = mInstaUrl;

            insta_image.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    try{
                        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(getInsta));
                        startActivity(browserIntent);
                    } catch (Exception e){
                        e.printStackTrace();
                    }
                }
            });

            facebook_image.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    try{
                        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(getFb));
                        startActivity(browserIntent);
                    } catch (Exception e){
                        e.printStackTrace();
                    }
                }
            });


            email_image.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    try{
                        Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                                "mailto", getEmail, null));
                        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "");
                        emailIntent.putExtra(Intent.EXTRA_TEXT, "");
                        startActivity(Intent.createChooser(emailIntent, "Send email..."));
                    } catch (Exception e){
                        e.printStackTrace();
                    }
                }
            });


            LinearLayout mobile_layout = (LinearLayout) view.findViewById(R.id.mobile_layout);
            mobile_layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    showMobileInterfaceDialog(getMobile, getName, getEmail);
                }
            });

            mobile_layout.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    if(Build.VERSION.SDK_INT>= Build.VERSION_CODES.HONEYCOMB){
                        android.content.ClipboardManager clipboard = (android.content.ClipboardManager) getActivity().getSystemService(Context.CLIPBOARD_SERVICE);
                        android.content.ClipData clip = android.content.ClipData.newPlainText("Mobile Number", getMobile);
                        clipboard.setPrimaryClip(clip);
                        Toast.makeText(getContext(), "Mobile Number copied", Toast.LENGTH_SHORT).show();
                    }
                    return false;
                }
            });


        } catch (Exception e){
            e.printStackTrace();
        }

        alertDialogBuilder.setPositiveButton("Done", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });


        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }




    public void openImageViewer(String type, String data){
        Intent i = new Intent(getContext(), ImageViewer.class);
        i.putExtra("type", type);
        i.putExtra("data", data);
        startActivity(i);
        getActivity().overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
    }



    public void fetchData(){
        progressBar.setVisibility(View.VISIBLE);
        try{
            tag_str_req = "teamMember_search_req";

            StringRequest strReq = new StringRequest(Request.Method.POST,
                    Constants.SERVER_BASE_URL + "teammember", new Response.Listener<String>() {

                @Override
                public void onResponse(String response) {
                    Log.d("Response", response);
                    check_search_response(response);
                }
            }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {
                    VolleyLog.d(tag_str_req, "Error: " + error.getMessage());
                    setErrorLayoutVisible("Internet connectivity issue.");
                }
            });

            strReq.setRetryPolicy(new DefaultRetryPolicy(0, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

            AppController.getInstance().addToRequestQueue(strReq, tag_str_req);

        } catch (Exception e){
            e.printStackTrace();
            setErrorLayoutVisible("An error occurred.");
        }
    }


    public void check_search_response(String response){
        String error, message;

        try {

            JSONObject jsonObj = new JSONObject(response);
            error = jsonObj.getString("error");
            message = jsonObj.getString("message");

            if(error.equals("true")){
                setErrorLayoutVisible("An error occurred.");
            } else if(error.equals("false")) {
                add_data(message);
            }

        } catch (JSONException e) {
            e.printStackTrace();
            Log.d("JSON Parse error ", e.toString());
            Log.d("Response causing error", response);
            setErrorLayoutVisible("An error occurred.");

        }
    }


    public void add_data(final String data){
        try{
            new AsyncTask<String, String, String>(){
                @Override
                protected String doInBackground(String... params){
                    try{
                        JSONArray a = new JSONArray(data);
                        for(int i = 0; i<a.length(); i++){
                            JSONObject o = a.getJSONObject(i);
                            ObjectTeamMember on = new ObjectTeamMember();
                            on.setId(o.getString("id"));
                            on.setMobile(o.getString("mobile"));
                            on.setImageUrl(o.getString("imageurl"));
                            on.setName(o.getString("name"));
                            on.setCourseDetail(o.getString("course"));
                            on.setEmail(o.getString("email"));
                            on.setFbUrl(o.getString("fburl"));
                            on.setInstaUrl(o.getString("instaurl"));
                            on.setPost(o.getString("post"));
                            list.add(on);
                        }

                        if(!list.isEmpty()){
                            adapter = new adapter_team_member(list, getContext());
                        }
                    } catch (JSONException e){
                        e.printStackTrace();
                    }
                    return null;
                }
                @Override
                public void onPostExecute(String data){
                    try{
                        if(!list.isEmpty()){
                            recyclerView.setAdapter(adapter);
                            new Handler().post(new Runnable() {
                                @Override
                                public void run() {
                                    progressBar.setVisibility(View.GONE);
                                    error_text.setVisibility(View.GONE);
                                    btn_reload.setVisibility(View.GONE);
                                    recyclerView.setVisibility(View.VISIBLE);
                                    setUpRecyclerItemClick();
                                }
                            });
                        } else{
                            setErrorLayoutVisible("No Team Member found.");
                        }
                    } catch (Exception e){
                        e.printStackTrace();
                        setErrorLayoutVisible("An error occurred.");
                    }
                }
            }.execute();
        } catch (Exception e){
            e.printStackTrace();
            setErrorLayoutVisible("An error occurred.");
        }
    }

    public void setUpRecyclerItemClick(){
        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getContext(), recyclerView, new ClickListener() {
            @Override
            public void onClick(View view, int position) {
                ObjectTeamMember o = list.get(position);
                showTeamDetailDialog(o);
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));
    }


    public void showTeamDetailDialog(final ObjectTeamMember o){

        LayoutInflater li = LayoutInflater.from(getContext());
        final View view = li.inflate(R.layout.item_team_member_detail, null);

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                getContext());
        alertDialogBuilder.setView(view);

        TextView name = (TextView) view.findViewById(R.id.name);
        TextView mobile = (TextView) view.findViewById(R.id.mobile);
        TextView post = (TextView) view.findViewById(R.id.post);
        TextView course = (TextView) view.findViewById(R.id.course);

        ImageView image = (ImageView) view.findViewById(R.id.image);
        ImageView insta_image = (ImageView) view.findViewById(R.id.insta_image);
        ImageView facebook_image = (ImageView) view.findViewById(R.id.facebook_image);
        ImageView email_image = (ImageView) view.findViewById(R.id.email_image);

        try{

            name.setText(o.getName());
            mobile.setText(o.getMobile());
            post.setText(o.getPost());
            course.setText(o.getCourseDetail());

            Picasso.with(getContext()).load(o.getImageUrl()).placeholder(getResources().getDrawable(R.drawable.loading_picasso)).error(getResources().getDrawable(R.drawable.loading_picasso)).into(image);
            image.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    openImageViewer("office", o.getImageUrl());
                }
            });

            insta_image.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    try{
                        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(o.getInstaUrl()));
                        startActivity(browserIntent);
                    } catch (Exception e){
                        e.printStackTrace();
                    }
                }
            });

            facebook_image.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    try{
                        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(o.getFbUrl()));
                        startActivity(browserIntent);
                    } catch (Exception e){
                        e.printStackTrace();
                    }
                }
            });

            email_image.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    try{
                        Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                                "mailto", o.getEmail(), null));
                        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "");
                        emailIntent.putExtra(Intent.EXTRA_TEXT, "");
                        startActivity(Intent.createChooser(emailIntent, "Send email..."));
                    } catch (Exception e){
                        e.printStackTrace();
                    }
                }
            });


            LinearLayout mobile_layout = (LinearLayout) view.findViewById(R.id.mobile_layout);
            mobile_layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    showMobileInterfaceDialog(o.getMobile(), o.getName(), o.getEmail());
                }
            });

            mobile_layout.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    if(Build.VERSION.SDK_INT>= Build.VERSION_CODES.HONEYCOMB){
                        android.content.ClipboardManager clipboard = (android.content.ClipboardManager) getActivity().getSystemService(Context.CLIPBOARD_SERVICE);
                        android.content.ClipData clip = android.content.ClipData.newPlainText("Mobile Number", o.getMobile());
                        clipboard.setPrimaryClip(clip);
                        Toast.makeText(getContext(), "Mobile Number copied", Toast.LENGTH_SHORT).show();
                    }
                    return false;
                }
            });


        } catch (Exception e){
            e.printStackTrace();
        }

        alertDialogBuilder.setPositiveButton("Done", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });


        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }


    public void showMobileInterfaceDialog(final String mobile, final String name, final String email){

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getContext());
        alertDialogBuilder.setMessage("Please choose a option");

        alertDialogBuilder.setPositiveButton("Call", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface arg0, int arg1) {
                try{
                    Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + mobile));
                    startActivity(intent);
                } catch (SecurityException e){
                    e.printStackTrace();
                    Toast.makeText(getContext(), "Sorry, this app don't have permission to call. Long press to copy number", Toast.LENGTH_SHORT).show();
                }
            }
        });

        alertDialogBuilder.setNegativeButton("Message", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                try{
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("sms:" + mobile));
                    intent.putExtra("sms_body", "");
                    startActivity(intent);
                } catch (Exception e){
                    e.printStackTrace();
                }
            }
        });


        alertDialogBuilder.setNeutralButton("Add to Contact", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                try{
                    Intent intent = new Intent(ContactsContract.Intents.Insert.ACTION);
                    intent.setType(ContactsContract.RawContacts.CONTENT_TYPE);
                    intent.putExtra(ContactsContract.Intents.Insert.NAME, name);
                    intent.putExtra(ContactsContract.Intents.Insert.EMAIL, email);
                    intent.putExtra(ContactsContract.Intents.Insert.PHONE, mobile);
                    startActivity(intent);
                } catch (Exception e){
                    e.printStackTrace();
                    Toast.makeText(getContext(), "Oops, an error occurred", Toast.LENGTH_SHORT).show();
                }
            }
        });



        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }


    public void setErrorLayoutVisible(String msg){
        error_text.setText(msg);
        new Handler().post(new Runnable() {
            @Override
            public void run() {
                progressBar.setVisibility(View.GONE);
                recyclerView.setVisibility(View.GONE);
                error_text.setVisibility(View.VISIBLE);
                btn_reload.setVisibility(View.VISIBLE);
            }
        });
    }


    public interface ClickListener {
        void onClick(View view, int position);

        void onLongClick(View view, int position);
    }


    public static class RecyclerTouchListener implements RecyclerView.OnItemTouchListener {

        private GestureDetector gestureDetector;
        private TeamMemberFragment.ClickListener clickListener;

        public RecyclerTouchListener(Context context, final RecyclerView recyclerView, final TeamMemberFragment.ClickListener clickListener) {
            this.clickListener = clickListener;
            gestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {
                @Override
                public boolean onSingleTapUp(MotionEvent e) {
                    return true;
                }

                @Override
                public void onLongPress(MotionEvent e) {
                    View child = recyclerView.findChildViewUnder(e.getX(), e.getY());
                    if (child != null && clickListener != null) {
                        clickListener.onLongClick(child, recyclerView.getChildPosition(child));
                    }
                }
            });
        }

        @Override
        public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {

            View child = rv.findChildViewUnder(e.getX(), e.getY());
            if (child != null && clickListener != null && gestureDetector.onTouchEvent(e)) {
                clickListener.onClick(child, rv.getChildPosition(child));
            }
            return false;
        }

        @Override
        public void onTouchEvent(RecyclerView rv, MotionEvent e) {
        }

        @Override
        public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

        }
    }



}
