package com.example.backend.utils;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReentrantLock;

public class FileLockingUtils {
    private static final Map<Long, ReentrantLock> fileLocks = new ConcurrentHashMap<>();

    private FileLockingUtils() {}

    public static ReentrantLock getFileLock(Long fileId) {
        return fileLocks.computeIfAbsent(fileId, k -> new ReentrantLock());
    }

    public static void lockFile(Long fileId) {
        ReentrantLock lock = getFileLock(fileId);
        lock.lock();
    }

    public static void unlockFile(Long fileId) {
        ReentrantLock lock = getFileLock(fileId);
        lock.unlock();
    }
}
