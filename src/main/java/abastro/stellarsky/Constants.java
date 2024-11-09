package abastro.stellarsky;

public class Constants {
    public static final int SECOND_IN_TICK = 20;

    // TODO Take sidereal vs. apparent into account
    public static final float YEAR_IN_DAY = 365.25f;
    public static final float DAY_IN_SEC = 1200f;
    public static final float AU_IN_KM = 1.5e8f;

    public static final float YEAR_IN_SEC = YEAR_IN_DAY * DAY_IN_SEC;
}
