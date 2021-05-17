package edu.uw.tcss450.stran373.ui.Contact.ContactsInfo;

import java.io.Serializable;

/**
 * Contact class contains name, id, and email of a contact
 * @author Andrew Bennett
 */
public class ContactCard implements Serializable {

    private final String mFirstName;
    private final String mLastName;
    private final String mMemberID;
    private final String mEmail;
    private final String mUserName;


    /**
     * Necessary builder function to build the contact card
     * @author Andrew Bennett
     */
    public static class Builder {
        private String mFirstName;
        private String mLastName;
        private String mMemberID;
        private String mEmail;
        private final String mUserName;

        public Builder()
        {
            mLastName = "Default";
            mFirstName = "Default";
            mMemberID = "-1";
            mEmail = "None Entered";
            mUserName = "Default";
        }

        public Builder(final String theContactID, final String theFirstName, final String theLastName,
                       final String theEmail, final String theUserName)
        {
            this.mMemberID = theContactID;
            this.mFirstName = theFirstName;
            this.mLastName = theLastName;
            this.mEmail = theEmail;
            this.mUserName = theUserName;
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
        public void setMemberId(final String theContactID) {
            this.mMemberID = theContactID;
        }

        /**
         * Set the email of the contact card
         * @author Andrew Bennett
         * @param theEmail is the email to be set in the contact
         */
        public void setEmail(final String theEmail) { this.mEmail = theEmail; }

        /**
         * Set the username of the contact card
         * @author Andrew Bennett
         * @param theUserName is the username to be set in the contact
         */
        public void setUserName(final String theUserName) { this.mEmail = theUserName; }

        /**
         * Builds the contact card
         * @return Contact
         */
        public ContactCard build()
        {
            return new ContactCard(this);
        }
    }

    /**
     * Builds the Contact
     * @param builder Builder in contact card
     */
    private ContactCard(final Builder builder)
    {
        this.mFirstName = builder.mFirstName;
        this.mLastName = builder.mLastName;
        this.mMemberID = builder.mMemberID;
        this.mEmail = builder.mEmail;
        this.mUserName = builder.mUserName;
    }

    /**
     * Returns the id corresponding to the contact
     * @return Integer chat id
     */
    public String getMemberID()
    {
        return mMemberID;
    }

    /**
     * Returns the first name of contact
     * @return mFirstName
     */
    public String getFirstName(){ return mFirstName;}

    /**
     * Returns the last name of contact
     * @return mLastName
     */
    public String getLastName(){return mLastName;}

    /**
     * Returns the email of contact
     * @return mEmail
     */
    public String getEmail(){return mEmail;}
}