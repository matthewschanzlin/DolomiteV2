package utils;

/**
 * Stores float dimensions relating to screen size / graph size
 */
public class ViewDimension {
    Float width;
    Float height;

    public ViewDimension(Float width, Float height) {
        this.width = width;
        this.height = height;
    }

    public Float getWidth() { return width; }

    public Float getHeight() { return height; }
}
