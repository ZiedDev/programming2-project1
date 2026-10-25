package marsops.facility;

public interface Location {
    String getName();
    default boolean isSurface() { return false; }
}
