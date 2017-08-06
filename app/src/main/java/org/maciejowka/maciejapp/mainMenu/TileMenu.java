package org.maciejowka.maciejapp.mainMenu;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import org.maciejowka.maciejapp.R;
import org.maciejowka.maciejapp.publications.SinglePublication;

/**
 * Created by maciej on 11.03.17.
 */
public class TileMenu extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tile_menu);

        GridView gridview = (GridView) findViewById(R.id.gridview);
        gridview.setAdapter(new ImageAdapter(this));

        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getApplicationContext(), SinglePublication.class);
                startActivity(intent);
            }
        });
    }

    private class ImageAdapter extends BaseAdapter {
        private Context mContext;

        public Integer[] mThumbIds = {
                R.mipmap.ic_launcher, R.mipmap.ic_launcher,
                R.mipmap.ic_launcher, R.mipmap.ic_launcher,
                R.mipmap.ic_launcher, R.mipmap.ic_launcher,
                R.mipmap.ic_launcher, R.mipmap.ic_launcher,
                R.mipmap.ic_launcher, R.mipmap.ic_launcher,
                R.mipmap.ic_launcher, R.mipmap.ic_launcher,
                R.mipmap.ic_launcher, R.mipmap.ic_launcher
        };


        public ImageAdapter(Context c) {
            mContext = c;
        }

        public int getCount() {
            return mThumbIds.length;
        }

        public Object getItem(int position) {
            return null;
        }

        public long getItemId(int position) {
            return 0;
        }

        // create a new ImageView for each item referenced by the Adapter
        public View getView(int position, View convertView, ViewGroup parent) {
            ImageView imageView;

            if (convertView == null) {
                imageView = new ImageView(mContext);
                imageView.setAdjustViewBounds(true);
            } else {
                imageView = (ImageView) convertView;
            }
            imageView.setImageResource(mThumbIds[position]);
            return imageView;
        }
    }
}
