package net.lonewolfcode.opensource.springutilities.errors;

public class NotFoundException extends Exception {
    public NotFoundException(){
        super();
    }

    public NotFoundException(String message){
        super(message);
    }
}
