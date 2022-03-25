package com.example.pokemongroupapp;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.RatingBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.pokemongroupapp.ui.createPost.CreatePostFragment;
import com.example.pokemongroupapp.ui.dashboard.DashboardFragment;
import com.example.pokemongroupapp.ui.home.ProfileFragment;
import com.example.pokemongroupapp.ui.hostOptions.HostOptionsFragment;
import com.example.pokemongroupapp.ui.notifications.SettingsFragment;
import com.example.pokemongroupapp.ui.raidDetails.RaidDetailsFragment;
import com.example.pokemongroupapp.ui.timePicker.TimePickerFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity implements TimePickerDialog.OnTimeSetListener {

    public ListView list;
    public ArrayList<RaidData> raids;
    public ArrayList<Boolean> raidConfimed;
    public PlayerData user;
    public int tag;
    //Allows for a message to be created for being sent to the server
    String message = "";
    //An array used to split up the data being received from a server
    String[] details;
    //All data that is received from a message
    String r_username = "";
    String r_stars = "";
    String r_time = "";
    String r_hours = "";
    String r_minutes = "";
    String r_timeKey = "";
    String r_location = "";
    String r_host = "";
    String r_level = "";
    String r_team = "";
    String r_status = "";
    String r_addremove = "";
    String r_battletime = "";
    String r_hatchtime = "";
    String r_rating = "";
    String r_confirmed = "";
    String r_jumptime = "";
    String r_pokemon = "";
    String r_newlyHatched = "";
    String r_newlyConfirmed = "";
    String r_jumpIn = "";
    //Sets a separating symbol to be used between parts of the message
    String s = "~";
    Socket sock2 = null;
    Thread trec;
    Thread tnotify;
    Socket sock = null;
    //A set of booleans for determining what section of the program a user is viewing
    public boolean settingsProf = false;
    public boolean browseRaids = false;
    public boolean viewCreateRaid = false;
    public int timePick = 0;
    //Tests that each string being read is different from the server
    int index = 0;
    Thread tsend;
    //Allows for the booleans for each of the switches to be kept and accessed in main
    public boolean notif_sending[] = {true,true,true,true,true,true,true};
    //Variables needed for notification pushing
    public static final String RAID_CHANNEL_ID = "raidChannel"; //Channel ID
    public NotificationManagerCompat notificationManager; //manages the channels

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_dashboard);

        //Gets information from Start Activity
        Intent intent = getIntent();
        String userName = intent.getStringExtra("username");
        String level = intent.getStringExtra("userLevel");
        String team = intent.getStringExtra("userTeam");

        //Create the player using the parsed information
        user = new PlayerData(userName, level, team);

        raids = new ArrayList<RaidData>();
        raidConfimed = new ArrayList<Boolean>();

        setContentView(R.layout.activity_main);
        BottomNavigationView bottomNav = findViewById(R.id.nav_view);
        bottomNav.getMenu().findItem(R.id.navigation_dashboard).setChecked(true);
        bottomNav.setOnNavigationItemSelectedListener(navListener);
        trec = new Thread(() -> ReadMessage());
        trec.start();

        notificationManager = NotificationManagerCompat.from(this);
        createNotificationChannel();

        SendMessageThread("UM"); //Update me if there are existing posts

        openDashboard();

        //raids.add(new RaidData("Other User's", 3,12 + ":" + 45, "0", new InterestData(this ,new PlayerData("CoreJJ","47", "Mystic"), 0), "Pikachu"));
        //raidConfimed.add(true);

    }

    private BottomNavigationView.OnNavigationItemSelectedListener navListener =
            new BottomNavigationView.OnNavigationItemSelectedListener()
            {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item)
                {
                    Fragment selectedFragment = null;

                    switch (item.getItemId())
                    {
                        case R.id.navigation_profile:
                            selectedFragment = new ProfileFragment();
                            break;
                        case R.id.navigation_dashboard:
                            openDashboard();
                            break;
                        case R.id.navigation_settings:
                            selectedFragment = new SettingsFragment();
                            break;
                    }
                    if(selectedFragment != null)
                    {
                        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                                selectedFragment).commit();
                    }
                    return true;
                }
            };


    public void SetTime(View v)
    {
        timePick = 1;
        DialogFragment timePicker = new TimePickerFragment();
        timePicker.show(getSupportFragmentManager(), "time picker");
    }
    public void SetTime2(View v)
    {
        timePick = 2;
        DialogFragment timePicker = new TimePickerFragment();
        timePicker.show(getSupportFragmentManager(), "time picker");
    }

    @Override
    public void onTimeSet(TimePicker timePicker, int i, int i1) {
        if(timePick == 1)
        {
            Button timebox = (Button)findViewById(R.id.CreatePost_timeBox);
            timebox.setText(formatTime(i, i1));
        }
        else if(timePick == 2)
        {
            Button timebox2 = (Button)findViewById(R.id.CreatePost_timeBox2);
            formatTime(i, i1);
            timebox2.setText(formatTime(i, i1));
        }
    }


    //Button OnClick events (we can find a better solution later)

    //Create Post OnClicks
    public void AddPost(View v)
    {
//        EditText gymInput = (EditText) findViewById(R.id.createPost_Gym_Input);
//        String gym = gymInput.getText().toString();

        RadioGroup rg = (RadioGroup) findViewById(R.id.CreatePost_eggHatched);
        EditText pokemonInput = (EditText) findViewById(R.id.CreatePost_Pokemon_Input);
        String timeKey = "H";
        String pokemon = "Unknown";
        if(rg.getCheckedRadioButtonId() == R.id.CreatePost_hatchedYes)
        {
            pokemon = pokemonInput.getText().toString();
            timeKey = "J";
        }

        Spinner gymSpinner = (Spinner) findViewById(R.id.CreatePost_Spinner);
        int selectedGym = gymSpinner.getSelectedItemPosition();
        String gym = (String)gymSpinner.getItemAtPosition(selectedGym);

        RatingBar rBar = (RatingBar) findViewById(R.id.CreatePostRating);
        int Rating = (int)rBar.getRating();

        //TimePicker tp = (TimePicker) findViewById(R.id.CreatePost_TimePicker);
        Button timebox = (Button)findViewById(R.id.CreatePost_timeBox);
        String timeboxtext = timebox.getText().toString();
        System.out.println(timeboxtext);
        if(timeboxtext.compareTo("Set Time") == 0)
        {
            toast("Please enter a time!");
        }
        else
            {
                String[] split = timeboxtext.split(":");
                int hour = Integer.parseInt(split[0]);
                int min = Integer.parseInt(split[1]);
                String time = formatTime(hour, min);
                InterestData host = new InterestData(this ,user, 2);
                //Also passes in a key to determine which time is being changed
                RaidInitialPost(time, timeKey, gym, Rating + "", pokemon);
                //raids.add(new RaidData(gym, Rating, time, "0", host, pokemon));
                raidConfimed.add(false);
                openDashboard();
            }
    }

    public void AddPostFunction(String gym, int Rating, String hour, String min, String timeKey, InterestData host, String pokemon)
    {
        RaidData newRaid = new RaidData(gym, Rating,hour + ":" + min, host, pokemon, false, false);
        //Checks the values of the passed in time key to know if the hatchtime or jumpIn time is to be set
        if(timeKey.compareTo("J") == 0){
            newRaid.setJumpTime(hour + ":" + min);
        }
        raids.add(newRaid);
    }

    public void openCreatePost(View v)
    {
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                new CreatePostFragment()).commit();
    }


    public void backToDash(View v)
    {
        openDashboard();
    }

    @Override
    public void onBackPressed() { }


    public void RemovePost(String hostName, String name, String time)
    {
        RaidData remove = null;
        for(RaidData r : raids)
        {
            if(r.isSameAs(hostName, name, time))
            {
                remove = r;
            }
        }
        if(remove != null)
        {
            raids.remove(remove);
        }
    }

    public void openDashboard()
    {
        Bundle bundle = new Bundle();
        bundle.putSerializable("raids", raids);
        bundle.putSerializable("user", user);
        Fragment fragment = new DashboardFragment();
        fragment.setArguments(bundle);

        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                fragment).commit();
    }

    public String formatTime(int hourNum, int minNum)
    {
        String hour;
        String min;
        if(hourNum < 10)
        {
            hour = "0" + hourNum;
            System.out.println(hourNum + " : " + hour);
        }
        else
        {
            hour = Integer.toString(hourNum);
        }
        if(minNum < 10)
        {
            min = "0" + minNum;
            System.out.println(minNum + " : " + min);
        }
        else
        {
            min = Integer.toString(minNum);
        }
        return hour + ":" + min;
    }

    //This Method Creates the Notification Channel
    @RequiresApi(api = Build.VERSION_CODES.O)
    public void createNotificationChannel()
    {
        //Builds the notification channel for Raids
        NotificationChannel raidChannel = new NotificationChannel(
                RAID_CHANNEL_ID,
                "RaidChannel",
                NotificationManager.IMPORTANCE_HIGH);
        raidChannel.setDescription("This is the Channel for Raid Notifications");

        NotificationManager manager = getSystemService(NotificationManager.class);
        manager.createNotificationChannel(raidChannel);
    }

    //This is called to create individual notifications and push them out
    public void sendNotification(String title, String text)
    {
        Drawable myDrawable = getDrawable(R.drawable.notification_icon);
        Bitmap icon      = ((BitmapDrawable) myDrawable).getBitmap();

        Notification notification = new NotificationCompat.Builder(this, RAID_CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_notification)
                .setLargeIcon(icon)
                .setContentTitle(title)
                .setContentText(text)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .build();
                new Handler(Looper.getMainLooper()).post((Runnable) () -> notificationManager.notify(1, notification));

    }

    //Continually checks for messages being sent from the server to display them on the screen
    public void ReadMessage() {
        try {
            //For determining the length of the message being received
            int len = 0;
            //Used to read the message sent from the server
            ByteArrayOutputStream returnedmessage = new ByteArrayOutputStream();
            //Creates a byte array for reading the data from the server
            byte[] buffer = new byte[1024];
            //A variable for allowing a thread to end when a new one is spawned
            boolean run = true;
            //Creates a new socket and makes a connection with the server
            sock2 = new Socket();
            sock2.connect(new InetSocketAddress("peer.cms.waikato.ac.nz", 2005), 3000);
            //Loops continually until the first bit of data is read from the socket
            while (run){
                //While there is data to be read from the socket and the end var hasn't been set
                while((len = sock2.getInputStream().read(buffer)) >= 0 && run) {
                    //Writes the data read into the buffer ignoring the 2 bytes at the start
                    //that the server uses for determining the length
                    returnedmessage.write(buffer, 2, len);
                    //Converts what is received from the socket into the the string
                    String message = returnedmessage.toString();
                    //Shows message received (In program this will create a thread to determine
                    //what action the message will require
                    MainActivity.this.runOnUiThread(new Runnable()
                    {
                        public void run()
                        {
                            //Reads the first 2 characters of the message
                            String code = message.substring(0, 2);
                            //Depending on the value of the code, send the
                            switch(code) {
                                case "RP" : RaidShowPosts(message);
                                    break;
                                case "RU" : RaidUpdateDetails(message);
                                    break;
                                case "RI" : RaidAddInterestedUsers(message);
                                    break;
                                case "OP" : RaidOldPost(message);
                                    break;
                                case "UM" :
                                    try {
                                        UpdateNewPlayer();
                                    } catch (InterruptedException e) {
                                        e.printStackTrace();
                                    }
                                    //If code is not any of the 3 then don't call any methods to interpret
                                default: break;
                            }

                            //MainActivity mA = (MainActivity).getApplicationContext();


                        }
                    });
                    trec = new Thread(() -> ReadMessage());
                    trec.start();
                    //Sets run to be false to kill the loop and start a new thread
                    run = false;
                }
            }
        }
        //If any issue is faced, output to the screen
        catch (IOException e) {
            //
        }
    }
    //A method for starting a thread to send a message to the server
    public void SendMessageThread(String message) {
        //tsend = new Thread(() -> SendMessage("RP~user1~V~19~1200~Gym1~4~"));
        tsend = new Thread(() -> SendMessage(message));
        tsend.start();
    }

    //Is a method for sending a message to the server via a socket
    private void SendMessage(String m2) {
        try {
            //Creates and sets up a new socket for making a connection with the server
            sock = new Socket();
            sock.connect(new InetSocketAddress("peer.cms.waikato.ac.nz", 2005), 3000);
            byte[] stringbytes = m2.getBytes();
            //Creates a byte array for storing the server message to be sent to the server (Adds 2 bytes to the
            //length of the stringbytes array required to fit the length information)
            int messagel = stringbytes.length;
            //Creates a byte array of the length of the stringbytes including the leading 2 extra bytes
            byte[] serverm = new byte[messagel + 2];
            //Adds the first 2 bits of information for the server to determine the length of the message being sent
            //by dividing to get the number of sets of 256 bytes and modulo to get the remainder
            serverm[0] = (byte) (serverm.length / 256);
            serverm[1] = (byte) (serverm.length % 256);
            //Goes through and adds all the data from the stringbytes byte array to the serverm byte array
            for (int i = 0; i < messagel; i++) {
                serverm[i + 2] = stringbytes[i];
            }
            //writes the message to the server
            sock.getOutputStream().write(serverm, 0, serverm.length);
            sock.close();
        }
        //If any issue is faced, output to the screen
        catch (IOException e) {
            //
        }
    }

    //Is a method which allows for the data of a raid to be posted
    public synchronized void RaidInitialPost(String time, String timeKey, String location, String rating, String pokemon){
        InterestData host = new InterestData(getApplicationContext() ,user, 0);
        //Reads the data from the host interest data
        PlayerData user = host.getPlayer();
        String username = user.getName();
        String level = user.getLevel();
        String team = user.getTeam();
        //Adds all fields together using the separating symbol with the code 'RP' at the beginning
        message = "RP" + s + username + s + level + s + team + s + time + s + timeKey + s + location + s + rating + s + pokemon + s;
        //Sends the message using the socket to the server
        SendMessageThread(message);
    }

    //Allows for a host to add details and make actions for events regarding a raid posting.
    public synchronized void RaidUpdateDetailsPost(String time, String location, String host, String battletime,
                                                   String hatchtime, String certainty, String jumptime, String pokemon,
                                                   String addremove) {
        //Adds all fields together using the separating symbol with the code 'RI' at the beginning
        message = "RU" + s + time + s + location + s + host + s + battletime + s + hatchtime + s +
                certainty + s + jumptime + s + pokemon + s + addremove + s;
        System.out.println(message);
        //Sends the message using the socket to the server
        SendMessageThread(message);
    }

    //Is used to send a message to the server once a user expresses interest in one of the raids
    public synchronized void RaidUserInterest(String time, String location, InterestData host, int status) {
        InterestData hostData = new InterestData(getApplicationContext() ,user, 0);
        //Reads the data from the host interest data
        PlayerData user = hostData.getPlayer();
        String username = user.getName();
        String level = user.getLevel();
        String team = user.getTeam();
        String hostName = host.getPlayer().getName();
        //Adds all fields together using the separating symbol with the code 'RI' at the beginning
        message = "RI" + s + time + s + location + s + hostName + s + username + s + level + s + team + s + status + s;
        //Sends the message using the socket to the server
        SendMessageThread(message);
    }

    public synchronized void UpdateNewPlayer() throws InterruptedException {
        for (int i = 0; i < raids.size(); i++){
            //RaidSendOldPost(String time, String timeKey, String location, String rating, String pokemon, String certainty, String jumptime, String battletime)
            String time = raids.get(i).raidTime;
            String location = raids.get(i).raidName;
            String rating = Integer.toString(raids.get(i).stars);
            String pokemon = raids.get(i).pokemon;
            String certainty = raids.get(i).getCertainty();
            String jumptime = raids.get(i).jumpTime;
            String battletime = raids.get(i).battleTime;
            String timekey = "";
            if (jumptime.compareTo("")==1){ //if jumptime is set to something
                timekey = "J";
            }
            else if(battletime.compareTo("")==1){
                timekey = "B";
            }
            else{
                timekey = "H";
            }
            String hostName = raids.get(i).host.getPlayer().getName();
            String hostLevel = raids.get(i).host.getPlayer().getLevel();
            String hostTeam = raids.get(i).host.getPlayer().getTeam();
            System.out.println("OP" + s + hostName + s + hostLevel + s + hostTeam + s + time + s + timekey + s + location + s + rating + s + pokemon + s + certainty + s + battletime + s + jumptime + s);
            RaidSendOldPost(hostName, hostLevel, hostTeam, time, timekey, location, rating, pokemon, certainty, jumptime, battletime);
            TimeUnit.MILLISECONDS.sleep(200);

            //loop through list of interested players and add if not duplicates
            for (int j = 0; j < raids.get(i).getRaidPlayerCount(); j++){
                InterestData player = raids.get(i).getPlayer(j);
                String username = player.getPlayer().getName();
                String level = player.getPlayer().getLevel();
                String team = player.getPlayer().getTeam();
                String status = Integer.toString(player.getNumStatus());
                //Adds all fields together using the separating symbol with the code 'RI' at the beginning
                System.out.println("RI" + s + time + s + location + s + hostName + s + username + s + level + s + team + s + status + s);
                RaidSendUserData(time, location, hostName, username, level, team, status);
                TimeUnit.MILLISECONDS.sleep(200);
            }
        }
    }

    public synchronized void RaidSendOldPost(String hostName, String hostLevel, String hostTeam, String time, String timekey, String location, String rating, String pokemon, String certainty, String jumptime, String battletime){
        //Adds all fields together using the separating symbol with the code 'RP' at the beginning
        message = "OP" + s + hostName + s + hostLevel + s + hostTeam + s + time + s + timekey + s + location + s + rating + s + pokemon + s + certainty + s + battletime + s + jumptime + s;
        //Sends the message using the socket to the server
        SendMessageThread(message);
    }
    //A method used to be able to send user data
    public synchronized void RaidSendUserData(String time, String location, String hostName, String username, String level, String team, String status){
        //Adds all fields together using the separating symbol with the code 'RI' at the beginning
        message = "RI" + s + time + s + location + s + hostName + s + username + s + level + s + team + s + status + s;
        //Sends the message using the socket to the server
        SendMessageThread(message);
    }

    //Set of methods called to update the details of raid events on their local devices
    //By reading the details of the received message from the socket

    public synchronized void RaidShowPosts(String m){
        //Splits the message into a array of strings
        details = m.split(s, 0);
        //showFromThread("RaidShowPosts");
        //Saves each of the strings from the given indexes (Starting at index 1 to skip
        //the message code at index 0)
        Boolean add = true;
        r_host = details[1];
        r_level = details[2];
        r_team = details[3];
        r_time = details[4];
        //Splits the 24 hours time into hours and minutes
        r_hours = r_time.substring(0, 2);
        r_minutes = r_time.substring(3, 5);
        r_timeKey = details[5];
        r_location = details[6];
        r_rating = details[7];
        r_pokemon = details[8];
        for (int i = 0; i < raids.size(); i++) {

           if (raids.get(i).getName().compareTo(r_location) == 0
                   && raids.get(i).getHost().getPlayer().getName().compareTo(r_host) == 0
                   && raids.get(i).getRaidTime().compareTo(r_time) == 0)
           {
              add = false;
              break;
           }
        }
        if(add){
            //Converts from string to int
            int int_r_rating = Integer.valueOf(r_rating);
            PlayerData received_Host = new PlayerData(r_host, r_level, r_team);
            //Checks the identity of the user through comparing the host info received
            //to know if the message received was was one which they sent to the server
            //to determine if the method needs to be triggered
            InterestData host = new InterestData(getApplicationContext(), received_Host, 0);
            AddPostFunction(r_location, int_r_rating, r_hours, r_minutes, r_timeKey, host, r_pokemon);
            openDashboard();
        }
    }

    //The following indexes are used for the switch booleans
    //0 : Raid at favourite Gym
    //1 : Raid at least x num people interested
    //2 : Raid i am interested in has hatched
    //3 : Raid i am interested in is confirmed
    //4 : Raid i am interested in is ready to jump in
    //(Might need to clarification on the behavior of these 2
    //5 : Hosted raid has hatched
    //6: Hosted raid is ready to jump in

    public synchronized void RaidUpdateDetails(String m) {
        //booleans to determine if there is a matching raid / raid is being deleted
        boolean raidFound = false;
        boolean raidRemoved = false;
        //Stores the data of a raid for determining what has been changed during the update
        String old_confirmed = "";
        String old_battletime = "";
        String old_jumptime = "";
        String old_pokemon = "";
        //Array list of player objects to find if the user is listed as being interested
        ArrayList<InterestData> players = new ArrayList<InterestData>();
        //Splits the message into a array of strings
        details = m.split(s, 0);
        //Saves each of the strings from the given indexes (Starting at index 1 to skip
        //the message code at index 0)
        r_time = details[1];
        //Splits the 24 hours time into hours and minutes
        System.out.println(r_time);
        //r_hours = r_time.substring(0, 2);
        //r_minutes = r_time.substring(3, 5);
        r_location = details[2];
        r_host = details[3];
        //r_hostLevel = details[4];
        //r_hostTeam = details[5];
        r_battletime = details[4];
        r_hatchtime = details[5];
        r_confirmed = details[6];
        r_jumptime = details[7];
        r_pokemon = details[8];
        r_addremove = details[9];
        InterestData hostdata = new InterestData(getApplicationContext(), user, 0);
        //Reads the data from the host interest data
        PlayerData user = hostdata.getPlayer();
        String username = user.getName();
        //String level = user.getLevel();
        //String team = user.getTeam();
        //Creates a raid key using the data received by the message
        String r_key = r_location + "~" + r_time + "~" + r_host;

        //Finds the raid data associated with the time, location and host
        for (int i = 0; i < raids.size(); i++) {
            //If the raid key matches the one received
            if (raids.get(i).getKey().equals(r_key)) {
                //Reads all the current properties of the raid data to see what has been updated
                //for notifying users
                old_battletime = raids.get(i).getBattleTime();
                old_confirmed = raids.get(i).getCertainty();
                old_jumptime = raids.get(i).getJumpTime();
                old_pokemon = raids.get(i).getPokemon();
                //Updates all the fields of the raid data to match what is sent in the message
                raids.get(i).updateDetails(r_confirmed, r_battletime, r_jumptime, r_pokemon);
                //Reads the player interest array from the raid at that index
                players = raids.get(i).getPlayerData();
                //Checks if the raid is to be deleted or not determined by the value of r_addremove
                System.out.println("addremove: " + r_addremove);
                if(r_addremove.compareTo("Y") == 0){
                    raids.remove(i);
                    //Toast.makeText(getApplicationContext(), "Post Removed!", Toast.LENGTH_SHORT).show();
                    openDashboard();
                    //Sets the boolean to allow for users to be notified that the raid has been
                    //removed
                    raidRemoved = true;
                }
                //Breaks the loop and sets raidFound to be true
                raidFound = true;
                openDashboard();
                break;
            }
        }
        //To know if there needs to be a notification that needs to be sent out, no notification call
        //is made if the user is not found to be interested in the raid, if the same as the host username
        //then ignore sending out notification
        if (raidFound && (r_host.compareTo(username) != 0)) {
            for (int i = 0; i < players.size(); i++) {
                //If the raid key matches the one received
                if (players.get(i).getPlayer().name.compareTo(username) == 0) {
                    //Runs the notification method as message is regarding a raid
                    //that the user is interested in
                    if (raidRemoved){
                        sendNotification("Raid Removed",
                                r_location + " raid has been removed");
                    }
                    else {
                        //Checks the values of the prev recorded raid data before update occurred
                        //to know if the user needs to be notified of an update to a raid which they
                        //are watching
                        //Checks if the raid has had its pokemon confirmed (Hatched) and notification setting for pokemon is set
                        if (r_pokemon.compareTo(old_pokemon) != 0 && r_pokemon.compareTo("Unknown") != 0 && notif_sending[2]
                            && r_jumptime.compareTo(old_jumptime) != 0 && r_jumptime.compareTo("NONE") != 0 && notif_sending[4]){
                            //toast(r_pokemon + " has hatched for raid " + r_location);
                            sendNotification("Pokemon Hatched, Jump Time Set to " + r_jumptime + "!",
                                    r_pokemon.toUpperCase() + " has hatched at " + r_location + "!");
                        }
                        //If pokemon notification is set but not jump time
                        else if (r_pokemon.compareTo(old_pokemon) != 0 && r_pokemon.compareTo("Unknown") != 0 && notif_sending[2]){
                            //toast(r_pokemon + " has hatched for raid " + r_location);
                            sendNotification("Pokemon Hatched!",
                                    r_pokemon.toUpperCase() + " has hatched at " + r_location + "!");
                        }
                        //Checks if the raid has had its jump in time set and jump in notification time has been set but not pokemon
                        else if (r_jumptime.compareTo(old_jumptime) != 0 && r_jumptime.compareTo("NONE") != 0 && notif_sending[4]){
                            //toast("Jump time has been set to " + r_jumptime + " for raid at " + r_location);
                            sendNotification("Jump In Time Set!",
                                    r_jumptime + " is jump time for " + r_location + ".");
                        }
                        //Checks if the raid has been confirmed and raid confirmed notification setting has been set
                        else if (r_confirmed.compareTo(old_confirmed) != 0 && r_confirmed.compareTo("C") == 0 && notif_sending[3]){
                            //Send raid confirmed notification
                            //toast("raid at " + r_location + " has been confirmed");
                            sendNotification("Raid Confirmed!",
                                    "Raid at " + r_location + " confirmed");
                        }
                        //Else if checks if the raid is ready to jump in (No notification slider setting is needed as this
                        //is core to the functionality of the app)
                        else if (r_battletime.compareTo(old_battletime) != 0 && r_battletime.compareTo("NONE") != 0) {
                            //toast("raid at " + r_location + " has started and group will finish at " + r_battletime);
                            sendNotification("Jump In Now!",
                                    r_location + " happening until " + r_battletime);
                        }
                    }
                    //Breaks the loop
                    break;
                }
            }
        }
        openDashboard();
        //Checks if the user is on a part of the application which would need to be refreshed
        if(browseRaids){
            openDashboard();
        }
    }

    public synchronized void RaidAddInterestedUsers(String m){
        //Splits the message into a array of strings
        details = m.split(s, 0);
        //Reads the different
        r_time = details[1];
        //Splits the 24 hours time into hours and minutes
        r_hours = r_time.substring(0, 2);
        r_minutes = r_time.substring(3, 5);
        r_location = details[2];
        r_host = details[3];
        r_username = details[4];
        r_level = details[5];
        r_team = details[6];
        r_status = details[7];

        //For the raid key, host, location, time
        for (int i = 0; i<raids.size(); i++){
            if (raids.get(i).getName().compareTo(r_location)==0
                    && raids.get(i).getHost().getPlayer().getName().compareTo(r_host)==0
                    && raids.get(i).getRaidTime().compareTo(r_hours + ":" + r_minutes)==0){
                //make PlayerData(String name, String level, String team)
                PlayerData player = new PlayerData(r_username, r_level, r_team);
                //make InterestData(Context context,PlayerData player, int status)

                if(raids.get(i).isInterested(player))
                {
                    raids.get(i).changeInterest(player, Integer.valueOf(r_status));
                    Toast.makeText(getApplicationContext(), "Raid Count: " + raids.get(i).getRaidPlayerCount(), Toast.LENGTH_SHORT).show();
                    System.out.println("already interested player: " + player.getName());
                }
                else
                {
                    raids.get(i).addPlayer(new InterestData(getApplicationContext(),player, Integer.valueOf(r_status)));
                    Toast.makeText(getApplicationContext(), "User Added!", Toast.LENGTH_SHORT).show();
                }
                //refresh fragment
                openDashboard();
            }
        }
    }

    public synchronized void RaidOldPost(String m){
        //Splits the message into a array of strings
        details = m.split(s, 0);
        //showFromThread("RaidShowPosts");
        //Saves each of the strings from the given indexes (Starting at index 1 to skip
        //the message code at index 0)
        Boolean add = true;
        r_host = details[1];
        r_level = details[2];
        r_team = details[3];
        r_time = details[4];
        //Splits the 24 hours time into hours and minutes
        r_hours = r_time.substring(0, 2);
        r_minutes = r_time.substring(3, 5);
        r_timeKey = details[5];
        r_location = details[6];
        r_rating = details[7];
        r_pokemon = details[8];
        r_confirmed = details[9];
        r_battletime = details[10];
        r_jumptime = details[11];

        for (int i = 0; i < raids.size(); i++) {

            if (raids.get(i).getName().compareTo(r_location) == 0
                    && raids.get(i).getHost().getPlayer().getName().compareTo(r_host) == 0
                    && raids.get(i).getRaidTime().compareTo(r_time) == 0)
            {
                add = false;
                break;
            }
        }
        if(add){
            //Converts from string to int
            int int_r_rating = Integer.valueOf(r_rating);
            PlayerData received_Host = new PlayerData(r_host, r_level, r_team);
            //Checks the identity of the user through comparing the host info received
            //to know if the message received was was one which they sent to the server
            //to determine if the method needs to be triggered
            InterestData host = new InterestData(getApplicationContext(), received_Host, 0);
            AddPostFunction(r_location, int_r_rating, r_hours, r_minutes, r_timeKey, host, r_pokemon);
            for (int i = 0; i < raids.size(); i++) {

                if (raids.get(i).getName().compareTo(r_location) == 0
                        && raids.get(i).getHost().getPlayer().getName().compareTo(r_host) == 0
                        && raids.get(i).getRaidTime().compareTo(r_time) == 0)
                {
                    raids.get(i).updateDetails(r_confirmed, r_battletime, r_jumptime, r_pokemon);
                    break;
                }
            }


            openDashboard();
        }
    }


    //Method for debugging purposes which allows for a toast to be displayed on the UI thread
    public void toast(final String message){
        MainActivity.this.runOnUiThread(new Runnable() {
            public void run() {
                Toast.makeText(getApplicationContext(), message,
                        Toast.LENGTH_LONG).show();
                Log.d("Toast", message);
            }
        });
    }

}


