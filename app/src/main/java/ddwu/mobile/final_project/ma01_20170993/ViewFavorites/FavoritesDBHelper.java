package ddwu.mobile.final_project.ma01_20170993.ViewFavorites;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class FavoritesDBHelper extends SQLiteOpenHelper {
	
	private final static String DB_NAME = "favorites_db";
	public final static String TABLE_NAME = "favorites_table";
	public final static String COL_ID = "_id";
    public final static String COL_TITLE = "title"; //제목
	public final static String COL_IMAGELINK = "imagelink"; //이미지주소
	public final static String COL_IMAGE = "image"; //이미지주소
	public final static String COL_ADDR = "addr"; //주소
	public final static String COL_PLACE = "place"; //장소
	public final static String COL_TEL = "tel"; //전화번호
	public final static String COL_TIME = "time"; //공연시간
	public final static String COL_HOME = "home"; //홈페이지
	public final static String COL_AGE = "age"; //관람연령
	public final static String COL_CONTENTID = "contentId";
	public final static String COL_CONTENTTYPEID = "contTypeId";
	public final static String COL_OPENDATE = "opendate";
	public final static String COL_ENDDATE = "enddate";

	public FavoritesDBHelper(Context context) {
		super(context, DB_NAME, null, 1);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL("create table " + TABLE_NAME + " ( " + COL_ID + " integer primary key autoincrement,"
				+ COL_TITLE + " TEXT, " + COL_IMAGELINK + " TEXT, " + COL_IMAGE + " TEXT, " + COL_ADDR + " TEXT, "  + COL_PLACE + " TEXT, " + COL_TEL + " TEXT, " + COL_TIME + " TEXT, " + COL_HOME + " TEXT, " + COL_AGE + " TEXT, " + COL_CONTENTID + " TEXT, " + COL_CONTENTTYPEID + " TEXT, " +  COL_OPENDATE + " TEXT, " +  COL_ENDDATE + " TEXT);");
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table " + TABLE_NAME);
        onCreate(db);
	}

}
