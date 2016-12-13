package org.studentaggregator.studentaggregator.helper;

import org.studentaggregator.studentaggregator.model.Attendance;
import org.studentaggregator.studentaggregator.model.Event;
import org.studentaggregator.studentaggregator.model.Fee;
import org.studentaggregator.studentaggregator.model.Holiday;
import org.studentaggregator.studentaggregator.model.Query;
import org.studentaggregator.studentaggregator.model.Student;
import org.studentaggregator.studentaggregator.model.StudentResult;
import org.studentaggregator.studentaggregator.model.Task;
import org.studentaggregator.studentaggregator.model.Timetable;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Ray on 4/25/2016.
 */
public class JSONParser {


    private static final String TAG_RESULTS = "result";
    private static final String TAG_ERROR = "error";
    private static final String TAG_ERROR_DESCRIPTION = "description";
    private static String errorDescription = "Oops something went wrong";

    /*  studentid    status   description   date    time    */
    /*------------      Attendance      -------------------*/
    private static final String ATTENDANCE_STUDENT_ID = "studentid";
    private static final String ATTENDANCE_STATUS = "status";
    private static final String ATTENDANCE_DESCRIPTION = "description";
    private static final String ATTENDANCE_DATE = "date";
    private static final String ATTENDANCE_TIME = "time";

    /*  id name dob gender father mother class division house contact    */
    /*------------      Student      -------------------*/
    private static final String TAG_STUDENT_ID = "id";
    private static final String TAG_STUDENT_NAME = "name";
    private static final String TAG_STUDENT_DOB = "dob";
    private static final String TAG_STUDENT_GENDER = "gender";
    private static final String TAG_STUDENT_FATHER = "father";
    private static final String TAG_STUDENT_MOTHER = "mother";
    private static final String TAG_STUDENT_CLASS = "class";
    private static final String TAG_STUDENT_DIVISION = "division";
    private static final String TAG_STUDENT_HOUSE = "house";
    private static final String TAG_STUDENT_CONTACT = "contact";

    /*  date description    */
    /*------------      Holiday      -------------------*/
    private static final String TAG_HOLIDAY_DATE = "date";
    private static final String TAG_HOLIDAY_DESC = "description";

    /*    id    title    description    date    images    */
    /*------------      Event      -------------------*/
    public static final String KEY_EVENT_ID = "id";
    public static final String KEY_EVENT_TITLE = "title";
    public static final String KEY_EVENT_DESCRIPTION = "description";
    public static final String KEY_EVENT_DATE = "date";
    public static final String KEY_EVENT_IMAGES = "images";

    /*    studentid    message    reply    date    */
    /*------------      Query      -------------------*/
    public static final String QUERY_STUDENTID = "studentid";
    public static final String QUERY_MESSAGE = "message";
    public static final String QUERY_REPLY = "reply";
    public static final String QUERY_DATE = "date";

    /*  classOfStudent    division    date    title   description     filename  */
    /*------------      Task      -------------------*/
    public static final String KEY_TASK_CLASS = "class";
    public static final String KEY_TASK_DIVISION = "division";
    public static final String KEY_TASK_DATE = "date";
    public static final String KEY_TASK_TITLE = "title";
    public static final String KEY_TASK_DESCRIPTION = "description";
    public static final String KEY_TASK_FILENAME = "filename";

    /*    studentid    title    description    date    data    */
    /*------------      Result      -------------------*/
    public static final String RESULT_STUDENTID = "studentid";
    public static final String RESULT_TITLE = "title";
    public static final String RESULT_DESCRIPTION = "description";
    public static final String RESULT_DATE = "date";
    public static final String RESULT_DATA = "data";

    /*    studentid date amount description type  */
    /*------------      Fee      -------------------*/
    public static final String FEE_STUDENT_ID = "studentid";
    public static final String FEE_DATE = "date";
    public static final String FEE_AMOUNT = "amount";
    public static final String FEE_DESCRIPTION = "description";
    public static final String FEE_TYPE = "type";

    /*  classOfStudent   division    day  startTime   endTime     subject     */
    /*------------      Timetable      -------------------*/
    public static final String TIMETABLE_CLASS = "class";
    public static final String TIMETABLE_DIVISION = "division";
    public static final String TIMETABLE_DAY = "day";
    public static final String TIMETABLE_STARTTIME = "starttime";
    public static final String TIMETABLE_ENDTIME = "endtime";
    public static final String TIMETABLE_SUBJECT = "subject";

    /*------------      Success      -------------------*/
    private static final String TAG_SUCCESS = "success";


    //Class functions
    private boolean isValidData(String jsonString) {

        MyUtils.logThis("JSONParser - jsonString = " + jsonString);
        try {
            JSONObject jsonObj = new JSONObject(jsonString);

            //check for successful reply which contains result
            String jsonArrayTest = jsonObj.optString(TAG_RESULTS);

            //JSON dose'nt have data with result
            if (jsonArrayTest.length() == 0) {
                MyUtils.logThis("JSONParser - No data with result");
                errorDescription = "Empty JSON : No data with Result";

                //check for successful reply which contains error
                String jsonArrayError = jsonObj.optString(TAG_ERROR);

                //JSON dose'nt have data with error
                if (jsonArrayError.length() == 0) {
                    MyUtils.logThis("JSONParser - no data with error");
                    errorDescription = "Empty JSON : No data with error";
                }
                //when error is not empty
                else {
                    JSONArray jsonArray = jsonObj.getJSONArray(TAG_ERROR);
                    JSONObject c = jsonArray.getJSONObject(0);
                    errorDescription = c.getString(TAG_ERROR_DESCRIPTION);
                    MyUtils.logThis("JSONParser - errorDescription = " + errorDescription);
                }
                return false;
            }
        } catch (JSONException e) {
            e.printStackTrace();
            MyUtils.logThis("JSONParser - Json exception : Wrong data");
            return false;
        }
        return true;
    }//isValidData


    public String getErrorDescription() {
        return errorDescription;
    }//getErrorDescription


    //----------------------------     parseSuccess     -------------------------------
    public Boolean parseSuccess(String jsonString) {
        //if JSON has error
        if (!isValidData(jsonString)) {
            return false;
        }
        //check if JSON contains success
        try {
            JSONObject jsonObj = new JSONObject(jsonString);
            JSONArray jsonArray = jsonObj.getJSONArray(TAG_RESULTS);
            JSONObject singleJsonObject = jsonArray.getJSONObject(0);

            //extract data from jsonObject
            String success = singleJsonObject.getString(TAG_SUCCESS);
            return success.equals("success");
        } catch (JSONException e) {
            e.printStackTrace();
            MyUtils.logThis("JSONParser - JSONException");
            errorDescription = "Oops something went wrong";
            return false;
        }
    }


    /*  studentid    status   description   date    time    */
    //----------------------------     parseAttendance     -------------------------------
    public ArrayList<Attendance> parseAttendance(String jsonString) {

        //if JSON has error
        if (!isValidData(jsonString)) {
            return null;
        }
        //parse JSON to Attendance
        try {
            JSONObject jsonObj = new JSONObject(jsonString);
            JSONArray jsonArray = jsonObj.getJSONArray(TAG_RESULTS);
            ArrayList<Attendance> attendanceList = new ArrayList<>();

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject singleJsonObj = jsonArray.getJSONObject(i);
                String id = singleJsonObj.getString(ATTENDANCE_STUDENT_ID);
                String status = singleJsonObj.getString(ATTENDANCE_STATUS);
                String desc = singleJsonObj.getString(ATTENDANCE_DESCRIPTION);
                String date = singleJsonObj.getString(ATTENDANCE_DATE);
                String time = singleJsonObj.getString(ATTENDANCE_TIME);
                Attendance attendance = new Attendance(id, status, desc, date, time);
                attendanceList.add(attendance);
            }
            return attendanceList;

        } catch (JSONException e) {
            e.printStackTrace();
            MyUtils.logThis("JSONParser - JSONException");
            errorDescription = "Oops something went wrong";
            return null;
        }
    }


    /*    studentid    message    reply    date    */
    //----------------------------     parseQuery     -------------------------------
    public ArrayList<Query> parseQuery(String jsonString) {
        //if JSON has error
        if (!isValidData(jsonString)) {
            return null;
        }
        //parse JSON to query
        try {
            JSONObject jsonObj = new JSONObject(jsonString);
            JSONArray jsonArray = jsonObj.getJSONArray(TAG_RESULTS);
            ArrayList<Query> queryList = new ArrayList<>();

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject singleJsonObj = jsonArray.getJSONObject(i);
                String studentid = singleJsonObj.getString(QUERY_STUDENTID);
                String message = singleJsonObj.getString(QUERY_MESSAGE);
                String reply = singleJsonObj.getString(QUERY_REPLY);
                String date = singleJsonObj.getString(QUERY_DATE);
                Query query = new Query(studentid, message, reply, date);
                queryList.add(query);
            }
            return queryList;
        } catch (JSONException e) {
            e.printStackTrace();
            MyUtils.logThis("JSONParser - JSONException");
            errorDescription = "Oops something went wrong";
            return null;
        }
    }


    /*    studentid date amount description type  */
    //--------------------------------     parseFee     -----------------------------------
    public ArrayList<Fee> parseFee(String jsonString) {

        //if JSON has error
        if (!isValidData(jsonString)) {
            return null;
        }
        //parse JSON to Fee
        try {
            JSONObject jsonObj = new JSONObject(jsonString);
            JSONArray jsonArray = jsonObj.getJSONArray(TAG_RESULTS);
            ArrayList<Fee> feeList = new ArrayList<>();

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject singleJsonObject = jsonArray.getJSONObject(i);
                Fee fee = new Fee();
                fee.setStudentId(singleJsonObject.getString(FEE_STUDENT_ID));
                fee.setDate(singleJsonObject.getString(FEE_DATE));
                fee.setAmount(singleJsonObject.getString(FEE_AMOUNT));
                fee.setDescription(singleJsonObject.getString(FEE_DESCRIPTION));
                fee.setType(singleJsonObject.getString(FEE_TYPE));
                feeList.add(fee);
            }
            return feeList;
        } catch (JSONException e) {
            e.printStackTrace();
            MyUtils.logThis("JSONParser - JSONException");
            errorDescription = "Oops something went wrong";
            return null;
        }
    }


    /*    id    title    description    date    images    */
    //--------------------------------     parse Event/Notification     -----------------------------------
    public ArrayList<Event> parseNotification(String jsonString) {

        //if JSON has error
        if (!isValidData(jsonString)) {
            return null;
        }
        //parse JSON to Event
        try {
            JSONObject jsonObj = new JSONObject(jsonString);
            JSONArray jsonArray = jsonObj.getJSONArray(TAG_RESULTS);
            ArrayList<Event> eventList = new ArrayList<>();

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject singleJsonObject = jsonArray.getJSONObject(i);
                String id = singleJsonObject.getString(KEY_EVENT_ID);
                String title = singleJsonObject.getString(KEY_EVENT_TITLE);
                String description = singleJsonObject.getString(KEY_EVENT_DESCRIPTION);
                String date = singleJsonObject.getString(KEY_EVENT_DATE);
                String images = singleJsonObject.getString(KEY_EVENT_IMAGES);
                Event a = new Event(id, title, description, date, images);
                eventList.add(a);
            }
            return eventList;
        } catch (JSONException e) {
            e.printStackTrace();
            MyUtils.logThis("JSONParser - JSONException");
            errorDescription = "Oops something went wrong";
            return null;
        }
    }//event/notification


    /*  classOfStudent    division    date    title   description     filename  */
    //--------------------------------      Task     -----------------------------------
    public ArrayList<Task> parseTask(String jsonString) {

        //if JSON has error
        if (!isValidData(jsonString)) {
            return null;
        }
        //parse JSON to Task
        try {
            JSONObject jsonObj = new JSONObject(jsonString);
            JSONArray jsonArray = jsonObj.getJSONArray(TAG_RESULTS);
            ArrayList<Task> taskList = new ArrayList<>();

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject singleJsonObject = jsonArray.getJSONObject(i);
                String classOfStudent = singleJsonObject.getString(KEY_TASK_CLASS);
                String division = singleJsonObject.getString(KEY_TASK_DIVISION);
                String date = singleJsonObject.getString(KEY_TASK_DATE);
                String title = singleJsonObject.getString(KEY_TASK_TITLE);
                String description = singleJsonObject.getString(KEY_TASK_DESCRIPTION);
                String filename = singleJsonObject.getString(KEY_TASK_FILENAME);
                Task a = new Task(classOfStudent, division, date, title, description, filename);
                taskList.add(a);
            }
            return taskList;
        } catch (JSONException e) {
            e.printStackTrace();
            MyUtils.logThis("JSONParser - JSONException");
            errorDescription = "Oops something went wrong";
            return null;
        }
    }//parseTask


    /*    studentid    title    description    date    data    */
    //--------------------------------      StudentResults     -----------------------------------
    public ArrayList<StudentResult> parseStudentResults(String jsonString) {

        //if JSON has error
        if (!isValidData(jsonString)) {
            return null;
        }
        //parse JSON to Task
        try {
            JSONObject jsonObj = new JSONObject(jsonString);
            JSONArray jsonArray = jsonObj.getJSONArray(TAG_RESULTS);
            ArrayList<StudentResult> resultList = new ArrayList<>();
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject c = jsonArray.getJSONObject(i);
                String studentid = c.getString(RESULT_STUDENTID);
                String title = c.getString(RESULT_TITLE);
                String description = c.getString(RESULT_DESCRIPTION);
                String date = c.getString(RESULT_DATE);
                String data = c.getString(RESULT_DATA);
                StudentResult studentResult = new StudentResult(studentid, title, description, date, data);
                resultList.add(studentResult);
            }
            return resultList;
        } catch (JSONException e) {
            e.printStackTrace();
            MyUtils.logThis("JSONParser - JSONException");
            errorDescription = "Oops something went wrong";
            return null;
        }
    }//parseStudentResults


    /*  id name dob gender father mother class division house contact    */
    //------------------------------     parseStudent     --------------------------------
    public Student parseStudent(String jsonString) {

        //if JSON has error
        if (!isValidData(jsonString)) {
            return null;
        }
        //parse JSON to Student
        try {
            JSONObject jsonObj = new JSONObject(jsonString);
            JSONArray jsonArray = jsonObj.getJSONArray(TAG_RESULTS);
            JSONObject singleJsonObject = jsonArray.getJSONObject(0);

            String id = singleJsonObject.getString(TAG_STUDENT_ID);
            String name = singleJsonObject.getString(TAG_STUDENT_NAME);
            String dob = singleJsonObject.getString(TAG_STUDENT_DOB);
            String gender = singleJsonObject.getString(TAG_STUDENT_GENDER);
            String father = singleJsonObject.getString(TAG_STUDENT_FATHER);
            String mother = singleJsonObject.getString(TAG_STUDENT_MOTHER);
            String classOfStudent = singleJsonObject.getString(TAG_STUDENT_CLASS);
            String division = singleJsonObject.getString(TAG_STUDENT_DIVISION);
            String house = singleJsonObject.getString(TAG_STUDENT_HOUSE);
            String contact = singleJsonObject.getString(TAG_STUDENT_CONTACT);
            return new Student(id, name, dob, gender, father, mother, classOfStudent, division, house, contact);
        } catch (JSONException e) {
            e.printStackTrace();
            MyUtils.logThis("JSONParser - JSONException");
            errorDescription = "Oops something went wrong";
            return null;
        }
    }


    /*  date description    */
    //------------------------------     parseHoliday     --------------------------------
    public ArrayList<Holiday> parseHoliday(String jsonString) {

        //if JSON has error
        if (!isValidData(jsonString)) {
            return null;
        }
        //parse JSON to Holiday
        try {
            JSONObject jsonObj = new JSONObject(jsonString);
            JSONArray jsonArray = jsonObj.getJSONArray(TAG_RESULTS);
            ArrayList<Holiday> holidayList = new ArrayList<>();

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject singleJsonObject = jsonArray.getJSONObject(i);
                String date = singleJsonObject.getString(TAG_HOLIDAY_DATE);
                String description = singleJsonObject.getString(TAG_HOLIDAY_DESC);
                Holiday holiday = new Holiday(date, description);
                holidayList.add(holiday);
            }
            return holidayList;

        } catch (JSONException e) {
            e.printStackTrace();
            MyUtils.logThis("JSONParser - JSONException");
            errorDescription = "Oops something went wrong";
            return null;
        }
    }

    /*  classOfStudent   division    day  startTime   endTime     subject     */
    //----------------------------     Timetable     -------------------------------
    public ArrayList<Timetable> parseTimetable(String jsonString) {
        //if JSON has error
        if (!isValidData(jsonString)) {
            return null;
        }
        //parse JSON to Timetable
        try {
            JSONObject jsonObj = new JSONObject(jsonString);
            JSONArray jsonArray = jsonObj.getJSONArray(TAG_RESULTS);
            ArrayList<Timetable> timetableList = new ArrayList<>();

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject singleJsonObj = jsonArray.getJSONObject(i);
                String classOfStudent = singleJsonObj.getString(TIMETABLE_CLASS);
                String division = singleJsonObj.getString(TIMETABLE_DIVISION);
                String day = singleJsonObj.getString(TIMETABLE_DAY);
                String starttime = singleJsonObj.getString(TIMETABLE_STARTTIME);
                String endtime = singleJsonObj.getString(TIMETABLE_ENDTIME);
                String subject = singleJsonObj.getString(TIMETABLE_SUBJECT);
                Timetable timetable = new Timetable(classOfStudent, division, day, starttime, endtime, subject);
                timetableList.add(timetable);
            }
            return timetableList;
        } catch (JSONException e) {
            e.printStackTrace();
            MyUtils.logThis("JSONParser - JSONException");
            errorDescription = "Oops something went wrong";
            return null;
        }
    }
}