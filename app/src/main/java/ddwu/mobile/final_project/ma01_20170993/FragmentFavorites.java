package ddwu.mobile.final_project.ma01_20170993;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cursoradapter.widget.CursorAdapter;
import androidx.fragment.app.Fragment;

import java.io.File;
import java.io.Serializable;

import ddwu.mobile.final_project.ma01_20170993.R;
import ddwu.mobile.final_project.ma01_20170993.FindEvent.EventDetailActivity;
import ddwu.mobile.final_project.ma01_20170993.FindEvent.EventDetailDto;
import ddwu.mobile.final_project.ma01_20170993.FindEvent.EventDto;
import ddwu.mobile.final_project.ma01_20170993.ViewFavorites.FavoritesAdapter;
import ddwu.mobile.final_project.ma01_20170993.ViewFavorites.FavoritesDBHelper;

public class FragmentFavorites extends Fragment {

    ListView lvFavorites = null;
    FavoritesDBHelper helper;
    FavoritesAdapter favoritesAdapter;
    Cursor cursor;

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_favorites, container, false);

        lvFavorites = (ListView) view.findViewById(R.id.lvFavorites);
        helper = new FavoritesDBHelper(getActivity());
        favoritesAdapter = new FavoritesAdapter(getActivity(), R.layout.custom_listview_memory, null, CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER);
        lvFavorites.setAdapter(favoritesAdapter);

        lvFavorites.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(!isOnline()){
                    Toast.makeText(getActivity(), "인터넷을 연결해주세요.", Toast.LENGTH_SHORT).show();
                }
                else {
                    EventDto d = new EventDto();
                    EventDetailDto detailDto = getFavorites(id);
                    d.setAddr1(detailDto.getAddr());
                    d.setContentTypeId(detailDto.getContentTypeId());
                    d.setContentid(detailDto.getContentId());
                    d.setFirstimage(detailDto.getImageLink());
                    d.setTitle(detailDto.getTitle());
                    d.setTel(detailDto.getTel());
                    d.setEventstartdate(detailDto.getEventstartdate());
                    d.setEventenddate(detailDto.getEventenddate());
                    Intent intent = new Intent(getActivity(), EventDetailActivity.class);
                    intent.putExtra("event", (Serializable) d);
                    startActivity(intent);
                }
            }
        });

        // 리스트 뷰 롱클릭 처리
        lvFavorites.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, final long id) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                final EventDetailDto dto = getFavorites(id);
                builder.setTitle("즐겨찾기 삭제")
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
                                db.delete(FavoritesDBHelper.TABLE_NAME, whereClause, whereArgs);
                                helper.close();
                                readAllFavorites();
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

    public EventDetailDto getFavorites(long id){
        SQLiteDatabase db = helper.getReadableDatabase();
        Cursor cursor = db.rawQuery("select * from " + FavoritesDBHelper.TABLE_NAME + " where _id=" + id, null);
        EventDetailDto dto = new EventDetailDto();
        while(cursor.moveToNext()){
            dto.set_id(cursor.getInt(cursor.getColumnIndex("_id")));
            dto.setTitle(cursor.getString(cursor.getColumnIndex(FavoritesDBHelper.COL_TITLE)));
            dto.setImage(cursor.getString(cursor.getColumnIndex(FavoritesDBHelper.COL_IMAGE)));
            dto.setImageLink(cursor.getString(cursor.getColumnIndex(FavoritesDBHelper.COL_IMAGELINK)));
            dto.setAddr(cursor.getString(cursor.getColumnIndex(FavoritesDBHelper.COL_ADDR)));
            dto.setTel(cursor.getString(cursor.getColumnIndex(FavoritesDBHelper.COL_TEL)));
            dto.setContentId(cursor.getString(cursor.getColumnIndex(FavoritesDBHelper.COL_CONTENTID)));
            dto.setContentTypeId(cursor.getString(cursor.getColumnIndex(FavoritesDBHelper.COL_CONTENTTYPEID)));
            dto.setEventenddate(cursor.getString(cursor.getColumnIndex(FavoritesDBHelper.COL_ENDDATE)));
            dto.setEventstartdate(cursor.getString(cursor.getColumnIndex(FavoritesDBHelper.COL_OPENDATE)));
        }
        cursor.close();
        helper.close();

        return dto;
    }

    public void readAllFavorites(){
        SQLiteDatabase db = helper.getReadableDatabase();
        cursor = db.rawQuery("select * from " + FavoritesDBHelper.TABLE_NAME, null);

        favoritesAdapter.changeCursor(cursor);
        helper.close();
    }

    public void onResume(){
        super.onResume();
        readAllFavorites();
    }

    public void onDestroy() {
        super.onDestroy();
        // cursor 사용 종료
        if (cursor != null) cursor.close();
    }

    /* 네트워크 관련 메소드 */
    /* 네트워크 환경 조사 */
    private boolean isOnline() {
        ConnectivityManager connMgr = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        return (networkInfo != null && networkInfo.isConnected());
    }
}
