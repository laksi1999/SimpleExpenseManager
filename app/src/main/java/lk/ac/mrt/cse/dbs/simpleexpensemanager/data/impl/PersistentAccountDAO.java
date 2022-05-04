package lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.LinkedList;
import java.util.List;

import lk.ac.mrt.cse.dbs.simpleexpensemanager.control.EMDatabaseHelper;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.AccountDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.exception.InvalidAccountException;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.Account;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.ExpenseType;

public class PersistentAccountDAO implements AccountDAO {
    private final EMDatabaseHelper dbHelper;

    public PersistentAccountDAO(EMDatabaseHelper dbHelper) {
        this.dbHelper = dbHelper;
    }
    @Override
    public List<String> getAccountNumbersList() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String select_query = "select accountNo from " + dbHelper.TABLE_ACCOUNT;
        Cursor c = db.rawQuery(select_query, null);
        List<String> acNumbers = new LinkedList<>();
        if (c.moveToFirst()) {
            do {
                acNumbers.add(c.getString(c.getColumnIndex("accountNo")));
            } while (c.moveToNext());
        }
        db.close();
        return acNumbers;
    }

    @Override
    public List<Account> getAccountsList() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String select_query = "select * from " + dbHelper.TABLE_ACCOUNT;
        Cursor c = db.rawQuery(select_query, null);
        List<Account> accounts = new LinkedList<>();
        if (c.moveToFirst()) {
            do {
                String accountNo = c.getString(c.getColumnIndex("accountNo"));
                String bankName = c.getString(c.getColumnIndex("bankName"));
                String accountHolderName = c.getString(c.getColumnIndex("accountHolderName"));
                double balance = c.getDouble(c.getColumnIndex("balance"));
                accounts.add(new Account(accountNo, bankName, accountHolderName, balance));
            } while (c.moveToNext());
        }
        db.close();
        return accounts;
    }

    @Override
    public Account getAccount(String accountNo) throws InvalidAccountException {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String select_query = "select * from " + dbHelper.TABLE_ACCOUNT + " where accountNo='" + accountNo + "'";
        Cursor c = db.rawQuery(select_query, null);
        if (c.moveToFirst()) {
            String accountNum = c.getString(c.getColumnIndex("accountNo"));
            String bankName = c.getString(c.getColumnIndex("bankName"));
            String accountHolderName = c.getString(c.getColumnIndex("accountHolderName"));
            double balance = c.getDouble(c.getColumnIndex("balance"));
            db.close();
            return new Account(accountNum, bankName, accountHolderName, balance);
        }
        db.close();
        String msg = "Account " + accountNo + " is invalid.";
        throw new InvalidAccountException(msg);
    }

    @Override
    public void addAccount(Account account) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues cv = new ContentValues();
        cv.put("accountNo", account.getAccountNo());
        cv.put("bankName", account.getBankName());
        cv.put("accountHolderName", account.getAccountHolderName());
        cv.put("balance", account.getBalance());

        if (db.insert(dbHelper.TABLE_ACCOUNT, null, cv) == -1) {
            Log.d("PAccountDAO", "Account was not added");
        }
        else {
            Log.d("PAccountDAO", "Account was added");
        }
        db.close();
    }

    @Override
    public void removeAccount(String accountNo) throws InvalidAccountException {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        if (db.delete(dbHelper.TABLE_ACCOUNT, "accountNo=?", new String[]{accountNo}) == -1) {
            Log.d("PAccountDAO", "Account was not removed");
            db.close();
            String msg = "Account " + accountNo + " is invalid.";
            throw new InvalidAccountException(msg);
        }
        else {
            Log.d("PAccountDAO", "Account was removed");
            db.close();
        }
    }

    @Override
    public void updateBalance(String accountNo, ExpenseType expenseType, double amount) throws InvalidAccountException {
        double new_balance = getAccount(accountNo).getBalance();
        switch (expenseType){
            case INCOME:
                new_balance +=amount;
                break;
            case EXPENSE:
                new_balance -=amount;
        }

        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("balance", new_balance);
        if (db.update(dbHelper.TABLE_ACCOUNT, cv, "accountNo=?", new String[]{accountNo}) == -1) {
            Log.d("PAccountDAO", "Account balance was not updated");
            db.close();
            String msg = "Account " + accountNo + " is invalid.";
            throw new InvalidAccountException(msg);
        }
        else {
            Log.d("PAccountDAO", "Account balance was updated");
            db.close();

        }
    }
}
