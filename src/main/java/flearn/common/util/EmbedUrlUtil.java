package flearn.common.util;

/**
 * Tiện ích chuyển đổi URL video/tài liệu sang dạng embed URL để nhúng iframe.
 * Hỗ trợ: YouTube (watch, short, share), Google Drive (file view/preview), video thông thường.
 */
public final class EmbedUrlUtil {

    private EmbedUrlUtil() {}

    /**
     * Chuyển URL thông thường sang URL embed cho iframe.
     * @return embed URL nếu có thể nhúng, null nếu không hỗ trợ.
     */
    public static String toEmbedUrl(String url) {
        if (url == null || url.isBlank()) return null;
        String trimmed = url.trim();

        // === YOUTUBE ===
        // Dạng: https://www.youtube.com/watch?v=VIDEO_ID[&...]
        if (trimmed.contains("youtube.com/watch")) {
            String videoId = extractQueryParam(trimmed, "v");
            if (videoId != null && !videoId.isBlank()) {
                return "https://www.youtube.com/embed/" + videoId;
            }
        }
        // Dạng ngắn: https://youtu.be/VIDEO_ID[?...]
        if (trimmed.contains("youtu.be/")) {
            int idx = trimmed.indexOf("youtu.be/") + "youtu.be/".length();
            String videoId = trimmed.substring(idx);
            int qIdx = videoId.indexOf('?');
            if (qIdx != -1) videoId = videoId.substring(0, qIdx);
            if (!videoId.isBlank()) {
                return "https://www.youtube.com/embed/" + videoId;
            }
        }
        // Đã là embed URL
        if (trimmed.contains("youtube.com/embed/")) {
            return trimmed;
        }
        // YouTube share link: https://youtube.com/shorts/VIDEO_ID
        if (trimmed.contains("youtube.com/shorts/")) {
            int idx = trimmed.indexOf("youtube.com/shorts/") + "youtube.com/shorts/".length();
            String videoId = trimmed.substring(idx);
            int qIdx = videoId.indexOf('?');
            if (qIdx != -1) videoId = videoId.substring(0, qIdx);
            if (!videoId.isBlank()) {
                return "https://www.youtube.com/embed/" + videoId;
            }
        }

        // === GOOGLE DRIVE ===
        // Dạng: https://drive.google.com/file/d/FILE_ID/view[?...]
        if (trimmed.contains("drive.google.com/file/d/")) {
            // Chuyển /view hoặc /edit thành /preview
            String result = trimmed.replaceAll("/view(\\?.*)?$", "/preview")
                                   .replaceAll("/edit(\\?.*)?$", "/preview");
            if (!result.contains("/preview")) {
                // Không có /view hay /edit — thêm /preview
                int fileEnd = result.indexOf("?");
                if (fileEnd == -1) {
                    result = result + "/preview";
                } else {
                    result = result.substring(0, fileEnd) + "/preview";
                }
            }
            return result;
        }
        // Dạng: https://drive.google.com/open?id=FILE_ID
        if (trimmed.contains("drive.google.com/open")) {
            String fileId = extractQueryParam(trimmed, "id");
            if (fileId != null && !fileId.isBlank()) {
                return "https://drive.google.com/file/d/" + fileId + "/preview";
            }
        }

        return null; // Không nhận dạng được, không thể embed
    }

    /**
     * Kiểm tra URL có phải là video có thể nhúng hay không (YouTube, Drive).
     */
    public static boolean isEmbeddable(String url) {
        return toEmbedUrl(url) != null;
    }


    /**
     * Trích xuất giá trị query param từ URL.
     */
    private static String extractQueryParam(String url, String paramName) {
        int idx = url.indexOf(paramName + "=");
        if (idx == -1) return null;
        String value = url.substring(idx + paramName.length() + 1);
        int end = value.indexOf('&');
        return end != -1 ? value.substring(0, end) : value;
    }
}
