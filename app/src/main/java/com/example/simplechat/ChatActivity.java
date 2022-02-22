package com.example.simplechat;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.parse.LogInCallback;
import com.parse.ParseAnonymousUtils;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.parse.SignUpCallback;

public class ChatActivity extends AppCompatActivity
{
    public static final String TAG = ChatActivity.class.getSimpleName();

    public static final String USER_ID_KEY = "userId";
    public static final String BODY_KEY = "body";
    public ParseUser user;

    EditText etMessage;
    ImageButton ibSend;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);


        if(ParseUser.getCurrentUser() != null)
        {
            Log.i(TAG,"here");
            startWithCurrentUser();
        }
        else
        {
            user = new ParseUser();
            user.setUsername("simplechat");
            user.setPassword("simplechat");

            Log.i(TAG,"running");
            ParseUser.logInInBackground(user.getUsername(),"simplechat");
            startWithCurrentUser();
        }

    }

    private void startWithCurrentUser()
    {
        // TODO:create startWithCurrentUserMethod
        setupMessagePosting();
    }

    private void setupMessagePosting()
    {

        etMessage = (EditText) findViewById(R.id.etMessage);
        ibSend = (ImageButton) findViewById(R.id.ibSend);

        ibSend.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                String data = etMessage.getText().toString();
                ParseObject message = ParseObject.create("Message");
                message.put(USER_ID_KEY, ParseUser.getCurrentUser());
                message.put(BODY_KEY, data);

                Log.i(TAG, "Failed to save message");
                message.saveInBackground(new SaveCallback()
                {

                    @Override
                    public void done(ParseException e)
                    {


                        if(e == null)
                        {
                            Toast.makeText(ChatActivity.this, "Successfully created message on Parse",
                                    Toast.LENGTH_SHORT).show();
                        } else {
                            Log.e(TAG, "Failed to save message", e);
                        }
                    }
                });
                etMessage.setText(null);
            }
        });

    }

    void login()
    {
        ParseAnonymousUtils.logIn(new LogInCallback()
        {
            @Override
            public void done(ParseUser user, ParseException e)
            {
                if(e != null)
                {
                    Log.e(TAG, "Anonymous login failed: ", e);
                }
                else
                {
                    startWithCurrentUser();
                }
            }
        });
    }
}