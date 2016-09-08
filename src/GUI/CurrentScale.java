package GUI;

/**
 * Used by the Scroll Pane to allow zooming of the image.
 *
 * Created by Wes on 9/7/2016.
 */
public class CurrentScale {
    private double scale;

    public CurrentScale() {
        scale = 1;
    }

    public void zoom(double zoomAmount) {
        scale *= zoomAmount;
    }

    public double getScale() {
        return scale;
    }
}
