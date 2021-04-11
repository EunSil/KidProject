package ddwu.mobile.final_project.ma01_20170993.MemoryRegistration;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import ddwu.mobile.final_project.ma01_20170993.FileManager.ImageFileManager;
import ddwu.mobile.final_project.ma01_20170993.R;

public class MemoryDBHelper extends SQLiteOpenHelper {
	
	private final static String DB_NAME = "memory_db";
	public final static String TABLE_NAME = "memory_table";
	public final static String COL_ID = "_id";
    public final static String COL_TITLE = "title"; //제목
	public final static String COL_DATE = "date"; //날짜
	public final static String COL_MEMO = "memo"; //메모
	public final static String COL_IMAGE = "image"; //메모
	private ImageFileManager imageFileManager = null;

	public MemoryDBHelper(Context context) {
		super(context, DB_NAME, null, 1);

		imageFileManager = new ImageFileManager(context);
		Bitmap bitmap = BitmapFactory.decodeResource(context.getApplicationContext().getResources(), R.drawable.noimage);
		String fileName = imageFileManager.saveBitmapToInternal(bitmap, "noImage");
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL("create table " + TABLE_NAME + " ( " + COL_ID + " integer primary key autoincrement,"
				+ COL_TITLE + " TEXT, " + COL_DATE + " TEXT, " + COL_MEMO + " TEXT, "  + COL_IMAGE + " TEXT);");
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table " + TABLE_NAME);
        onCreate(db);
	}

}
