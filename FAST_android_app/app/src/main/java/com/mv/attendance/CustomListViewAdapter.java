package com.mv.attendance;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;

import org.eazegraph.lib.charts.PieChart;
import org.eazegraph.lib.models.PieModel;

import java.util.List;

public class CustomListViewAdapter extends BaseAdapter {

    List<Integer> present;
    List<Integer> absent;
    List<String> subjTitle;
    Context context;
    LayoutInflater inflter;

    public CustomListViewAdapter(Context applicationContext, List<Integer> present, List<Integer> absent, List<String> subjTitle) {
        this.context = applicationContext;
        this.present = present;
        this.absent = absent;
        this.subjTitle = subjTitle;
        inflter = (LayoutInflater.from(applicationContext));
    }

    @Override
    public int getCount() {
        return subjTitle.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        inflter = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflter.inflate(R.layout.pie_chart_listview_element, viewGroup, false);
            //TextView tvPresent = view.findViewById(R.id.textView_Present_inListView);
            //TextView tvAbsent = view.findViewById(R.id.textView_Absent_inListView);
            CardView cardViewOfPresenty = view.findViewById(R.id.idPresenty_card);
            TextView title = view.findViewById(R.id.textInCardViewAttendance_InListView);
            TextView textView_Present_inListView = view.findViewById(R.id.textView_Present_inListView);
            TextView textView_Absent_inListView = view.findViewById(R.id.textView_Absent_inListView);
            PieChart pieChart = view.findViewById(R.id.piechart_ListView);
            title.setText(subjTitle.get(i));
            //Log.d("QWERT", "WHY NOT WORKINGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGG");
            pieChart.addPieSlice(
                    new PieModel(
                            "Present",
                            present.get(i),
                            Color.parseColor("#00FB54")));
            pieChart.addPieSlice(
                    new PieModel(
                            "Absent",
                            absent.get(i),
                            Color.parseColor("#FF0000")));
            //pieChart.startAnimation();
            // To animate the pie chart
        pieChart.setInnerValueString(String.valueOf(present.get(i)/(present.get(i)+absent.get(i))));
        //pieChart.update();
        textView_Present_inListView.setText(100*present.get(i)/(present.get(i)+absent.get(i)) + "%  (Present)");
        textView_Absent_inListView.setText(100*absent.get(i)/(present.get(i)+absent.get(i)) + "%  (Absent)");
        pieChart.setValueTextSize(0);
            cardViewOfPresenty.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    clickHere(i);
                }
            });
        return view;
    }

    private void clickHere(int i) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        LinearLayout layout = new LinearLayout(context);
        layout.setBackgroundColor(Color.parseColor("#EEEEEE"));
        Drawable drawable = ContextCompat.getDrawable(context,  R.drawable.white_alert_dialogue_background);
        layout.setBackground(drawable);
        layout.setPadding(20,20,20,0);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.FILL_PARENT));

                /*final EditText inputEditTextNameOfStudent = new EditText(getApplicationContext());
                final EditText inputEditTextDivOfStudent = new EditText(getApplicationContext());
                final EditText inputEditTextRollNoOfStudent = new EditText(getApplicationContext());
                inputEditTextNameOfStudent.setHint("Name");
                inputEditTextDivOfStudent.setHint("Div");
                inputEditTextRollNoOfStudent.setHint("Roll No.");
                inputEditTextRollNoOfStudent.setInputType(InputType.TYPE_CLASS_NUMBER);
                layout.addView(inputEditTextNameOfStudent);
                layout.addView(inputEditTextDivOfStudent);
                layout.addView(inputEditTextRollNoOfStudent);*/
        final TextView textViewPresenty = new TextView(context);
        layout.addView(textViewPresenty);
        textViewPresenty.setTextSize(18);
        textViewPresenty.setText(Html.fromHtml(StatsActivity.presentyClassWise.get(i)));
        Log.d("QWERT", String.valueOf(StatsActivity.presentyClassWise));
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                //  Action for 'NO' Button
                dialog.cancel();
            }
        });
        //Creating dialog box
        AlertDialog alert = builder.create();
        //Setting the title manually
        alert.setTitle("Past classes\n");
        alert.setView(layout);
        drawable = ContextCompat.getDrawable(context,  R.drawable.grey_alert_dialogue_background);
        alert.getWindow().setBackgroundDrawable(drawable);
        alert.show();
        WindowManager.LayoutParams lp = alert.getWindow().getAttributes();
        lp.dimAmount = 0.75f;
        alert.getWindow().setAttributes(lp);
    }
}
