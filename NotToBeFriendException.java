package exceptions;

/**
 * Throws when trying to make an adult and a child friend or connect two
 * children with an age gap larger than 3.
 */
public class NotToBeFriendException extends Exception {
    public NotToBeFriendException() {
    }


}
