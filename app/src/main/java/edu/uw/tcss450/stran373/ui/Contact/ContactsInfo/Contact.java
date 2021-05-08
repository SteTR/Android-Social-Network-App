package edu.uw.tcss450.stran373.ui.Contact.ContactsInfo;

import java.io.Serializable;

/**
 * Contact class contains name, id, and email of a contact
 * @author Andrew Bennett
 */
public class Contact implements Serializable {

    private final String mFirstName;
    private final String mLastName;
    private final int mContactID;
    private final String mEmail;

    /**
     * Necessary builder function to build the contact card
     * @author Andrew Bennett
     */
    public static class Builder {
        private String mFirstName;
        private String mLastName;
        private int mContactID;
        private String mEmail;

        public Builder()
        {
            mLastName = "Default";
            mFirstName = "Default";
            mContactID = -1;
            mEmail = "None Entered";
        }

        public Builder(final String theFirstName, final String theLastName, final int theContactID, final String theEmail)
        {
            this.mFirstName = theFirstName;
            this.mLastName = theLastName;
            this.mContactID = theContactID;
            this.mEmail = theEmail;
        }

        /**
         * Sets the first name
         * @author Andrew Bennett
         * @param theFirstName first name of the contact card
         */
        public void setFirstName(final String theFirstName) {
            mFirstName = theFirstName;
        }

        /**
         * Sets the last name
         * @author Andrew Bennett
         * @param theLastName returns the last name
         */
        public void setLastName(final String theLastName) {
            mLastName = theLastName;
        }

        /**
         * Set the contact id of the contact card
         *
         * @param theContactID contactid of this contact
         */
        public void setContactId(final int theContactID) {
            this.mContactID = theContactID;
        }

        /**
         * Set the email of the contact card
         * @author Andrew Bennett
         * @param theEmail is the email to be set in the contact
         */
        public void setEmail(final String theEmail) { this.mEmail = theEmail; }

        /**
         * Builds the contact card
         * @return Contact
         */
        public Contact build()
        {
            return new Contact(this);
        }
    }

    /**
     * Builds the Contact
     * @param builder Builder in contact card
     */
    private Contact(final Builder builder)
    {
        this.mFirstName = builder.mFirstName;
        this.mLastName = builder.mLastName;
        this.mContactID = builder.mContactID;
        this.mEmail = builder.mEmail;
    }

    /**
     * Returns the id corresponding to the contact
     * @author Andrew Bennett
     * @return Integer chat id
     */
    public int getContactID()
    {
        return mContactID;
    }

    /**
     * Returns the first name of contact
     * @author Andrew Bennett
     * @return mFirstName
     */
    public String getFirstName(){ return mFirstName;}

    /**
     * Returns the last name of contact
     * @author Andrew bennett
     * @return mLastName
     */
    public String getLastName(){return mLastName;}

    /**
     * Returns the email of contact
     * @return mEmail
     */
    public String getEmail(){return mEmail;}
}