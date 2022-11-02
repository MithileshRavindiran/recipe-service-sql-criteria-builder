package nl.abn.api.receipe.entity;

/**
 * The enum Category.
 */
public enum Category {
    /**
     * Vegetarian category.
     */
    VEGETARIAN("vegetarian"),
    /**
     * Non vegetarian category.
     */
    NON_VEGETARIAN("nonVegetarian");

    private String code;

    Category(String code) {
        this.code = code;
    }

    /**
     * Gets code.
     *
     * @return the code
     */
    public String getCode() {
        return code;
    }
}
