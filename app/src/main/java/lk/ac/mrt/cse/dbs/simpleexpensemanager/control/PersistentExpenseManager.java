package lk.ac.mrt.cse.dbs.simpleexpensemanager.control;

import android.content.Context;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.AccountDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.TransactionDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl.PersistentAccountDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl.PersistentTransactionDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.Account;

public class PersistentExpenseManager extends ExpenseManager{

    private EMDatabaseHelper dbHelper;
    public PersistentExpenseManager(Context context) {
        dbHelper =  new EMDatabaseHelper(context);
        setup();
    }

    @Override
    public void setup() {
        /*** Begin generating dummy data for In-Memory implementation ***/

        TransactionDAO persistentTransactionDAO = new PersistentTransactionDAO(dbHelper);
        setTransactionsDAO(persistentTransactionDAO);

        AccountDAO persistentAccountDAO = new PersistentAccountDAO(dbHelper);
        setAccountsDAO(persistentAccountDAO);

        // dummy data
        if(!persistentAccountDAO.getAccountNumbersList().contains("78945Z")) {
            Account dummyAcct2 = new Account("78945Z", "Clone BC", "Obi-Wan Kenobi", 80000.0);
            getAccountsDAO().addAccount(dummyAcct2);
        }

        if(!persistentAccountDAO.getAccountNumbersList().contains("12345A")) {
            Account dummyAcct1 = new Account("12345A", "Yoda Bank", "Anakin Skywalker", 10000.0);
            getAccountsDAO().addAccount(dummyAcct1);
        }
        /*** End ***/
    }
}
