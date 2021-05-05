package edu.uw.tcss450.stran373.ui.Contact.ContactsInfo;

import java.util.Arrays;
import java.util.List;

/**
 * This class is used to create Dummy contacts. Use it for development.
 * In future labs, connect to an API to gain actual Blog Data.
 */
public final class ContactGenerator {

    private static final Contact[] SCARDS;
    public static final int COUNT = 30;


    static {
        SCARDS = new Contact[COUNT];
        for (int i = 0; i < SCARDS.length; i++) {
            SCARDS[i] = (new Contact
                    .Builder("First"+ Integer.toString(i), "Last" + Integer.toString(i), i, "dummy@email.com")
                    .build());
        }
    }

    /**
     * SHALLOW COPY OF BUNK DATA! WILL NEED TO BE REPLACED
     * @return list of contact cards
     */
    public static List<Contact> getCARDS() {
        return Arrays.asList(SCARDS);
    }

    private ContactGenerator() { }
}