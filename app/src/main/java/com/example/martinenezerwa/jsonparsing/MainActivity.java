package com.example.martinenezerwa.jsonparsing;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView;
import android.view.View;
import android.widget.Toast;


import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;


public class MainActivity extends Activity
{
    ArrayList<Events> eventsList;           // used to store the events

    EventAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        eventsList = new ArrayList<Events>();       // instantiate the array

        // execute the JSONAsynctask with the appropriate URl
        new JSONAsyncTask().execute("https://datatank.stad.gent/4/toerisme/visitgentevents.json");

        // Locate the listView in the activity_main.xml
        ListView listview = (ListView)findViewById(R.id.list);

        // Pass the result into the EventAdapter.java
        adapter = new EventAdapter(getApplicationContext(), R.layout.row, eventsList);

        // set the adapter to the listView
        listview.setAdapter(adapter);
    }

    /*
     * Class that fetches the JSON data from the server.
     * The asyncTask returns true if the data was fetched from the server and false otherwise.
     * A progressDialog is used to show the progress of the fetching process.
     */
    class JSONAsyncTask extends AsyncTask<String, Void, Boolean>
    {

        ProgressDialog dialog;

        @Override
        protected void onPreExecute()
        {
            super.onPreExecute();
            // create a progressionDialog and set the appropriate fields.
            dialog = new ProgressDialog(MainActivity.this);
            dialog.setMessage("Loading, please wait");
            dialog.setTitle("Connecting server");
            dialog.show();
            dialog.setCancelable(false);
        }

        @Override
        protected Boolean doInBackground(String... urls)
        {
            // Download JSON Data from the URL
            try
            {
                HttpGet httpPost = new HttpGet(urls[0]);
                HttpClient httpClient = new DefaultHttpClient();
                HttpResponse response = httpClient.execute(httpPost);

                int status = response.getStatusLine().getStatusCode();
                if (status == 200)
                {
                    HttpEntity entity = response.getEntity();

                    // convert the entity to a string
                    String data = EntityUtils.toString(entity);

                    // Retrieve the JSON arrays from the given URL
                    JSONArray JArray = new JSONArray(data);

                    // Loop through the JSON array and set the JSON object into the arrayList.
                    for (int i = 0; i < JArray.length(); i++)
                    {
                        Events event = new Events();

                        JSONObject object = JArray.getJSONObject(i);
                        String title = object.getString("title");
                        event.setTitle(title);

                        // The image object is composed of multiple objects.
                        // set the imageURL to the first JSON element in the image object.
                        JSONArray imgArray = object.getJSONArray("images");
                        String imageURL = null;

                        for (int j=0; j < imgArray.length();j++)
                        {
                            imageURL = imgArray.getString(0);
                        }
                        event.setImage(imageURL);

                        // add the particular event to the events' list.
                        eventsList.add(event);
                    }
                    return true;
                }
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
            catch (JSONException e)
            {
                e.printStackTrace();
            }
            return false;
        }

        protected void onPostExecute(Boolean result)
        {
            dialog.cancel();
            adapter.notifyDataSetChanged();
            if(result == false)
                Toast.makeText(getApplicationContext(), "Unable to fetch data from server", Toast.LENGTH_LONG).show();

        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings)
        {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
