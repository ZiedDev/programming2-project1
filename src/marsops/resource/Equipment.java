package marsops.resource;

public class Equipment {
    private Boolean isDamaged;
    private static int cnt = 0;
    private int id;

    public Equipment() {
        this.isDamaged = false;
        id = ++this.cnt;
    }

    public boolean isAvailable() {
        return isDamaged;
    }

    public void fix() {
        this.isDamaged = false;
    }

    public void damage() {
        this.isDamaged = true;
    }
}