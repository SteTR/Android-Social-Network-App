package edu.uw.tcss450.stran373.ui.Contact.ContactsInfo;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * This class is used to create Dummy contacts. Use it for development.
 * In future labs, connect to an API to gain actual Blog Data.
 */
public final class ContactGenerator {

    private static final Contact[] CARDS;
    public static final int COUNT = 30;


    static {
        CARDS = new Contact[COUNT];
        for (int i = 0; i < CARDS.length; i++) {
            CARDS[i] = (new Contact
                    .Builder("First"+ Integer.toString(i), "Last" + Integer.toString(i), i, "dummy@email.com")
                    .build());
        }
    }

    /**
     * SHALLOW COPY OF BUNK DATA! WILL NEED TO BE REPLACED
     * @return list of contact cards
     */
    public static List<Contact> getCARDS() {
        return Arrays.asList(CARDS);
    }

    private ContactGenerator() { }
}