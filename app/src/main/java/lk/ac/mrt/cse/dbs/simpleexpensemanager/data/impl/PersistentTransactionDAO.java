package lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import lk.ac.mrt.cse.dbs.simpleexpensemanager.control.EMDatabaseHelper;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.TransactionDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.ExpenseType;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.Transaction;

public class PersistentTransactionDAO implements TransactionDAO {
    private final EMDatabaseHelper dbHelper;

    public PersistentTransactionDAO(EMDatabaseHelper dbHelper) {
        this.dbHelper = dbHelper;
    }
    @Override
    public void logTransaction(Date date, String accountNo, ExpenseType expenseType, double amount) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues cv = new ContentValues();

        SimpleDateFormat formatter = new SimpleDateFormat("dd-mm-yyyy");
        String stringDate = formatter.format(date);
        cv.put("date", stringDate);
        cv.put("accountNo", accountNo);
        cv.put("expenseType", expenseType.equals(ExpenseType.EXPENSE) ? "EXPENSE": "INCOME");
        cv.put("amount", amount);

        if ( db.insert(dbHelper.TABLE_TRANSACTION, null, cv) == -1) {
            Log.d("PTransactionDAO", "Transaction was not logged");
        }
        else {
            Log.d("PTransactionDAO", "Transaction was logged");
        }
        db.close();
    }

    @Override
    public List<Transaction> getAllTransactionLogs() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String select_query ="select * from " + dbHelper.TABLE_TRANSACTION + " order by transaction_id desc";

        Cursor c = db.rawQuery(select_query, null);
        List<Transaction> transactions = new LinkedList<>();
        SimpleDateFormat formatter = new SimpleDateFormat("dd-mm-yyyy");
        if (c.moveToFirst()) {
            do {
                try {
                    Date date = formatter.parse(c.getString(c.getColumnIndex("date")));
                    String accountNo = c.getString(c.getColumnIndex("accountNo"));
                    String expenseSType = c.getString(c.getColumnIndex("expenseType"));
                    ExpenseType expenseType;
                    if(expenseSType.equals("EXPENSE") ){
                        expenseType = ExpenseType.EXPENSE;
                    }else {
                        expenseType = ExpenseType.INCOME;
                    }
                    double amount = c.getDouble(c.getColumnIndex("amount"));

                    transactions.add(new Transaction(date, accountNo, expenseType, amount));
                } catch (ParseException e) {db.close();}
            } while (c.moveToNext());
        }
        db.close();
        return transactions;
    }

    @Override
    public List<Transaction> getPaginatedTransactionLogs(int limit) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String select_query ="select * from " + dbHelper.TABLE_TRANSACTION + " order by transaction_id desc limit " + limit;

        Cursor c = db.rawQuery(select_query, null);
        List<Transaction> transactions = new LinkedList<>();
        SimpleDateFormat formatter = new SimpleDateFormat("dd-mm-yyyy");
        if (c.moveToFirst()) {
            do {
                try {
                    Date date = formatter.parse(c.getString(c.getColumnIndex("date")));
                    String accountNo = c.getString(c.getColumnIndex("accountNo"));
                    String expenseSType = c.getString(c.getColumnIndex("expenseType"));

                    ExpenseType expenseType;
                    if(expenseSType.equals("EXPENSE") ){
                        expenseType = ExpenseType.EXPENSE;
                    }else {
                        expenseType = ExpenseType.INCOME;
                    }
                    double amount = c.getDouble(c.getColumnIndex("amount"));

                    transactions.add(new Transaction(date, accountNo, expenseType, amount));
                } catch (ParseException e) {db.close();}
            } while (c.moveToNext());
        }
        db.close();
        return transactions;
    }
}
