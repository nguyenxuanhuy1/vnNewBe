package com.example.demo.util;

public class SlugUtil {

    public static String toSlug(String input) {
        if (input == null) return null;

        // 1. Chuyển về NFD để tách dấu
        String normalized = java.text.Normalizer.normalize(input, java.text.Normalizer.Form.NFD);
        // 2. Xóa tất cả các ký tự dấu
        String slug = normalized.replaceAll("\\p{InCombiningDiacriticalMarks}+", "");
        // 3. Chuyển về lowercase và thay khoảng trắng bằng '-'
        slug = slug.toLowerCase().replaceAll("\\s+", "-");
        // 4. Xóa ký tự không hợp lệ khác (chỉ giữ chữ, số, '-')
        slug = slug.replaceAll("[^a-z0-9\\-]", "");

        return slug;
    }
}

