package utils;

public class FileCountResult {

    private final int attributeFileCount;
    private final int noAttributeFileCount;
    private final String screenshotPath;

    public FileCountResult(
            int attributeFileCount,
            int noAttributeFileCount,
            String screenshotPath) {

        this.attributeFileCount = attributeFileCount;
        this.noAttributeFileCount = noAttributeFileCount;
        this.screenshotPath = screenshotPath;
    }

    public int getAttributeFileCount() {
        return attributeFileCount;
    }

    public int getNoAttributeFileCount() {
        return noAttributeFileCount;
    }

    public String getScreenshotPath() {
        return screenshotPath;
    }
}