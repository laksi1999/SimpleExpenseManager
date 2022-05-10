/*
 * Copyright 2015 Department of Computer Science and Engineering, University of Moratuwa.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *                  http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package lk.ac.mrt.cse.dbs.simpleexpensemanager;

import static org.junit.Assert.assertTrue;
import org.junit.Before;
import org.junit.Test;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.control.ExpenseManager;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.control.InMemoryDemoExpenseManager;

public class ApplicationTest {
    private ExpenseManager expenseManager;

    @Before
    public void setUp() {
        expenseManager = new InMemoryDemoExpenseManager();
    }

    @Test
    public void addAccountOne() {
        expenseManager.addAccount("190346A", "HNB", "Laksika T.", 10000.0);
        assertTrue(expenseManager.getAccountNumbersList().contains("190346A"));
    }

    @Test
    public void addAccountTwo() {
        expenseManager.addAccount("000763068411", "NSB", "Laksika T..", 1000.0);
        assertTrue(expenseManager.getAccountNumbersList().contains("000763068411"));
    }

    @Test
    public void addAccountThree() {
        expenseManager.addAccount("0771234567", "BOC", "Laksika T.", 100.0);
        assertTrue(expenseManager.getAccountNumbersList().contains("0771234567"));
    }
}