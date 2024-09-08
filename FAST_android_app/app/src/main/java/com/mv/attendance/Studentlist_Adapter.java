package com.mv.attendance;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.ListAdapter;

import java.util.ArrayList;

public class Studentlist_Adapter extends ArrayAdapter<ListElement_class> {

    public Studentlist_Adapter(Context context, ArrayList<ListElement_class> elementArrayList){
        super(context, R.layout.listitem_cardview, elementArrayList);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        ListElement_class listElement = getItem(position);

        if (convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.listitem_cardview_students, parent,  false);
        }

        ImageView image_element = convertView.findViewById(R.id.image_element);
        TextView text_element = convertView.findViewById(R.id.text_element);

        image_element.setImageResource(listElement.ImageID);
        text_element.setText(listElement.list_element);

        return convertView;
    }
}
