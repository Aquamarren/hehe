package com.example.marrenmatias.trynavdrawer;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;


/**
 * Created by Nea Montecer on 12/19/2016.
 */
public class DatabaseHelper extends SQLiteOpenHelper {
    public static String DATABASE_NAME = "THRIFTY.db";
    public static String TABLE_NAME = "INCOME";
    public static String TABLE_NAME2 = "CATEGORY";
    public static String COL_1 = "ID";
    public static String COL_id = "_id";
    public static String COL_2 = "IncomeAmount";
    public static String COL_3 = "IncomeDateTo";
    public static String COL_4 = "IncomeDateFrom";
    public static String COL_5 = "SavingsAmount";
    public static String COL_6 = "ACTIVE";
    public static String KEY_TASK = "CategoryName";
    public static String KEY_TASK2 = "Budget";
    public static String KEY_TASK3 = "ExpenseAmount";


    private static final String TABLE_NAMEZ = "contact";
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_FNAME = "fname";
    private static final String COLUMN_LNAME = "lname";
    private static final String COLUMN_BDAY = "birthday";
    private static final String COLUMN_EMAIL = "email";
    private static final String COLUMN_UNAME = "uname";
    private static final String COLUMN_PASS = "pass";
    private static final String COLUMN_ACTIVE = "active";
    private static final String COLUMN_DP = "picture";



    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
        SQLiteDatabase db = this.getWritableDatabase();
        Log.d("Database Operations", "Database Created");
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL("CREATE TABLE contact (id integer primary key not null, " +
                "fname text not null, lname text not null, birthday text not null, email text not null unique, " +
                "uname text not null unique, pass text not null, active integer not null, picture text)");

        db.execSQL("CREATE TABLE " + TABLE_NAME + " (ID INTEGER PRIMARY KEY AUTOINCREMENT," +
                " IncomeAmount FLOAT, IncomeDateTo DATE, IncomeDateFrom DATE, ACTIVE INTEGER)");

        db.execSQL("CREATE TABLE SAVINGS (ID INTEGER PRIMARY KEY AUTOINCREMENT, SavingsDate DATE," +
                "SavingsAmount FLOAT)");

        db.execSQL("CREATE TABLE CATEGORY (ID INTEGER PRIMARY KEY AUTOINCREMENT, CategoryName TEXT, " +
                "CategoryImg TEXT, Budget FLOAT, BudgetCost FLOAT, " +
                "Checkbox INTEGER, DueDate DATE, DueTime TEXT, IconID INTEGER, ACTIVE INTEGER)");

        db.execSQL("INSERT INTO CATEGORY (CategoryName,Budget,BudgetCost,ACTIVE) VALUES('Electricity',0,0,1), ('Water',0,0,1), ('House Rent',0,0,1)");

        db.execSQL("CREATE TABLE EXPENSE (ID INTEGER PRIMARY KEY AUTOINCREMENT, ExpenseAmount FLOAT, " +
                "ExpenseDate DATE, CategoryName TEXT,ACTIVE INTEGER)");

        db.execSQL("CREATE TABLE GOALS (ID INTEGER PRIMARY KEY AUTOINCREMENT, GoalName TEXT, GoalCost FLOAT, GoalDate DATE, " +
                "GoalRank INTEGER, GoalPoints INTEGER, GoalAccomplished INTEGER, MoneySaved FLOAT, GoalImage TEXT)");

        db.execSQL("INSERT INTO GOALS (GoalRank, GoalAccomplished, MoneySaved) VALUES (1,2,0),(2,2,0),(3,2,0),(4,2,0),(5,1,0)");

        db.execSQL("INSERT INTO EXPENSE(ExpenseAmount, ExpenseDate, CategoryName, ACTIVE) " +
                "VALUES (20, '2017-01-30', 'Water', 1), (30, '2017-01-29', 'Electricity', 1)"); //Sample for forecastBudget
        //db.execSQL("UPDATE INCOME SET IncomeDateTo = 'February 18, 2017'");
        //db.execSQL("CREATE TABLE IF NOT EXISTS REPORTS (ID INTEGER PRIMARY KEY AUTOINCREMENT, " + "CategoryID INTEGER, TotalExpense FLOAT)");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP IF TABLE EXISTS " + TABLE_NAME + ", " + TABLE_NAME2 + ", 'SAVINGS','EXPENSE'");
        //onCreate(db);
    }

    public boolean insertData(String Income, String DateTo, String DateFrom){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_2, Income);
        contentValues.put(COL_6, "1");
        contentValues.put(COL_3, DateTo);
        contentValues.put(COL_4, DateFrom);
        long result = db.insert(TABLE_NAME, null, contentValues);
        if(result == -1){
            return false;
        }else{
            return  true;
        }
    }

    public boolean insertSavings(Float Savings, String Timestamp){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues content = new ContentValues();
        content.put(COL_5, Savings);
        content.put("SavingsDate", Timestamp);
        long result1 = db.insert("Savings",null ,content);
        if(result1 == -1){
            return false;
        }else{
            return  true;
        }
    }

    public void calculateIncome(Float number){
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT IncomeAmount FROM Income WHERE ACTIVE = 1";
        Cursor cursor = db.rawQuery(query,null);
        float current = 0;
        float totalIncome;
        if(cursor.moveToFirst()){
            current = Float.parseFloat(cursor.getString(0));
        }
        totalIncome = current - number;


        SQLiteDatabase db1 = this.getWritableDatabase();
        String rawQuery = "UPDATE Income SET IncomeAmount='" + totalIncome +"'";
        db1.execSQL(rawQuery);
    }


    public void AddCategory(String Category, String Check, String DueDate, String DueTime, String IconID){
        SQLiteDatabase db = this. getWritableDatabase();
        String SQL = "INSERT INTO CATEGORY (CategoryName, ACTIVE, Checkbox, DueDate, DueTime, IconID,Budget, BudgetCost) " +
                "VALUES ('" + Category + "', 1,"+ Check +","+ DueDate +",'"+ DueTime +"',"+IconID+",0,0)";
        db.execSQL(SQL);
    }

    public void AddIncome(Float Income){
        SQLiteDatabase db = this.getWritableDatabase();
        String SQL = "UPDATE Income SET " + COL_2 + "= "+ Income + " WHERE ACTIVE = 1";
        db.execSQL(SQL);
    }

    public void UpdateIncomeAmount(Float Income){
        SQLiteDatabase db = this.getWritableDatabase();
        String SQL = "UPDATE INCOME SET IncomeAmount = '"+ Income + "' WHERE ACTIVE = 1";
        db.execSQL(SQL);
    }

    public Cursor Income(){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM INCOME WHERE ACTIVE = 1", null);
        return cursor;
    }

    public Cursor getListContentsCategory(){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor data = db.rawQuery("SELECT ID AS _id, * FROM CATEGORY WHERE ACTIVE = 1", null);
        return data;
    }

    public Cursor ContentsCategory(){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor data = db.rawQuery("SELECT * FROM CATEGORY WHERE ACTIVE = 1", null);
        return data;
    }

    public Cursor getListExpenses(){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor data = db.rawQuery("SELECT EXPENSE.ID AS _id, * FROM EXPENSE, INCOME" +
                " WHERE EXPENSE.ACTIVE = 1 AND INCOME.ACTIVE = 1 AND EXPENSE.ExpenseDate BETWEEN " +
                "INCOME.IncomeDateTo AND INCOME.IncomeDateFrom GROUP BY EXPENSE.ExpenseDate ORDER BY ExpenseDate ASC",null);
        return  data;
    }

    public void AddExpense(String Expense, String Date, String CategoryName, String CategoryID){
        SQLiteDatabase db = this.getWritableDatabase();
        String SQL = "INSERT INTO EXPENSE (ExpenseAmount, ExpenseDate, CategoryName, ACTIVE) VALUES ('" + Expense + "', '"+Date+"','"+ CategoryName +"', 1)";
        db.execSQL(SQL);

        String SQLUpdate = "UPDATE CATEGORY SET BUDGET = BUDGET - "+ Expense + " WHERE ID = "+CategoryID;
        db.execSQL(SQLUpdate);
    }

    public void UpdateExpenseAmount(String Expense, String Date, String CategoryName, String CategoryID){
        SQLiteDatabase db = this.getWritableDatabase();
        String SQL = "UPDATE EXPENSE SET ExpenseAmount = ExpenseAmount + "+ Expense +" WHERE ExpenseDate = '"+ Date +"' " +
                "AND CategoryName = '"+ CategoryName +"'";
        db.execSQL(SQL);

        String SQLUpdate = "UPDATE CATEGORY SET BUDGET = BUDGET -"+ Expense + " WHERE ID = "+CategoryID;
        db.execSQL(SQLUpdate);
    }

    public void UpdateExpenseName(String CategoryName, String OldCategoryName){
        SQLiteDatabase db = this.getWritableDatabase();
        String SQL1 = "UPDATE CATEGORY SET CategoryName = '"+ CategoryName +"' " +
                "WHERE ACTIVE = 1 AND CategoryName = '" + OldCategoryName +"'";
        db.execSQL(SQL1);

        String SQL = "UPDATE EXPENSE SET CategoryName = '" + CategoryName + "' " +
                "WHERE ACTIVE = 1 AND CategoryName = '"+ OldCategoryName +"'";
        db.execSQL(SQL);
    }

    public void updateSavings(String SavingsAmount){
        SQLiteDatabase db = this.getWritableDatabase();
        String SQL = "UPDATE SAVINGS SET SavingsAmount = SavingsAmount + "+SavingsAmount;
        db.execSQL(SQL);
    }

    public void updateBudget(String BudgetCost,  String BudgetC, String ID){
        SQLiteDatabase db = this.getWritableDatabase();
        String SQL = "UPDATE CATEGORY SET  Budget = "+ BudgetC +",BudgetCost = "+ BudgetCost +" WHERE ID = " + ID;
        db.execSQL(SQL);
    }

    public void insertGoal(String Goal, String GoalCost, String GoalDate, String GoalRank, byte[] imageBytes){
        SQLiteDatabase db = this.getWritableDatabase();
        String SQL = "UPDATE GOALS SET GoalName ='"+Goal+"',GoalCost ='"+GoalCost+"',GoalDate ='"+GoalDate+"', " +
                "GoalImage = "+ imageBytes +" WHERE GoalRank = "+GoalRank;
        db.execSQL(SQL);
    }

    public byte[] retreiveImageFromDB() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cur = db.rawQuery("",null);
        if (cur.moveToFirst()) {
            byte[] blob = cur.getBlob(cur.getColumnIndex("GoalImage"));
            cur.close();
            return blob;
        }
        cur.close();
        return null;
    }

    public void updateGoalRank(Integer GoalRank){
        SQLiteDatabase db = this.getWritableDatabase();
        String SQL = "UPDATE GOALS SET GoalAccomplished ='1' WHERE GoalRank = "+GoalRank;
        db.execSQL(SQL);
    }

    public void updateGoal(String Goal, String GoalCost, String GoalRank, byte[] imageBytes){
        SQLiteDatabase db = this.getWritableDatabase();
        String SQL = "UPDATE GOALS SET GoalName ='"+Goal+"',GoalCost ='"+GoalCost+"' WHERE GoalRank ="+GoalRank;
        db.execSQL(SQL);
    }

    public void excessIncomeBudget(String IncomeAmount){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT SavingsAmount FROM SAVINGS", null);
        cursor.moveToFirst();
        String SavingsAmount = cursor.getString(0);
        float AdditionalSavings = Float.valueOf(SavingsAmount) + Float.valueOf(IncomeAmount);
        String SQL = "UPDATE SAVINGS SET SavingsAmount ="+AdditionalSavings;
        db.execSQL(SQL);
        deActivateIncome();
    }

    public void deActivateIncome() {
        SQLiteDatabase db = this.getWritableDatabase();
        String SQL = "UPDATE INCOME SET ACTIVE = 0 WHERE ACTIVE = 1";
        db.execSQL(SQL);
    }

    public void deactivate() {
        SQLiteDatabase db = this.getWritableDatabase();
        String SQL = "UPDATE contact SET ACTIVE = 0 WHERE ACTIVE = 1";
        db.execSQL(SQL);
    }

    public void activate() {
        SQLiteDatabase db = this.getWritableDatabase();
        String SQL = "UPDATE contact SET ACTIVE = 1 WHERE ACTIVE = 0";
        db.execSQL(SQL);
    }

    public void useSavingsAddIncome(String Amount){
        SQLiteDatabase db = this.getWritableDatabase();
        String SQL = "UPDATE SAVINGS SET SavingsAmount = SavingsAmount - " + Amount;
        db.execSQL(SQL);
    }

    public  void useSavingsAmount(String Amount, String GoalRank){
        SQLiteDatabase db = this.getWritableDatabase();
        String SQL = "UPDATE SAVINGS SET SavingsAmount = SavingsAmount - " + Amount;
        db.execSQL(SQL);

        String SQL1 = "UPDATE GOALS SET MoneySaved = MoneySaved + " + Amount + " WHERE GoalAccomplished = 1 AND GoalRank = " + GoalRank;
        db.execSQL(SQL1);
    }

    public void insertContact(Contact c)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        String query = "select * from contact";
        Cursor cursor = db.rawQuery(query, null);
        int count = cursor.getCount();

        values.put(COLUMN_ID, count);
        values.put(COLUMN_FNAME, c.getFname());
        values.put(COLUMN_LNAME, c.getLname());
        values.put(COLUMN_BDAY, c.getLname());
        values.put(COLUMN_EMAIL, c.getEmail());
        values.put(COLUMN_UNAME, c.getUname());
        values.put(COLUMN_PASS, c.getPass());
        values.put(COLUMN_ACTIVE, "1");

        db.insert(TABLE_NAMEZ, null, values);
    }

    public void insertFB(String id, String fname, String lname, String email, String birthday)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        String query = "select * from contact";

        values.put(COLUMN_ID, id);
        values.put(COLUMN_FNAME, fname);
        values.put(COLUMN_LNAME, lname);
        values.put(COLUMN_BDAY, birthday);
        values.put(COLUMN_EMAIL, email);
        values.put(COLUMN_UNAME, fname);
        values.put(COLUMN_PASS, lname);
        values.put(COLUMN_ACTIVE, "1");

        db.insert(TABLE_NAMEZ, null, values);
    }

    public String searchPass(String uname)
    {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "select uname, pass from "+TABLE_NAMEZ;
        Cursor cursor = db.rawQuery(query, null);

        String a, b;
        b = "not found";

        if(cursor.moveToFirst())
        {
            do
            {
                a = cursor.getString(0);
                b = cursor.getString(1);

                if(a.equals(uname))
                {
                    b = cursor.getString(1);
                    break;                }
            }
            while(cursor.moveToNext());
        }

        return b;
    }

}
