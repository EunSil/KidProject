package ddwu.mobile.final_project.ma01_20170993.VaccinationCalendar;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.DatePickerDialog;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import java.util.Calendar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CursorAdapter;
import android.widget.DatePicker;
import android.widget.ListView;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;

import ddwu.mobile.final_project.ma01_20170993.R;

public class ScheduleActivity extends AppCompatActivity {

    Button btnDate;
    int m[] = {0, 1, 2, 4, 6, 12, 15, 18, 24, 36, 48, 72};

    ListView lvSchedules = null;
    VaccinationDBHelper helper;
    Cursor cursor;
    VaccinationAdapter scheduleAdapter;

    TextView tvAge1;
    TextView tvAge2;
    TextView tvDesc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule);

        // 툴바
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setDisplayHomeAsUpEnabled(true);

        btnDate = findViewById(R.id.btnBirthDate);
        Date date = new Date(System.currentTimeMillis());
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        String d = format.format(date);
        btnDate.setText(d);
        btnDate.setOnClickListener(mDateListener);

        lvSchedules = (ListView) findViewById(R.id.lvschs);
        helper = new VaccinationDBHelper(this);
        scheduleAdapter = new VaccinationAdapter(this, R.layout.custom_listview_vaccination, null, CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER);
        lvSchedules.setAdapter(scheduleAdapter);

        tvAge1 = findViewById(R.id.tvAge1);
        tvAge2 = findViewById(R.id.tvAge2);
        tvDesc = findViewById(R.id.tvDesc);
    }

    // 날짜 버튼 클릭 : datePicker
    Button.OnClickListener mDateListener = new Button.OnClickListener(){
        public void onClick(View v){
            new DatePickerDialog(ScheduleActivity.this, R.style.DialogTheme, mDateSetListener, Integer.parseInt(btnDate.getText().toString().split("-")[0]), Integer.parseInt(btnDate.getText().toString().split("-")[1]) - 1, Integer.parseInt(btnDate.getText().toString().split("-")[2])).show();
        }
    };

    DatePickerDialog.OnDateSetListener mDateSetListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            Calendar cal = Calendar.getInstance();
            cal.set(Calendar.YEAR, year);
            cal.set(Calendar.MONTH, month);
            cal.set(Calendar.DATE, dayOfMonth);
            btnDate.setText(format.format(cal.getTime()));
        }
    };

    public void onClick(View v){
        switch (v.getId()){
            case R.id.btnBirthSearch:
                int diff = Integer.parseInt(getDiffMonth());
                Log.d("차이", Integer.toString(diff));
                if(diff <= 72) {
                    int i;
                    for (i = 0; i < m.length; i++) {
                        if (m[i] > diff) {
                            break;
                        }
                    }
                    scheduleAdapter.setBirth(btnDate.getText().toString());
                    tvAge1.setVisibility(View.VISIBLE);
                    tvAge2.setVisibility(View.VISIBLE);
                    tvAge2.setText(diff + "개월");
                    tvDesc.setVisibility(View.GONE);
                    lvSchedules.setVisibility(View.VISIBLE);
                    readAllMemorys(m[i-1]);
                }else{
                    tvAge1.setVisibility(View.GONE);
                    tvAge2.setVisibility(View.GONE);
                    tvDesc.setVisibility(View.VISIBLE);
                    lvSchedules.setVisibility(View.GONE);
                    tvDesc.setText("6세 이하인 어린이의 예방접종 일정만 제공합니다.");
                }
                break;
        }
    }

    public boolean onOptionsItemSelected(MenuItem item){
        switch(item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public String getDiffMonth(){
        try {
            String startDate = btnDate.getText().toString(); //시작날짜
            Date date = new Date(System.currentTimeMillis());
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            String d = format.format(date);
            String endDate = d;  //마지막 날짜

            String sd2 = startDate.substring(5, 7); //월

            // 날짜를 data타입으로 변경
            Date sDate = format.parse(startDate);
            Date eDate = format.parse(endDate);
            // 시간차이를 시간,분,초를 곱한 값으로 나누면 하루 단위가 나옴
            long diff = eDate.getTime() - sDate.getTime();
            long diffDays = diff / (24 * 60 * 60 * 1000) ;
            long difMonth = (diffDays+1)/30; //총개월수 ( 대략 30으로 나눴을때 나오는 개월수 )
            long chkNum = 0;
            int j=0;
            //개월수 체크 ( 시작한날짜월부터 위에서 대충 계산한 개월수까지 )
            //각 월별로 해당하는 월수에 맞게 더해줌
            for(int i=Integer.parseInt(sd2); j<difMonth; i++) {
                if(i==1 || i==3 || i==5 || i==7 || i==8 || i==10 || i==12 ) {
                    chkNum += 31;
                }else if(i==4 || i==6 || i==9 || i==11 ) {
                    chkNum += 30;
                }
                if(i==2) {
                    //윤달체크
                    if( ((Integer.parseInt(sd2))%400) == 0 ) {
                        chkNum+=29;
                    } else {
                        chkNum+=28;
                    }
                }
                j++;
                if(i>12) { i=1; j=j-1;}
            }
            long allMonth = (chkNum+1)/30; //진짜 총개월수
            if(diffDays < chkNum) {
                allMonth = allMonth-1; // 대충 구한개월수는 더많을수 있어서 1빼줘서 진짜 개월수를 구함
            }
            return Long.toString(difMonth);
        } catch (Exception e) {}
        return null;
    }

    public void readAllMemorys(int n){
        SQLiteDatabase db = helper.getReadableDatabase();
        String selection = "month=?";
        String[] selectArgs = new String[]{Integer.toString(n)};

        cursor = db.query(VaccinationDBHelper.TABLE_NAME, null, selection, selectArgs, null, null, null, null);
        scheduleAdapter.changeCursor(cursor);
        helper.close();
    }

    public void onDestroy() {
        super.onDestroy();
        // cursor 사용 종료
        if (cursor != null) cursor.close();
    }
}
