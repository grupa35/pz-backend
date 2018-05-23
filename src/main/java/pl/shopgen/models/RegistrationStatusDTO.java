package pl.shopgen.models;

public class RegistrationStatusDTO {

    private int resultCode;

    public RegistrationStatusDTO(int resultCode) {
        this.resultCode = resultCode;
    }

    public RegistrationStatusDTO(RegistrationStatusDTO other) {
        if (other != null) {
            resultCode = other.resultCode;
        }
    }

    @Override
    final public int hashCode() {
        return resultCode;
    }

    @Override
    final public boolean equals(Object o) {
        if(this == o) {
            return true;
        }
        if(!(o instanceof RegistrationStatusDTO)) {
            return false;
        }

        RegistrationStatusDTO that = (RegistrationStatusDTO) o;

        return resultCode == that.resultCode;
    }

    public int getResultCode() {
        return resultCode;
    }

    public void setResultCode(int resultCode) {
        this.resultCode = resultCode;
    }
}
