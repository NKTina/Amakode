package com.example.martinenezerwa.jsonparsing;

/**
 * Created by martine.nezerwa on 9/29/16.
 * The EventAdapter class customizes and inserts the data in the ListView.
 * The constructor inflate the row.xml and shows it into the listView.
 *
 * An event's image is downloaded using an Async Task.
 */


import android.view.LayoutInflater;
import android.widget.ArrayAdapter;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;


import java.util.ArrayList;
import java.io.InputStream;

public class EventAdapter extends ArrayAdapter<Events>
{
    ArrayList<Events> eventList;            // List that contains the events' title and image(s)

    LayoutInflater vi;
    int Resource;
    ViewHolder holder;                      // To make the scrolling between items smooth

    public EventAdapter(Context context, int resource, ArrayList<Events> objects)
    {
        super(context, resource, objects);
        vi = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        Resource = resource;
        eventList = objects;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        View v = convertView;

        if (v == null)
        {
            // inflate the layout
            v = vi.inflate(Resource, null);

            // set up the viewHolder.
            // Locate the imageView and the textView in the activity_main.xml
            holder = new ViewHolder();
            holder.imageview = (ImageView) v.findViewById(R.id.ivImage);
            holder.title = (TextView)v.findViewById(R.id.title);

            // store the holder with the view.
            v.setTag(holder);

        }
        else
        {
            holder = (ViewHolder) v.getTag();
        }
        holder.imageview.setImageResource(R.drawable.ic_launcher);
        new DownloadImageTask(holder.imageview).execute(eventList.get(position).getImage());
        holder.title.setText(eventList.get(position).getTitle());
        return v;
    }

    // caches the TextView and the ImageView
    static class ViewHolder
    {
        public ImageView imageview;
        public TextView title;
    }

    /* Class that download the image.
     * The image is decoded into a bitmap and set into an ImageView.
     */
    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap>
    {
        ImageView bmImage;

        public DownloadImageTask(ImageView bmImage)
        {
            this.bmImage = bmImage;
        }
        protected Bitmap doInBackground(String... urls)
        {
            String urlDisplay = urls[0];
            Bitmap Image = null;
            try
            {
                InputStream in = new java.net.URL(urlDisplay).openStream();
                Image = BitmapFactory.decodeStream(in);
            }
            catch (Exception e)
            {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return Image;
        }

        // Method to handle the result from doInBackground method.
        protected void onPostExecute(Bitmap result)
        {
            bmImage.setImageBitmap(result);
        }
    }
}
