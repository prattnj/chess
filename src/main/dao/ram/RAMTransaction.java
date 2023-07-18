package dao.ram;

import dao.Transaction;

public class RAMTransaction implements Transaction {

    private static final Transaction instance = new RAMTransaction();

    public static Transaction getInstance() {
        return instance;
    }

    @Override
    public void openTransaction() {
    }

    @Override
    public void closeTransaction(boolean commit) {
    }

    @Override
    public boolean isOpen() {
        return true;
    }
}
