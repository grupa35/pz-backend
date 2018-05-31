package pl.shopgen.models;

public class NewPasswordDTO {

    private String oldPassword;
    private String newPassword;
    private String reNewPassword;


    public NewPasswordDTO() {

    }

    public NewPasswordDTO(String oldPassword, String newPassword, String reNewPassword) {
        this.oldPassword = oldPassword;
        this.newPassword = newPassword;
        this.reNewPassword = reNewPassword;
    }

    @Override
    public int hashCode() {
        int result = oldPassword != null ? oldPassword.hashCode() : 0;
        result = 31 * result + (newPassword != null ? newPassword.hashCode() : 0);
        result = 31 * result + (reNewPassword != null ? reNewPassword.hashCode() : 0);
        return result;
    }

    @Override
    public boolean equals(Object o) {
        if(this == o) {
            return true;
        }
        if(!(o instanceof NewPasswordDTO)) {
            return false;
        }

        NewPasswordDTO that = (NewPasswordDTO) o;

        if(oldPassword != null ? !oldPassword.equals(that.oldPassword) : that.oldPassword != null) {
            return false;
        }
        if(newPassword != null ? !newPassword.equals(that.newPassword) : that.newPassword != null) {
            return false;
        }
        return reNewPassword != null ? reNewPassword.equals(that.reNewPassword) : that.reNewPassword == null;
    }

    public String getOldPassword() {
        return oldPassword;
    }

    public void setOldPassword(String oldPassword) {
        this.oldPassword = oldPassword;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }

    public String getReNewPassword() {
        return reNewPassword;
    }

    public void setReNewPassword(String reNewPassword) {
        this.reNewPassword = reNewPassword;
    }
}
