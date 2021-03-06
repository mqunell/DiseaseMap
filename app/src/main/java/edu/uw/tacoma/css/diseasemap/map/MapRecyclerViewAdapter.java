package edu.uw.tacoma.css.diseasemap.map;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import edu.uw.tacoma.css.diseasemap.ColorsActivity;
import edu.uw.tacoma.css.diseasemap.R;
import edu.uw.tacoma.css.diseasemap.database_connection.DiseaseRecord;
import edu.uw.tacoma.css.diseasemap.database_connection.UserPrefsDB;

import static edu.uw.tacoma.css.diseasemap.account.MainActivity.EMAIL_ADDRESS;

/**
 * {@link RecyclerView.Adapter} that can display MapItems
 */
public class MapRecyclerViewAdapter extends RecyclerView.Adapter<MapRecyclerViewAdapter.ViewHolder> {

    private DiseaseRecord mDiseaseRecord;
    private int mWeek;
    private UserPrefsDB mUserPrefsDB;

    public MapRecyclerViewAdapter(DiseaseRecord diseaseRecord, int week) {
        mDiseaseRecord = diseaseRecord;
        mWeek = week;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_map_item, parent, false);

        mUserPrefsDB = new UserPrefsDB(parent.getContext());

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        Map<String, DiseaseRecord.WeekInfo> map = mDiseaseRecord.getInfoForWeek(mWeek);
        List<String> keyList = new ArrayList<>(map.keySet());
        Collections.sort(keyList);


        DiseaseRecord.WeekInfo week = map.get(keyList.get(position));

        holder.locationTextView.setText(week.getReportingArea());
        holder.weekTextView.setText("Infections this week: " + week.getInfected());
        holder.seasonTextView.setText("Cumulative infections: " + week.getCumulativeInfected());

        //The current lowest/highest cumulative infection.
        Integer highest = mDiseaseRecord.getMaxForWeek(week.getWeek()).getInfected();
        Integer lowest = mDiseaseRecord.getMinForWeek(week.getWeek()).getInfected();

        Integer greatestRange = highest - lowest;

        Integer thisRange = week.getInfected() - lowest;
        float ratio;
        if (greatestRange > 0) {
            ratio = ((float) thisRange) / ((float) greatestRange);
        } else {
            ratio = 0;
        }

        String email = holder.mImageView.getContext()
                .getSharedPreferences(holder.mImageView
                        .getContext().getString(R.string.app),
                        Context.MODE_PRIVATE).getString(EMAIL_ADDRESS, "");

        List<Integer> savedColors = new ArrayList<>();
        if (mUserPrefsDB.getColors(email).size() > 0) {
            savedColors = mUserPrefsDB.getColors(email);
        } else {
            savedColors.add(0x00cd00);
            savedColors.add(0xff0000);
        }

        //SharedPreferences prefs = holder.mImageView.getContext().getSharedPreferences(holder.mImageView.getContext().getString(R.string.app), Context.MODE_PRIVATE);

        Integer fromColor = savedColors.get(0);//prefs.getInt(ColorsActivity.SELECTED_COOL_COLOR, 0x00ff00);
        Integer toColor = savedColors.get(1);//prefs.getInt(ColorsActivity.SELECTED_WARM_COLOR, 0xff0000);
        int color = interpolateColor(fromColor, toColor, ratio);


        //Edit the drawable
        Bitmap bitmap = BitmapFactory.decodeResource(holder.mImageView.getContext().getResources(),
                R.drawable.circle);

        bitmap = bitmap.copy(Bitmap.Config.ARGB_8888, true);

        int[] allpixels = new int[bitmap.getHeight() * bitmap.getWidth()];

        bitmap.getPixels(allpixels, 0, bitmap.getWidth(), 0, 0, bitmap.getWidth(), bitmap.getHeight());

        for (int i = 0; i < allpixels.length; i++) {
            if (allpixels[i] == Color.WHITE) {
                allpixels[i] = color;
            }
        }

        bitmap.setPixels(allpixels, 0, bitmap.getWidth(), 0, 0, bitmap.getWidth(), bitmap.getHeight());

        holder.mImageView.setImageDrawable(new BitmapDrawable(holder.mImageView.getContext().getResources(), bitmap));
    }

    private int interpolateColor(int a, int b, float proportion) {
        float[] hsva = new float[3];
        float[] hsvb = new float[3];
        Color.colorToHSV(a, hsva);
        Color.colorToHSV(b, hsvb);

        for (int i = 0; i < 3; i++) {
            hsvb[i] = interpolate(hsva[i], hsvb[i], proportion);
        }

        return Color.HSVToColor(hsvb);
    }

    private float interpolate(float a, float b, float proportion) {
        return (a + ((b - a) * proportion));
    }

    @Override
    public int getItemCount() {
        if (mDiseaseRecord != null) {
            return mDiseaseRecord.getInfoForWeek(mWeek).keySet().size();
        } else {
            return 0;
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final ImageView mImageView;
        public final TextView locationTextView;
        public final TextView seasonTextView;
        public final TextView weekTextView;

        public ViewHolder(View view) {
            super(view);
            mImageView = view.findViewById(R.id.imageView);
            locationTextView = view.findViewById(R.id.locationTextView);
            seasonTextView = view.findViewById(R.id.seasonTextView);
            weekTextView = view.findViewById(R.id.weekTextView);
        }
    }
}
