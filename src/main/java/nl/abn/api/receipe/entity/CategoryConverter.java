package nl.abn.api.receipe.entity;


import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

/**
 * The type Category converter.
 */
@Converter(autoApply = true)
public class CategoryConverter implements AttributeConverter<Category, String> {

    @Override
    public String convertToDatabaseColumn(Category category) {
        if (category == null) {
            return null;
        }
        return category.getCode();
    }

    @Override
    public Category convertToEntityAttribute(String code) {
        if (code == null) {
            return null;
        }

        switch (code) {
            case "vegetarian":
                return Category.VEGETARIAN;

            case "nonVegetarian":
                return Category.NON_VEGETARIAN;

            default:
                throw new IllegalArgumentException("Category [" + code
                        + "] not supported.");
        }
    }
}