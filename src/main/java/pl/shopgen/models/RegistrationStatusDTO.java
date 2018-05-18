package pl.shopgen.models;

public class RegistrationStatusDTO extends SimpleObject {

    private int resultCode;

    public RegistrationStatusDTO(RegistrationStatusDTO other) {
        if (other != null) {
            resultCode = other.resultCode;
        }
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + resultCode;
        return result;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        RegistrationStatusDTO registrationStatusDTO = (RegistrationStatusDTO) o;
        return Integer.compare(registrationStatusDTO.resultCode, resultCode) != 0;
    }

    public int getResultCode() {
        return resultCode;
    }

    public void setResultCode(int resultCode) {
        this.resultCode = resultCode;
    }
}
