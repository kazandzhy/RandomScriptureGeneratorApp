package com.kazandzhy.randomscripture;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Favorites List Array Adapter
 *
 * This class is a custom Array Adapter that lets us populate
 * the Favorites List with our unique elements, a TextView,
 * and a Button.
 *
 * @author Vlad Kazandzhy, Nathan Tagg, Tyler Braithwaite
 */

public class FavoritesListArrayAdapter extends BaseAdapter implements ListAdapter {
    private ArrayList<String> list = new ArrayList<String>();
    private Context context;
    private String message;

    /**
     * The constructor takes in the Array list that we want to put into
     * our view.
     *
     * @param list
     * @param context
     */
    public FavoritesListArrayAdapter(ArrayList<String> list, Context context) {
        this.list = list;
        this.context = context;
        this.message = list.get(0);


    }


    /**
     * Returns the size of the list
     *
     * @return size of the list
     */
    @Override
    public int getCount() {
        return list.size();
    }

    /**
     * Takes in an integer for position and gives back the object in
     * that position.
     *
     * @param pos (position)
     * @return Object at that position
     */
    @Override
    public Object getItem(int pos) {
        return list.get(pos);
    }

    /**
     * This function is required, but since the items we are storing
     * are only strings, they wont have an id. so we just return zero.
     *
     * @param pos
     * @return 0
     */
    @Override
    public long getItemId(int pos) { return 0; }

    /**
     * Handles the individual elements of the favorites list
     *
     * @param position
     * @param convertView
     * @param parent
     * @return The view specified
     */
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.list_item, null);
        }

        //Handle TextView and display string from your list
        final TextView listItemText = (TextView)view.findViewById(R.id.list_item_string);
        listItemText.setText(list.get(position));

        //Handle buttons and add onClickListeners
        Button deleteBtn = (Button)view.findViewById(R.id.delete_btn);

        // hide the close button when displaying no favorites message or loading message
        if (message.equals("You have no Favorites saved yet.") || message.equals("Loading Favorites, please wait...")) {
            deleteBtn.setVisibility(View.INVISIBLE);
        }

        /**
         * Returns the size of the list
         *
         * @param v
         */
        deleteBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Log.i("verse is ", listItemText.getText().toString());
                FavoritesActivity.deleteFavorite(listItemText.getText().toString());
                list.remove(position); //or some other task
                notifyDataSetChanged();
            }
        });


        return view;
    }
}
