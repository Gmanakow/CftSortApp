package cft_fs_manakov;

public class IncorrectInputException extends Exception {
    private String message = "";

    public IncorrectInputException(){
        super();
    }

    public IncorrectInputException(String message){
        this.message = message;
    }

    public String getMessage(){
        return this.message;
    }
}
