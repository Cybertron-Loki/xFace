package project.test.xface.utils;

public interface ILock {
    public boolean tryLock(long timeoutSec);
    public void unlock();
}
