package ddwu.mobile.final_project.ma01_20170993.VaccinationCalendar;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class VaccinationDBHelper extends SQLiteOpenHelper {

	private final static String DB_NAME = "vaccination_db";
	public final static String TABLE_NAME = "vaccination_table";
	public final static String COL_ID = "_id";
    public final static String COL_MONTH = "month"; //개월
	public final static String COL_DISEASE = "disease"; //대상감염병
	public final static String COL_VACCINATION = "vaccination"; //백신종류및 방법
	public final static String COL_TERM = "term"; //기간

	public VaccinationDBHelper(Context context) {
		super(context, DB_NAME, null, 1);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL("create table " + TABLE_NAME + " ( " + COL_ID + " integer primary key autoincrement,"
				+ COL_MONTH + " TEXT, " + COL_DISEASE + " TEXT, " + COL_VACCINATION + " TEXT, " + COL_TERM + " TEXT);");

		db.execSQL("insert into " + TABLE_NAME + " values (null, '0', '결핵', 'BCG', '1회 0-1');");
		db.execSQL("insert into " + TABLE_NAME + " values (null, '0', 'B형간염', 'Hepβ', '1차 0');");

		db.execSQL("insert into " + TABLE_NAME + " values (null, '1', 'B형간염', 'Hepβ', '2차 1');");

		db.execSQL("insert into " + TABLE_NAME + " values (null, '2', '디프테리아\n파상풍\n백일해', 'DTap', '1차 2');");
		db.execSQL("insert into " + TABLE_NAME + " values (null, '2', '폴리오', 'IPV', '1차 2');");
		db.execSQL("insert into " + TABLE_NAME + " values (null, '2', 'b형헤모필루스\n인풀루엔자', 'Hib', '1차 2');");
		db.execSQL("insert into " + TABLE_NAME + " values (null, '2', '폐렴구균', 'PCV', '1차 2');");
		db.execSQL("insert into " + TABLE_NAME + " values (null, '2', '로타바이러스\n감염증', 'RV1', '1차 2');");
		db.execSQL("insert into " + TABLE_NAME + " values (null, '2', '로타바이러스\n감염증', 'RV5', '1차 2');");

		db.execSQL("insert into " + TABLE_NAME + " values (null, '4', '디프테리아\n파상풍\n백일해', 'DTap', '2차 4');");
		db.execSQL("insert into " + TABLE_NAME + " values (null, '4', '폴리오', 'IPV', '2차 4');");
		db.execSQL("insert into " + TABLE_NAME + " values (null, '4', 'b형헤모필루스\n인풀루엔자', 'Hib', '2차 4');");
		db.execSQL("insert into " + TABLE_NAME + " values (null, '4', '폐렴구균', 'PCV', '2차 4');");
		db.execSQL("insert into " + TABLE_NAME + " values (null, '4', '로타바이러스\n감염증', 'RV1', '2차 4');");
		db.execSQL("insert into " + TABLE_NAME + " values (null, '4', '로타바이러스\n감염증', 'RV5', '2차 4');");

		db.execSQL("insert into " + TABLE_NAME + " values (null, '6', 'B형간염', 'Hepβ', '3차 6');");
		db.execSQL("insert into " + TABLE_NAME + " values (null, '6', '디프테리아\n파상풍\n백일해', 'DTap', '3차 6');");
		db.execSQL("insert into " + TABLE_NAME + " values (null, '6', '폴리오', 'IPV', '3차 6');");
		db.execSQL("insert into " + TABLE_NAME + " values (null, '6', 'b형헤모필루스\n인풀루엔자', 'Hib', '3차 6');");
		db.execSQL("insert into " + TABLE_NAME + " values (null, '6', '폐렴구균', 'PCV', '3차 6');");
		db.execSQL("insert into " + TABLE_NAME + " values (null, '6', '로타바이러스\n감염증', 'RV5', '3차 6');");
		db.execSQL("insert into " + TABLE_NAME + " values (null, '6', '인풀루엔자', 'IIV', '우선접종권장대상자 6-60');");

		db.execSQL("insert into " + TABLE_NAME + " values (null, '12', 'b형헤모필루스\n인풀루엔자', 'Hib', '추4차 12-16');");
		db.execSQL("insert into " + TABLE_NAME + " values (null, '12', '폐렴구균', 'PCV', '추4차 12-16');");
		db.execSQL("insert into " + TABLE_NAME + " values (null, '12', '인풀루엔자', 'IIV', '우선접종권장대상자 6-60');");
		db.execSQL("insert into " + TABLE_NAME + " values (null, '12', '홍역\n유행성\n이하선염 품진', 'MMR', '1차 12-16');");
		db.execSQL("insert into " + TABLE_NAME + " values (null, '12', '수두', 'VAR', '1차 12-16');");
		db.execSQL("insert into " + TABLE_NAME + " values (null, '12', 'A형간염', 'HeapA', '1차 12-24');");
		db.execSQL("insert into " + TABLE_NAME + " values (null, '12', '일본뇌염', 'IJEV', '1-2차 12-24');");
		db.execSQL("insert into " + TABLE_NAME + " values (null, '12', '일본뇌염', 'LJEV', '1차 12-24');");

		db.execSQL("insert into " + TABLE_NAME + " values (null, '15', '디프테리아\n파상풍\n백일해', 'DTap', '추4차 15-19');");
		db.execSQL("insert into " + TABLE_NAME + " values (null, '15', 'b형헤모필루스\n인풀루엔자', 'Hib', '추4차 12-16');");
		db.execSQL("insert into " + TABLE_NAME + " values (null, '15', '폐렴구균', 'PCV', '추4차 12-16');");
		db.execSQL("insert into " + TABLE_NAME + " values (null, '15', '인풀루엔자', 'IIV', '우선접종권장대상자 6-60');");
		db.execSQL("insert into " + TABLE_NAME + " values (null, '15', '홍역\n유행성\n이하선염 품진', 'MMR', '1차 12-16');");
		db.execSQL("insert into " + TABLE_NAME + " values (null, '15', '수두', 'VAR', '1차 12-16');");
		db.execSQL("insert into " + TABLE_NAME + " values (null, '15', 'A형간염', 'HeapA', '1차 12-24');");
		db.execSQL("insert into " + TABLE_NAME + " values (null, '15', '일본뇌염', 'IJEV', '1-2차 12-24');");
		db.execSQL("insert into " + TABLE_NAME + " values (null, '15', '일본뇌염', 'LJEV', '1차 12-24');");

		db.execSQL("insert into " + TABLE_NAME + " values (null, '18', '디프테리아\n파상풍\n백일해', 'DTap', '추4차 15-19');");
		db.execSQL("insert into " + TABLE_NAME + " values (null, '18', '인풀루엔자', 'IIV', '우선접종권장대상자 6-60');");
		db.execSQL("insert into " + TABLE_NAME + " values (null, '18', 'A형간염', 'HeapA', '1차 12-24');");
		db.execSQL("insert into " + TABLE_NAME + " values (null, '18', '일본뇌염', 'IJEV', '1-2차 12-24');");
		db.execSQL("insert into " + TABLE_NAME + " values (null, '18', '일본뇌염', 'LJEV', '1차 12-24');");

		db.execSQL("insert into " + TABLE_NAME + " values (null, '24', '폐렴구균', 'PPSV', '고위험군에 한하여 접종 24-156');");
		db.execSQL("insert into " + TABLE_NAME + " values (null, '24', '인풀루엔자', 'IIV', '우선접종권장대상자 6-60');");
		db.execSQL("insert into " + TABLE_NAME + " values (null, '24', 'A형간염', 'HeapA', '2차 1차 접종 6개월 후');");
		db.execSQL("insert into " + TABLE_NAME + " values (null, '24', '일본뇌염', 'IJEV', '1-2차 12-24');");
		db.execSQL("insert into " + TABLE_NAME + " values (null, '24', '일본뇌염', 'LJEV', '1차 12-24');");

		db.execSQL("insert into " + TABLE_NAME + " values (null, '36', '폐렴구균', 'PPSV', '고위험군에 한하여 접종 24-156');");
		db.execSQL("insert into " + TABLE_NAME + " values (null, '36', '인풀루엔자', 'IIV', '우선접종권장대상자 6-60');");
		db.execSQL("insert into " + TABLE_NAME + " values (null, '36', 'A형간염', 'HeapA', '2차 1차 접종 6개월 후');");
		db.execSQL("insert into " + TABLE_NAME + " values (null, '36', '일본뇌염', 'IJEV', '3차 2차 접종 후 12개월 후');");
		db.execSQL("insert into " + TABLE_NAME + " values (null, '36', '일본뇌염', 'LJEV', '2차 1차 접종 후 12개월 후');");

		db.execSQL("insert into " + TABLE_NAME + " values (null, '48', '디프테리아\n파상풍\n백일해', 'DTap', '추5차 48-84');");
		db.execSQL("insert into " + TABLE_NAME + " values (null, '48', '폴리오', 'IPV', '추4차 48-84');");
		db.execSQL("insert into " + TABLE_NAME + " values (null, '48', '폐렴구균', 'PPSV', '고위험군에 한하여 접종 24-156');");
		db.execSQL("insert into " + TABLE_NAME + " values (null, '48', '인풀루엔자', 'IIV', '우선접종권장대상자 6-60');");
		db.execSQL("insert into " + TABLE_NAME + " values (null, '48', '홍역\n유행성\n이하선염 품진', 'MMR', '2차 48-156');");

		db.execSQL("insert into " + TABLE_NAME + " values (null, '72', '디프테리아\n파상풍\n백일해', 'DTap', '추5차 48-84');");
		db.execSQL("insert into " + TABLE_NAME + " values (null, '72', '폴리오', 'IPV', '추4차 48-84');");
		db.execSQL("insert into " + TABLE_NAME + " values (null, '72', '폐렴구균', 'PPSV', '고위험군에 한하여 접종 24-156');");
		db.execSQL("insert into " + TABLE_NAME + " values (null, '72', '홍역\n유행성\n이하선염 품진', 'MMR', '2차 48-156');");
		db.execSQL("insert into " + TABLE_NAME + " values (null, '72', '일본뇌염', 'IJEV', '추4차 72');");
	}
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table " + TABLE_NAME);
        onCreate(db);
	}

}
