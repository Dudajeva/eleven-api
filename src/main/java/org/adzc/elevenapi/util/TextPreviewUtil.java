package org.adzc.elevenapi.util;

import org.adzc.elevenapi.domain.ChatMessage;

public class TextPreviewUtil {
    public static String from(ChatMessage m) {
        if (m.getMessageType() != null && m.getMessageType() == 2) return "[图片]";
        String s = m.getContent() == null ? "" : m.getContent().trim();
        if (s.length() > 50) s = s.substring(0, 50);
        return s;
    }
}
