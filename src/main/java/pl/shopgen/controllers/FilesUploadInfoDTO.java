package pl.shopgen.controllers;

public class FilesUploadInfoDTO {

    private String url;

    private String fileType;


    public FilesUploadInfoDTO() {
    }

    @Override
    public int hashCode() {
        int result = url != null ? url.hashCode() : 0;
        result = 31 * result + (fileType != null ? fileType.hashCode() : 0);
        return result;
    }

    @Override
    public boolean equals(Object o) {
        if(this == o) {
            return true;
        }
        if(o == null || getClass() != o.getClass()) {
            return false;
        }

        FilesUploadInfoDTO that = (FilesUploadInfoDTO) o;

        if(url != null ? !url.equals(that.url) : that.url != null) {
            return false;
        }
        return fileType != null ? fileType.equals(that.fileType) : that.fileType == null;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getFileType() {
        return fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }
}

