package com.itmo.phone_book.utils;

public final class IOUtils {
    private IOUtils() {}

    public static void closeQuietly(AutoCloseable closeable) {
        if (closeable != null) {
            try {
                closeable.close();
            } catch (Exception e) {
                // ignore
            }
        }
    }
}
