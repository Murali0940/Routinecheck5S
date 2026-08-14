package utils;

public class FileCountResult {

    private final int attributeFileCount;
    private final int nonAttributeFileCount;
    private final int totalFileCount;
    private final String screenshotPath;

    public FileCountResult(
            int attributeFileCount,
            int nonAttributeFileCount,
            int totalFileCount,
            String screenshotPath) {

        this.attributeFileCount = attributeFileCount;
        this.nonAttributeFileCount = nonAttributeFileCount;
        this.totalFileCount = totalFileCount;
        this.screenshotPath = screenshotPath;
    }

    public FileCountResult(
            int attributeFileCount,
            int nonAttributeFileCount,
            String screenshotPath) {
        this(attributeFileCount, nonAttributeFileCount, attributeFileCount + nonAttributeFileCount, screenshotPath);
    }

    public int getAttributeFileCount() {
        return attributeFileCount;
    }

    public int getNonAttributeFileCount() {
        return nonAttributeFileCount;
    }

    public int getNoAttributeFileCount() {
        return nonAttributeFileCount;
    }

    public int getTotalFileCount() {
        return totalFileCount;
    }

    public String getScreenshotPath() {
        return screenshotPath;
    }
}