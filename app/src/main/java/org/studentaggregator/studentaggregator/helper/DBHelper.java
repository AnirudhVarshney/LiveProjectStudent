package org.studentaggregator.studentaggregator.helper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import org.studentaggregator.studentaggregator.model.Attendance;
import org.studentaggregator.studentaggregator.model.Event;
import org.studentaggregator.studentaggregator.model.Fee;
import org.studentaggregator.studentaggregator.model.Holiday;
import org.studentaggregator.studentaggregator.model.Query;
import org.studentaggregator.studentaggregator.model.Student;
import org.studentaggregator.studentaggregator.model.StudentResult;
import org.studentaggregator.studentaggregator.model.Task;
import org.studentaggregator.studentaggregator.model.Timetable;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;

/**
 * Created by Ray on 1/30/2016.
 */
public class DBHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "database.db";

    //------------------        Holiday     ---------------------------
    private static final String TABLE_NAME_HOLIDAY = "holiday";

    private static final String KEY_HOLIDAY_DATE = "date";
    private static final String KEY_HOLIDAY_DESCRIPTION = "description";


    /*  id name dob gender father mother class division house contact    */
    //------------------        Student     ---------------------------
    private static final String TABLE_NAME_STUDENT = "student";

    private static final String KEY_STUDENT_STUDENTID = "id";
    private static final String KEY_STUDENT_NAME = "name";
    private static final String KEY_STUDENT_DOB = "dob";
    private static final String KEY_STUDENT_GENDER = "gender";
    private static final String KEY_STUDENT_FATHER = "father";
    private static final String KEY_STUDENT_MOTHER = "mother";
    private static final String KEY_STUDENT_CLASS = "class";
    private static final String KEY_STUDENT_DIVISION = "division";
    private static final String KEY_STUDENT_HOUSE = "house";
    private static final String KEY_STUDENT_CONTACT = "contact";


    /*  studentid    status   description   date    time    */
    //------------------        attendance     ---------------------------
    private static final String TABLE_NAME_ATTENDANCE = "attendance";

    private static final String KEY_ATTENDANCE_STUDENTID = "studentid";
    private static final String KEY_ATTENDANCE_STATUS = "status";
    private static final String KEY_ATTENDANCE_DESCRIPTION = "description";
    private static final String KEY_ATTENDANCE_DATE = "date";
    private static final String KEY_ATTENDANCE_TIME = "time";

    //    studentId date amount description type
    //------------------        fee     ---------------------------
    private static final String TABLE_NAME_FEE = "fee";

    public static final String KEY_FEE_STUDENT_ID = "studentid";
    public static final String KEY_FEE_DATE = "date";
    public static final String KEY_FEE_AMOUNT = "amount";
    public static final String KEY_FEE_DESCRIPTION = "description";
    public static final String KEY_FEE_TYPE = "type";

    /*    id    title    description    date    images    */
    //------------------        event     ---------------------------
    private static final String TABLE_NAME_EVENT = "event";

    public static final String KEY_EVENT_ID = "id";
    public static final String KEY_EVENT_TITLE = "title";
    public static final String KEY_EVENT_DESCRIPTION = "description";
    public static final String KEY_EVENT_DATE = "date";
    public static final String KEY_EVENT_IMAGES = "images";

    /*  classOfStudent    division    date    title   description     filename  */
    //------------------        task     ---------------------------
    private static final String TABLE_NAME_TASK = "task";

    public static final String KEY_TASK_CLASS = "class";
    public static final String KEY_TASK_DIVISION = "division";
    public static final String KEY_TASK_DATE = "date";
    public static final String KEY_TASK_TITLE = "title";
    public static final String KEY_TASK_DESCRIPTION = "description";
    public static final String KEY_TASK_FILENAME = "filename";


    /*    studentid    title    description    date    data    */
    //------------------        result     ---------------------------
    private static final String TABLE_NAME_RESULT = "result";

    public static final String KEY_RESULT_STUDENT_ID = "studentid";
    public static final String KEY_RESULT_TITLE = "title";
    public static final String KEY_RESULT_DESCRIPTION = "description";
    public static final String KEY_RESULT_DATE = "date";
    public static final String KEY_RESULT_DATA = "data";


    /*    studentid    message    reply    date    */
    //------------------        Query     ---------------------------
    private static final String TABLE_NAME_QUERY = "query";

    public static final String KEY_QUERY_STUDENT_ID = "studentid";
    public static final String KEY_QUERY_MESSAGE = "title";
    public static final String KEY_QUERY_REPLY = "description";
    public static final String KEY_QUERY_DATE = "date";

    /*  classOfStudent   division    day  startTime   endTime     subject     */
    //------------------        Timetable     ---------------------------
    private static final String TABLE_NAME_TIMETABLE = "timetable";

    public static final String KEY_TIMETABLE_CLASS = "class";
    public static final String KEY_TIMETABLE_DIVISION = "division";
    public static final String KEY_TIMETABLE_DAY = "day";
    public static final String KEY_TIMETABLE_STARTTIME = "starttime";
    public static final String KEY_TIMETABLE_ENDTIME = "endtime";
    public static final String KEY_TIMETABLE_SUBJECT = "subject";


    public DBHelper(Context con) {
        super(con, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        /*----------------------------------------Holiday------------------------------------------------*/

        String CREATE_TABLE_HOLIDAY = "CREATE TABLE " + TABLE_NAME_HOLIDAY +
                "(" + KEY_HOLIDAY_DATE + " text, "
                + KEY_HOLIDAY_DESCRIPTION + " text"
                + ")";

        db.execSQL(CREATE_TABLE_HOLIDAY);
        MyUtils.logThis("DBHelper - Table Holiday created");

        /*----------------------------------------Student------------------------------------------------*/

        String CREATE_TABLE_STUDENT = "CREATE TABLE " + TABLE_NAME_STUDENT +
                "(" + KEY_STUDENT_STUDENTID + " text, "
                + KEY_STUDENT_NAME + " text, "
                + KEY_STUDENT_DOB + " text, "
                + KEY_STUDENT_GENDER + " text, "
                + KEY_STUDENT_FATHER + " text, "
                + KEY_STUDENT_MOTHER + " text, "
                + KEY_STUDENT_CLASS + " text, "
                + KEY_STUDENT_DIVISION + " text, "
                + KEY_STUDENT_HOUSE + " text, "
                + KEY_STUDENT_CONTACT + " text"
                + ")";
        db.execSQL(CREATE_TABLE_STUDENT);
        MyUtils.logThis("DBHelper - Table Student created");


        /*---------------------------------------Attendance-------------------------------------------------*/
        String CREATE_TABLE_ATTENDANCE = "CREATE TABLE " + TABLE_NAME_ATTENDANCE +
                "(" + KEY_ATTENDANCE_STUDENTID + " text, "
                + KEY_ATTENDANCE_STATUS + " text, "
                + KEY_ATTENDANCE_DESCRIPTION + " text, "
                + KEY_ATTENDANCE_DATE + " text, "
                + KEY_ATTENDANCE_TIME + " text"
                + ")";
        db.execSQL(CREATE_TABLE_ATTENDANCE);
        MyUtils.logThis("DBHelper - Table Attendance created");


        /*---------------------------------------Fee-------------------------------------------------*/
        String CREATE_TABLE_FEE = "CREATE TABLE " + TABLE_NAME_FEE +
                "(" + KEY_FEE_STUDENT_ID + " text, "
                + KEY_FEE_DATE + " text, "
                + KEY_FEE_AMOUNT + " text, "
                + KEY_FEE_DESCRIPTION + " text, "
                + KEY_FEE_TYPE + " text"
                + ")";
        db.execSQL(CREATE_TABLE_FEE);
        MyUtils.logThis("DBHelper - Table Fee created");


        /*---------------------------------------Event-------------------------------------------------*/
        Log.d("mylog", "DBHelper - creating table Event");
        String CREATE_TABLE_EVENT = "CREATE TABLE " + TABLE_NAME_EVENT +
                "(" + KEY_EVENT_ID + " text, "
                + KEY_EVENT_TITLE + " text, "
                + KEY_EVENT_DESCRIPTION + " text, "
                + KEY_EVENT_DATE + " text, "
                + KEY_EVENT_IMAGES + " text"
                + ")";

        db.execSQL(CREATE_TABLE_EVENT);
        Log.d("mylog", "DBHelper - Table event created");


         /*---------------------------------------Task-------------------------------------------------*/
        String CREATE_TABLE_TASK = "CREATE TABLE " + TABLE_NAME_TASK +
                "(" + KEY_TASK_CLASS + " text, "
                + KEY_TASK_DIVISION + " text, "
                + KEY_TASK_DATE + " text, "
                + KEY_TASK_TITLE + " text, "
                + KEY_TASK_DESCRIPTION + " text, "
                + KEY_TASK_FILENAME + " text"
                + ")";

        db.execSQL(CREATE_TABLE_TASK);
        MyUtils.logThis("DBHelper - Table Task created");

        /*    studentid    title    description    date    data    */
        /*---------------------------------------Result-------------------------------------------------*/
        String CREATE_TABLE_RESULT = "CREATE TABLE " + TABLE_NAME_RESULT +
                "(" + KEY_RESULT_STUDENT_ID + " text, "
                + KEY_RESULT_TITLE + " text, "
                + KEY_RESULT_DESCRIPTION + " text, "
                + KEY_RESULT_DATE + " text, "
                + KEY_RESULT_DATA + " text"
                + ")";
        db.execSQL(CREATE_TABLE_RESULT);
        MyUtils.logThis("DBHelper - Table Result created");

        /*    studentid    message    reply    date    */
        /*---------------------------------------Query-------------------------------------------------*/
        String CREATE_TABLE_QUERY = "CREATE TABLE " + TABLE_NAME_QUERY +
                "(" + KEY_QUERY_STUDENT_ID + " text, "
                + KEY_QUERY_MESSAGE + " text, "
                + KEY_QUERY_REPLY + " text, "
                + KEY_QUERY_DATE + " text"
                + ")";
        db.execSQL(CREATE_TABLE_QUERY);
        MyUtils.logThis("DBHelper - Table Query created");


        /*  classOfStudent   division    day  startTime   endTime     subject     */
        /*---------------------------------------Timetable-------------------------------------------------*/
        String CREATE_TABLE_TIMETABLE = "CREATE TABLE " + TABLE_NAME_TIMETABLE +
                "(" + KEY_TIMETABLE_CLASS + " text, "
                + KEY_TIMETABLE_DIVISION + " text, "
                + KEY_TIMETABLE_DAY + " text, "
                + KEY_TIMETABLE_STARTTIME + " text, "
                + KEY_TIMETABLE_ENDTIME + " text, "
                + KEY_TIMETABLE_SUBJECT + " text"
                + ")";
        db.execSQL(CREATE_TABLE_TIMETABLE);
        MyUtils.logThis("DBHelper - Table Timetable created");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_HOLIDAY);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_STUDENT);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_ATTENDANCE);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_FEE);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_EVENT);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_TASK);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_RESULT);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_QUERY);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_TIMETABLE);
        onCreate(db);
        MyUtils.logThis("DBHelper - Tables upgraded");
    }

    public void clear() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_HOLIDAY);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_STUDENT);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_ATTENDANCE);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_FEE);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_EVENT);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_TASK);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_RESULT);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_QUERY);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_TIMETABLE);
        onCreate(db);
    }

    /*  date description    */
    public void addHoliday(ArrayList<Holiday> holidayList) {
        SQLiteDatabase db = this.getWritableDatabase();
        for (int i = 0; i < holidayList.size(); i++) {
            ContentValues values = new ContentValues();
            values.put(KEY_HOLIDAY_DATE, holidayList.get(i).getDate());
            values.put(KEY_HOLIDAY_DESCRIPTION, holidayList.get(i).getDescription());
            db.insert(TABLE_NAME_HOLIDAY, null, values);
            MyUtils.logThis("DBHelper - Holiday added : values = " + values.toString());
        }
        db.close();
    }

    /*  id name dob gender father mother class division house contact    */
    public void addStudent(Student student) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_STUDENT_STUDENTID, student.getId());
        values.put(KEY_STUDENT_NAME, student.getName());
        values.put(KEY_STUDENT_DOB, student.getDob());
        values.put(KEY_STUDENT_GENDER, student.getGender());
        values.put(KEY_STUDENT_FATHER, student.getFather());
        values.put(KEY_STUDENT_MOTHER, student.getMother());
        values.put(KEY_STUDENT_CLASS, student.getClassOfStudent());
        values.put(KEY_STUDENT_DIVISION, student.getDivision());
        values.put(KEY_STUDENT_HOUSE, student.getHouse());
        values.put(KEY_STUDENT_CONTACT, student.getContact());
        db.insert(TABLE_NAME_STUDENT, null, values);
        MyUtils.logThis("DBHelper - Student added : values = " + values.toString());
        db.close();
    }

    /*  studentid    status   description   date    time    */
    public void addAttendance(ArrayList<Attendance> attendanceList) {
        SQLiteDatabase db = this.getWritableDatabase();
        for (int i = 0; i < attendanceList.size(); i++) {
            ContentValues values = new ContentValues();
            values.put(KEY_ATTENDANCE_STUDENTID, attendanceList.get(i).getStudentId());
            values.put(KEY_ATTENDANCE_STATUS, attendanceList.get(i).getStatus());
            values.put(KEY_ATTENDANCE_DESCRIPTION, attendanceList.get(i).getDesc());
            values.put(KEY_ATTENDANCE_DATE, attendanceList.get(i).getDate());
            values.put(KEY_ATTENDANCE_TIME, attendanceList.get(i).getTime());
            db.insert(TABLE_NAME_ATTENDANCE, null, values);
            MyUtils.logThis("DBHelper - Attendance added : values = " + values.toString());
        }
        db.close();
    }

    /*    studentid date amount description type  */
    public void addFee(ArrayList<Fee> arrayList) {
        SQLiteDatabase db = this.getWritableDatabase();
        for (int i = 0; i < arrayList.size(); i++) {
            ContentValues values = new ContentValues();
            values.put(KEY_FEE_STUDENT_ID, arrayList.get(i).getStudentId());
            values.put(KEY_FEE_DATE, arrayList.get(i).getDate());
            values.put(KEY_FEE_AMOUNT, arrayList.get(i).getAmount());
            values.put(KEY_FEE_DESCRIPTION, arrayList.get(i).getDescription());
            values.put(KEY_FEE_TYPE, arrayList.get(i).getType());
            db.insert(TABLE_NAME_FEE, null, values);
            MyUtils.logThis("DBHelper - fee added : values = " + values.toString());
        }
        db.close();
    }

    /*    id    title    description    date    images    */
    public void addEvent(ArrayList<Event> arrayList) {
        SQLiteDatabase db = this.getWritableDatabase();
        for (int i = 0; i < arrayList.size(); i++) {
            ContentValues values = new ContentValues();
            values.put(KEY_EVENT_ID, arrayList.get(i).getId());
            values.put(KEY_EVENT_TITLE, arrayList.get(i).getTitle());
            values.put(KEY_EVENT_DESCRIPTION, arrayList.get(i).getDescription());
            values.put(KEY_EVENT_DATE, arrayList.get(i).getDate());
            values.put(KEY_EVENT_IMAGES, String.valueOf(arrayList.get(i).getImages()));
            db.insert(TABLE_NAME_EVENT, null, values);

            MyUtils.logThis("DBHelper - event added : values = " + values.toString());
        }
        db.close();
    }

    /*  classOfStudent    division    date    title   description     filename  */
    public void addTask(ArrayList<Task> arrayList) {
        SQLiteDatabase db = this.getWritableDatabase();
        for (int i = 0; i < arrayList.size(); i++) {
            ContentValues values = new ContentValues();
            values.put(KEY_TASK_CLASS, arrayList.get(i).getClassOfStudent());
            values.put(KEY_TASK_DIVISION, arrayList.get(i).getDivision());
            values.put(KEY_TASK_DATE, String.valueOf(arrayList.get(i).getDate()));
            values.put(KEY_TASK_TITLE, arrayList.get(i).getTitle());
            values.put(KEY_TASK_DESCRIPTION, String.valueOf(arrayList.get(i).getDescription()));
            values.put(KEY_TASK_FILENAME, arrayList.get(i).getFilename());
            db.insert(TABLE_NAME_TASK, null, values);
            MyUtils.logThis("DBHelper - task added : values = " + values.toString());
        }
        db.close();
    }

    /*    studentid    title    description    date    data    */
    public void addResult(ArrayList<StudentResult> arrayList) {
        SQLiteDatabase db = this.getWritableDatabase();
        for (int i = 0; i < arrayList.size(); i++) {
            ContentValues values = new ContentValues();
            values.put(KEY_RESULT_STUDENT_ID, arrayList.get(i).getStudentid());
            values.put(KEY_RESULT_TITLE, arrayList.get(i).getTitle());
            values.put(KEY_RESULT_DESCRIPTION, arrayList.get(i).getDescription());
            values.put(KEY_RESULT_DATE, arrayList.get(i).getDate());
            values.put(KEY_RESULT_DATA, String.valueOf(arrayList.get(i).getData()));
            db.insert(TABLE_NAME_RESULT, null, values);
            MyUtils.logThis("DBHelper - result added : values = " + values.toString());
        }
        db.close();
    }

    /*    studentid    message    reply    date    */
    public void addQuery(ArrayList<Query> arrayList) {
        SQLiteDatabase db = this.getWritableDatabase();
        for (int i = 0; i < arrayList.size(); i++) {
            ContentValues values = new ContentValues();
            values.put(KEY_QUERY_STUDENT_ID, arrayList.get(i).getStudentId());
            values.put(KEY_QUERY_MESSAGE, arrayList.get(i).getMessage());
            values.put(KEY_QUERY_REPLY, arrayList.get(i).getReply());
            values.put(KEY_QUERY_DATE, arrayList.get(i).getDate());
            db.insert(TABLE_NAME_QUERY, null, values);
            MyUtils.logThis("DBHelper - result added : values = " + values.toString());
        }
        db.close();
    }

    /*  classOfStudent   division    day  startTime   endTime     subject     */
    public void addTimetable(ArrayList<Timetable> arrayList) {
        SQLiteDatabase db = this.getWritableDatabase();
        for (int i = 0; i < arrayList.size(); i++) {
            ContentValues values = new ContentValues();
            values.put(KEY_TIMETABLE_CLASS, arrayList.get(i).getClassOfStudent());
            values.put(KEY_STUDENT_DIVISION, arrayList.get(i).getDivision());
            values.put(KEY_TIMETABLE_DAY, arrayList.get(i).getDay());
            values.put(KEY_TIMETABLE_STARTTIME, arrayList.get(i).getStartTime());
            values.put(KEY_TIMETABLE_ENDTIME, arrayList.get(i).getEndTime());
            values.put(KEY_TIMETABLE_SUBJECT, arrayList.get(i).getSubject());
            db.insert(TABLE_NAME_TIMETABLE, null, values);
            MyUtils.logThis("DBHelper - Timetable added : values = " + values.toString());
        }
        db.close();
    }


    /*  studentid    status   description   date    time    */
    public ArrayList<Attendance> getAttendanceForMonth(String id, GregorianCalendar date) {

        ArrayList<Attendance> attendanceList = new ArrayList<>();

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM", Locale.getDefault());
        String t = dateFormat.format(date.getTime()) + "%";

        String selectQuery = "SELECT " + KEY_ATTENDANCE_STUDENTID + ", " + KEY_ATTENDANCE_STATUS
                + "," + KEY_ATTENDANCE_DESCRIPTION + ", " + KEY_ATTENDANCE_DATE + ", " +
                KEY_ATTENDANCE_TIME + " FROM " + TABLE_NAME_ATTENDANCE + " where " +
                KEY_ATTENDANCE_STUDENTID + "='" + id + "' and " + KEY_ATTENDANCE_DATE + " like '" + t + "'";

        MyUtils.logThis("DBHelper - querry = " + selectQuery);

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                Attendance attendance = new Attendance();
                attendance.setStudentId(cursor.getString(0));
                attendance.setStatus(cursor.getString(1));
                attendance.setDesc(cursor.getString(2));
                attendance.setDate(cursor.getString(3));
                attendance.setTime(cursor.getString(4));
                MyUtils.logThis("DBHelper - attendance = " + attendance);
                attendanceList.add(attendance);
            } while (cursor.moveToNext());
            return attendanceList;
        }
        cursor.close();
        db.close();
        return null;
    }


    public ArrayList<Holiday> getHolidaysForMonth(GregorianCalendar date) {
        ArrayList<Holiday> holidayList = new ArrayList<>();

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM", Locale.getDefault());
        String t = dateFormat.format(date.getTime()) + "%";

        String selectQuery = "SELECT  * FROM " + TABLE_NAME_HOLIDAY + " where " + KEY_HOLIDAY_DATE + " like '" + t + "'";
        //Log.d("mylog", "DBHelper - querry = " + selectQuery);

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                Holiday holiday = new Holiday();

                //adding data to object holiday
                holiday.setDate(cursor.getString(0));
                holiday.setDescription(cursor.getString(1));

                //adding object holiday to list
                //Log.d("mylog", "DBHelper - DBhelper read holiday = " + holiday.toString());
                holidayList.add(holiday);
            } while (cursor.moveToNext());
            return holidayList;
        }
        cursor.close();
        db.close();
        return null;
    }

    public int getHolidayCountBetween(GregorianCalendar sessionStartDate, GregorianCalendar gcNow) {

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        String startDate = dateFormat.format(sessionStartDate.getTime()) + "%";
        String endDate = dateFormat.format(gcNow.getTime()) + "%";


        String selectQuery = "SELECT  count(date) FROM " + TABLE_NAME_HOLIDAY + " where " +
                KEY_HOLIDAY_DATE + " BETWEEN '" + startDate + "' and '" + endDate + "'";

        //Log.d("mylog", "DBHelper - querry = " + selectQuery);

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        String daysCount = "0";
        if (cursor.moveToFirst()) {
            daysCount = cursor.getString(0);
        }
        int days = Integer.parseInt(daysCount);
        //MyUtils.logThis("DBHelper - daysCount = " + days);

        cursor.close();
        db.close();
        return days;
    }

    public int getTotalPresent(String id) {

        String selectQuery = "SELECT  count(date) FROM " + TABLE_NAME_ATTENDANCE + " where " +
                KEY_ATTENDANCE_STUDENTID + " = '" + id + "' and " + KEY_ATTENDANCE_STATUS +
                " = '1'";

        //Log.d("mylog", "DBHelper - querry = " + selectQuery);

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        String daysCount = "0";
        if (cursor.moveToFirst()) {
            daysCount = cursor.getString(0);
        }
        int days = Integer.parseInt(daysCount);
        //MyUtils.logThis("DBHelper - daysCount = " + days);

        cursor.close();
        db.close();
        return days;
    }

    public int getHolidayCountOnWeekends(GregorianCalendar sessionStartDate, GregorianCalendar gcNow) {
        int count = 0;

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        String startDate = dateFormat.format(sessionStartDate.getTime()) + "%";
        String endDate = dateFormat.format(gcNow.getTime()) + "%";


        String selectQuery = "SELECT  date FROM " + TABLE_NAME_HOLIDAY + " where " +
                KEY_HOLIDAY_DATE + " BETWEEN '" + startDate + "' and '" + endDate + "'";

        //Log.d("mylog", "DBHelper - querry = " + selectQuery);

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                //adding data to object holiday
                String date = cursor.getString(0);
                GregorianCalendar gregorianCalendar = MyUtils.getGregorianCalendar(date);

                if (gregorianCalendar != null) {
                    if (gregorianCalendar.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY
                            || gregorianCalendar.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY) {
                        count++;
                    }
                }
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return count;
    }

    /*    id    title    description    date    images    */
    public ArrayList<Event> getAllEvents() {
        ArrayList<Event> EventList = new ArrayList<>();
        String selectQuery = "SELECT  * FROM " + TABLE_NAME_EVENT;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                Event event = new Event();
                //adding data to object holiday
                event.setId(cursor.getString(0));
                event.setTitle(cursor.getString(1));
                event.setDescription(cursor.getString(2));
                event.setDate(cursor.getString(3));
                event.setImages(cursor.getString(4));
                EventList.add(event);
            } while (cursor.moveToNext());
            return EventList;
        }
        cursor.close();
        return null;
    }

    public ArrayList<Fee> getAllFee(String sid) {
        //studentId time amount type description

        ArrayList<Fee> FeeList = new ArrayList<>();
        String selectQuery = "SELECT " + KEY_FEE_STUDENT_ID + ", " + KEY_FEE_DATE + ", " +
                KEY_FEE_AMOUNT + ", " + KEY_FEE_TYPE + ", " + KEY_FEE_DESCRIPTION + " FROM " +
                TABLE_NAME_FEE + " where " + KEY_FEE_STUDENT_ID + " = '" + sid + "'";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                Fee fee = new Fee();
                //adding data to object holiday
                fee.setStudentId(cursor.getString(0));
                fee.setDate(cursor.getString(1));
                fee.setAmount(cursor.getString(2));
                fee.setType(cursor.getString(3));
                fee.setDescription(cursor.getString(4));
                //Log.d("mylog", "DBHelper - DBhelper read fee = " + fee.toString());
                FeeList.add(fee);
            } while (cursor.moveToNext());
            return FeeList;
        }
        cursor.close();
        return null;
    }

    /*    studentid    title    description    date    data    */
    public ArrayList<StudentResult> getAllResult(String id) {
        ArrayList<StudentResult> resultList = new ArrayList<>();
        String selectQuery = "SELECT  " + KEY_RESULT_STUDENT_ID + ", " + KEY_RESULT_TITLE + ", " +
                KEY_RESULT_DESCRIPTION + ", " + KEY_RESULT_DATE + ", " + KEY_RESULT_DATA +
                " FROM " + TABLE_NAME_RESULT + " where " + KEY_RESULT_STUDENT_ID + " = '"
                + id + "'";
        MyUtils.logThis("selectQuery=" + selectQuery);
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                StudentResult result = new StudentResult();
                //adding data to object Result
                result.setStudentid(cursor.getString(0));
                result.setTitle(cursor.getString(1));
                result.setDescription(cursor.getString(2));
                result.setDate(cursor.getString(3));
                result.setData(cursor.getString(4));
                resultList.add(result);
                MyUtils.logThis("DBHelper read result = " + result.toString());
            } while (cursor.moveToNext());
            return resultList;
        }
        cursor.close();
        return null;
    }

    /*  classOfStudent    division    date    title   description     filename  */
    public ArrayList<Task> getAllTask(String classOfStudent, String division) {

        ArrayList<Task> taskList = new ArrayList<>();
        String selectQuery = "SELECT  " + KEY_TASK_CLASS + ", " + KEY_TASK_DIVISION + ", "
                + KEY_TASK_DATE + ", " + KEY_TASK_TITLE + ", " + KEY_TASK_DESCRIPTION + ", "
                + KEY_TASK_FILENAME + " FROM " + TABLE_NAME_TASK + " where " + KEY_TASK_CLASS + " = '"
                + classOfStudent + "' and " + KEY_TASK_DIVISION + " = '" + division + "'";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                Task task = new Task();
                //adding data to object Task
                task.setClassOfStudent(cursor.getString(0));
                task.setDivision(cursor.getString(1));
                task.setDate(cursor.getString(2));
                task.setTitle(cursor.getString(3));
                task.setDescription(cursor.getString(4));
                task.setFilename(cursor.getString(5));
                taskList.add(task);
            } while (cursor.moveToNext());
            return taskList;
        }
        cursor.close();
        return null;
    }

    /*  id name dob gender father mother class division house contact */
    public ArrayList<Student> getAllStudents() {
        ArrayList<Student> studentList = new ArrayList<>();
        String selectQuery = "SELECT  * FROM " + TABLE_NAME_STUDENT;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                Student student = new Student();
                //adding data to object student
                student.setId(cursor.getString(0));
                student.setName(cursor.getString(1));
                student.setDob(cursor.getString(2));
                student.setGender(cursor.getString(3));
                student.setFather(cursor.getString(4));
                student.setMother(cursor.getString(5));
                student.setClassOfStudent(cursor.getString(6));
                student.setDivision(cursor.getString(7));
                student.setHouse(cursor.getString(8));
                student.setContact(cursor.getString(9));
                //adding object student to list
                Log.d("mylog", "DBHelper - read student = " + student.toString());
                studentList.add(student);
            } while (cursor.moveToNext());
            cursor.close();
            db.close();
            return studentList;
        }
        cursor.close();
        db.close();
        return null;
    }

    /*  classOfStudent   division    day  startTime   endTime     subject     */
    public Student getStudent(String id) {

        String selectQuery = "SELECT  * FROM " + TABLE_NAME_STUDENT + " where " + KEY_STUDENT_STUDENTID + " = '" + id + "'";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            Student student = new Student();
            //adding data to object student
            student.setId(cursor.getString(0));
            student.setName(cursor.getString(1));
            student.setDob(cursor.getString(2));
            student.setGender(cursor.getString(3));
            student.setFather(cursor.getString(4));
            student.setMother(cursor.getString(5));
            student.setClassOfStudent(cursor.getString(6));
            student.setDivision(cursor.getString(7));
            student.setHouse(cursor.getString(8));
            student.setContact(cursor.getString(9));
            cursor.close();
            db.close();
            return student;
        }
        cursor.close();
        db.close();
        return null;
    }

    /*    studentid    message    reply    date    */
    public ArrayList<Query> getAllQuery(String studentid) {
        //studentId time amount type description

        ArrayList<Query> queryList = new ArrayList<>();
        String selectQuery = "SELECT " + KEY_QUERY_STUDENT_ID + ", " + KEY_QUERY_MESSAGE + ", " +
                KEY_QUERY_REPLY + ", " + KEY_QUERY_DATE + " FROM " +
                TABLE_NAME_QUERY + " where " + KEY_QUERY_STUDENT_ID + " = '" + studentid + "'";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                Query query = new Query();
                //adding data to object holiday
                query.setStudentId(cursor.getString(0));
                query.setMessage(cursor.getString(1));
                query.setReply(cursor.getString(2));
                query.setDate(cursor.getString(3));
                //MyUtils.logThis("DBHelper - read query = " + query.toString());
                queryList.add(query);
            } while (cursor.moveToNext());
            return queryList;
        }
        cursor.close();
        return null;
    }

    /*  classOfStudent   division    day  startTime   endTime     subject     */
    public ArrayList<Timetable> getTimetableFor(String classOfStudent, String division, String day) {

        ArrayList<Timetable> timetableList = new ArrayList<>();
        String selectQuery = "SELECT " + KEY_TIMETABLE_CLASS + ", " + KEY_TIMETABLE_DIVISION + ", " +
                KEY_TIMETABLE_DAY + ", " + KEY_TIMETABLE_STARTTIME + ", " + KEY_TIMETABLE_ENDTIME + ", " +
                KEY_TIMETABLE_SUBJECT + " FROM " + TABLE_NAME_TIMETABLE + " where " +
                KEY_TIMETABLE_CLASS + " = '" + classOfStudent + "' and " + KEY_TIMETABLE_DIVISION +
                " = '" + division + "' and " + KEY_TIMETABLE_DAY + " = '" + day + "'";
        MyUtils.logThis(selectQuery);
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                Timetable timetable = new Timetable();
                //adding data to object Timetable
                timetable.setClassOfStudent(cursor.getString(0));
                timetable.setDivision(cursor.getString(1));
                timetable.setDay(cursor.getString(2));
                timetable.setStartTime(cursor.getString(3));
                timetable.setEndTime(cursor.getString(4));
                timetable.setSubject(cursor.getString(5));
                MyUtils.logThis("DBHelper - read timetable = " + timetable.toString());
                timetableList.add(timetable);
            } while (cursor.moveToNext());
            return timetableList;
        }
        cursor.close();
        return null;
    }

    public List<String> getSubjects(Student student) {

        List<String> subjectList = new ArrayList<>();

        String selectQuery = "SELECT DISTINCT " + KEY_TIMETABLE_SUBJECT + " FROM " + TABLE_NAME_TIMETABLE + " where " +
                KEY_TIMETABLE_CLASS + " = '" + student.getClassOfStudent() + "' and " + KEY_TIMETABLE_DIVISION +
                " = '" + student.getDivision() + "'";
        MyUtils.logThis(selectQuery);
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                String subject = cursor.getString(0);
                subjectList.add(subject);
            } while (cursor.moveToNext());
            return subjectList;
        }
        cursor.close();
        return null;
    }
}//DBHelper