package ddwu.mobile.final_project.ma01_20170993;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CursorAdapter;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.io.File;

import ddwu.mobile.final_project.ma01_20170993.R;
import ddwu.mobile.final_project.ma01_20170993.MemoryRegistration.InsertMemoryActivity;
import ddwu.mobile.final_project.ma01_20170993.MemoryRegistration.MemoryAdapter;
import ddwu.mobile.final_project.ma01_20170993.MemoryRegistration.MemoryDBHelper;
import ddwu.mobile.final_project.ma01_20170993.MemoryRegistration.MemoryDto;
import ddwu.mobile.final_project.ma01_20170993.MemoryRegistration.ViewMemoryActivity;

public class FragmentMyroom extends Fragment {
    ListView lvMemorys = null;
    MemoryDBHelper helper;
    Cursor cursor;
    MemoryAdapter memoryAdapter;
    int flag = 0;

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_myroom, container, false);

        lvMemorys = (ListView) view.findViewById(R.id.lvMemory);
        helper = new MemoryDBHelper(getActivity());
        memoryAdapter = new MemoryAdapter(getActivity(), R.layout.custom_listview_memory, null, CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER);
        lvMemorys.setAdapter(memoryAdapter);

        // 툴바
        setHasOptionsMenu(true);

        // 리스트 뷰 클릭 처리 : 선택한 추억 보기
        lvMemorys.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getActivity(), ViewMemoryActivity.class);
                intent.putExtra("id", Long.toString(id));
                getActivity().startActivityForResult(intent, 2);
            }
        });

        // 리스트 뷰 롱클릭 처리 : 선택한 추억 삭제
        lvMemorys.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, final long id) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                final MemoryDto dto = getMemory(id);
                builder.setTitle("추억 삭제")
                        .setMessage(dto.getTitle() + "을(를) 삭제하시겠습니까?")
                        .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if(dto.getImage() != null) {
                                    String fileName = Uri.parse(dto.getImage()).getLastPathSegment();
                                    File file = new File(getActivity().getFilesDir() + "/" + fileName);
                                    file.delete();
                                }
                                SQLiteDatabase db = helper.getWritableDatabase();
                                String whereClause = "_id=?";
                                String[] whereArgs = new String[]{String.valueOf(id)};
                                db.delete(MemoryDBHelper.TABLE_NAME, whereClause, whereArgs);
                                helper.close();
                                readAllMemorys();
                            }
                        })
                        .setNegativeButton("취소", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });
                builder.show();
                return true;
            }
        });

        return view;
    }

    // id에 해당하는 행을 가져와 MemoryDto에 담아 전달
    public MemoryDto getMemory(long id){
        SQLiteDatabase db = helper.getReadableDatabase();
        Cursor cursor = db.rawQuery("select * from " + MemoryDBHelper.TABLE_NAME + " where _id=" + id, null);
        MemoryDto dto = new MemoryDto();
        while(cursor.moveToNext()){
            dto.setId(cursor.getInt(cursor.getColumnIndex("_id")));
            dto.setTitle(cursor.getString(cursor.getColumnIndex(MemoryDBHelper.COL_TITLE)));
            dto.setDate(cursor.getString(cursor.getColumnIndex(MemoryDBHelper.COL_DATE)));
            dto.setImage(cursor.getString(cursor.getColumnIndex(MemoryDBHelper.COL_IMAGE)));
            dto.setMemo(cursor.getString(cursor.getColumnIndex(MemoryDBHelper.COL_MEMO)));
        }
        cursor.close();
        helper.close();

        return dto;
    }

    public void readAllMemorys(){
        SQLiteDatabase db = helper.getReadableDatabase();
        cursor = db.rawQuery("select * from " + MemoryDBHelper.TABLE_NAME, null);

        memoryAdapter.changeCursor(cursor);
        helper.close();
    }

    public void onResume(){
        super.onResume();
        getActivity().invalidateOptionsMenu();
        readAllMemorys();
        flag = 0;
    }

    public void onDestroy() {
        super.onDestroy();
        // cursor 사용 종료
        if (cursor != null) cursor.close();
    }

    // 툴바 : 오른쪽에 + 버튼 표시
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater){
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.toolbar_new, menu);
    }

    // 툴바 클릭
    public boolean onOptionsItemSelected(MenuItem item){
        switch(item.getItemId()) {
            case R.id.toolbar_new:
                Intent intent = new Intent(getActivity(), InsertMemoryActivity.class);
                startActivity(intent);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode == getActivity().RESULT_OK){
            switch (requestCode){
                case 2:
                    flag = (data.getStringExtra("result").equals("OK"))? 0: 1;
                    break;
            }
        }
    }

}
