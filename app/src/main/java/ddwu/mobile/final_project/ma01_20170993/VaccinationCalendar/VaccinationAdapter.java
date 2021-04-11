package ddwu.mobile.final_project.ma01_20170993.VaccinationCalendar;

import android.content.Context;
import android.database.Cursor;

import java.util.Calendar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import java.util.Date;
import java.text.SimpleDateFormat;

import ddwu.mobile.final_project.ma01_20170993.R;

public class VaccinationAdapter extends CursorAdapter {

    int layout;
    LayoutInflater inflater;
    Cursor cursor;
    String birth;

    public VaccinationAdapter(Context context, int layout, Cursor c, int flags) {
        super(context, c, flags);
        this.layout = layout;
        inflater = (LayoutInflater)context.getSystemService((Context.LAYOUT_INFLATER_SERVICE));
        cursor = c;
    }

    public void setBirth(String birth){
        this.birth = birth;
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        View view = inflater.inflate(layout, parent, false);
        ViewHolder viewHolder = new ViewHolder();
        viewHolder.tvDisease = view.findViewById(R.id.tvDisease);
        viewHolder.tvVaccination = view.findViewById(R.id.tvVaccination);
        viewHolder.tvTerm = view.findViewById(R.id.tvTerm);
        view.setTag(viewHolder);
        return view;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        ViewHolder viewHolder = (ViewHolder)view.getTag();

        viewHolder.tvDisease.setText(cursor.getString(cursor.getColumnIndex(VaccinationDBHelper.COL_DISEASE)));
        viewHolder.tvVaccination.setText(cursor.getString(cursor.getColumnIndex(VaccinationDBHelper.COL_VACCINATION)));
        String term = cursor.getString(cursor.getColumnIndex(VaccinationDBHelper.COL_TERM));
        String[] t = term.split(" ");
        if(t.length != 1){
            String[] t2 = t[1].split("-");
            String out = t[0] + "\n";

            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            SimpleDateFormat format2 = new SimpleDateFormat("yyyy.MM.dd");
            try {
                for(int i = 0; i < t2.length; i++){
                    if(i != 0)
                        out += "\n~\n";
                    Calendar cal = Calendar.getInstance();
                    Date date = format.parse(birth);
                    cal.setTime(date);
                    cal.add(Calendar.MONTH, Integer.parseInt(t2[i])); //월 더하기
                    out += format2.format(cal.getTime());
                }
                viewHolder.tvTerm.setText(out);
            }catch (Exception e){};

        }else {
            viewHolder.tvTerm.setText(term);
        }
    }

    static class ViewHolder {
        public TextView tvDisease = null;
        public TextView tvVaccination = null;
        public TextView tvTerm = null;
    }

}
