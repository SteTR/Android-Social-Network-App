package edu.uw.tcss450.stran373.ui.Contact.InvitesInfo;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * This class is used to create Dummy Blogs. Use it for development.
 * In future labs, connect to an API to gain actual Blog Data.
 */
public final class InviteGenerator {

    private static final Invite CARDS[];
    public static final int COUNT = 20;


    static {
        CARDS = new Invite[COUNT];
        for (int i = 0; i < CARDS.length; i++) {
            CARDS[i] = new Invite
                    .Builder("First", "Last", i, "dummy@email.com","05/02/2021")
                    .build();
        }
    }

    /**
     * SHALLOW COPY OF BUNK DATA! WILL NEED TO BE REPLACED
     * @return list of contact cards
     */
    public static List<Invite> getCARDS() {
        return Arrays.asList(CARDS);
    }

    private InviteGenerator() { }
}