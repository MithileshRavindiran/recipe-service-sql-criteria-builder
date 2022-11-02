package nl.abn.api.receipe.exception;

/**
 * The type Recipe service exception.
 */
public class RecipeServiceException extends RuntimeException {

    private static final long serialVersionUID = 1L;


    /**
     * Instantiates a new Recipe service exception.
     *
     * @param message the message
     */
    public RecipeServiceException(String message) {
        super(message);
    }
}

