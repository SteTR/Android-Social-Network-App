package edu.uw.tcss450.stran373.ui.Contact.InvitesInfo;

import java.io.Serializable;

public class Invite implements Serializable {

    private final String mFirstName;
    private final String mLastName;
    private final int mContactID;
    private final String mEmail;
    private final String mDate;

    /**
     * Necessary builder function to build the invite card
     * @author Andrew Bennett
     */
    public static class Builder {
        private String mFirstName;
        private String mLastName;
        private int mContactID;
        private String mEmail;
        private String mDate;

        public Builder()
        {
            mLastName = "Default";
            mFirstName = "Default";
            mContactID = -1;
            mEmail = "None Entered";
            mDate = "0/0/0";
        }

        public Builder(final String theFirstName, final String theLastName, final int theContactID,
                       final String theEmail, final String theDate)
        {
            this.mFirstName = theFirstName;
            this.mLastName = theLastName;
            this.mContactID = theContactID;
            this.mEmail = theEmail;
            this.mDate = theDate;
        }

        /**
         * Sets the first name
         * @author Andrew Bennett
         * @param theFirstName first name of the request card
         */
        public void setFirstName(final String theFirstName) {
            mFirstName = theFirstName;
        }

        /**
         * Sets the last name
         * @author Andrew Bennett
         * @param theLastName
         */
        public void setLastName(final String theLastName) {
            mLastName = theLastName;
        }

        /**
         * Set the contact id of the invitee
         *
         * @param theContactID contact id of the request
         */
        public void setContactId(final int theContactID) {
            this.mContactID = theContactID;
        }

        /**
         * Set the email of the invitee
         * @author Andrew Bennett
         * @param theEmail is the email to be set in the request
         */
        public void setEmail(final String theEmail) { this.mEmail = theEmail; }

        /**
         * Builds the invite card
         * @return Invite with info on the invitee
         */
        public Invite build()
        {
            return new Invite(this);
        }
    }

    /**
     * Builds the Invite card
     * @param builder builder that has same attributes as Invite
     */
    private Invite(final Builder builder)
    {
        this.mFirstName = builder.mFirstName;
        this.mLastName = builder.mLastName;
        this.mContactID = builder.mContactID;
        this.mEmail = builder.mEmail;
        this.mDate = builder.mDate;
    }

    /**
     * Returns the invitee contact id
     * @return mContactID
     */
    public int getContactID()
    {
        return mContactID;
    }
    public String getFirstName(){ return mFirstName;}
    public String getLastName(){return mLastName;}
    public String getEmail(){return mEmail;}

}