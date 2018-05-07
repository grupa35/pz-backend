package pl.shopgen.exceptions;

public class PageOutOfBoundException extends IndexOutOfBoundsException{
    public PageOutOfBoundException() {
    }

    public PageOutOfBoundException(String s) {
        super(s);
    }

    public PageOutOfBoundException(int index) {
        super(index);
    }

}
