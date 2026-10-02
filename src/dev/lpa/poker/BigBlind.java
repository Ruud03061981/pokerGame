package dev.lpa.poker;

public enum BigBlind {
    HUNDRED(100),
    TWO_HUNDRED(200),
    THREE_HUNDRED(300),
    FOUR_HUNDRED(400),
    FIVE_HUNDRED(500);
    private final int blind;

    BigBlind(int blind) {
        this.blind = blind;
    }

}
