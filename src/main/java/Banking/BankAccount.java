package Banking;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class BankAccount {
    private final int id;
    private int balance;
    private final Lock lock = new ReentrantLock();

    public BankAccount(int id, int initialBalance) {
        this.id = id;
        this.balance = initialBalance;
    }

    public int getId() {
        return id;
    }

    public int getBalance() {
        lock.lock();
        try {
            return balance;
        } finally {
            lock.unlock();
        }
    }

    public Lock getLock() {
        return lock;
    }

    public void deposit(int amount) {
        lock.lock();
        try {
            if (amount > 0) {
                balance += amount;
            }
        } finally {
            lock.unlock();
        }
    }

    public void withdraw(int amount) {
        lock.lock();
        try {
            if (amount > 0 && balance >= amount) {
                balance -= amount;
            }
        } finally {
            lock.unlock();
        }
    }

    public void transfer(BankAccount target, int amount) {
        BankAccount firstLock = this.id < target.getId() ? this : target;
        BankAccount secondLock = this.id < target.getId() ? target : this;

        firstLock.getLock().lock();
        secondLock.getLock().lock();
        try {
            if (amount > 0 && this.balance >= amount) {
                this.withdraw(amount);
                target.deposit(amount);
            }
        } finally {
            secondLock.getLock().unlock();
            firstLock.getLock().unlock();
        }
    }
}
