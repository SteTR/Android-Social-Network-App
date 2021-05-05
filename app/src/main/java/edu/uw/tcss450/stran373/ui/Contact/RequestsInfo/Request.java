package edu.uw.tcss450.stran373.ui.Contact.RequestsInfo;

import java.io.Serializable;

public class Request implements Serializable {

    private final String mFirstName;
    private final String mLastName;
    private final int mContactID;
    private final String mEmail;
    private final String mDate;

    /**
     * Necessary builder function to build the contact card
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
         * Set the contact id of the request card
         *
         * @param theContactID integer chat id
         */
        public void setContactId(final int theContactID) {
            this.mContactID = theContactID;
        }

        /**
         * Set the email of the request card
         * @author Andrew Bennett
         * @param theEmail is the email to be set in the contact
         */
        public void setEmail(final String theEmail) { this.mEmail = theEmail; }

        /**
         * Builds the contact card
         * @author Andrew Bennett
         * @return Contact
         */
        public Request build()
        {
            return new Request(this);
        }
    }

    /**
     * Builds the request card
     * @author Andrew Bennett
     * @param builder that has same attributes as request
     */
    private Request(final Builder builder)
    {
        this.mFirstName = builder.mFirstName;
        this.mLastName = builder.mLastName;
        this.mContactID = builder.mContactID;
        this.mEmail = builder.mEmail;
        this.mDate = builder.mDate;
    }

    /**
     * Returns the id corresponding to the request card
     * @return Integer chat id
     */
    public int getRequestID()
    {
        return mContactID;
    }

    /**
     * Returns first name
     * @return mFirstName
     */
    public String getFirstName(){ return mFirstName;}

    /**
     * Returns last name
     * @return mLastName
     */
    public String getLastName(){return mLastName;}

    /**
     * Returns email
     * @return mEmail
     */
    public String getEmail(){return mEmail;}
}
