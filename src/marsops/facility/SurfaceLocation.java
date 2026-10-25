package marsops.facility;

public class SurfaceLocation implements Location {
    private String name;

    public SurfaceLocation(String name) {
        this.name = name;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public boolean isSurface() {
        return true;
    }
}
