package ru.job4j.cash;

import net.jcip.annotations.GuardedBy;
import net.jcip.annotations.ThreadSafe;

import java.util.HashMap;
import java.util.Optional;

@ThreadSafe
public class AccountStorage {

    @GuardedBy("this")
    private final HashMap<Integer, Account> accounts = new HashMap<>();

    public synchronized boolean add(Account account) {
        return accounts.putIfAbsent(account.id(), account) == null;
    }

    public synchronized boolean update(Account account) {
        boolean isUpdated = accounts.containsKey(account.id());
        if (isUpdated) {
            accounts.put(account.id(), account);
        }
        return isUpdated;
    }

    public synchronized boolean delete(int id) {
        return accounts.remove(id) != null;
    }

    public synchronized Optional<Account> getById(int id) {
        return accounts.get(id) != null ? Optional.of(accounts.get(id)) : Optional.empty();
    }

    public synchronized boolean transfer(int fromId, int toId, int amount) {
        Optional<Account> optFrom = getById(fromId);
        Optional<Account> optTo = getById(toId);
        boolean rsl = optFrom.isPresent()
                && optTo.isPresent()
                && optFrom.get().amount() >= amount;
        if (rsl) {
            update(new Account(optFrom.get().id(), optFrom.get().amount() - amount));
            update(new Account(optTo.get().id(), optTo.get().amount() + amount));
        }
        return rsl;
    }

}
