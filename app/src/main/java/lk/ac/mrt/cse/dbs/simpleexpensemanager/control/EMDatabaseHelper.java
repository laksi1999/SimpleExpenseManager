package lk.ac.mrt.cse.dbs.simpleexpensemanager.control;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.Nullable;
import android.widget.Toast;

public class EMDatabaseHelper extends SQLiteOpenHelper {
    private final static String DB_NAME = "190346A.db";
    public final static String TABLE_ACCOUNT = "accounts";
    public final static String TABLE_TRANSACTION = "transactions";

    public EMDatabaseHelper(@Nullable Context context) {
        super(context, DB_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String create_table_query = "create table " + TABLE_ACCOUNT + " (accountNo text primary key, bankName text not null, accountHolderName text not null, balance real not null)";
        sqLiteDatabase.execSQL(create_table_query);

        create_table_query = "create table " + TABLE_TRANSACTION + " (transaction_id integer primary key autoincrement, date text not null, accountNo text not null, expenseType text not null, amount real not null)";
        sqLiteDatabase.execSQL(create_table_query);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("drop table if exists " + TABLE_ACCOUNT);
        sqLiteDatabase.execSQL("drop table if exists " + TABLE_TRANSACTION);
        onCreate(sqLiteDatabase);
    }
}
